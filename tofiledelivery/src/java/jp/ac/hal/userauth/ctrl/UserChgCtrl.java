package jp.ac.hal.userauth.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.JsonObject;

import jp.ac.hal.security.Aes;
import jp.ac.hal.userauth.dao.UserInfoMapper;
import jp.ac.hal.userauth.model.UserInfo;

public class UserChgCtrl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public UserChgCtrl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userId = request.getParameter("userid");
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
//        String emailRe = request.getParameter("emailRe");
//        String useSystemId = request.getParameter("useSystemId");
//        String secret = request.getParameter("secret");

        String callback = request.getParameter("callback");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(Long.parseLong(userId));
        userInfo.setUserName(userName);
        if (StringUtils.isBlank(password)) {
            userInfo.setPassword("");
        } else {
            userInfo.setPassword(Aes.encrypt(password));
        }
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setPhoneNo(phoneNo);
        userInfo.setPostalCd(postalCd);
        userInfo.setPref(pref);
        userInfo.setAddress1(address1);
        userInfo.setAddress2(address2);
        userInfo.setEmail(email);

        JsonObject jsonObj = new JsonObject();

        boolean flg = this.setUserCng(userInfo);

        if (flg) {
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


    private boolean setUserCng( UserInfo userInfo) {

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
            	retFlg = mapper.updateUserinfo(userInfo);
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
