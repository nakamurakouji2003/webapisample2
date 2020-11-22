package jp.ac.hal.userauth.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.JsonObject;

import jp.ac.hal.userauth.dao.UserInfoMapper;

public class UserDelCtrl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public UserDelCtrl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userid = request.getParameter("userid");
        String callback = request.getParameter("callback");

        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        JsonObject jsonObj = new JsonObject();

        boolean flg = this.delInfo(Long.parseLong(userid));

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


    private boolean delInfo(long userid) {

        SqlSession session = null;
        boolean flg = true;

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
            	flg = mapper.deleteUserinfo(userid);
            	session.commit();
            } finally {
                session.close();
            }
        }

        return flg;

    }

    public static String getMyStackTrace(Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        return stringWriter.toString();

    }
}
