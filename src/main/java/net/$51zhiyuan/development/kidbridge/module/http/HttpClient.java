package net.$51zhiyuan.development.kidbridge.module.http;

import net.$51zhiyuan.development.kidbridge.module.Util;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具包，常用于与第三方服务的数据交互及项目API接口测试
 */
public class HttpClient {

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();

    private BasicCookieStore basicCookieStore = new BasicCookieStore();

    private SSLConnectionSocketFactory sslConnectionSocketFactory;

    private TrustManager truseAllManager = new X509TrustManager() {

        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) {
            // TODO Auto-generated method stub
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) {
            // TODO Auto-generated method stub
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    };

    public HttpClient() {
    }

    public HttpClient(String certfile,String key) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(certfile));
        try {
            keyStore.load(instream, key.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        this.sslConnectionSocketFactory = sslConnectionSocketFactory;
    }

    public String doPost(String url, List<NameValuePair> params) throws IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException {
        return this.doPost(url, "utf-8", params);
    }

    public String doPost(String url, String responseCharset, List<NameValuePair> params) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String response = null;
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            closeableHttpClient = HttpClients.custom().setDefaultCookieStore(this.basicCookieStore).setSSLSocketFactory(this.getSSLConnectionSocketFactory()).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(urlEncodedFormEntity);
            httpPost.setConfig(requestConfig);
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            int status = closeableHttpResponse.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                response = EntityUtils.toString(httpEntity, responseCharset);
            }
            EntityUtils.consume(httpEntity);
        } finally {
            if (closeableHttpResponse != null) {
                closeableHttpResponse.close();
            }
            if (closeableHttpClient != null) {
                closeableHttpClient.close();
            }
        }
        return response;

    }

    public String doPost(String url, String token, Map param) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String version = "1.0.0";
        String device = "ios";
        String timestamp = String.valueOf(String.valueOf(System.currentTimeMillis()));
        String sign = Util.sign(new HashMap(){{
            this.put("version",version);
            this.put("device",device);
            this.put("timestamp",timestamp);
            this.put("token",token);
            this.put("uri",new URL(url).getPath());
        }});
        return this.doPost(url,new Header[]{new BasicHeader("version",version),new BasicHeader("device",device),new BasicHeader("timestamp",timestamp),new BasicHeader("token",token),new BasicHeader("sign",sign)}, this.kidbridgeObjectMapper.writeValueAsString(param));
    }

    public String doPost(String url, String content) throws IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException {
        return this.doPost(url, "utf-8", null,content);
    }

    public String doPost(String url, String responseCharset, Header[] headers, String content) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String response = null;
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            StringEntity stringEntity = new StringEntity(content, "utf-8");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            closeableHttpClient = HttpClients.custom().setDefaultCookieStore(this.basicCookieStore).setSSLSocketFactory(this.getSSLConnectionSocketFactory()).build();
            HttpPost httpPost = new HttpPost(url);
            if(headers != null){
                httpPost.setHeaders(headers);
            }
            httpPost.setEntity(stringEntity);
            httpPost.setConfig(requestConfig);
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            int status = closeableHttpResponse.getStatusLine().getStatusCode();
                response = EntityUtils.toString(httpEntity, responseCharset);
            EntityUtils.consume(httpEntity);
        } finally {
            if (closeableHttpResponse != null) {
                closeableHttpResponse.close();
            }
            if (closeableHttpClient != null) {
                closeableHttpClient.close();
            }
        }
        return response;
    }


    public String doPost(String url,Header[] headers, String requestBody) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String response = null;
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            StringEntity stringEntity = new StringEntity(requestBody, "utf-8");
            stringEntity.setContentType("application/json;charset=utf-8");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            closeableHttpClient = HttpClients.custom().setDefaultCookieStore(this.basicCookieStore).setSSLSocketFactory(this.getSSLConnectionSocketFactory()).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeaders(headers);
            httpPost.setEntity(stringEntity);
            httpPost.setConfig(requestConfig);
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            int status = closeableHttpResponse.getStatusLine().getStatusCode();
            response = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } finally {
            if (closeableHttpResponse != null) {
                closeableHttpResponse.close();
            }
            if (closeableHttpClient != null) {
                closeableHttpClient.close();
            }
        }
        return response;
    }



    public String doPost(String url, File file) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String response = null;
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            FileBody fileBody = new FileBody(file);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.addPart("media", fileBody);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            HttpEntity entity = multipartEntityBuilder.build();
            closeableHttpClient = HttpClients.custom().setDefaultCookieStore(this.basicCookieStore).setSSLSocketFactory(this.getSSLConnectionSocketFactory()).build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(entity);
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            int status = closeableHttpResponse.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                response = EntityUtils.toString(httpEntity, "utf-8");
            }
            EntityUtils.consume(httpEntity);
        } finally {
            if (closeableHttpResponse != null) {
                closeableHttpResponse.close();
            }
            if (closeableHttpClient != null) {
                closeableHttpClient.close();
            }
        }
        return response;

    }


    public String doGet(String url) throws IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException {

        return this.doGet(url, "utf-8");
    }

    public String doGet(String url, String responseCharset) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String response = null;
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            closeableHttpClient = HttpClients.custom().setDefaultCookieStore(this.basicCookieStore).setSSLSocketFactory(this.getSSLConnectionSocketFactory()).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = closeableHttpResponse.getEntity();
            int status = closeableHttpResponse.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                response = EntityUtils.toString(entity, responseCharset);
            }
            EntityUtils.consume(entity);
        } finally {
            if (closeableHttpResponse != null) {
                closeableHttpResponse.close();
            }
            if (closeableHttpClient != null) {
                closeableHttpClient.close();
            }
        }
        return response;
    }

    private SSLConnectionSocketFactory getSSLConnectionSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
        SSLConnectionSocketFactory socketFactory = null;
        if (this.sslConnectionSocketFactory == null) {
            SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.SSL);
            sslContext.init(null, new TrustManager[]{this.truseAllManager}, null);
            return new SSLConnectionSocketFactory(sslContext);
        }

        return this.sslConnectionSocketFactory;
    }

}