package net.$51zhiyuan.development.kidbridge.service;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.enums.PushType;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import net.$51zhiyuan.development.kidbridge.ui.model.CommentPush;
import net.$51zhiyuan.development.kidbridge.ui.model.SystemPush;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息通知
 */
@Service
public class PushService {

    private JPushClient pushClient;

    private final Logger logger = LogManager.getLogger(PushService.class);

    private final KidbridgeObjectMapper objectMapper = new KidbridgeObjectMapper();

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 向用户发送评论通知消息
     * @param userId
     * @param message
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    public void sendCommentPush(Integer userId,CommentPush message) throws APIConnectionException, APIRequestException, JsonProcessingException {
        this.send(PushType.COMMENT,userId,message);
    }

    /**
     * 向用户发送系统通知消息
     * @param userId
     * @param message
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    public void sendSystemPush(Integer userId,SystemPush message) throws APIConnectionException, APIRequestException, JsonProcessingException {
        this.send(PushType.SYSTEM,userId,message);
    }

    /**
     * 极光推送，消息发送
     * @param type
     * @param userId
     * @param body
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    private PushResult send(PushType type, Integer userId, Object body) throws JsonProcessingException {
        PushResult result = null;
        Map message = new HashMap();
        message.put("type",type.ordinal());
        message.put("body",body);
        PushPayload payload = this.getAliasPushPayload(userId,message);
        try{
            //this.getPushClient().deleteAlias(DigestUtils.md5Hex(String.format("%s:%s",userId,Configuration.SYSTEM_SIGN_SALT)),"ios,android");
            // 客户端状态验证
            this.getPushClient().sendPushValidate(payload);
            result = this.getPushClient().sendPush(payload);
        }catch (Exception e){
            // 可能会因为客户端用户离线状态，导致消息无法送达，此时将消息存储在redis中，当用户活跃时再发送
            this.redisTemplate.opsForList().rightPush(String.format("%s:%s",Configuration.SYSTEM_MESSAGE_QUEUE_NAMESPACE,userId),message);
        }
        this.logger.debug("push, id: {}, message: {}", userId,this.objectMapper.writeValueAsString(message));
        return result;
    }

    /**
     * 构建JPushClient对象
     * @return
     */
    public JPushClient getPushClient() {
        if(this.pushClient == null){
            this.pushClient = new JPushClient(Configuration.property(Configuration.JIGUANG_MASTER_SECRET), Configuration.property(Configuration.JIGUANG_APP_KEY),null, ClientConfig.getInstance());
        }
        return this.pushClient;
    }

    /**
     * 极光，别名推送，别名为用户hash后的ID值
     * @param userId
     * @param message
     * @return
     * @throws JsonProcessingException
     */
    public PushPayload getAliasPushPayload(Integer userId, Map message) throws JsonProcessingException {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(DigestUtils.md5Hex(String.format("%s:%s",userId,Configuration.SYSTEM_SIGN_SALT))))
                .setMessage(
                        Message.newBuilder()
                        .setMsgContent(this.objectMapper.writeValueAsString(message)).build()
                )
                .build();
    }

    /**
     * 获取redis存储的消息通知，推送
     * @param userId
     * @throws JsonProcessingException
     */
    public void sendQueue(Integer userId) throws JsonProcessingException {
        Map message;
        while ((message = (Map) this.redisTemplate.opsForList().leftPop(String.format("%s:%s",Configuration.SYSTEM_MESSAGE_QUEUE_NAMESPACE,userId))) != null){
            PushPayload payload = this.getAliasPushPayload(userId,message);
            try{
                this.getPushClient().sendPush(payload);
            }catch (Exception e){
                this.redisTemplate.opsForList().leftPush(String.format("%s:%s",Configuration.SYSTEM_MESSAGE_QUEUE_NAMESPACE,userId),message);
                break;
            }
        }
    }

    public void setPushClient(JPushClient pushClient) {
        this.pushClient = pushClient;
    }
}
