# TiUP Java SSO 接入示例 (代理简化模式)

## 环境依赖
标准JavaWeb架构，可以运行在Tomcat，glassfish等Servlet容器中。

### 1. 使用
1. 运行 TiUP SSO SDK (https://github.com/tiup/oauth_sso)
2. 修改 web/WEB-INF/web.xml 11 行的代理地址。（默认是：http://localhost:2081/sso）
3. 修改 web/WEB-INF/web.xml 33 行为用户信息地址。（默认是：http://api-accounts.***.edu.cn/oauth2/v1/userinfo）
4. 修改 web/WEB-INF/web.xml 38 学校代码，可以为：cug，ruc，等


### 2. SSO登录演示

进入： /api/v1/userinfo  程序可实现自动登录，并获取到用户信息，您的程序可参考 src/cn/tiup/GetUserInfo.java 获取到用户信息。


### 3. SSO退出演示

进入：/sso/logout?redirect_uri=<你的退出后返回的地址>

## 4. 集成到现有项目

4.1 下载：build/sso.jar, web/WEB-INF/lib 所有文件，到您的web项目 的 lib文件夹。
4.2 参考web/WEB-INF/web.xml, 修改您的 web.xml 文件。
4.3 参考 GetUserInfo.java 在您项目的登录入口和需要的地方获取用户信息

备注：如果您的jdk版本在1.6 或以下，可能需要自行编译sso.jar 文件，源码在src目录。





