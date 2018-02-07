package net.$51zhiyuan.development.kidbridge.test.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserControllerTest {

    {
        System.setProperty("log4j.configurationFile","classpath:log/log4j2.xml");
    }

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private HttpClient http;
    private final String domain = "http://127.0.0.1:83/user";
    private final Logger logger = LogManager.getLogger(UserControllerTest.class);

    private String userToken = "afa6f2a076234832a44bb47c922e89be";

    @Before
    public void setUp() {
        this.http = new HttpClient();
    }


    @Test
    public void login() throws Exception {
        String api = "/login";
        Map param = new HashMap();
        param.put("phone","+8613656690321");
        param.put("password",DigestUtils.md5Hex(String.format("%s:%s:%s","+8613656690321","12345678",Configuration.SYSTEM_SIGN_SALT)));
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void auth() throws Exception {
        String api = "/auth";
        Map param = new HashMap();
        param.put("code","011l4sA92EI8YN0QTBy92vdqA92l4sA9");
        param.put("type",0);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void logout() throws Exception {
        String api = "/logout";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void balance() throws Exception {
        String api = "/balance";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void bonus() throws Exception {
        String api = "/bonus";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void info() throws Exception {
        String api = "/info";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void receiving() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/receiving";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_receiving() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/receiving";
        Map param = new HashMap();
        param.put("contact","1");
        param.put("phone","2");
        param.put("region","3");
        param.put("street","4");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book";
        Map param = new HashMap();
        param.put("free",0);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course";
        Map param = new HashMap();
        param.put("offset",0);
        param.put("limit",20);
        param.put("status",0);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void medal() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/medal";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void my() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/my";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_token() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/token";
        Map param = new HashMap();
        param.put("id",91);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_token() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/course/book/repeat/token";
        Map param = new HashMap();
        param.put("course",45);
        param.put("book",16);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat";
        Map param = new HashMap();
        param.put("repeat",new HashMap(){{
            this.put("token","1b7df857d1364f63ac0c0000f1c3f812");
            this.put("segment",new ArrayList(){{
                this.add(new HashMap(){{
                    this.put("id",57);
                    this.put("audio",new HashMap(){{
                        this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
                        this.put("time",123);
                    }});
                }});
                this.add(new HashMap(){{
                    this.put("id",58);
                    this.put("audio",new HashMap(){{
                        this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
                        this.put("time",123);
                    }});
                }});
                this.add(new HashMap(){{
                    this.put("id",59);
                    this.put("audio",new HashMap(){{
                        this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
                        this.put("time",123);
                    }});
                }});
            }});
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/list";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/list";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_book_repeat_list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/book/repeat/list";
        Map param = new HashMap();
        param.put("id",45);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_rank() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/rank";
        Map param = new HashMap();
        param.put("id",16);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_detail() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/detail";
        Map param = new HashMap();
        param.put("id",34);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_detail() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/detail";
        Map param = new HashMap();
        param.put("id",16);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_like() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/like";
        Map param = new HashMap();
        param.put("id",103);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_like() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/like";
        Map param = new HashMap();
        param.put("id",21);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_comment_list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/comment/list";
        Map param = new HashMap();
        param.put("limit",10);
        param.put("offset",0);
        param.put("id",30);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_comment() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/comment";
        Map param = new HashMap();
        param.put("id",49);
        param.put("text","差不多了。 ");
        param.put("audio",new HashMap(){{
            this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
            this.put("time",1238);
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_comment_reply() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/comment/reply";
        Map param = new HashMap();
        param.put("id",49);
        param.put("quote",134);
        param.put("text","差不多了");
        param.put("audio",new HashMap(){{
            this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
            this.put("time",1238);
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_comment() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/comment";
        Map param = new HashMap();
        param.put("id",9);
        param.put("text","还是再确认一下 ");
        param.put("audio",new HashMap(){{
            this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
            this.put("time",1238);
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_comment_reply() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/comment/reply";
        Map param = new HashMap();
        param.put("id",9);
        param.put("quote",2);
        param.put("text","飘过");
//        param.put("audio",new HashMap(){{
//            this.put("source","FizMGEyPzgFPYu3orpGXkgdBJkch");
//            this.put("time",1238);
//        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_comment_list() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/comment/list";
        Map param = new HashMap();
        param.put("id",20);
        param.put("offset",0);
        param.put("limit",20);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_schedule() throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String api = "/course/schedule/list";
        Map param = new HashMap();
        param.put("id",33);
        param.put("date","201709");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void course_repeat_teacher_comment() throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String api = "/course/repeat/teacher/comment";
        Map param = new HashMap();
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }
    @Test
    public void register() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/register";
        Map param = new HashMap();
        param.put("phone","+8618768150321");
        param.put("password","helloworld");
        param.put("code","511753");
        //param.put("bind","54f9eb66b7c34c62bb37101ec3ecd733");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void register_verification() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/register/verification";
        Map param = new HashMap();
        param.put("phone","+8618768150321");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_password_verification() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/password/verification";
        Map param = new HashMap();
        param.put("phone","+8613656690321");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_password() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/password";
        Map param = new HashMap();
        param.put("phone","+8613656690321");
        param.put("code","498583");
        param.put("password","hell0w0rld");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_head() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/head";
        Map param = new HashMap();
        param.put("head","FhRPAf4xGMMKgtscsHe8ASsyRwNB");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_nickname() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/nickname";
        Map param = new HashMap();
        param.put("nickname","Spring .");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_signature() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/signature";
        Map param = new HashMap();
        param.put("signature","lalala .");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void update_birthday() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String api = "/update/birthday";
        Map param = new HashMap();
        param.put("birthday","19940321");
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: {}",this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: {}",response);
    }

    @Test
    public void book_repeat_reward() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/reward";
        Map param = new HashMap();
        param.put("id",62);
        param.put("fee",1.23);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void course_repeat_reward() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/course/repeat/reward";
        Map param = new HashMap();
        param.put("id",26);
        param.put("fee",1.23);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }


    @Test
    public void book_repeat_delete() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/book/repeat/delete";
        Map param = new HashMap();
        param.put("id",new ArrayList(){{
            this.add(67);
            this.add(68);
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void balance_ratio() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/balance/ratio";
        Map param = new HashMap();
        param.put("bonus",1241);
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void t() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String respone = this.http.doGet("http://10.10.10.182:8080/ZhiChuang/ajaxfindfile.do?inid=38&content=金");
    }

}