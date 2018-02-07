package net.$51zhiyuan.development.kidbridge.service;

import net.$51zhiyuan.development.kidbridge.ui.model.Recharge;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 余额充值
 */
@Service
public class RechargeService {

    private final Logger logger = LogManager.getLogger(RechargeService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.recharge.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 新增充值订单
     * @param id
     * @param userId
     * @param fee
     * @param feeType
     * @param method
     * @return
     */
    public Integer add(String id, Integer userId, BigDecimal fee,Integer feeType,Integer method){
        return this.sqlSessionTemplate.insert(this.namespace + "add",new Recharge(){
            /**
             * 订单编号
             * @return
             */
            @Override
            public String getId() {
                return id;
            }

            /**
             * 下单用户
             * @return
             */
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }

            /**
             * 下单金额
             * @return
             */
            @Override
            public BigDecimal getFee() {
                return fee;
            }

            /**
             * 金额类型，0现金，1水滴
             * @return
             */
            @Override
            public Integer getFeeType() {
                return feeType;
            }

            /**
             * 订单状态，默认未支付
             * @return
             */
            @Override
            public Integer getStatus() {
                return 0;
            }

            /**
             * 支付方式0，本平台，1支付宝，2微信
             * @return
             */
            @Override
            public Integer getMethod() {
                return method;
            }
        });
    }

    /**
     * 更新订单
     * @param recharge
     * @return
     */
    public Integer update(Recharge recharge){
        return this.sqlSessionTemplate.update(this.namespace + "update",recharge);
    }

    /**
     * 获取订单
     * @param id
     * @return
     */
    public Recharge get(String id){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",id);
    }

}
