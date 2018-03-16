package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Article;
import net.$51zhiyuan.development.kidbridge.ui.model.SearchRecord;
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
public class SearchRecordService {

    private final Logger logger = LogManager.getLogger(SearchRecordService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.searchRecord.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 记录用户搜索的关键词
     * @param searchRecord
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(SearchRecord searchRecord){
        this.sqlSessionTemplate.insert(this.namespace + "add",searchRecord);
    }

    @Deprecated
    public List<SearchRecord> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list");
    }

}
