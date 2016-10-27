package cn.tiup;

import cn.tiup.models.UserInfo;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Created by leenanxi on 2016/10/26.
 */
public  class Utils {


    public static UserInfo getUserInfo(HttpServletRequest req, String userURL, String schoolCode){
        //1. 获取access token
        String accessToken = Utils.getAccessToken(req);
        if (accessToken == null) {
            return null;
        }
        //2. 获取用户信息
        UserInfo userInfo = null;
        try {
            userInfo = Utils.getUserInfo(userURL,accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static UserInfo getUserInfo(String userURL, String accessToken) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpClient client = createHttpClientAcceptsUntrustedCerts();
        HttpGet userInfoRequest = new HttpGet(userURL);
        userInfoRequest.addHeader("Authorization", "Bearer " + accessToken);
        HttpResponse userInfoResponse = client.execute(userInfoRequest);
        int status = userInfoResponse.getStatusLine().getStatusCode();
        if (status != 200) {
            return null;
        }
        Reader reader = new InputStreamReader(userInfoResponse.getEntity().getContent());
        return (new Gson()).fromJson(reader, UserInfo.class);
    }


    public static String getAccessToken(HttpServletRequest req) {
        //1. 获取access token
        String accessToken = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    try {
                        accessToken = cookie.getValue();
                    } catch (Exception e) {
                        //do noting
                    }
                }
            }
        }
        return accessToken;
    }

    public static void login(HttpServletRequest req, HttpServletResponse res, String schoolCode) throws IOException {
        StringBuffer requestURL = req.getRequestURL();
        String queryString = req.getQueryString();
        requestURL = requestURL.append('?').append(queryString);
        String redirectUri = "/sso/login?school_code=" + schoolCode + "&redirect_uri=" + URLEncoder.encode(requestURL.toString(), "UTF-8");
        res.sendRedirect(redirectUri);
    }

    //创建忽略SSL证书的http client，学校证书可能不合法，如果配置正确，可删除此代码
    public  static  HttpClient createHttpClientAcceptsUntrustedCerts() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                return true;
            }
        }).build();
        b.setSslcontext( sslContext);
          SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        b.setConnectionManager( connMgr);
        HttpClient client = b.build();
        return client;
    }

}
