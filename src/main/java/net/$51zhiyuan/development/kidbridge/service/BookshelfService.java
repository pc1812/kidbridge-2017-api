package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.BookCopyright;
import net.$51zhiyuan.development.kidbridge.ui.model.Bookshelf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 书单
 */

@Service
public class BookshelfService {

    private final Logger logger = LogManager.getLogger(BookshelfService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.bookshelf.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取单个书单详情，一个书单包含多个绘本
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Bookshelf get(Bookshelf param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<Bookshelf> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new BookCopyright());
    }

    @Deprecated
    public List<Bookshelf> list(BookCopyright param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<Bookshelf> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 获取热门书单列表
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Bookshelf> hot(PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "hot",new Bookshelf(),page);
    }

    /**
     * 获取今日打卡书单列表
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Bookshelf> recommend(PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "recommend",new Bookshelf(),page);
    }

}
