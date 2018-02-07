package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.BookCopyright;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 绘本版权
 */

@Service
@Deprecated
public class BookCopyrightService {

    private final Logger logger = LogManager.getLogger(BookCopyrightService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.bookCopyright.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public BookCopyright get(BookCopyright param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<BookCopyright> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new BookCopyright());
    }

    @Deprecated
    public List<BookCopyright> list(BookCopyright param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<BookCopyright> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

}
