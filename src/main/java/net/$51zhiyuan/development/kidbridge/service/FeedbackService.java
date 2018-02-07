package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Feedback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 意见反馈
 */
@Service
public class FeedbackService {

    private final Logger logger = LogManager.getLogger(FeedbackService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.feedback.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public Feedback get(Feedback param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<Feedback> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Feedback());
    }

    @Deprecated
    public List<Feedback> list(Feedback param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<Feedback> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 新增反馈内容
     * @param param
     * @return
     */
    public Integer add(Feedback param){
        return this.sqlSessionTemplate.insert(this.namespace + "add",param);
    }
}
