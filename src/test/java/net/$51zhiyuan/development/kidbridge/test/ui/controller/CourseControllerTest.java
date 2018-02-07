package net.$51zhiyuan.development.kidbridge.test.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class CourseControllerTest {


    {
        System.setProperty("log4j.configurationFile","classpath:log/log4j2.xml");
    }

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private HttpClient http;
    private final String domain = "http://127.0.0.1:83/course";
    private final Logger logger = LogManager.getLogger(UserControllerTest.class);

    private String userToken = "6c26be66efe641ddbddd0c5d7270ad55";

    @Before
    public void setUp() {
        this.http = new HttpClient();
    }

    @Test
    public void list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/list";
        Map param = new HashMap();
        //param.put("offset",0);
        //param.put("limit",2);
        //param.put("fit",0);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void get() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/get";
        Map param = new HashMap();
        param.put("id",29);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void hot() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/hot/list";
        Map param = new HashMap();
        param.put("offset",0);
        param.put("limit",2);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void sign() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/sign";
        Map param = new HashMap();
        param.put("id",33);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void today_sign() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/sign/today/list";
        Map param = new HashMap();
        param.put("date","20170923");
        param.put("id",19);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void appreciation_comment() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/appreciation/comment";
        Map param = new HashMap();
        param.put("id",14);
        param.put("text","world");
        param.put("audio",new HashMap(){{
            this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
            this.put("time",1238);
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void appreciation_comment_list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/appreciation/comment/list";
        Map param = new HashMap();
        param.put("id",19);
        param.put("offset",0);
        param.put("limit",20);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }


    @Test
    public void appreciation_comment_reply() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/appreciation/comment/reply";
        Map param = new HashMap();
        param.put("id",13);
        param.put("quote",1);
        param.put("text","不带音频的 la la la ..");
//        param.put("audio",new HashMap(){{
//            this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
//            this.put("time",1238);
//        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void unlock() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/unlock";
        Map param = new HashMap();
        param.put("id",16);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void reward() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/reward";
        Map param = new HashMap();
        param.put("id",14);
        param.put("fee",1.23);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }


}
