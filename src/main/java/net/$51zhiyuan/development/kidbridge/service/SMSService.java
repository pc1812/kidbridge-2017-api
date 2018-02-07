package net.$51zhiyuan.development.kidbridge.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSystemException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.Util;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 短信
 */
@Service
public class SMSService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 容联云短信发送
     * @param phone
     * @param datas
     * @return
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    private Map sendCN(String[] phone,String[] datas) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();
        // 容联云请求必要参数
        String appId = Configuration.property(Configuration.SMS_CN_APP_ID);
        String templet = Configuration.property(Configuration.SMS_CN_TEMPLET_VERIFICATION_CODE);
        String accountSid = Configuration.property(Configuration.SMS_CN_ACCOUNT_SID);
        String authToken = Configuration.property(Configuration.SMS_CN_AUTH_TOKEN);
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String url = String.format("https://app.cloopen.com:8883/2013-12-26/Accounts/%s/SMS/TemplateSMS?sig=%s",accountSid, DigestUtils.md5Hex(accountSid+authToken+timestamp));
        Header[] headers = new Header[]{new BasicHeader("Authorization", Base64.encodeBase64String(String.format("%s:%s",accountSid,timestamp).getBytes())),new BasicHeader("Accept","application/json")};
        String requestBody = kidbridgeObjectMapper.writeValueAsString(new HashMap(){{
            this.put("to", StringUtils.join(phone,','));
            this.put("appId",appId);
            this.put("templateId",templet);
            this.put("datas",datas);
        }});
        Map responseBody = kidbridgeObjectMapper.readValue(new HttpClient().doPost(url,headers,requestBody),HashMap.class);
        if(!responseBody.get("statusCode").toString().equals("000000")){
            if(responseBody.get("statusCode").toString().equals("160040")){
                throw new KidbridgeSimpleException("当天该号码接受短信验证码次数已达上限，请明日再试 ~");
            }
            throw new KidbridgeSystemException("短信服务异常: " + responseBody.get("statusCode"));
        }
        Map result = (Map) responseBody.get("templateSMS");
        return result;
    }

    /**
     * twilio国外短信发送
     * @param phone
     * @param code
     * @return
     */
    private Message sendUS(String phone,String code){
        Twilio.init(Configuration.property(Configuration.SMS_US_ACCOUNT_SID), Configuration.property(Configuration.SMS_US_AUTH_TOKEN));
        Message message = Message.creator(new PhoneNumber(phone),
                new PhoneNumber(Configuration.property(Configuration.SMS_US_NUMBER)),String.format("[kidbridge]Your SMS verification code is: %s",code)) .create();
        return message;
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    public Map verificationCode(String phone) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        ValueOperations valueOperations = this.redisTemplate.opsForValue();
        String storageKey = String.format("%s:%s",Configuration.SYSTEM_SMS_NAMESPACE,phone);
        if(valueOperations.get(storageKey) != null){
            throw new KidbridgeSimpleException("短信验证码获取频繁 ~");
        }
        int activeSecond = 120;
        String code = String.valueOf(Util.nextInt(100000,999999));
        Map result = null;
        // 手机号码国别判断
        if(phone.indexOf("+86") == 0){
            this.sendCN(new String[]{phone.substring(3)},new String[]{code,activeSecond+"秒"}); // 中国号码，容联云短信发送
        }else if(phone.indexOf("+1") == 0){
            this.sendUS(phone,code); // 美国号码，twilio短信发送
        }else{
            throw new KidbridgeSimpleException("手机号码格式有误 ~");
        }
        // 通过redis实现防止频繁获取验证码
        valueOperations.set(storageKey,code,activeSecond, TimeUnit.SECONDS);
        return result;
    }
}
