package jp.ac.hal.userauth.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.JsonObject;

import jp.ac.hal.security.Aes;
import jp.ac.hal.userauth.common.UniqueId;
import jp.ac.hal.userauth.dao.UserInfoMapper;
import jp.ac.hal.userauth.model.UserInfo;

public class UserAddCtrl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public UserAddCtrl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNo = request.getParameter("phoneNo");
        String postalCd = request.getParameter("postalCd");
        String pref = request.getParameter("pref");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String email = request.getParameter("email");
        String emailRe = request.getParameter("emailRe");
        String useSystemId = request.getParameter("useSystemId");
        String secret = request.getParameter("secret");

        String callback = request.getParameter("callback");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        //TODO 同じユーザー名があった場合エラー

        JsonObject jsonObj = new JsonObject();

        if (!email.equals(emailRe)) {
            jsonObj.addProperty("mailng", false);
            //TODO エラー処理
        } else {

    		HttpSession session = request.getSession(false);
    		if (session == null){
    			session = request.getSession(true);
    		}

    		String sessionId = session.getId();

    		UniqueId now = new UniqueId();
    		long userId = now.createId();

    		//TODO 二段階認証

            UserInfo paraUserInfo = new UserInfo();
            paraUserInfo.setUserId(userId);
            paraUserInfo.setUserName(userName);
            paraUserInfo.setPassword(Aes.encrypt(password));
            paraUserInfo.setFirstName(firstName);
            paraUserInfo.setLastName(lastName);
            paraUserInfo.setPhoneNo(phoneNo);
            paraUserInfo.setPostalCd(postalCd);
            paraUserInfo.setPref(pref);
            paraUserInfo.setAddress1(address1);
            paraUserInfo.setAddress2(address2);
            paraUserInfo.setEmail(email);
            paraUserInfo.setJsessionId(sessionId);
            paraUserInfo.setUseSystemId(useSystemId);
            paraUserInfo.setSecret(secret);
            paraUserInfo.setAvailabilityFlag("0");

            boolean retFlg = this.addInfo(paraUserInfo);

            if(retFlg){
                jsonObj.addProperty("success", true);
            } else {
                jsonObj.addProperty("success", false);
            }

            if(callback != null) {
                out.println(callback + "(" + jsonObj.toString() + ");");
            }
            else {
                out.println(jsonObj.toString());
            }

            out.close();
        }

    }


    private boolean addInfo(UserInfo userInfo) {

        SqlSession session = null;
        boolean retFlg = false;

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
            	retFlg = mapper.insertUserinfo(userInfo);
            	session.commit();
            } finally {
                session.close();
            }
        }

        return retFlg;

    }

    public static String getMyStackTrace(Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        return stringWriter.toString();

    }
}
