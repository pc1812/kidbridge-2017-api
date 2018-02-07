package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.CourseDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程详情
 */

@Service
@Deprecated
public class CourseDetailService {

    private final Logger logger = LogManager.getLogger(CourseDetailService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.courseDetail.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public CourseDetail get(CourseDetail param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<CourseDetail> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new CourseDetail());
    }

    @Deprecated
    public List<CourseDetail> list(CourseDetail param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<CourseDetail> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }
}
