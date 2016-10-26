<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>获取用户数据演示</title>
  </head>
  <body>
  <div><a href="/api/v1/userinfo">获取用户信息 (自动登录)</a></div>
  <div><a href="/sso/logout?redirect_uri=http://localhost:8080">退出并返回到主页</a></div>
  </body>
</html>
