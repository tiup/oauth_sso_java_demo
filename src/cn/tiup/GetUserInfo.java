package cn.tiup;


import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/*
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  Hello
  </body>
</html>

 */
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
        String userInfo = null;
        try {
            userInfo = Utils.getUserInfo(userURL,accessToken);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        if (userInfo == null) {
            Utils.login(request, response,schoolCode);
        }
        //打印用户信息，（可替换为其他操作）
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<h1>获取到的用户信息内容</h1>");
        out.println("<div>" + userInfo + "</div>");
        out.println("<h1>其中： linkedaccounts是学号信息</h1>");
        out.println("</html>");
    }

}


