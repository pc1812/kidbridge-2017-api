package net.$51zhiyuan.development.kidbridge.test.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import net.$51zhiyuan.development.kidbridge.service.PushService;
import net.$51zhiyuan.development.kidbridge.test.ui.controller.UserControllerTest;
import net.$51zhiyuan.development.kidbridge.ui.model.SystemPush;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class PushServiceTest {

    {
        System.setProperty("log4j.configurationFile","classpath:log/log4j2.xml");
    }

    private PushService pushService;
    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private final Logger logger = LogManager.getLogger(UserControllerTest.class);

    @Before
    public void setUp() {
        this.pushService = new PushService();
    }

    @Test
    public void send() throws APIConnectionException, APIRequestException, JsonProcessingException {
        SystemPush systemPush = new SystemPush();
        systemPush.setMessage("hello, world .");
        this.pushService.sendSystemPush(35,systemPush);
    }
}
