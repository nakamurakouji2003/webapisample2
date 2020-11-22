package jp.ac.hal.zipcode.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jp.ac.hal.zipcode.dao.ZipCodeMapper;
import jp.ac.hal.zipcode.model.ZipCode;

public class ZipCodeSearchCtrl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ZipCodeSearchCtrl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String zipcode = request.getParameter("zipcode");
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

        if (StringUtils.isEmpty(zipcode)) {
            jsonObj.addProperty("inputempty", true);
            jsonObj.addProperty("success", false);
            out.close();
            return;
        }

    	List<ZipCode> zipCodeList = new ArrayList<ZipCode>();

    	zipCodeList = this.getAddress(zipcode);

    	if (zipCodeList == null || zipCodeList.size() == 0) {
            jsonObj.addProperty("success", false);
            out.close();
            return;
    	}

    	ZipCode zipCode = zipCodeList.get(0);

        JsonElement zipCodeObj = gson.toJsonTree(zipCode);

        if(zipCode == null){
            jsonObj.addProperty("success", false);
        } else {

            jsonObj.addProperty("success", true);
        }
        jsonObj.add("zipCode", zipCodeObj);
        if(callback != null) {
            out.println(callback + "(" + jsonObj.toString() + ");");
        }
        else {
            out.println(jsonObj.toString());
        }

        out.close();

    }


    private List<ZipCode> getAddress(String zipcode) {

    	List<ZipCode> zipCodeList = new ArrayList<ZipCode>();
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
            	ZipCodeMapper mapper = session.getMapper(ZipCodeMapper.class);
            	zipCodeList = mapper.selectByZipCode(zipcode);
            } finally {
                session.close();
            }
        }

        return zipCodeList;

    }

    public static String getMyStackTrace(Exception e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        return stringWriter.toString();

    }
}
