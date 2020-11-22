package jp.ac.hal.userauth.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.ac.hal.security.Aes;
import jp.ac.hal.userauth.dao.UserInfoMapper;
import jp.ac.hal.userauth.model.UserInfo;

public class LoginCtrl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public LoginCtrl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String callback = request.getParameter("callback");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        Gson gson = new Gson();
        JsonObject jsonObj = new JsonObject();

        jsonObj.addProperty("inputempty", false);

        if (StringUtils.isEmpty(userName)) {
            jsonObj.addProperty("inputempty", true);
            jsonObj.addProperty("success", false);
            out.close();
            return;
        }
        if (StringUtils.isEmpty(password)) {
            jsonObj.addProperty("inputempty", true);
            jsonObj.addProperty("success", false);
            out.close();
            return;
        }

    	List<UserInfo> userInfoList = new ArrayList<UserInfo>();
    	userInfoList = getInfo(userName);

    	UserInfo userInfo = null;

    	for(UserInfo uInfo : userInfoList) {

    		String pass = uInfo.getPassword();

    		if (password.equals(Aes.decrypt(pass))) {
    			userInfo = uInfo;
    			break;
    		}

    	}

        JsonElement userObj = gson.toJsonTree(userInfo);

        if(userInfo == null){
            jsonObj.addProperty("success", false);
        } else {

            jsonObj.addProperty("success", true);

        }
        jsonObj.add("userInfo", userObj);
        if(callback != null) {
            out.println(callback + "(" + jsonObj.toString() + ");");
        }
        else {
            out.println(jsonObj.toString());
        }

		HttpSession session = request.getSession(false);
		if (session == null){
			session = request.getSession(true);
		}

		String sessionId = session.getId();

        setLogout(sessionId, userInfo.getUserId());

        out.close();

    }


    private List<UserInfo> getInfo(String userName) {

    	List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        SqlSession session = null;

        try {
            SqlSessionFactory sqlSessionFactory
            = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("resources/mybatis-config.xml"));
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(session != null) {
            try {
            	UserInfoMapper mapper = session.getMapper(UserInfoMapper.class);
            	userInfoList = mapper.selectByUserName(userName);
            } finally {
                session.close();
            }
        }

        return userInfoList;

    }

    private boolean setLogout(String jsessionId, long userId) {

        SqlSession session = null;
        boolean rtnFlg = false;

        try {
            SqlSessionFactory sqlSessionFactory
            = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("resources/mybatis-config.xml"));
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(session != null) {
            try {
            	UserInfoMapper mapper = session.getMapper(UserInfoMapper.class);
            	rtnFlg = mapper.updateJsessionId(userId, jsessionId);
            	session.commit();
            } finally {
                session.close();
            }
        }

        return rtnFlg;

    }


    public static String getMyStackTrace(Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        return stringWriter.toString();

    }
}
