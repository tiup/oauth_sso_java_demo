package cn.tiup;


import cn.tiup.models.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


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
        //1. 获取用户信息
        UserInfo userInfo = Utils.getUserInfo(request,userURL,schoolCode);
        if (userInfo == null) {
            //如果获取不到用户信息，执行登录操作，登录完成后会重新执行此次请求。
            Utils.login(request,response,schoolCode);
            return;
        }
        //2.打印用户信息，（可替换为其他操作）
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<h1>获取到的用户信息内容</h1>");
        out.println("<div>UID：" + userInfo.uid + "</div>");
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


