package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.BookCopyright;
import net.$51zhiyuan.development.kidbridge.ui.model.BookshelfDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 书单详情
 */

@Service
@Deprecated
public class BookshelfDetailService {

    private final Logger logger = LogManager.getLogger(BookshelfDetailService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.bookshelfDetail.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public BookshelfDetail get(BookshelfDetail param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<BookshelfDetail> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new BookCopyright());
    }

    @Deprecated
    public List<BookshelfDetail> list(BookCopyright param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<BookshelfDetail> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

}
