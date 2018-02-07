package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Medal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 勋章
 */
@Service
public class MedalService {

    private final Logger logger = LogManager.getLogger(MedalService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.medal.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public Medal get(Medal param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    /**
     * 获取勋章列表
     * @return
     */
    public List<Medal> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Medal());
    }

    @Deprecated
    public List<Medal> list(Medal param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<Medal> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 根据积分获取当前勋章信息
     * @param bonus
     * @return
     */
    public Medal now(Integer bonus){
        return this.sqlSessionTemplate.selectOne(this.namespace + "now",bonus);
    }

    /**
     * 根据积分获取下一个勋章信息
     * @param bonus
     * @return
     */
    public Medal future(Integer bonus){
        return this.sqlSessionTemplate.selectOne(this.namespace + "future",bonus);
    }

}
