package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Article;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文章
 */
@Service
public class ArticleService {

    private final Logger logger = LogManager.getLogger(ArticleService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.article.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取单个文章详情
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Article get(Article param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    /**
     * 获取文章列表
     * @return
     */
    @Deprecated
    public List<Article> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Article());
    }

    /**
     * 获取是文章列表
     * @param param
     * @return
     */
    @Deprecated
    public List<Article> list(Article param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    /**
     * 获取文章列表
     * @param param
     * @param page
     * @return
     */
    @Deprecated
    public List<Article> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }
}
