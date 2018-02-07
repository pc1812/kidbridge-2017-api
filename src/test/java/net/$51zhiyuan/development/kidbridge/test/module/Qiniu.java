package net.$51zhiyuan.development.kidbridge.test.module;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Qiniu {

    private Client http = new Client();

    @Test
    public void createBucket() throws QiniuException {
        Auth auth = Auth.create(Configuration.property(Configuration.QINIU_ACCESSKEY),Configuration.property(Configuration.QINIU_SECRETKEYSPEC));
        String request = String.format("http://rs.qiniu.com/mkbucketv2/%s/region/%s",UrlSafeBase64.encodeToString(Configuration.property(Configuration.QINIU_BUCKET)),"z0");
        StringMap header = auth.authorization(request);
        Response response = http.post(request, (byte[]) null,header);
        System.out.println(response.bodyString());
    }

    @Test
    public void getDomain() throws QiniuException {
        Auth auth = Auth.create(Configuration.property(Configuration.QINIU_ACCESSKEY),Configuration.property(Configuration.QINIU_SECRETKEYSPEC));
        String request = String.format("http://rsf.qbox.me/list?bucket=%s",Configuration.property(Configuration.QINIU_BUCKET));
        StringMap header = auth.authorization(request);
        Response response = http.post(request, (byte[]) null,header);
        System.out.println(response.bodyString());
    }



    @Test
    public void getUploadToken() {
        Auth auth = Auth.create(Configuration.property(Configuration.QINIU_ACCESSKEY),Configuration.property(Configuration.QINIU_SECRETKEYSPEC));
        //String upToken = auth.uploadToken(Field.Qiniu_STIRAGE_BUCKET);
        long deadline = System.currentTimeMillis() / 1000 + 3600;
        Map putPolicy = new HashMap();
        //putPolicy.put("callbackUrl", "http://service.51zhiyuan.net/test/upload.do");
        //putPolicy.put("callbackBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        //putPolicy.put("callbackBodyType", "application/json");
        //putPolicy.put("callbackUrl", "http://service.51zhiyuan.net/test/upload.do");
        //KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
//        putPolicy.put("callbackBody", kidbridgeObjectMapper.writeValueAsString(new HashMap(){{
//            this.put("key","$(key)");
//            this.put("hash","$(etag)");
//            this.put("bucket","$(bucket)");
//            this.put("fsize","$(fsize)");
//            this.put("principal","1231");
//        }}));
//        putPolicy.put("callbackBodyType", "application/json");
//        putPolicy.put("scope", "picturebook:x");
//        putPolicy.put("insertOnly", 1);
//        putPolicy.put("deadline", deadline);

        String upToken = auth.uploadTokenWithPolicy(putPolicy);
        System.out.println(upToken);
    }

    @Test
    public void down(){
//        Auth auth = Auth.create(Configuration.property(Configuration.QINIU_ACCESSKEY),Configuration.property(Configuration.QINIU_SECRETKEYSPEC));
//        String key = auth.privateDownloadUrl("http://ou396fprt.bkt.clouddn.com/Fk5HHpucAymrMIasyuAEo48DVIRu",3600);
//        System.out.println(key);


    }

    @Test
    public void upload() {
        //构造一个带指定Zone对象的配置类
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\Administrator\\Desktop\\60@2x.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "x";
        try {
            long begin = System.currentTimeMillis();
            Response response = uploadManager.put(localFilePath, null, "jJEBx6Xxlz-7vmKhIAcx73Pbkxv5kcdEP4u3lSS1:Q2yqIWMqq_TmpGQrNCEY1MHhwFM=:e30=");
            long end = System.currentTimeMillis();
            System.out.println(((end - begin) / 1000));
            System.out.println("response: " + response.bodyString());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println("key: " + putRet.key);
            System.out.println("hash: " + putRet.hash);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
