package net.$51zhiyuan.development.kidbridge.ui.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.service.CourseHotService;
import net.$51zhiyuan.development.kidbridge.service.CourseService;
import net.$51zhiyuan.development.kidbridge.service.UserCourseService;
import net.$51zhiyuan.development.kidbridge.ui.model.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseHotService courseHotService;

    @Autowired
    private UserCourseService userCourseService;

    /**
     * 课程详情
     * @param param id：课程编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/get")
    Message get(@RequestBody Map<String,Object> param){
        // 课程详情
        Course course = this.courseService.get(new Course(){
            @Override
            public Integer getId() {
                return (int)param.get("id");
            }
        });
        // 当前用户是否已解锁该绘本
        UserCourse belong = this.userCourseService.get(new UserCourse(){
            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return (int)SecurityUtils.getSubject().getPrincipal();
                    }
                };
            }

            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getId() {
                        return (int)param.get("id");
                    }
                };
            }
        });
        return new Message(new HashMap(){{
            this.put("course",course); // 课程信息
            this.put("belong",(belong == null ? -1 : belong.getId())); // 当前用户是否已解锁该绘本，0未解锁 1已解锁
        }});
    }

    /**
     * 课程列表
     * @param param fit：适合年龄段，0：3-5, 1：6-8, 2：9-12
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    Message list(@RequestBody Map<String,Object> param){
        Course course = new Course();
        course.setUser(new User(){
            @Override
            public Integer getId() {
                return (int)SecurityUtils.getSubject().getPrincipal();
            }
        });
        if(param.get("fit") != null){
            // 按年龄段筛选课程
            course.setFit((int) param.get("fit"));
        }
        // 课程列表
        List<Course> courseList = this.courseService.list(course,new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(courseList);
    }

    /**
     * 热门课程列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/hot/list")
    Message hot_list(@RequestBody Map<String,Object> param){
        Course course = new Course();
        course.setUser(new User(){
            @Override
            public Integer getId() {
                return (int)SecurityUtils.getSubject().getPrincipal();
            }
        });
        // 热门课程列表
        List<Course> courseList = this.courseHotService.hot(course,new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(courseList);
    }

    /**
     * 课程打卡信息
     * @param param id：课程编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/sign")
    Message book_list(@RequestBody Map<String,Object> param){
        Course course = this.courseService.getCourseBookList((int)param.get("id"));
        return new Message(course);
    }

    /**
     * 课程今日打卡，用户跟读信息
     * @param param id：课程编号，date：日期 yyyyMMdd 格式
     * @return
     */
    @ResponseBody
    @RequestMapping("/sign/today/list")
    Message today_sign(@RequestBody Map<String,Object> param){
        // 用户跟读列表
        List<Map> userCourseRepeatList = this.courseService.getTodaySignList((int)param.get("id"),(String) param.get("date"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(new HashMap(){{
            this.put("sign",userCourseRepeatList);
        }});
    }

    /**
     * 课程赏析评论息提交
     * @param param id：课程编号，text：文本内容，audio音频内容
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation/comment")
    Message appreciation_comment(@RequestBody Map<String,Object> param){
        this.courseService.insAppreciationComment((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 课程赏析评论信息列表
     * @param param id：课程编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation/comment/list")
    Message appreciation_comment_list(@RequestBody Map<String,Object> param){
        // 评论列表
        List<CourseComment> bookCommentList = this.courseService.getAppreciationCommentList((int)param.get("id"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(new HashMap(){{
            this.put("comment",bookCommentList);
        }});
    }

    /**
     * 课程赏析富文本H5页面
     * @param id 课程编号
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    @RequestMapping("/richtext/{id}")
    ModelAndView outline(@PathVariable String id) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        ModelAndView view = new ModelAndView("/richtext/course");
        // 课程详情信息
        Course course = this.courseService.get(new Course(){
            @Override
            public Integer getId() {
                return Integer.valueOf(id);
            }
        });
        // 获取七牛云存储的富文本内容
        String html = this.courseService.getRichtext(course.getRichText());
        view.addObject("html",html.replace("data-w","width")); // 富文本内容
        view.addObject("course",course); // 课程信息
        return view;
    }

    /**
     * 课程赏析评论，子回复提交
     * @param param id：课程编号，quote：父评论id，text：评论文本，audio评论音频
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation/comment/reply")
    Message appreciation_comment_reply(@RequestBody Map<String,Object> param){
        this.courseService.insAppreciationCommentReply((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(int)param.get("quote"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 课程解锁
     * @param param id：课程编号
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping("/unlock")
    Message unlock(@RequestBody Map<String,Object> param) throws APIConnectionException, APIRequestException, JsonProcessingException {
        this.courseService.unlock((int) SecurityUtils.getSubject().getPrincipal(),(int) param.get("id"));
        return new Message();
    }

    /**
     * 课程打赏
     * @param param fee：打赏金额，id：课程编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/reward")
    Message reward(@RequestBody Map<String,Object> param){
        this.courseService.reward((int)SecurityUtils.getSubject().getPrincipal(), BigDecimal.valueOf(Double.parseDouble(param.get("fee").toString())),(int)param.get("id"));
        return new Message();
    }

}
