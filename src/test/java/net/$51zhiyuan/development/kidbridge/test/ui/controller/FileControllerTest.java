package net.$51zhiyuan.development.kidbridge.test.ui.controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

public class FileControllerTest {

    {
        System.setProperty("log4j.configurationFile","classpath:log/log4j2.xml");
    }

    private Auth auth;
    private HttpClient http;
    private final String domain = "http://127.0.0.1/file";
    private final Logger logger = LogManager.getLogger(UserControllerTest.class);
    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private String userToken = "cf58511544a143568dd541ab15c2277e";

    @Before
    public void setUp() {
        this.http = new HttpClient();
        this.auth = Auth.create(Configuration.property(Configuration.QINIU_ACCESSKEY),Configuration.property(Configuration.QINIU_SECRETKEYSPEC));
    }

    @Test
    public void getToken(){
        Map<String,Object> policy = new HashMap();
        policy.put("scope", Configuration.property(Configuration.QINIU_BUCKET));
        policy.put("deadline", System.currentTimeMillis() / 1000 + 36000);
        String token = this.auth.uploadTokenWithPolicy(policy);
        this.logger.debug("qiniu upload token: {}",token);
        //jJEBx6Xxlz-7vmKhIAcx73Pbkxv5kcdEP4u3lSS1:YsFNDxokaT0DjmSp9RvUQ6H-6tQ=:eyJzY29wZSI6InBpY3R1cmVib29rIiwiZGVhZGxpbmUiOjE1MDIyMTgwMTF9
    }

    @Test
    public void upload() throws QiniuException {
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String token = "jJEBx6Xxlz-7vmKhIAcx73Pbkxv5kcdEP4u3lSS1:vwr-WG_JLxJyFHeFN9nGdfpZTpI=:eyJzY29wZSI6InBpY3R1cmVib29rIiwiZGVhZGxpbmUiOjE1MTM0NDc4MzJ9";
            //File file = new File("C:\\Users\\Spring\\Desktop\\APP 资料样本\\APP 资料样本\\Clifford's Field Day 音频切片");
            //for(File f : file.listFiles()){
        String s = "C:\\Users\\Administrator\\Desktop\\60@2x.png";
                Response response = uploadManager.put(new File(s), null, token);
                System.out.println("name:" + "key: " + response.jsonToMap().get("key"));
            //}
    }

    @Test
    public void upload_token_get() throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        String api = "/upload/token";
        Map param = new HashMap();
        param.put("type",2);
        param.put("option",new HashMap(){{
            this.put("id",1);
        }});
        String response = this.http.doPost(this.domain+api,this.userToken,param);
        this.logger.debug("request: " + this.kidbridgeObjectMapper.writeValueAsString(param));
        this.logger.debug("response: " + response);
    }

    @Test
    public void get(){
        String key = "d0e1b588f87554395408c22c2391c513c";
        FileInfo fileInfo = null;
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(this.auth, cfg);
        try {
            fileInfo = bucketManager.stat(Configuration.property(Configuration.QINIU_BUCKET), key);
            System.out.println(fileInfo.hash);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }

}
