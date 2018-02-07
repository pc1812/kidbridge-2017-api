package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSystemException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import net.$51zhiyuan.development.kidbridge.ui.model.Authc;
import net.$51zhiyuan.development.kidbridge.ui.model.MessageType;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 授权登录
 */

@Service
public class AuthcService {

    private final Logger logger = LogManager.getLogger(AuthcService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.authc.";

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
    private final HttpClient httpClient = new HttpClient();

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 第三方授权登录处理
     * @param code 第三方授权码
     * @param type 授权类别 0微信
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    public User authcLogin(String code, Integer type) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        if(StringUtils.isBlank(code)){
            throw new KidbridgeSimpleException("未知的授权 ~");
        }
        if(type == null){
            throw new KidbridgeSimpleException("未知的授权类型 ~");
        }
        // 此处为通过客户端用户授权后获取到的code，获取对应的微信openid信息
        String authcCode = this.getAuthCode(code, type);
        // 通过openid查询出本平台记录的授权信息
        Authc auth = this.get(new Authc(){
            @Override
            public String getCode() {
                return authcCode;
            }

            @Override
            public Integer getType() {
                return type;
            }
        });
        if(auth == null || auth.getUser() == null){
            // 如果某个openid在本平台没有记录，说明这个第三方用户还未绑定平台中的用户
            // 引导用户注册到本平台，绑定手机号码
            String token = UUID.randomUUID().toString().replace("-","");
            Map authc = new HashMap();
            authc.put("code",authcCode);
            authc.put("type",type);
            // 临时存储授权信息，后续用户完成手机号码绑定后，携带此token内容，取到对应的授权信息，完成用户注册及第三方授权信息的关联绑定
            this.redisTemplate.opsForValue().set(String.format("%s:%s",Configuration.SYSTEM_AUTHC_NAMESPACE,token),authc,10, TimeUnit.MINUTES);
            throw new KidbridgeSimpleException(MessageType.UNUSER,new HashMap(){{
                this.put("token",token);
            }},"用户不存在 ~");
        }
        return auth.getUser();
    }

    /**
     * 获取第三方的用户标识， 微信为openid
     * @param code 客户端取到的授权码
     * @param type 授权类别 0微信
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    private String getAuthCode(String code,Integer type) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String authcCode;
        switch (type){
            // 微信授权
            case 0:
                Map responseBody = this.kidbridgeObjectMapper.readValue(this.httpClient.doGet(String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", Configuration.property(Configuration.WECHAT_APP_ID),Configuration.property(Configuration.WECHAT_APP_SECRET),code)), HashMap.class);
                if(responseBody.get("errcode") != null){
                    if(responseBody.get("errcode").toString().equals("40029")){
                        throw new KidbridgeSimpleException("非法的微信授权码 ~");
                    }
                    if(responseBody.get("errcode").toString().equals("40163")){
                        throw new KidbridgeSimpleException("微信授权码已失效 ~");
                    }
                    throw new KidbridgeSystemException("微信授权异常 - " + responseBody);
                }
                authcCode = (String) responseBody.get("openid"); // 微信平台的用户标识为openid
                break;
            default:
                throw new KidbridgeSimpleException("未知的登录授权类型 ~");
        }
        return authcCode;
    }

    /**
     * 保存授权信息
     * @param authc
     * @return
     */
    public Integer save(Authc authc){
        return this.sqlSessionTemplate.insert(this.namespace + "save",authc);
    }

    /**
     * 获取授权信息
     * @param param
     * @return
     */
    public Authc get(Authc param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<Authc> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Authc());
    }

    @Deprecated
    public List<Authc> list(Authc param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<Authc> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

}
