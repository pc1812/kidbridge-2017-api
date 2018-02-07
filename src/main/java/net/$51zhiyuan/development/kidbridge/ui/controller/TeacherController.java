package net.$51zhiyuan.development.kidbridge.ui.controller;

import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.service.TeacherService;
import net.$51zhiyuan.development.kidbridge.ui.model.Course;
import net.$51zhiyuan.development.kidbridge.ui.model.Message;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 教师
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 教师用户的课程列表
     * @param param status：0：未开课(报名中)，1：已开课，2：已结束
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/list")
    Message medal(@RequestBody Map<String,Object> param){
        // 教师课程列表
        List<Course> courseList = this.teacherService.course((int)SecurityUtils.getSubject().getPrincipal(),(Integer) param.get("status"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(courseList);
    }

}
