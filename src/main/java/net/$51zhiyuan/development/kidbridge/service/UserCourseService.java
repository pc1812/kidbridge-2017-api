package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Course;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import net.$51zhiyuan.development.kidbridge.ui.model.UserCourse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户课程
 */
@Service
public class UserCourseService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.userCourse.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取单个用户的课程
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserCourse get(UserCourse param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    /**
     * 获取某一课程被哪些用户解锁过
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<UserCourse> get(Integer courseId){
        return this.sqlSessionTemplate.selectList(this.namespace + "get",new UserCourse(){
            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getId() {
                        return courseId;
                    }
                };
            }
        });
    }

    @Deprecated
    public List<UserCourse> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new UserCourse());
    }

    @Deprecated
    public List<UserCourse> list(UserCourse param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<UserCourse> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 获取用户解锁过的课程列表
     * @param userId
     * @param status
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Course> userCourse(Integer userId,Integer status,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "userCourse",new UserCourse(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }

            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getStatus() {
                        return status;
                    }
                };
            }
        },page);
    }

    /**
     * 判断用户是否解锁了某一课程
     * @param userId
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean exist(Integer userId,Integer courseId){
        return (((int)this.sqlSessionTemplate.selectOne(this.namespace + "exist",new UserCourse(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }
            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getId() {
                        return courseId;
                    }
                };
            }
        })) > 0);
    }

    /**
     * 用户课程解锁新增
     * @param userId
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer add(Integer userId,Integer courseId){
        return this.sqlSessionTemplate.insert(this.namespace + "add",new UserCourse(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }

            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getId() {
                        return courseId;
                    }
                };
            }
        });
    }
}
