package net.$51zhiyuan.development.kidbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.ui.model.Article;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAPProduct;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAPRecharge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
public class IOSIAPRechargeService {

    private final Logger logger = LogManager.getLogger(IOSIAPRechargeService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.IOSIAPRecharge.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 通过苹果交易编号查询本平台支付记录
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public IOSIAPRecharge get(String id){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",id);
    }

    /**
     * 保存支付记录
     * @param iosiapRecharge
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(IOSIAPRecharge iosiapRecharge){
        this.sqlSessionTemplate.insert(this.namespace + "save",iosiapRecharge);
    }

}
