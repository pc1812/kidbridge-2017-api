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

public class PaymentControllerTest {

    {
        System.setProperty("log4j.configurationFile","classpath:log/log4j2.xml");
    }

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private HttpClient http;
    private final String domain = "http://127.0.0.1/payment";
    private final Logger logger = LogManager.getLogger(UserControllerTest.class);

    private String userToken = "0585ed80d2354cc68e56d1a8ca3a6043";

    @Before
    public void setUp() {
        this.http = new HttpClient();
    }

    @Test
    public void wechat() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/wechat";
        Map param = new HashMap();
        param.put("fee",13.14);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void wechat_callback() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/wechat/callback";
        Map param = new HashMap();
        param.put("fee",13.14);
        String response = this.http.doPost("http://127.0.0.1/payment/alipay/callback",this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void alipay() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/alipay";
        Map param = new HashMap();
        param.put("fee",13.14);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

}
