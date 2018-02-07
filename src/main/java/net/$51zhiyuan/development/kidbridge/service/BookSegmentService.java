package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.BookSegment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 绘本段落
 */

@Service
public class BookSegmentService {

    private final Logger logger = LogManager.getLogger(BookSegmentService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.bookSegment.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public BookSegment get(BookSegment param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<BookSegment> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new BookSegment());
    }

    /**
     * 获取绘本下的段落列表，一个绘本有多个跟读段落
     * @param param
     * @return
     */
    public List<BookSegment> list(BookSegment param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<BookSegment> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }
}
