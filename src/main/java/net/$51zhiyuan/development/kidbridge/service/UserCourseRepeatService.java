package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.UserCourseRepeat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户课程跟读
 */
@Service
@Deprecated
public class UserCourseRepeatService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.userCourseRepeat.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public UserCourseRepeat get(UserCourseRepeat param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<UserCourseRepeat> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new UserCourseRepeat());
    }

    @Deprecated
    public List<UserCourseRepeat> list(UserCourseRepeat param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<UserCourseRepeat> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }
}
