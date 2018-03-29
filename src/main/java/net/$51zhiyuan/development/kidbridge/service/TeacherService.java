package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Teacher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 教师
 */
@Service
public class TeacherService {

    private final Logger logger = LogManager.getLogger(TeacherService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.teacher.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取教师信息
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Teacher get(Teacher param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    /**
     * 获取某个教师的课程信息列表
     * @param userId
     * @param status
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List course(Integer userId,Integer status, PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "course",new HashMap(){{
            this.put("userId",userId);
            this.put("status",status);
        }},page);
    }
}
