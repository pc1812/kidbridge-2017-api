package net.$51zhiyuan.development.kidbridge.ui.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.enums.Role;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.module.shiro.KidbridgeAuthLogin;
import net.$51zhiyuan.development.kidbridge.module.shiro.KidbridgeSimpleLogin;
import net.$51zhiyuan.development.kidbridge.service.*;
import net.$51zhiyuan.development.kidbridge.ui.model.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private MedalService medalService;

    @Autowired
    private BillService billService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AuthcService authcService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ConfigService configService;

    /**
     * 账号密码登录
     * @param param phone：登录账号 password：登录密码
     * @return
     */
    @ResponseBody
    @RequestMapping("/login")
    Message login(@RequestBody Map<String,Object> param){
        Map<String,Object> data = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            // 如果当前用户未登录，进行登录
            subject.login(new KidbridgeSimpleLogin((String) param.get("phone"),(String) param.get("password")));
        }
        User user = this.userService.info((int)subject.getPrincipal());
        // hash过的用户标识，主要用于极光推送区分用户
        data.put("id",DigestUtils.md5Hex(String.format("%s:%s",subject.getPrincipal(),Configuration.SYSTEM_SIGN_SALT)));
        // 后续会话token，每次请求必须携带
        data.put("token",subject.getSession().getId());
        // 用户头像
        data.put("head",user.getHead());
        // 用户昵称
        data.put("nickname",user.getNickname());
        return new Message(data);
    }

    /**
     * 授权登录
     * @param param code：授权code type：0=微信授权
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth")
    Message auth(@RequestBody Map<String,Object> param){
        Map<String,Object> data = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            // 如果当前用户未登录，进行登录
            subject.login(new KidbridgeAuthLogin((String) param.get("code"),(int)param.get("type")));
        }
        User user = this.userService.info((int)subject.getPrincipal());
        // hash过的用户标识，主要用于极光推送区分用户
        data.put("id",DigestUtils.md5Hex(String.format("%s:%s",subject.getPrincipal(),Configuration.SYSTEM_SIGN_SALT)));
        // 后续会话token，每次请求必须携带
        data.put("token",subject.getSession().getId());
        // 用户头像
        data.put("head",user.getHead());
        // 用户昵称
        data.put("nickname",user.getNickname());
        return new Message(data);
    }

    /**
     * 注销登录
     * @return
     */
    @ResponseBody
    @RequestMapping("/logout")
    Message logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout(); // 注销
        return new Message();
    }

    /**
     * 用户余额信息
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/balance")
    Message balance(@RequestBody Map<String,Object> param){
        User User = this.userService.balance((int) SecurityUtils.getSubject().getPrincipal());
        return new Message(User);
    }

    /**
     * 用户水滴信息
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/bonus")
    Message bonus(@RequestBody Map<String,Object> param){
        User User = this.userService.bonus(
                (int) SecurityUtils.getSubject().getPrincipal()
        );
        return new Message(User);
    }

    /**
     * 用户个人资料
     * @return
     */
    @ResponseBody
    @RequestMapping("/info")
    Message info() {
        User User = this.userService.info(
                (int) SecurityUtils.getSubject().getPrincipal()
        );
        return new Message(User);
    }

    /**
     * 用户收货信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/receiving")
    Message receiving(){
        User user = this.userService.info(
                (int) SecurityUtils.getSubject().getPrincipal()
        );
        return new Message(new HashMap(){{
            // 联系人
            this.put("contact",user.getReceivingContact());
            // 联系方式
            this.put("phone",user.getReceivingPhone());
            // 收货区域
            this.put("region",user.getReceivingRegion());
            // 收货街道
            this.put("street",user.getReceivingStreet());
        }});
    }

    /**
     * 用户绘本列表
     * @param param free：0=付费,1=免费
     * @return
     */
    @ResponseBody
    @RequestMapping("/book")
    Message book(@RequestBody Map<String,Object> param){
        List<Book> bookList = this.userBookService.userBook(
                (int) SecurityUtils.getSubject().getPrincipal(),
                (int)param.get("free"),
                new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT))
        );
        return new Message(bookList);
    }

    /**
     * 用户课程列表
     * @param param status：0：未开课(报名中)，1：已开课，2：已结束
     * @return
     */
    @ResponseBody
    @RequestMapping("/course")
    Message course(@RequestBody Map<String,Object> param){
        List<Course> courseList = this.userCourseService.userCourse((int)SecurityUtils.getSubject().getPrincipal(),param.get("status") == null ? null :(int) param.get("status"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));

        return new Message(courseList);
    }

    /**
     * 用户是否解锁了某个绘本
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/exist")
    Message book_exist(@RequestBody Map<String,Object> param){
        boolean exist = this.userBookService.exist((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"));
        return new Message(new HashMap(){{
            this.put("exist",(exist ? 1 : 0)); // 0未解锁 1已解锁
        }});
    }

    /**
     * 用户勋章
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/medal")
    Message medal(@RequestBody Map<String,Object> param){
        int bonus = this.userService.get(new User(){
            @Override
            public Integer getId() {
                return (int)SecurityUtils.getSubject().getPrincipal();
            }
        }).getBonus();
        List<Medal> medalList = this.medalService.list();
        return new Message(new HashMap(){{
            this.put("bonus",bonus); // 用户当前积分
            this.put("medalList",medalList); // 勋章信息
        }});
    }

    /**
     * 用户资料
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/my")
    Message my(@RequestBody Map<String,Object> param){
        // 当前水滴信息
        int bonus = this.userService.get(new User(){
            @Override
            public Integer getId() {
                return (int)SecurityUtils.getSubject().getPrincipal();
            }
        }).getBonus();
        // 用户信息
        User user = this.userService.outline((int)SecurityUtils.getSubject().getPrincipal());
        user.setBonus(bonus);
        // 当前的勋章信息
        Medal now = this.medalService.now(bonus);
        //下一个勋章信息
        Medal future = this.medalService.future(bonus);
        // 角色
        int role = SecurityUtils.getSubject().hasRole(Role.NORMAL.name()) ? Role.NORMAL.ordinal() : Role.TEACHER.ordinal();
        // 客服
        String customerservice = this.configService.get(ConfigService.CUSTOMER_SERVICE);
        return new Message(new HashMap(){{
            this.put("user",user); // 用户信息
            this.put("phase",new HashMap(){{ // 勋章阶段信息
                this.put("over",future == null ? -1 : (future.getBonus() - bonus)); // 距离下一个勋章所需的水滴数
                this.put("now",now == null ? new HashMap(){{ // 当前的勋章信息
                    this.put("id",-1);
                    this.put("name","");
                }} : now);
                this.put("future",future == null ? new HashMap(){{ //下一个勋章信息
                    this.put("id",-1);
                    this.put("name","");
                }} : future);
            }});
            this.put("role",role); // 用户角色， 教师或普通用户
            this.put("customerservice",customerservice); // 客服电话
        }});
    }

    /**
     * 读获绘本跟读token
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/token")
    Message book_repeat_token(@RequestBody Map<String,Object> param){
        String token = this.userService.getBookRepeatToken((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"));
        return new Message(new HashMap(){{
            this.put("token",token); // 完成跟读发布时需携带此参数
        }});
    }

    /**
     * 获取课程跟读token
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/book/repeat/token")
    Message course_repeat_token(@RequestBody Map<String,Object> param){
        System.out.println("token param: " + param);
        String token = this.userService.getCourseRepeatToken((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("course"),(int)param.get("book"));
        return new Message(new HashMap(){{
            this.put("token",token); // 完成跟读发布时需携带此参数
        }});
    }

    /**
     * 用户绘本跟读信息提交
     * @param param repeat：跟读token
     * @return
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping("/book/repeat")
    Message book_repeat(@RequestBody Map<String,Object> param) throws JsonProcessingException {
        Map result = this.userService.bookRepeat((int)SecurityUtils.getSubject().getPrincipal(),(Map) param.get("repeat"));
        return new Message(result);
    }

    /**
     * 用户绘本跟读信息删除
     * @param param id：绘本编号，数组类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/delete")
    Message book_repeat_delete(@RequestBody Map<String,Object> param){
        this.userService.bookRepeatDelete((int)SecurityUtils.getSubject().getPrincipal(),(ArrayList)param.get("id"));
        return new Message();
    }

    /**
     * 用户绘本跟读列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/list")
    Message book_repeat_list(@RequestBody Map<String,Object> param){
        List<UserBookRepeat> userBookRepeatList = this.userService.getUserBookRepeatList((int)SecurityUtils.getSubject().getPrincipal(),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(userBookRepeatList);
    }

    /**
     * 用户课程跟读列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/list")
    Message course_repeat_list(@RequestBody Map<String,Object> param){
        List<UserCourseRepeat> userCourseRepeats = this.userService.getUserCourseRepeatList((int)SecurityUtils.getSubject().getPrincipal(),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(userCourseRepeats);
    }

    /**
     * 用户某个课程下的绘本跟读列表
     * @param param id：课程编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/book/repeat/list")
    Message course_book_repeat_list(@RequestBody Map<String,Object> param){
        List<Map> bookRepeatList = this.userService.getUserCourseBookRepeatList((int)param.get("id"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(bookRepeatList);
    }

    /**
     * 用户绘本跟读详情
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/detail")
    Message book_repeat_detail(@RequestBody Map<String,Object> param){
        UserBookRepeat userBookRepeat = this.userService.getUserBookRepeat((int)param.get("id"));
        // 绘本详赏析信息
        Book appreciation = this.bookService.getAppreciation(userBookRepeat.getUserBook().getBook().getId());
        userBookRepeat.setBook(new Book(){{
            this.setId(appreciation.getId());
            this.setName(appreciation.getName());
            this.setBookSegmentList(appreciation.getBookSegmentList());
        }});
        return new Message(new HashMap(){{
            this.put("user",new HashMap(){{
                this.put("like",(userService.userBookRepeatLikeExist((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"))) == null ? 0 : 1); // 用户绘本跟读点赞数量
            }});
            this.put("repeat",userBookRepeat); // 用户跟读信息
        }});
    }

    /**
     * 用户课程跟读详情
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/detail")
    Message course_repeat_detail(@RequestBody Map<String,Object> param){
        UserCourseRepeat userCourseRepeat = this.userService.getUserCourseRepeat((int)param.get("id"));
        // 课程赏析
        Book appreciation = this.bookService.getAppreciation(userCourseRepeat.getBook().getId());
        userCourseRepeat.setBook(new Book(){{
            this.setId(appreciation.getId());
            this.setName(appreciation.getName());
            this.setBookSegmentList(appreciation.getBookSegmentList());
        }});
        return new Message(new HashMap(){{
            this.put("user",new HashMap(){{
                this.put("like",(userService.userCourseRepeatLikeExist((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"))) == null ? 0 : 1); // 点赞数量
            }});
            this.put("repeat",userCourseRepeat); // 跟读信息
        }});
    }

    /**
     * 用户绘本跟读点赞
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/like")
    Message book_repeat_like(@RequestBody Map<String,Object> param){
        Integer status = this.userService.insUserBookRepeatLike((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"));
        Integer count = this.userService.getUserBookRepeatLikeCount((int)param.get("id"));
        return new Message(new HashMap(){{
            this.put("status",status); // 点赞状态 0=取消赞，1=点赞
            this.put("count",count); // 点赞数量
        }});
    }

    /**
     * 用户课程跟读点赞
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/like")
    Message course_repeat_like(@RequestBody Map<String,Object> param){
        Integer status = this.userService.insUserCourseRepeatLike((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"));
        Integer count = this.userService.getUserCourseRepeatLikeCount((int)param.get("id"));
        return new Message(new HashMap(){{
            this.put("status",status); // 点赞状态 0=取消赞，1=点赞
            this.put("count",count); // 点赞数量
        }});
    }

    /**
     * 用户绘本跟读评论信息列表
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/comment/list")
    Message book_repeat_comment_list(@RequestBody Map<String,Object> param){
        List<UserBookRepeatComment> bookRepeatCommentList = this.userService.getUserBookRepeatCommentList((int)param.get("id"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(new HashMap(){{
            this.put("comment",bookRepeatCommentList); // 评论信息列表
        }});
    }

    /**
     * 用户绘本跟读评论提交
     * @param param id：绘本编号 text：评论文本 audio：评论音频
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping("/book/repeat/comment")
    Message book_repeat_comment(@RequestBody Map<String,Object> param) throws APIConnectionException, APIRequestException, JsonProcessingException {
        this.userService.insUserBookRepeatComment((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(String)param.get("text"),(HashMap) param.get("audio"));
       return new Message();
    }

    /**
     * 用户绘本跟读评论，子评论提交
     * @param param id：绘本编号 quote：父机评论编号 text：评论文本 audio：评论音频
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/comment/reply")
    Message book_repeat_comment_reply(@RequestBody Map<String,Object> param){
        this.userService.insUserBookRepeatCommentReply((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(int)param.get("quote"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 用户课程跟读评论提交
     * @param param id：课程编号 text：评论文本 audio：评论音频
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping("/course/repeat/comment")
    Message course_repeat_comment(@RequestBody Map<String,Object> param) throws APIConnectionException, APIRequestException, JsonProcessingException {
        this.userService.insUserCourseRepeatComment((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 用户课程跟读，子评论提交
     * @param param id：课程编号 quote：父机评论编号 text：评论文本 audio：评论音频
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/comment/reply")
    Message course_repeat_comment_reply(@RequestBody Map<String,Object> param){
        this.userService.insUserCourseRepeatCommentReply((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(int)param.get("quote"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 用户课程跟读评论列表
     * @param param id：课程编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/comment/list")
    Message course_repeat_comment_list(@RequestBody Map<String,Object> param){
        List bookRepeatNoramlCommentList = this.userService.getUserCourseRepeatCommentList((int)param.get("id"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(new HashMap(){{
            this.put("important",new ArrayList());
            this.put("normal",bookRepeatNoramlCommentList);
        }});
    }

    /**
     * 用户课程跟读，教师评论信息
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/teacher/comment")
    Message course_repeat_teacher_comment(@RequestBody Map<String,Object> param){
        List bookRepeatTeacherCommentList = this.userService.getTeacherComment((int)SecurityUtils.getSubject().getPrincipal());
        return new Message(bookRepeatTeacherCommentList);
    }

    /**
     * 用户绘本跟读，跟读榜信息
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/rank")
    Message book_repeat_rank(@RequestBody Map<String,Object> param){
        List<UserBookRepeat> userBookRepeatList = this.userService.getUserBookRepeatRank((int)param.get("id"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(new HashMap(){{
            this.put("rank",userBookRepeatList);
        }});
    }

    /**
     * 用户课程跟读，打卡信息获取
     * @param param id：课程编号 date：跟读月份 yyyyMM格式
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping("/course/schedule/list")
    Message course_schedule(@RequestBody Map<String,Object> param) {
        List<UserCourseRepeat> userCourseRepeatList = this.courseService.getUserCourseSignSchedule((int)param.get("id"),(String) param.get("date"));
        return new Message(new HashMap(){{
            this.put("schedule",userCourseRepeatList);
        }});
    }

    /**
     * 用户注册
     * @param param phone：注册手机号码 password：登录密码 code：短信验证码 bind：第三方授权码可选
     * @return
     */
    @ResponseBody
    @RequestMapping("/register")
    Message register(@RequestBody Map<String,Object> param){
        String phone = (String) param.get("phone");
        String password = (String) param.get("password");
        this.userService.register(phone,password,(String) param.get("code"),(String) param.get("bind"));
        Subject subject = SecurityUtils.getSubject();
        subject.login(new KidbridgeSimpleLogin(phone, DigestUtils.md5Hex(String.format("%s:%s:%s",phone,password,Configuration.SYSTEM_SIGN_SALT))));
        return new Message(new HashMap(){{
            this.put("token",subject.getSession().getId());
        }});
    }

    /**
     * 用户余额兑换
     * @param param bonus：兑换的积分
     * @return
     */
    @ResponseBody
    @RequestMapping("/balance/ratio")
    Message balance_ratio(@RequestBody Map<String,Object> param){
        this.userService.balanceRatio((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("bonus"));
        return new Message();
    }

    /**
     * 用户注册，发送验证码
     * @param param phone 手机号码
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    @ResponseBody
    @RequestMapping("/register/verification")
    Message register_verification(@RequestBody Map<String,Object> param) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        this.userService.registerVerification((String) param.get("phone"));
        return new Message();
    }

    /**
     * 修改密码
     * @param param phone：用户手机号 code：验证码 password：新密码
     * @return
     */
    @ResponseBody
    @RequestMapping("/update/password")
    Message update_password(@RequestBody Map<String,Object> param){
        this.userService.updatePassword((String) param.get("phone"),(String) param.get("code"),(String) param.get("password"));
        return new Message();
    }

    /**
     * 修改头像
     * @param param head：头像七牛资源
     * @return
     */
    @ResponseBody
    @RequestMapping("/update/head")
    Message update_head(@RequestBody Map<String,Object> param){
        this.userService.updateHead((int)SecurityUtils.getSubject().getPrincipal(),(String) param.get("head"));
        return new Message(new HashMap(){{
            this.put("head", param.get("head"));
        }});
    }

    /**
     * 修改昵称
     * @param param nickname：新昵称
     * @return
     */
    @ResponseBody
    @RequestMapping("/update/nickname")
    Message update_nickname(@RequestBody Map<String,Object> param){
        this.userService.updateNickname((int)SecurityUtils.getSubject().getPrincipal(),(String) param.get("nickname"));
        return new Message(new HashMap(){{
            this.put("nickname", param.get("nickname"));
        }});
    }

    /**
     * 修改出生日期
     * @param param birthday：新出生日期 yyyyMMdd格式
     * @return
     */
    @ResponseBody
    @RequestMapping("/update/birthday")
    Message update_birthday(@RequestBody Map<String,Object> param){
        this.userService.updateBirthday((int)SecurityUtils.getSubject().getPrincipal(),(String) param.get("birthday"));
        return new Message();
    }

    /**
     * 修改签名
     * @param param signature：新签名
     * @return
     */
    @ResponseBody
    @RequestMapping("/update/signature")
    Message update_signature(@RequestBody Map<String,Object> param){
        this.userService.updateSignature((int)SecurityUtils.getSubject().getPrincipal(),(String) param.get("signature"));
        return new Message();
    }

    /**
     * 修改收货地址信息
     * @param param contact：联系人 phone：联系方式 region：收货区域 street：收货街道
     * @return
     */
    @ResponseBody
    @RequestMapping("/update/receiving")
    Message update_receiving(@RequestBody Map<String,Object> param){
        this.userService.updateReceiving((int)SecurityUtils.getSubject().getPrincipal(),(String) param.get("contact"),(String) param.get("phone"),(String) param.get("region"),(String) param.get("street"));
        return new Message();
    }

    /**
     * 修改密码，发送短信验证码
     * @param param phone：手机号码
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    @ResponseBody
    @RequestMapping("/update/password/verification")
    Message user_update_password_verification(@RequestBody Map<String,Object> param) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        this.userService.updatePasswordVerification((String) param.get("phone"));
        return new Message();
    }

    /**
     * 用户绘本跟读打赏
     * @param param fee：打赏金额 id：用户绘本跟读编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/book/repeat/reward")
    Message user_book_course_repeat_reward(@RequestBody Map<String,Object> param){
        this.userService.userBookRepeatReward((int)SecurityUtils.getSubject().getPrincipal(), BigDecimal.valueOf(Double.parseDouble(param.get("fee").toString())),(int)param.get("id"));
        return new Message();
    }

    /**
     * 用户课程跟读打赏
     * @param param fee：打赏金额 id：用户课程跟读编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/course/repeat/reward")
    Message user_course_repeat_reward(@RequestBody Map<String,Object> param){
        this.userService.userCourseRepeatReward((int)SecurityUtils.getSubject().getPrincipal(), BigDecimal.valueOf(Double.parseDouble(param.get("fee").toString())),(int)param.get("id"));
        return new Message();
    }

}
