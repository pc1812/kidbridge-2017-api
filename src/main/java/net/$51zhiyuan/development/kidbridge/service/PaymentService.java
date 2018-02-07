package net.$51zhiyuan.development.kidbridge.service;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSystemException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.Util;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.module.json.KidbridgeObjectMapper;
import net.$51zhiyuan.development.kidbridge.ui.model.Recharge;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支付
 */
@Service
public class PaymentService {

    private final HttpClient httpClient = new HttpClient();

    private final XmlMapper xmlMapper = new XmlMapper();

    private final KidbridgeObjectMapper kidbridgeObjectMapper = new KidbridgeObjectMapper();

    private String wechatPaymentCallbackUrl;

    private String alipayPaymentCallbackUrl;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private BillService billService;

    @Autowired
    private UserService userService;

    @Autowired
    private RechargeService rechargeService;

    /**
     * 微信支付，统一下单
     * @param userId
     * @param body
     * @param totalFee
     * @param spbillCreateIp
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    private String wechatUnifiedorder(Integer userId,String body,Integer totalFee,String spbillCreateIp) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParseException {
        String transaction = String.valueOf(this.getNextOrderId());
        this.rechargeService.add(transaction,userId,(new BigDecimal(String.valueOf(totalFee)).divide(new BigDecimal("100"))),0,2);
        Map<String,Object> param = new HashMap<>();
        // 应用id
        param.put("appid", Configuration.property(Configuration.WECHAT_APP_ID));
        // 商户id
        param.put("mch_id",Configuration.property(Configuration.WECHAT_MCH_ID));
        // 随机字符串
        param.put("nonce_str",Util.generateString(32,true));
        // 售卖商品描述
        param.put("body",body);
        // 平台订单编号
        param.put("out_trade_no",transaction);
        // 支付金额，单位分
        param.put("total_fee",totalFee);
        // 客户端真实IP
        param.put("spbill_create_ip",spbillCreateIp);
        // 支付成功通知回调地址
        param.put("notify_url",this.getWechatPaymentCallbackUrl());
        // 支付类别 ，默认APP支付
        param.put("trade_type","APP");
        // 签名
        param.put("sign",this.getWechatPaymentSign(param));
        Map response = this.xmlMapper.readValue(this.httpClient.doPost("https://api.mch.weixin.qq.com/pay/unifiedorder",this.xmlMapper.writer().withRootName("xml").writeValueAsString(param)),HashMap.class);
        if(!response.get("return_code").toString().equals("SUCCESS") || !response.get("result_code").toString().equals("SUCCESS")){
            throw new KidbridgeSystemException("微信支付异常: " + response.get("err_code") + ", " + response.get("err_code_des") );
        }
        return (String) response.get("prepay_id");
    }

    /**
     * 支付宝支付，统一下单
     * @param userId
     * @param body
     * @param totalFee
     * @return
     * @throws JsonProcessingException
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    private String alipayUnifiedorder(Integer userId,String body,BigDecimal totalFee) throws ParseException {
        String transaction = String.valueOf(this.getNextOrderId());
        this.rechargeService.add(transaction,userId,totalFee,0,1);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                Configuration.property(Configuration.ALIPAY_APP_ID), Configuration.property(Configuration.ALIPAY_APP_PRIVATE_KEY),
                "json",
                "UTF-8",
                Configuration.property(Configuration.ALIPAY_ALIPAY_PUBLIC_KEY),
                "RSA2"
        );
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        // 售卖商品描述
        model.setSubject(body);
        // 平台订单编号
        model.setOutTradeNo(transaction);
        // 支付金额
        model.setTotalAmount(totalFee.toPlainString());
        // 产品代码 ，默认APP支付
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        // 支付成功通知回调地址
        request.setNotifyUrl(this.getAlipayPaymentCallbackUrl());
        AlipayTradeAppPayResponse response;
        try{
            response = alipayClient.sdkExecute(request);
        }catch (Exception e){
            throw new KidbridgeSystemException(e.getMessage(),e);
        }
        return response.getBody();
    }

    /**
     * 构建支付宝支付参数
     * @param userId
     * @param fee
     * @return
     * @throws AlipayApiException
     * @throws ParseException
     * @throws JsonProcessingException
     */
    public String alipayPayment(Integer userId, BigDecimal fee) throws ParseException, JsonProcessingException {
        return this.alipayUnifiedorder(userId,"藤桥教育-余额充值",fee);
    }

    /**
     * 构建微信支付参数
     * @param userId
     * @param fee
     * @param spbillCreateIp
     * @return
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws SAXException
     * @throws KeyStoreException
     * @throws KeyManagementException
     * @throws ParseException
     */
    public Map wechatPayment(Integer userId, BigDecimal fee, String spbillCreateIp) throws IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, ParserConfigurationException, TransformerException, SAXException, KeyStoreException, KeyManagementException, ParseException {
        String prepayId = this.wechatUnifiedorder(userId,"藤桥教育-余额充值", new BigDecimal(fee.toPlainString()).multiply(new BigDecimal("100")).intValue(), spbillCreateIp);
        Map<String,Object> param = new HashMap<>();
        // 必填支付参数
        param.put("appid",Configuration.property(Configuration.WECHAT_APP_ID));
        param.put("partnerid",Configuration.property(Configuration.WECHAT_MCH_ID));
        param.put("prepayid",prepayId);
        param.put("package","Sign=WXPay");
        param.put("noncestr",Util.generateString(32,true));
        param.put("timestamp",System.currentTimeMillis() / 1000);
        param.put("sign",this.getWechatPaymentSign(param));
        return param;
    }

    /**
     * 支付宝支付通知回调
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized String alipayCallback(Map param) throws AlipayApiException {
        // 平台订单编号
        String transaction = (String) param.get("out_trade_no");
        boolean signVerified = false;
        try{
            // 参数签名验证
            signVerified = AlipaySignature.rsaCheckV1(
                    param,
                    Configuration.property(Configuration.ALIPAY_ALIPAY_PUBLIC_KEY),
                    "UTF-8",
                    "RSA2"
            );
        } catch (AlipayApiException e) {
            throw new KidbridgeSystemException("支付宝支付签名验证异常: " + e);
        }
        if(!signVerified){
            throw new KidbridgeSystemException("支付宝支付签名验证失败: " + param);
        }
        // 额外通过平台订单编号查询支付宝平台订单信息真实性，此步骤与签名可二选一
        AlipayTradeQueryResponse orderQuery = this.alipayOrderQuery(transaction);
        if(!orderQuery.getTradeStatus().equals("TRADE_SUCCESS")){
            throw new KidbridgeSystemException("支付宝支付回调，未知的交易状态: " + param);
        }
        // 通过订单编号获取平台支付订单
        Recharge recharge = this.rechargeService.get(transaction);
        BigDecimal fee = new BigDecimal(orderQuery.getTotalAmount());
        // 支付金额校验
        if(recharge.getFee().compareTo(fee) != 0){
            throw new KidbridgeSystemException("支付宝支付回调，异常的交易金额 " + param);
        }
        // 订单状态校验，是否为待支付状态
        if(recharge.getStatus().intValue() == 0){
            // 更新订单状态为已支付状态
            this.rechargeService.update(new Recharge(){
                @Override
                public String getId() {
                    return transaction;
                }

                @Override
                public Integer getStatus() {
                    return 1;
                }
            });
           // 更新用户余额信息
           this.userService.assetsUpdate(recharge.getUser().getId(),fee,0,2,new HashMap(){{
               this.put("transaction",transaction);
           }});

        }
        return "success";
    }

    /**
     * 微信支付成功回调
     * @param requestBody
     * @return
     * @throws IOException
     * @throws UnrecoverableKeyException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized String wechatCallback(String requestBody) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Map param = this.xmlMapper.readValue(requestBody,HashMap.class);
        if(!param.get("return_code").toString().equals("SUCCESS") || !param.get("result_code").toString().equals("SUCCESS")){
            throw new KidbridgeSystemException("微信支付回调异常: " + param.get("return_msg") + " | " + param.get("err_code_des"));
        }
        // 获取回调请求参数中的签名
        String sign = (String) param.get("sign");
        param.remove("sign");
        String validateSign = this.getWechatPaymentSign(param);
        // 签名校验
        if(!sign.equals(validateSign)){
            throw new KidbridgeSystemException("微信支付回调，签名验证失败: " + param);
        }
        // 获取回调参数中的订单编号
        String transaction = (String) param.get("out_trade_no");
        // 通过订单编号查询微信支付平台中的信息，验证真实性，此处理与签名二选一
        Map orderQuery = this.wechatOrderQuery(transaction);
        if(!orderQuery.get("trade_state").toString().equals("SUCCESS")){
            throw new KidbridgeSystemException("微信支付回调，未知的交易状态: " + param);
        }
        // 通过订单编号查询订单
        Recharge recharge = this.rechargeService.get(transaction);
        BigDecimal fee = (new BigDecimal((String) orderQuery.get("total_fee")).divide(new BigDecimal("100")));
        // 订单金额校验
        if(recharge.getFee().compareTo(fee) != 0){
            throw new KidbridgeSystemException("微信支付回调，异常的交易金额 " + param);
        }
        // 订单状态校验，是否为待支付状态
        if(recharge.getStatus().intValue() == 0){
            // 更新订单状态为已支付状态
            this.rechargeService.update(new Recharge(){
                @Override
                public String getId() {
                    return transaction;
                }

                @Override
                public Integer getStatus() {
                    return 1;
                }
            });
            // 更新用户余额信息
            this.userService.assetsUpdate(recharge.getUser().getId(),fee,0,2,new HashMap(){{
                this.put("transaction",transaction);
            }});
        }
        return this.xmlMapper.writer().withRootName("xml").writeValueAsString(new HashMap(){{
            this.put("return_code","SUCCESS");
            this.put("return_msg","OK");
        }});
    }

    /**
     * 生成平台订单编号
     * @return
     * @throws ParseException
     */
    private synchronized Long getNextOrderId() {
        ValueOperations<String,Object> valueOperations = this.redisTemplate.opsForValue();
        long prefix = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        // redis中当前存储的订单编号
        long currentOrderId = valueOperations.increment(Configuration.SYSTEM_INCREMENT_TRANSACTION_NAMESPACE,0);
        // 下一个订单编号
        long nextOrderId =  ((currentOrderId != 0 && Integer.parseInt(String.valueOf(currentOrderId).substring(0,8)) == prefix) ? valueOperations.increment(Configuration.SYSTEM_INCREMENT_TRANSACTION_NAMESPACE,1) : valueOperations.increment(Configuration.SYSTEM_INCREMENT_TRANSACTION_NAMESPACE,(prefix * 100000000 + 1) - currentOrderId));
        return nextOrderId;
    }

    /**
     * 获取微信支付成功回调地址
     * @return
     */
    private String getWechatPaymentCallbackUrl() {
        String protocol = "http";
        String domain = Configuration.property(Configuration.DOMAIN);
        if(StringUtils.isBlank(this.wechatPaymentCallbackUrl)){
            Pattern pattern = Pattern.compile("(.*?)\\|(.*?)");
            Matcher matcher = pattern.matcher(domain);
            if (matcher.matches()){
                protocol = matcher.group(1);
                domain = matcher.group(2);
            }
            this.wechatPaymentCallbackUrl = String.format("%s://%s%s",protocol,domain,Configuration.SYSTEM_WECHAT_PAYMENT_CALLBACK_MAPPING);
        }
        return this.wechatPaymentCallbackUrl;
    }

    /**
     * 获取支付宝支付成功回调地址
     * @return
     */
    private String getAlipayPaymentCallbackUrl() {
        String protocol = "http";
        String domain = Configuration.property(Configuration.DOMAIN);
        if(StringUtils.isBlank(this.alipayPaymentCallbackUrl)){
            Pattern pattern = Pattern.compile("(.*?)\\|(.*?)");
            Matcher matcher = pattern.matcher(domain);
            if (matcher.matches()){
                protocol = matcher.group(1);
                domain = matcher.group(2);
            }
            this.alipayPaymentCallbackUrl = String.format("%s://%s%s",protocol,domain,Configuration.SYSTEM_ALIPAY_PAYMENT_CALLBACK_MAPPING);
        }
        return this.alipayPaymentCallbackUrl;
    }

    /**
     * 微信支付参数签名
     * @param param
     * @return
     */
    private String getWechatPaymentSign(Map<String,Object> param){
        SortedMap<String, Object> sortedMap = new TreeMap<>(param);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> map : sortedMap.entrySet()) {
            if (StringUtils.isBlank(map.getValue().toString())) {
                continue;
            }
            stringBuilder.append(String.format("%s=%s&", map.getKey(), map.getValue()));
        }
        stringBuilder.append(String.format("%s=%s", "key", Configuration.property(Configuration.WECHAT_API_KEY)));
        return DigestUtils.md5Hex(stringBuilder.toString()).toUpperCase();

    }

    /**
     * 支付宝平台订单查询
     * @param orderId
     * @return
     * @throws AlipayApiException
     */
    private AlipayTradeQueryResponse alipayOrderQuery(String orderId) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",Configuration.property(Configuration.ALIPAY_APP_ID),Configuration.property(Configuration.ALIPAY_APP_PRIVATE_KEY),"json","UTF-8",Configuration.property(Configuration.ALIPAY_ALIPAY_PUBLIC_KEY),"RSA2");
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(orderId);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(!response.isSuccess()){
            throw new KidbridgeSystemException("支付宝支付订单查询异常: " + response.getMsg());
        }
        return response;
    }

    /**
     * 微信支付平台订单查询
     * @param orderId
     * @return
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    private Map wechatOrderQuery(String orderId) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        Map param = new HashMap();
        param.put("appid",Configuration.property(Configuration.WECHAT_APP_ID));
        param.put("mch_id",Configuration.property(Configuration.WECHAT_MCH_ID));
        param.put("out_trade_no",orderId);
        param.put("nonce_str",Util.generateString(32,true));
        param.put("sign",this.getWechatPaymentSign(param));
        Map response = this.xmlMapper.readValue(this.httpClient.doPost("https://api.mch.weixin.qq.com/pay/orderquery",this.xmlMapper.writer().withRootName("xml").writeValueAsString(param)),HashMap.class);
        if(!response.get("return_code").toString().equals("SUCCESS") || !response.get("result_code").toString().equals("SUCCESS")){
            throw new KidbridgeSystemException("微信支付订单查询异常: " + response.get("return_msg"));
        }
        return response;
    }


}
