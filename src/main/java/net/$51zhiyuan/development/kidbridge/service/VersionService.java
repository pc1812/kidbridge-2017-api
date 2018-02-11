package net.$51zhiyuan.development.kidbridge.service;

import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import net.$51zhiyuan.development.kidbridge.ui.model.MessageType;
import net.$51zhiyuan.development.kidbridge.ui.model.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hkhl.cn on 2018/2/10.
 */
@Service
public class VersionService {

    private final Logger logger = LogManager.getLogger(VersionService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.version.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public Version check(String device){
        if(!(device.toLowerCase().equals("ios") || device.toLowerCase().equals("android"))){
            throw new KidbridgeSimpleException("未知的终端设备 ~");
        }
        return this.lastVersion(device.equals("ios") ? 1 : 0);
    }

    public Version lastVersion(Integer device){
        return this.sqlSessionTemplate.selectOne(this.namespace + "lastVersion",new Version().setDevice(device));
    }
}
