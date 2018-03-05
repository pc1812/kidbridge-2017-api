package net.$51zhiyuan.development.kidbridge.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.http.HttpClient;
import net.$51zhiyuan.development.kidbridge.ui.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 课程
 */

@Service
public class CourseService {

    private final Logger logger = LogManager.getLogger(CourseService.class);

    private final HttpClient httpClient = new HttpClient();

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.course.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
    private PushService pushService;

    @Autowired
    private UserBookService userBookService;

    /**
     * 获取提前预习的课程
     * @return
     */
    public List<Map> getPreviewList(){
        return this.sqlSessionTemplate.selectList(this.namespace + "getPreviewList");
    }

    /**
     * 获取单个课程详情
     * @param param
     * @return
     */
    public Course get(Course param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<Course> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Course());
    }

    @Deprecated
    public List<Course> list(Course param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    /**
     * 获取课程列表信息
     * @param param
     * @param page
     * @return
     */
    public List<Course> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    @Deprecated
    public List<Course> teacherCourse(Integer userId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "teacherCourse",userId, page);
    }

    /**
     * 获取课程中的富文本信息，富文本内容存储在七牛云
     * @param resKey
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    public String getRichtext(String resKey) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String html = this.httpClient.doGet(String.format("http://%s/%s", Configuration.property(Configuration.QINIU_BUCKET_DOMAIN),resKey));
        return html;
    }

    /**
     * 获取课程下的绘本列表信息
     * @param courseId
     * @return
     */
    public Course getCourseBookList(Integer courseId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getCourseBookList",courseId);
    }

    /**
     * 用户解锁课程
     * @param userId
     * @param courseId
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    public void unlock(Integer userId,Integer courseId) throws APIConnectionException, APIRequestException, JsonProcessingException {
        if(this.userCourseService.exist(userId,courseId)){
            throw new KidbridgeSimpleException("已解锁该课程 ~");
        }
        // 新增解锁记录
        this.userCourseService.add(userId,courseId);
        // 获取课程详情信息
        Course course = this.get(new Course(){
            @Override
            public Integer getId() {
                return courseId;
            }
        });
        // 扣费更新余额
        this.userService.assetsUpdate(userId,course.getPrice().negate(),0,1,new HashMap(){{
            this.put("id",courseId);
        }});
        // 消息通知，通知用户已解锁课程成功
        SystemPush systemPush = new SystemPush();
        systemPush.setMessage(String.format("您已解锁新课程：《%s》，快快去跟读吧 ~",course.getName()));
        this.pushService.sendSystemPush(userId,systemPush);

        // 如果用户报名的时间为课程开课的前一天，则将课程第一本绘本给与用户预习
        Course previewCourse = this.getCourseBookList(courseId);
        CourseDetail first = previewCourse.getCourseDetailList().get(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(first.getStartTime());
        // 减去一天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // 减去一天等于用户解锁当天，给与绘本
        if((new SimpleDateFormat("yyyyMMdd").format(calendar.getTime())).equals(new SimpleDateFormat("yyyyMMdd").format(new Date()))){
            // 给与绘本
            int resultRow = this.userBookService.add(userId,first.getBook().getId(),true,new HashMap(){{
                this.put("type",0);
                this.put("course",courseId);
                this.put("timestamp",new Date());
            }});
            // 发送推送消息
            if(resultRow > 0){
                SystemPush previewPush = new SystemPush();
                previewPush.setMessage(String.format("您的课程《%s》中的绘本《%s》已开放预习，快快去跟读吧 ~",previewCourse.getName(),first.getBook().getName()));
                this.pushService.sendSystemPush(userId,previewPush);
            }
        }
    }

    /**
     * 课程打赏
     * @param userId
     * @param fee
     * @param courseId
     */
    public void reward(Integer userId, BigDecimal fee,Integer courseId){
        if(fee.compareTo(new BigDecimal("0")) <= 0){
            throw new KidbridgeSimpleException("请输入正确的打赏金额 ~");
        }
        // 课程详情信息
        Course course = this.get(new Course(){
            @Override
            public Integer getId() {
                return courseId;
            }
        });
        if(course == null){
            throw new KidbridgeSimpleException("未知的被打赏课程 ~");
        }
        if(course.getCopyright().getUser().getId().intValue() == -1){
            throw new KidbridgeSimpleException("未知的被打赏用户 ~");
        }
        // 打赏用户，扣除余额
        this.userService.assetsUpdate(userId,fee.negate(),0,3,new HashMap(){{
            this.put("user",course.getCopyright().getUser().getId());
            this.put("type",1);
            this.put("target",courseId);
        }});
        // 被打赏用户，新增余额
        this.userService.assetsUpdate(course.getCopyright().getUser().getId(),fee,0,4,new HashMap(){{
            this.put("user",userId);
            this.put("type",1);
            this.put("target",courseId);
        }});
    }

    @Deprecated
    public CourseDetail getCourseDetail(Integer courseId,Integer bookId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getCourseDetail",new CourseDetail(){
            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getId() {
                        return courseId;
                    }
                };
            }

            @Override
            public Book getBook() {
                return new Book(){
                    @Override
                    public Integer getId() {
                        return bookId;
                    }
                };
            }
        });
    }

    /**
     * 获取课程下某个绘本的有效跟读时间
     * @param courseId
     * @param bookId
     * @return
     */
    public CourseDetail getCourseBookActiveTime(Integer courseId, Integer bookId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getCourseBookActiveTime",new CourseDetail(){
            @Override
            public Course getCourse() {
                return new Course(){
                    @Override
                    public Integer getId() {
                        return courseId;
                    }
                };
            }

            @Override
            public Book getBook() {
                return new Book(){
                    @Override
                    public Integer getId() {
                        return bookId;
                    }
                };
            }
        });
    }

    /**
     * 获取某一课程，某一月份的用户打卡记录
     * @param userCourseId
     * @param date
     * @return
     */
    public List<UserCourseRepeat> getUserCourseSignSchedule(Integer userCourseId,String date) {
        try{
            DateUtils.parseDate(date,"yyyyMM");
        }catch (ParseException p){
            throw new KidbridgeSimpleException("日期格式有误 ~");
        }
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserCourseSignSchedule", new HashMap(){{
            this.put("userCourseId",userCourseId); // 用户的课程编号
            this.put("date",date); // 月份时间
        }});
    }

    /**
     * 获取课程中的今日打卡模块数据
     * @param courseId
     * @param date
     * @param page
     * @return
     */
    public List<Map> getTodaySignList(Integer courseId,String date,PageRowBounds page){
        if(!StringUtils.isBlank(date)){
            try{
                DateUtils.parseDate(date,"yyyyMMdd");
            } catch (ParseException e) {
                throw new KidbridgeSimpleException("非法的日期参数 ~");
            }
        }
        return this.sqlSessionTemplate.selectList(this.namespace + "getTodaySignList",new HashMap(){{
            this.put("courseId",courseId);
            this.put("date",date);
        }},page);
    }

    /**
     * 课程赏析区，新增评论内容
     * @param userId
     * @param courseId
     * @param text
     * @param audio
     * @return
     */
    public Integer insAppreciationComment(Integer userId,Integer courseId,String text,Map audio){
        if(StringUtils.isBlank(text) && audio == null){
            throw new KidbridgeSimpleException("未知的评论内容 ~");
        }
        // 评论的课程信息
        Course course = this.get(new Course(){
            @Override
            public Integer getId() {
                return courseId;
            }
        });
        if(course == null){
            throw new KidbridgeSimpleException("未知的课程信息 ~");
        }
        // 插入数据
        return this.sqlSessionTemplate.insert(this.namespace + "insAppreciationComment",new CourseComment(){
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

            @Override
            public Map getContent() {
                return new HashMap(){{
                    this.put("text",(StringUtils.isBlank(text) ? "" : text));
                    this.put("audio",new HashMap(){{
                        this.put("source",audio == null ? "" : audio.get("source"));
                        this.put("time",audio == null ? -1 : audio.get("time"));
                    }});
                }};
            }
        });
    }

    /**
     * 获取课程赏析区中某一评论的详情信息
     * @param courseCommentId
     * @return
     */
    public CourseComment getAppreciationComment(Integer courseCommentId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getAppreciationComment",courseCommentId);
    }

    /**
     * 获取课程赏析区中的评论列表
     * @param courseId
     * @param page
     * @return
     */
    public List<CourseComment> getAppreciationCommentList(Integer courseId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "getAppreciationCommentList",new HashMap(){{
            this.put("courseId",courseId);
            this.put("page",page);
        }});
    }

    /**
     * 新增课程赏析区评论，子评论内容
     * @param userId
     * @param courseId
     * @param quote
     * @param text
     * @param audio
     * @return
     */
    public Integer insAppreciationCommentReply(Integer userId,Integer courseId,Integer quote, String text,Map audio){
        if((StringUtils.isBlank(text) && audio == null) || quote == null){
            throw new KidbridgeSimpleException("非法的请求参数 ~");
        }
        // 被引用的父评论信息
        CourseComment courseComment = this.getAppreciationComment(quote);
        // 判断父评论是否存在
        if(courseComment == null){
            throw new KidbridgeSimpleException("未知的评论 ~");
        }
        // 获取课程详情信息
        Course course = this.get(new Course(){
            @Override
            public Integer getId() {
                return courseId;
            }
        });
        if(course == null){
            throw new KidbridgeSimpleException("未知的课程信息 ~");
        }
        // 插入评论数据
        return this.sqlSessionTemplate.insert(this.namespace + "insAppreciationCommentReply",new CourseComment(){
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

            @Override
            public CourseComment getQuote() {
                return new CourseComment(){
                    @Override
                    public Integer getId() {
                        return quote;
                    }
                };
            }

            @Override
            public Map getContent() {
                return new HashMap(){{
                    this.put("text",(StringUtils.isBlank(text) ? "" : text));
                    this.put("audio",new HashMap(){{
                        this.put("source",audio == null ? "" : audio.get("source"));
                        this.put("time",audio == null ? -1 : audio.get("time"));
                    }});
                }};
            }
        });
    }
}
