package net.$51zhiyuan.development.kidbridge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSystemException;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.ui.model.Article;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAPProduct;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAPRecharge;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章
 */
@Service
public class IOSIAPProductService {

    private final Logger logger = LogManager.getLogger(IOSIAPProductService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.IOSIAPProduct.";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = new HttpClient();

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private IOSIAPRechargeService iosiapRechargeService;

    @Autowired
    private UserService userService;

    public IOSIAPProduct get(IOSIAPProduct iosiapProduct){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",iosiapProduct);
    }

    /**
     * 获取价格档次列表
     * @return
     */
    public List<IOSIAPProduct> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Article());
    }

    /**
     * 苹果支付凭证验证
     * @param userId
     * @param receiptData
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    @Transactional
    public void validate(Integer userId,String receiptData) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        if(StringUtils.isBlank(receiptData)){
            throw new KidbridgeSystemException("未知的支付凭证");
        }
        // 请求苹果服务器，验证凭证
        // 沙盒环境：https://sandbox.itunes.apple.com/verifyReceipt
        // 正式环境：https://buy.itunes.apple.com/verifyReceipt
        Map response = this.objectMapper.readValue(httpClient.doPost("https://sandbox.itunes.apple.com/verifyReceipt",this.objectMapper.writeValueAsString(new HashMap(){{
            this.put("receipt-data",receiptData);
        }})),HashMap.class);
        // 凭证状态，0为支付成功
        int status = (int) response.get("status");
        if(status != 0){
            throw new KidbridgeSystemException("非法的支付凭证状态");
        }
        Map receipt = (Map) response.get("receipt");
        List<Map> inAppList = (List<Map>) receipt.get("in_app");
        Map payment = inAppList.get(0);
        String transactionId = (String) payment.get("transaction_id");
        // 通过苹果交易号查询本平台记录，判断是否存在
        IOSIAPRecharge iosiapRecharge = this.iosiapRechargeService.get(transactionId);
        if(iosiapRecharge != null){
            throw new KidbridgeSimpleException("支付凭证已失效");
        }
        int quantity = Integer.valueOf((String) payment.get("quantity"));
        // 查询产品信息
        IOSIAPProduct product = this.get(new IOSIAPProduct(){
            @Override
            public String getProduct() {
                return (String) payment.get("product_id");
            }
        });
        if(product == null){
            throw new KidbridgeSystemException("未知的产品编号");
        }
        // 内购充值记录保存
        this.iosiapRechargeService.save(new IOSIAPRecharge(){
            @Override
            public String getId() {
                return transactionId;
            }

            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }

            @Override
            public BigDecimal getFee() {
                return product.getPrice();
            }

            @Override
            public IOSIAPProduct getIosiapProduct() {
                return new IOSIAPProduct(){
                    @Override
                    public Integer getId() {
                        return product.getId();
                    }
                };
            }

            @Override
            public Integer getQuantity() {
                return quantity;
            }
        });
        // 记录流水，更新用户金额
        this.userService.assetsUpdate(userId,product.getPrice(),0,11,new HashMap(){{
            this.put("transaction",transactionId);
        }});
    }

}
