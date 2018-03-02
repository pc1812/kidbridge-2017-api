package net.$51zhiyuan.development.kidbridge.service;

import net.$51zhiyuan.development.kidbridge.ui.model.Article;
import net.$51zhiyuan.development.kidbridge.ui.model.IOSIAPProduct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章
 */
@Service
public class IOSIAPProductService {

    private final Logger logger = LogManager.getLogger(IOSIAPProductService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.IOSIAPProduct.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;


    /**
     * 获取价格档次列表
     * @return
     */
    public List<IOSIAPProduct> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Article());
    }

}
