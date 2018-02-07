package net.$51zhiyuan.development.kidbridge.service;

import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.ui.model.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据库表config配置信息
 */
@Service
public class ConfigService {

    public static final String CUSTOMER_SERVICE = "customerservice";

    private final Logger logger = LogManager.getLogger(ConfigService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.config.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public String get(String key){
        // 根据参数键取出对应参数信息
        Config config = this.sqlSessionTemplate.selectOne(this.namespace + "get",key);
        if(config == null){
            throw new KidbridgeSimpleException("未知的参数键");
        }
        return config.getValue();
    }
}
