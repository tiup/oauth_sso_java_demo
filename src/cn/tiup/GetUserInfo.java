package cn.tiup;


import cn.tiup.models.UserInfo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


/**
 * 获取用户信息示例
 *
 * Created by leenanxi on 2016/10/26.
 */
public class GetUserInfo extends HttpServlet {

    String userURL = null;
    String schoolCode = null;

    @Override
    public void init() throws ServletException {
        userURL = getServletConfig().getInitParameter("userURL");
        schoolCode = getServletConfig().getInitParameter("schoolCode");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取access token
        String accessToken = Utils.getAccessToken(request);
        if (accessToken == null) {
            Utils.login(request, response,schoolCode);
            return;
        }
        //2. 获取用户信息
        UserInfo userInfo = null;
        try {
            userInfo = Utils.getUserInfo(userURL,accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userInfo == null) {
            Utils.login(request, response,schoolCode);
            return;
        }
        //3.打印用户信息，（可替换为其他操作）

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<h1>获取到的用户信息内容</h1>");
        out.println("<div>姓名：" + userInfo.name + "</div>");
        out.println("<div>性别：" + userInfo.sex + "</div>");
        out.println("<div>生日：" + userInfo.birthday + "</div>");
        if (userInfo.linkedaccounts.size() > 0) {
            int studentN = 0;
            for (UserInfo.Linkedaccount act : userInfo.linkedaccounts) {
               if (act.host.equals(schoolCode) ){
                   out.println("<div>学号" + Integer.toString(studentN) + ": " + act.username + "</div>");
                   studentN ++;
                }
            }
        }
        out.println("</html>");
    }

}


