package net.$51zhiyuan.development.kidbridge.module;

import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 系统参数配置信息
 */
@Component
public class Configuration {

    private static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    // 系统签名盐值，与客户端盐值对应
    public static final String SYSTEM_SIGN_SALT = "Picture_B00k";
    // 签名请求字段
    public static final String SYSTEM_REQUEST_SIGN = "sign";
    // token请求字段
    public static final String SYSTEM_REQUEST_TOKEN = "token";
    // 版本号请求字段
    public static final String SYSTEM_REQUEST_VERSION = "version";
    // 设备名称
    public static final String SYSTEM_REQUEST_DEVICE = "device";
    // 时间戳请求字段
    public static final String SYSTEM_REQUEST_TIMESTAMP = "timestamp";
    // 七牛云文件上传回调接口
    public static final String SYSTEM_QINIU_UPLOAD_CALLBACK_MAPPING = "/file/upload/callback";
    // 微信支付回调接口
    public static final String SYSTEM_WECHAT_PAYMENT_CALLBACK_MAPPING = "/payment/wechat/callback";
    // 支付宝支付回调接口
    public static final String SYSTEM_ALIPAY_PAYMENT_CALLBACK_MAPPING = "/payment/alipay/callback";
    // 分页请求参数
    public static final String SYSTEM_PAGINATION_OFFSET = "offset";
    public static final String SYSTEM_PAGINATION_LIMIT = "limit";
    // redis session信息存储路径
    public static final String SYSTEM_SESSION_NAMESPACE = "kidbridge:session";
    // redis session与用户关联信息存储路径
    public static final String SYSTEM_SESSION_MAPPING_NAMESPACE = "kidbridge:session:mapping";
    // redis 用户打卡跟读临时数据存储路径
    public static final String SYSTEM_REPEAT_NAMESPACE = "kidbridge:repeat";
    // redis 短信验证码与手机号关联信息存储路径
    public static final String SYSTEM_SMS_NAMESPACE = "kidbridge:sms";
    // redis 授权登录注册临时数据存储路径
    public static final String SYSTEM_AUTHC_NAMESPACE = "kidbridge:authc";
    // redis 订单编号存储路径
    public static final String SYSTEM_INCREMENT_TRANSACTION_NAMESPACE = "kidbridge:increment:transaction";
    // redis 历史消息队列存储路径
    public static final String SYSTEM_MESSAGE_QUEUE_NAMESPACE = "kidbridge:message.queue";
    // 系统登录接口
    public static final String[] SYSTEM_SHIRO_LOGIN_URL = new String[]{"/user/login","/user/auth","/user/register"};

    // session过期时间， 默认7天
    public static final String SESSION_TIMEOUT = "kidbridge.session.timeout";
    // 系统域名
    public static final String DOMAIN = "kidbridge.domain";
    // 默认系统级异常信息提示
    public static final String EXCEPTION_TIP = "kidbridge.exception_tip";
    // 七牛云bucket参数
    public static final String QINIU_BUCKET = "kidbridge.qiniu.bucket";
    // 七牛云accesskey参数
    public static final String QINIU_ACCESSKEY = "kidbridge.qiniu.accesskey";
    // 七牛云secretkeyspec参数
    public static final String QINIU_SECRETKEYSPEC = "kidbridge.qiniu.secretkeyspec";
    // 七牛云上传时间限制
    public static final String QINIU_UPLOAD_TOKEN_QUIET_TIME = "kidbridge.qiniu.uploadTokenQuietTime";
    // 七牛云bucket域名
    public static final String QINIU_BUCKET_DOMAIN = "kidbridge.qiniu.bucket.domain";
    // 容联云国内短信accountSid
    public static final String SMS_CN_ACCOUNT_SID = "kidbridge.sms.cn.accountSid";
    // 容联云国内短信authToken
    public static final String SMS_CN_AUTH_TOKEN = "kidbridge.sms.cn.authToken";
    // 容联云国内短信appId
    public static final String SMS_CN_APP_ID = "kidbridge.sms.cn.appId";
    // 容联云国内短信验证码模板编号
    public static final String SMS_CN_TEMPLET_VERIFICATION_CODE = "kidbridge.sms.cn.templet.verificationCode";

    // twilio国外短信accountSID
    public static final String SMS_US_ACCOUNT_SID = "kidbridge.sms.us.accountSID";
    // twilio国外短信authToken
    public static final String SMS_US_AUTH_TOKEN = "kidbridge.sms.us.authToken";
    // twilio国外短信number
    public static final String SMS_US_NUMBER = "kidbridge.sms.us.number";
    // 微信appId
    public static final String WECHAT_APP_ID = "kidbridge.wechat.appId";
    // 微信appSecret
    public static final String WECHAT_APP_SECRET= "kidbridge.wechat.appSecret";
    // 微信mchId
    public static final String WECHAT_MCH_ID = "kidbridge.wechat.mchId";
    // 微信apiKey
    public static final String WECHAT_API_KEY = "kidbridge.wechat.apiKey";
    // 支付宝appId
    public static final String ALIPAY_APP_ID = "kidbridge.alipay.appId";
    // 支付宝appPrivateKey
    public static final String ALIPAY_APP_PRIVATE_KEY = "kidbridge.alipay.appPrivateKey";
    // 支付宝alipayPublicKey
    public static final String ALIPAY_ALIPAY_PUBLIC_KEY = "kidbridge.alipay.alipayPublicKey";
    // 极光推送appKey
    public static final String JIGUANG_APP_KEY = "kidbridge.jiguang.appKey";
    // 极光推送masterSecret
    public static final String JIGUANG_MASTER_SECRET = "kidbridge.jiguang.masterSecret";
    // 系统金额积分兑换比率，默认1:100
    public static final String BONUS_TO_BALANCE_RATIO = "kidbridge.bonus.balanceRatio";
    // 系统用户跟读增加水滴数
    public static final String BONUS_INCREASE_BOOK_REPEAT = "kidbridge.bonus.increase.bookRepeat";
    // 系统课程跟读增加水滴数
    public static final String BONUS_INCREASE_COURSE_REPEAT = "kidbridge.bonus.increase.courseRepeat";

    // 系统用户跟读分享增加水滴数
    public static final String BONUS_INCREASE_BOOK_REPEAT_SHARE = "kidbridge.bonus.increase.bookRepeatShare";
    // 系统课程跟读分享增加水滴数
    public static final String BONUS_INCREASE_COURSE_REPEAT_SHARE = "kidbridge.bonus.increase.courseRepeatShare";

    private static PropertiesFactoryBean PROPERTIES_FACTORY;

    /**
     * 从配置文件中读取配置信息
     * @param key
     * @return
     */
    public static String property(String key) {
        String value = null;
        try{
            if(PROPERTIES_FACTORY == null){
                ClassPathResource properties = new ClassPathResource("config/global.properties");
                if(!properties.exists()){
                    throw new KidbridgeSystemException("configuration [ classpath:config/global.properties ] file cannot be found");
                }
                PROPERTIES_FACTORY = new PropertiesFactoryBean();
                PROPERTIES_FACTORY.setFileEncoding("utf-8");
                PROPERTIES_FACTORY.setLocation(properties);
                PROPERTIES_FACTORY.afterPropertiesSet();
            }
            value = PROPERTIES_FACTORY.getObject().getProperty(key);
        }catch (IOException io){
            LOGGER.error(io.getMessage(),io);
        }
        if(StringUtils.isBlank(value)){
            throw new KidbridgeSystemException("configuration key [ "+key+" ] Non-existent");
        }
        return value;
    }

}
