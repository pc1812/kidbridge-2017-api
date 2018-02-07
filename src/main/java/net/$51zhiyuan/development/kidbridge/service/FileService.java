package net.$51zhiyuan.development.kidbridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 音频文件上传
 */
@Service
public class FileService {

    private Auth auth;

    private String callbackUrl;

    private long uploadTokenQuietTime = -1;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();

    @Deprecated
    public FileInfo get(String key){
        FileInfo fileInfo = null;
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(this.getAuth(), cfg);
        try {
            fileInfo = bucketManager.stat(Configuration.property(Configuration.QINIU_BUCKET), key);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return fileInfo;
    }

    @Deprecated
    public Boolean exist(String key){
        return (this.get(key) != null);
    }

    @Deprecated
    public void validCallback(String callbackAuthHeader,String callbackBodyType){
        boolean validCallback = this.getAuth().isValidCallback(callbackAuthHeader,this.getCallbackUrl(),null,callbackBodyType);
        if(!validCallback){
            throw new KidbridgeSimpleException("非法的上传回调请求");
        }
    }

    @Deprecated
    public void validBase(Object option){
        if(option == null){
            throw new KidbridgeSimpleException("非法的上传参数");
        }
    }

    @Deprecated
    public void validUserRepeat(Object option){
        this.validBase(option);
        Map repeat = (Map) option;
        if(repeat == null || repeat.get("repeat") == null || StringUtils.isBlank((String)repeat.get("repeat"))){
            throw new KidbridgeSimpleException("非法的上传参数");
        }
        Object userRepeat = this.redisTemplate.opsForValue().get(String.format("%s:%s",Configuration.SYSTEM_REPEAT_NAMESPACE, repeat.get("repeat")));
        if(userRepeat == null){
            throw new KidbridgeSimpleException("无效的跟读会话 ~");
        }
    }

    @Deprecated
    public void validUserBookRepeat(Object option){
        this.validBase(option);
        Map book = (Map) option;
        if(book == null || book.get("id") == null){
            throw new KidbridgeSimpleException("非法的上传参数");
        }
    }

    @Deprecated
    public void validUserCourseRepeat(Object option){
        this.validBase(option);
        Map book = (Map) option;
        if(book == null || book.get("id") == null){
            throw new KidbridgeSimpleException("非法的上传参数");
        }
    }

    @Deprecated
    public void validBookAppreciation(Object option){
        this.validBase(option);
        Map book = (Map) option;
        if(book == null || book.get("id") == null){
            throw new KidbridgeSimpleException("非法的上传参数");
        }
    }

    @Deprecated
    public void validCourseAppreciation(Object option){
        this.validBase(option);
        Map course = (Map) option;
        if(course == null || course.get("id") == null){
            throw new KidbridgeSimpleException("非法的上传参数");
        }
    }

    @Deprecated
    public void validHead(Object option){
        this.validBase(option);
    }

    /**
     * 获取七牛云上传token
     * @param principal
     * @param type
     * @param option
     * @return
     * @throws JsonProcessingException
     */
    public String getUploadToken(Integer principal,Integer type,Object option) throws JsonProcessingException {
//        switch (type == null ? -1 : type.intValue()){
//            case 0:
//                this.validUserRepeat(option);
//                break;
//            case 1:
//                this.validUserBookRepeat(option);
//                break;
//            case 2:
//                this.validBookAppreciation(option);
//                break;
//            case 3:
//                this.validUserRepeat(option);
//                break;
//            case 4:
//                this.validCourseAppreciation(option);
//                break;
//            case 5:
//                this.validUserCourseRepeat(option);
//                break;
//            case 6:
//
//                break;
//            default:
//                throw new KidbridgeSimpleException("未知的文件上传类别 ~");
//        }
        String token = this.getAuth().uploadTokenWithPolicy(this.getDefaultPolicy(principal,type,option));
        return token;
    }

    /**
     * 构造上传参数
     * @param principal
     * @param type
     * @param option
     * @return
     * @throws JsonProcessingException
     */
    public Map getDefaultPolicy(Object principal,Integer type,Object option) {
        Map<String,Object> policy = new HashMap();
//        policy.put("callbackUrl", this.getCallbackUrl());
//        policy.put("callbackBody", this.kidbridgeObjectMapper.writeValueAsString(new HashMap(){{
//            this.put("key","$(key)");
//            this.put("hash","$(etag)");
//            this.put("bucket","$(bucket)");
//            this.put("fsize","$(fsize)");
//            this.put("principal",principal);
//            this.put("type",type);
//            this.put("option",option);
//        }}));
//        policy.put("callbackBodyType", "application/json");
//        policy.put("scope", String.format("%s:%s",Configuration.property(Configuration.QINIU_BUCKET),fileKey));
        policy.put("scope", Configuration.property(Configuration.QINIU_BUCKET));
        //policy.put("insertOnly", 1);
        // 上传时间限制
        policy.put("deadline", this.getUploadTokenQuietTime());
        return policy;
    }

    @Deprecated
    public String getCallbackUrl() {
        String protocol = "http";
        String domain = Configuration.property(Configuration.DOMAIN);
        if(StringUtils.isBlank(this.callbackUrl)){
            Pattern pattern = Pattern.compile("(.*?)\\|(.*?)");
            Matcher matcher = pattern.matcher(domain);
            if (matcher.matches()){
                protocol = matcher.group(1);
                domain = matcher.group(2);
            }
            this.callbackUrl = String.format("%s://%s%s",protocol,domain,Configuration.SYSTEM_QINIU_UPLOAD_CALLBACK_MAPPING);
        }
        return this.callbackUrl;
    }

    /**
     * 获取系统配置的七牛云资源上传时间限制
     * @return
     */
    public long getUploadTokenQuietTime() {
        if(this.uploadTokenQuietTime == -1){
            this.uploadTokenQuietTime = Long.valueOf(Configuration.property(Configuration.QINIU_UPLOAD_TOKEN_QUIET_TIME));
        }
        return (System.currentTimeMillis() / 1000 + this.uploadTokenQuietTime);
    }

    /**
     * 构建Auth对象
     * @return
     */
    public Auth getAuth(){
        if(this.auth == null){
            this.auth = Auth.create(Configuration.property(Configuration.QINIU_ACCESSKEY),Configuration.property(Configuration.QINIU_SECRETKEYSPEC));
        }
        return this.auth;
    }

}
