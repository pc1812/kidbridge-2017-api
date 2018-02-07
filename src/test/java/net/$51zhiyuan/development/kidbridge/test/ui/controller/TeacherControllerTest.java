package net.$51zhiyuan.development.kidbridge.test.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class TeacherControllerTest {

    {
        System.setProperty("log4j.configurationFile","classpath:log/log4j2.xml");
    }

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private HttpClient http;
    private final String domain = "http://127.0.0.1/teacher";
    private final Logger logger = LogManager.getLogger(TeacherControllerTest.class);

    private String userToken = "d7e6ee8c9bc84894af002f72434e09c2";

    @Before
    public void setUp() {
        this.http = new HttpClient();
    }


    @Test
    public void course_list() throws Exception {
        String api = "/course/list";
        Map param = new HashMap();
        //param.put("status",0);
        param.put("offset",0);
        param.put("limit",10);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }


}