package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Course;
import net.$51zhiyuan.development.kidbridge.ui.model.CourseHot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 热门课程
 */

@Service
public class CourseHotService {

    private final Logger logger = LogManager.getLogger(CourseHotService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.courseHot.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public CourseHot get(CourseHot param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<CourseHot> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new CourseHot());
    }

    @Deprecated
    public List<CourseHot> list(CourseHot param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<CourseHot> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 获取热门课程
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Course> hot(Course param,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "hot",param,page);
    }
}
