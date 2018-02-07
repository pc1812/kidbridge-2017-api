package net.$51zhiyuan.development.kidbridge.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.exception.KidbridgeSimpleException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.ui.model.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import java.util.concurrent.TimeUnit;


/**
 * 用户
 */
@Service
public class UserService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.user.";

    @Autowired
    private BillService billService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookSegmentService bookSegmentService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserCourseService  userCourseService;

    @Autowired
    private AuthcService authcService;

    @Autowired
    private PushService pushService;

    /**
     * 用户名密码登录
     * @param phone
     * @param password
     * @return
     */
    public User simpleLogin(String phone, String password){
        if(StringUtils.isBlank(phone)){
            throw new KidbridgeSimpleException("请输入手机号码");
        }
        if(StringUtils.isBlank(password)){
            throw new KidbridgeSimpleException("请输入登录密码");
        }
        User user = this.login(phone);
        if(user == null){
            throw new KidbridgeSimpleException("用户不存在");
        }
        if(!user.getPassword().toLowerCase().equals(password.toLowerCase())){
            throw new KidbridgeSimpleException("密码输入有误");
        }
        return user;
    }

    /**
     * 获取用户详情信息
     * @param param
     * @return
     */
    public User get(User param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<User> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new User());
    }

    @Deprecated
    public List<User> list(User param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<User> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 通过手机号码查询出用户信息
     * @param phone
     * @return
     */
    public User login(String phone){
        return this.sqlSessionTemplate.selectOne(this.namespace + "login",phone);
    }

    /**
     * 获取用户余额信息
     * @param id
     * @return
     */
    public User balance(Integer id){
        return this.sqlSessionTemplate.selectOne(this.namespace + "balance",id);
    }

    /**
     * 获取用户当前水滴信息
     * @param id
     * @return
     */
    public User bonus(Integer id){
        return this.sqlSessionTemplate.selectOne(this.namespace + "bonus",id);
    }

    /**
     * 通过用户id获取信息
     * @param id
     * @return
     */
    public User info(Integer id){
        return this.sqlSessionTemplate.selectOne(this.namespace + "info",id);
    }

    /**
     * 修改用户信息
     * @param param
     * @return
     */
    public Integer update(User param){
        return this.sqlSessionTemplate.update(this.namespace + "update",param);
    }

    /**
     * 获取用户基本信息
     * @param userId
     * @return
     */
    public User outline(Integer userId){
       return this.sqlSessionTemplate.selectOne(this.namespace + "outline",userId);
    }

//    public String getRepeatToken(Integer type,Integer userId, Integer bookId, Integer courseId){
//        String token = null;
//        if(type == null || (type.intValue() != 0 && type.intValue() != 1)){
//            throw new KidbridgeSimpleException("未知的跟读类别");
//        }
//        if(type.intValue() == 0){
//            UserBook userBook = this.userBookService.get(new UserBook(){
//                @Override
//                public User getUser() {
//                    return new User(){
//                        @Override
//                        public Integer getId() {
//                            return userId;
//                        }
//                    };
//                }
//
//                @Override
//                public Book getBook() {
//                    return new Book(){
//                        @Override
//                        public Integer getId() {
//                            return bookId;
//                        }
//                    };
//                }
//            });
//            if(userBook == null){
//                throw new KidbridgeSimpleException("未解锁该绘本 ~");
//            }
//            Map repeatModel = new HashMap(){{
//                this.put("bookId",bookId);
//                this.put("userId",userId);
//                this.put("userBookId",userBook.getId());
//            }};
//            token = UUID.randomUUID().toString().replace("-","");
//            ValueOperations<String,Object> valueOperations = this.redisTemplate.opsForValue();
//            valueOperations.set(token,repeatModel,1, TimeUnit.HOURS);
//        }
//        return token;
//    }

    /**
     * 获取绘本跟读token
     * @param userId
     * @param userBookId
     * @return
     */
    public String getBookRepeatToken(Integer userId,Integer userBookId){
        // 通过用户要跟读的绘本ID，获取用户绘本信息
        UserBook userBook = this.userBookService.get(new UserBook() {
            @Override
            public User getUser() {
                return new User() {
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }

            @Override
            public Integer getId() {
                return userBookId;
            }
        });
        // 判断跟读的绘本是否已被用户解锁
        if(userBook == null){
            throw new KidbridgeSimpleException("绘本不存在或未解锁 ~");
        }
        // 获取绘本有效跟读时间
        Integer repeatActiveTime = this.bookService.getRepeatActiveTime(userBook.getBook().getId());
        String token = UUID.randomUUID().toString().replace("-","");
        UserBookRepeat userBookRepeat = new UserBookRepeat();
        userBookRepeat.setId(this.bookRepeatExist(userBook.getId()));
        userBookRepeat.setUserBook(userBook);
        // redis临时存储跟读信息
        this.redisTemplate.opsForValue().set(String.format("%s:%s", Configuration.SYSTEM_REPEAT_NAMESPACE,token),userBookRepeat,repeatActiveTime + repeatActiveTime / 2,TimeUnit.SECONDS);
        return token;
    }

    /**
     * 获取课程跟读信息
     * @param userId
     * @param userCourseId
     * @param bookId
     * @return
     */
    public String getCourseRepeatToken(Integer userId, Integer userCourseId, Integer bookId){
        // 获取绘本有效跟读时间
        Integer repeatActiveTime = this.bookService.getRepeatActiveTime(bookId);
        if(repeatActiveTime == null){
            throw new KidbridgeSimpleException("未知的绘本信息 ~");
        }
        // 获取用户课程信息，主要判断跟读的课程是否已被用户解锁
        UserCourse userCourse = this.userCourseService.get(new UserCourse(){
            @Override
            public Integer getId() {
                return userCourseId;
            }

            @Override
            public User getUser() {
                return new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }
        });
        // 是否被用户解锁
        if(userCourse == null){
            throw new KidbridgeSimpleException("课程不存在或未解锁 ~");
        }

        // 获取课程下的绘本跟读信息
        CourseDetail courseDetail = this.courseService.getCourseBookActiveTime(userCourse.getCourse().getId(),bookId);
        if(courseDetail == null){
            throw new KidbridgeSimpleException("课程下的绘本状态异常 ~");
        }
        Calendar activeTime = Calendar.getInstance();
        activeTime.setTime(courseDetail.getStartTime());
        Calendar quietTime = Calendar.getInstance();
        quietTime.setTime(activeTime.getTime());
        quietTime.add(Calendar.DAY_OF_MONTH,courseDetail.getCycle());
        Calendar now = Calendar.getInstance();
        // 打卡时间正确性判断
        if(!(activeTime.compareTo(now) <= 0 && quietTime.compareTo(now) > 0)){
            throw new KidbridgeSimpleException("该绘本未到打卡时间或已过期 ~");
        }
        // 获取当天跟读的课程信息
        Integer userCourseRepeatId = this.courseRepeatExist(userCourse.getId(),bookId);
        // 课程打卡，每天只能打卡一次
        if(userCourseRepeatId != null){
            throw new KidbridgeSimpleException("今天已经打过卡喽，请明天继续~");
        }
        String token = UUID.randomUUID().toString().replace("-","");
        Book book = new Book();
        book.setId(bookId);
        UserCourseRepeat userCourseRepeat = new UserCourseRepeat();
        userCourseRepeat.setId(userCourseRepeatId);
        userCourseRepeat.setUserCourse(userCourse);
        userCourseRepeat.setBook(book);
        // redis临时存储跟读信息
        this.redisTemplate.opsForValue().set(String.format("%s:%s", Configuration.SYSTEM_REPEAT_NAMESPACE,token),userCourseRepeat,repeatActiveTime + repeatActiveTime / 2,TimeUnit.SECONDS);
        return token;
    }

    /**
     * 绘本跟读
     * @param userId
     * @param repeat
     * @return
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public Map bookRepeat(Integer userId, Map repeat) {
        ObjectMapper objectMapper = new ObjectMapper();
        // 参数校验
        if(repeat == null || repeat.get("token") == null || repeat.get("segment") == null || ((ArrayList)(repeat.get("segment"))).size() == 0 || StringUtils.isBlank((String)repeat.get("token"))){
            throw new KidbridgeSimpleException("非法的跟读参数 ~");
        }
        String token = (String)repeat.get("token");
        // 获取redis中存储的token信息
        Object userRepeat = this.redisTemplate.opsForValue().get(String.format("%s:%s",Configuration.SYSTEM_REPEAT_NAMESPACE,token));
        // 是否已过跟读时间
        if(userRepeat == null){
            throw new KidbridgeSimpleException("无效的跟读会话 ~");
        }
        // token中存储的用户信息是否和当前发布跟读的用户信息一致，不是很需要执行这一步骤，可省略
        if(userId.intValue() != (userRepeat instanceof UserBookRepeat ? ((UserBookRepeat)userRepeat).getUserBook().getUser().getId().intValue() : ((UserCourseRepeat)userRepeat).getUserCourse().getUser().getId().intValue())){
            throw new KidbridgeSimpleException("非法的跟读用户 ~");
        }
        // 获取用户请求参数中的跟读段落信息
        List segmentList = ((ArrayList) repeat.get("segment"));
        if(segmentList == null){
            throw new KidbridgeSimpleException("非法的跟读参数 ~");
        }
        // 获取真实的绘本段落信息
        List<BookSegment> bookSegmentList = this.bookSegmentService.list(new BookSegment(){
            @Override
            public Book getBook() {
                return new Book(){
                    @Override
                    public Integer getId() {
                        return  (userRepeat instanceof UserBookRepeat ? ((UserBookRepeat)userRepeat).getUserBook().getBook().getId() : ((UserCourseRepeat)userRepeat).getBook().getId());
                    }
                };
            }
        });
        // 用户跟读段落要与绘本原段落一致，不许空段
        if(bookSegmentList.size() != segmentList.size()){
            throw new KidbridgeSimpleException("跟读段落未匹配 ~");
        }
        // 最终通过数据校验的用户跟读段落，避免脏数据
        List<Map> copySegmentList = new ArrayList<>();
        for(int i=0;i<bookSegmentList.size();i++){
            Map segment = (Map) segmentList.get(i);
            BookSegment bookSegment = bookSegmentList.get(i);
            // 参数校验，判断是否有缺少的必要参数
            if(segment.get("id") == null || StringUtils.isBlank(segment.get("id").toString()) || segment.get("audio") == null || ((HashMap)segment.get("audio")).get("source") == null || ((HashMap)segment.get("audio")).get("time") == null){
                throw new KidbridgeSimpleException("非法的跟读参数 ~");
            }
            // 用户跟读的段落顺序必须与绘本原段落一致
            if(bookSegment.getId().intValue() != (int)segment.get("id")){
                throw new KidbridgeSimpleException("非法段落编号或顺序 ~");
            }
            copySegmentList.add(new HashMap(){{
                this.put("id", segment.get("id"));
                this.put("audio", new HashMap(){{
                    this.put("source",((HashMap)segment.get("audio")).get("source"));
                    this.put("time",((HashMap)segment.get("audio")).get("time"));
                }});
            }});
        }
        // 标识课程跟读还是绘本跟读,0绘本 1课程，指引客户端展现不同的界面
        Integer repeatType = null;
        // 跟读id
        Integer repeatId = null;
        if(userRepeat instanceof UserBookRepeat){ // 如果是绘本跟读
            // 这个绘本之前是否已被用户跟读过，一个绘本只能跟读一次，新的跟读数据会替换旧的跟读数据
            if(((UserBookRepeat)userRepeat).getId() == null){
                // 没有跟读过
                UserBookRepeat userBookRepeat = new UserBookRepeat(){
                    @Override
                    public UserBook getUserBook() {
                        return new UserBook(){
                            @Override
                            public Integer getId() {
                                return ((UserBookRepeat)userRepeat).getUserBook().getId();
                            }
                        };
                    }

                    @Override
                    public List getSegment() {
                        return copySegmentList;
                    }
                };
                // 没有跟读过，新增跟读数据
                this.sqlSessionTemplate.insert(this.namespace + "bookRepeat",userBookRepeat);
                repeatId = userBookRepeat.getId();
                Integer bookRepeatId = repeatId;
                // 用户跟读成功后增加水滴，默认为1
                this.assetsUpdate(userId,new BigDecimal(Configuration.property(Configuration.BONUS_INCREASE_BOOK_REPEAT)),1,7,new HashMap(){{
                    this.put("id",bookRepeatId);
                }});
            }else{
                // 有跟读过
                repeatId = ((UserBookRepeat)userRepeat).getId();
                // 今天是否已跟读过
                UserBookRepeat exist = this.getUserBookRepeat(repeatId);
                if(!(new SimpleDateFormat("yyyyMMdd").format(exist.getCreateTime())).equals((new SimpleDateFormat("yyyyMMdd").format(new Date())))){
                    // 今天还未跟读
                    Integer finalRepeatId1 = repeatId;
                    // 用户可以每天多次进行跟读，但只有每天的第一次跟读才能获得水滴奖励
                    this.assetsUpdate(userId,new BigDecimal(Configuration.property(Configuration.BONUS_INCREASE_BOOK_REPEAT)),1,7,new HashMap(){{
                        this.put("id", finalRepeatId1);
                    }});
                }
                // 覆盖旧的跟读数据
                this.updateUserBookRepeat(new UserBookRepeat(){
                    @Override
                    public Integer getId() {
                        return ((UserBookRepeat)userRepeat).getId();
                    }

                    @Override
                    public List getSegment() {
                        return copySegmentList;
                    }
                });
            }
            repeatType = 0;
        }else if(userRepeat instanceof UserCourseRepeat){ // 如果是课程跟读
            UserCourseRepeat userCourseRepeat = ((UserCourseRepeat)userRepeat);
            if(((UserCourseRepeat)userRepeat).getId() == null){
                // 如果用户之前没有跟读过这个课程，直接新增跟读数据
                repeatId = this.courseRepeat(userCourseRepeat.getUserCourse().getId(),userCourseRepeat.getBook().getId(),copySegmentList);
                Integer courseRepeatId = repeatId;
                // 新的课程跟读，奖励水滴，默认为1
                this.assetsUpdate(userId,new BigDecimal(Configuration.property(Configuration.BONUS_INCREASE_COURSE_REPEAT)),1,8,new HashMap(){{
                    this.put("id", courseRepeatId);
                }});
            }else{
                // 下面这段没用，不会执行到，但又不想删
                this.sqlSessionTemplate.update(this.namespace + "updateCourseRepeat",new UserCourseRepeat(){
                    @Override
                    public Integer getId() {
                        return userCourseRepeat.getId();
                    }
                    @Override
                    public List getSegment() {
                        return copySegmentList;
                    }
                });
                repeatId = userCourseRepeat.getId();
            }
            repeatType = 1;
        }
        this.redisTemplate.delete(String.format("%s:%s",Configuration.SYSTEM_REPEAT_NAMESPACE,token));
        Integer finalRepeatId = repeatId;
        Integer finalRepeatType = repeatType;
        return new HashMap(){{
           this.put("repeat", finalRepeatId); // 跟读发布成功，返回给客户端跟读的编号
           this.put("type", finalRepeatType); // 跟读类别，0绘本 1课程，客户端根据这两个参数打卡对应的跟读结果页
        }};
    }

    /**
     * 绘本跟读删除
     * @param userId
     * @param userBookRepeatId
     */
    public void bookRepeatDelete(Integer userId, List userBookRepeatId){
         if(userBookRepeatId == null || userBookRepeatId.size() == 0){
             throw new KidbridgeSimpleException("非法的请求参数 ~");
         }
         this.sqlSessionTemplate.delete(this.namespace + "bookRepeatDetele",new HashMap(){{
             this.put("userId",userId);
             this.put("idList",userBookRepeatId);
         }});
    }

    /**
     * 新增课程跟读
     * @param userCourseId
     * @param bookId
     * @param segment
     * @return
     */
    public Integer courseRepeat(Integer userCourseId, Integer bookId, List segment){
        UserCourseRepeat userCourseRepeat = new UserCourseRepeat(){
            @Override
            public UserCourse getUserCourse() {
                return new UserCourse(){
                    @Override
                    public Integer getId() {
                        return userCourseId;
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

            @Override
            public List getSegment() {
                return segment;
            }
        };
        this.sqlSessionTemplate.insert(this.namespace + "courseRepeat",userCourseRepeat);
        return userCourseRepeat.getId();
    }

    /**
     * 判断用户当天是否已跟读过，如果已跟读会返回跟读id，否则为空
     * @param userCourseId
     * @param bookId
     * @return
     */
    public Integer courseRepeatExist(Integer userCourseId, Integer bookId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "courseRepeatExist",new UserCourseRepeat(){
            @Override
            public UserCourse getUserCourse() {
                return new UserCourse(){
                    @Override
                    public Integer getId() {
                        return userCourseId;
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

    @Deprecated
    public Integer bookRepeatTodayExist(Integer userBookId){
        return  this.sqlSessionTemplate.selectOne(this.namespace + "bookRepeatTodayExist",userBookId);
    }

    /**
     * 判断用户的绘本是否被跟读过，如果已跟读，返回跟读ID，否则为空
     * @param userBookId
     * @return
     */
    public Integer bookRepeatExist(Integer userBookId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "bookRepeatExist",userBookId);
    }

    /**
     * 更新用户绘本跟读的数据
     * @param userBookRepeat
     * @return
     */
    public Integer updateUserBookRepeat(UserBookRepeat userBookRepeat){
        return this.sqlSessionTemplate.update(this.namespace + "updateUserBookRepeat",userBookRepeat);
    }

    /**
     * 获取用户的绘本跟读列表
     * @param userId
     * @param page
     * @return
     */
    public List getUserBookRepeatList(Integer userId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserBookRepeatList", userId,page);
    }

    /**
     * 获取用户绘本跟读详情
     * @param userBookRepeatId
     * @return
     */
    public UserBookRepeat getUserBookRepeat(Integer userBookRepeatId){
        UserBookRepeat userBookRepeat = this.sqlSessionTemplate.selectOne(this.namespace + "getUserBookRepeat",userBookRepeatId);
        if(userBookRepeat == null){
            throw new KidbridgeSimpleException("未知的绘本跟读信息 ~");
        }
        return userBookRepeat;
    }

    /**
     * 获取用户课程跟读详情
     * @param userCourseepeatId
     * @return
     */
    public UserCourseRepeat getUserCourseRepeat(Integer userCourseepeatId){
        UserCourseRepeat userCourseRepeat = this.sqlSessionTemplate.selectOne(this.namespace + "getUserCourseRepeat",userCourseepeatId);
        if(userCourseRepeat == null){
            throw new KidbridgeSimpleException("未知的课程跟读信息 ~");
        }
        return userCourseRepeat;
    }

    /**
     * 获取某一用户绘本跟读的点赞数量统计
     * @param userBookRepeatId
     * @return
     */
    public Integer getUserBookRepeatLikeCount(Integer userBookRepeatId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getUserBookRepeatLikeCount",userBookRepeatId);
    }

    /**
     * 获取某一用户课程跟读的点赞数量统计
     * @param userCourseRepeatId
     * @return
     */
    public Integer getUserCourseRepeatLikeCount(Integer userCourseRepeatId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getUserCourseRepeatLikeCount",userCourseRepeatId);
    }

    /**
     * 新增用户绘本跟读点赞数据
     * @param userId
     * @param userBookRepeatId
     * @return
     */
    public Integer insUserBookRepeatLike(Integer userId, Integer userBookRepeatId){
        UserBookRepeat userBookRepeat = this.getUserBookRepeat(userBookRepeatId);
        if(userBookRepeat == null){
            throw new KidbridgeSimpleException("未知的绘本跟读信息 ~");
        }
        UserBookRepeatLike userBookRepeatLike = this.userBookRepeatLikeExist(userId,userBookRepeatId);
        if(userBookRepeatLike != null && userBookRepeatLike.getId() != null){
            // 如果用户已经点过赞，则删除点赞记录
            this.updateUserBookRepeatLike(new UserBookRepeatLike(){
                @Override
                public Integer getId() {
                    return userBookRepeatLike.getId();
                }

                @Override
                public Boolean getDelFlag() {
                    return true;
                }
            });
            return 0; // 用户点赞动作，0取消点赞 1点赞
        }
        // 插入点赞数据
        this.sqlSessionTemplate.insert(this.namespace + "insUserBookRepeatLike",new UserBookRepeatLike(){
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
            public UserBookRepeat getUserBookRepeat() {
                return new UserBookRepeat(){
                    @Override
                    public Integer getId() {
                        return userBookRepeatId;
                    }
                };
            }
        });
        return 1;
    }

    /**
     * 新增用户课程跟读点赞信息
     * @param userId
     * @param userCourseRepeatId
     * @return
     */
    public Integer insUserCourseRepeatLike(Integer userId, Integer userCourseRepeatId){
        UserCourseRepeat userCourseRepeat = this.getUserCourseRepeat(userCourseRepeatId);
        if(userCourseRepeat == null){
            throw new KidbridgeSimpleException("未知的课程跟读信息 ~");
        }
        UserCourseRepeatLike userCourseRepeatLike = this.userCourseRepeatLikeExist(userId,userCourseRepeatId);
        if(userCourseRepeatLike != null && userCourseRepeatLike.getId() != null){
            // 如果用户已经点过赞，则删除点赞记录
            this.updateUserCourseRepeatLike(new UserCourseRepeatLike(){
                @Override
                public Integer getId() {
                    return userCourseRepeatLike.getId();
                }

                @Override
                public Boolean getDelFlag() {
                    return true;
                }
            });
            return 0; // 用户点赞动作，0取消点赞 1点赞
        }
        // 新增点赞数据
        this.sqlSessionTemplate.insert(this.namespace + "insUserCourseRepeatLike",new UserCourseRepeatLike(){
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
            public UserCourseRepeat getUserCourseRepeat() {
                return new UserCourseRepeat(){
                    @Override
                    public Integer getId() {
                        return userCourseRepeatId;
                    }
                };
            }
        });
        return 1;
    }


    /**
     * 用户课程跟读，当前用户是否已点过赞
     * @param userId
     * @param userCourseRepeatId
     * @return
     */
    public UserCourseRepeatLike userCourseRepeatLikeExist(Integer userId,Integer userCourseRepeatId ){
        return this.sqlSessionTemplate.selectOne(this.namespace + "userCourseRepeatLikeExist",new UserCourseRepeatLike(){
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
            public UserCourseRepeat getUserCourseRepeat() {
                return new UserCourseRepeat(){
                    @Override
                    public Integer getId() {
                        return userCourseRepeatId;
                    }
                };
            }
        });
    }

    /**
     * 用户绘本跟读，当前用户是否已点过赞
     * @param userId
     * @param userBookRepeatId
     * @return
     */
    public UserBookRepeatLike userBookRepeatLikeExist(Integer userId,Integer userBookRepeatId ){
        return this.sqlSessionTemplate.selectOne(this.namespace + "userBookRepeatLikeExist",new UserBookRepeatLike(){
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
            public UserBookRepeat getUserBookRepeat() {
                return new UserBookRepeat(){
                    @Override
                    public Integer getId() {
                        return userBookRepeatId;
                    }
                };
            }
        });
    }

    /**
     * 绘本跟读，用户跟读榜
     * @param bookId
     * @param page
     * @return
     */
    public List getUserBookRepeatRank(Integer bookId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserBookRepeatRank",bookId,page);
    }

    /**
     * 更新绘本点赞记录
     * @param userBookRepeatLike
     * @return
     */
    public Integer updateUserBookRepeatLike(UserBookRepeatLike userBookRepeatLike){
        return this.sqlSessionTemplate.update(this.namespace + "updateUserBookRepeatLike",userBookRepeatLike);
    }

    /**
     * 更新课程点赞记录
     * @param userCourseRepeatLike
     * @return
     */
    public Integer updateUserCourseRepeatLike(UserCourseRepeatLike userCourseRepeatLike){
        return this.sqlSessionTemplate.update(this.namespace + "updateUserCourseRepeatLike",userCourseRepeatLike);
    }

    /**
     * 用户绘本跟读评论列表
     * @param userBookRepeatId
     * @param page
     * @return
     */
    public List getUserBookRepeatCommentList(Integer userBookRepeatId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserBookRepeatCommentList",new HashMap(){{
            this.put("userBookRepeatId",userBookRepeatId);
            this.put("page",page);
        }});
    }

    /**
     * 用户课程跟读评论列表
     * @param userCourseRepeatId
     * @param page
     * @return
     */
    public List getUserCourseRepeatCommentList(Integer userCourseRepeatId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserCourseRepeatCommentList",new HashMap(){{
            this.put("userCourseRepeatId",userCourseRepeatId);
            this.put("page",page);
        }});
    }

    @Deprecated
    public List getUserCourseRepeatTeacherCommentList(Integer userCourseRepeatId){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserCourseRepeatTeacherCommentList",userCourseRepeatId);
    }

    /**
     * 获取教师评论列表
     * @param userId
     * @return
     */
    public List getTeacherComment(Integer userId){
        return this.sqlSessionTemplate.selectList(this.namespace + "teacherComment",userId);
    }

    /**
     * 获取绘本评论详情
     * @param userBookRepeatCommentId
     * @return
     */
    public UserBookRepeatComment getUserBookRepeatComment(Integer userBookRepeatCommentId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getUserBookRepeatComment",userBookRepeatCommentId);
    }

    /**
     * 新增用户绘本跟读评论
     * @param userId
     * @param userBookRepeatId
     * @param text
     * @param audio
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    public void insUserBookRepeatComment(Integer userId,Integer userBookRepeatId,String text,Map audio) throws APIConnectionException, APIRequestException, JsonProcessingException {
        if (StringUtils.isBlank(text) && audio == null) {
            throw new KidbridgeSimpleException("未知的评论内容 ~");
        }
        UserBookRepeat userBookRepeat = this.getUserBookRepeat(userBookRepeatId);
        if (userBookRepeat == null) {
            throw new KidbridgeSimpleException("未知的绘本跟读 ~");
        }
        UserBookRepeatComment userBookRepeatComment = new UserBookRepeatComment() {
            @Override
            public User getUser() {
                return new User() {
                    @Override
                    public Integer getId() {
                        return userId;
                    }
                };
            }

            @Override
            public UserBookRepeat getUserBookRepeat() {
                return new UserBookRepeat() {
                    @Override
                    public Integer getId() {
                        return userBookRepeatId;
                    }
                };
            }

            @Override
            public Map getContent() {
                return new HashMap() {{
                    this.put("text", (StringUtils.isBlank(text) ? "" : text));
                    this.put("audio", new HashMap() {{
                        this.put("source", audio == null ? "" : audio.get("source"));
                        this.put("time", audio == null ? 0 : audio.get("time"));
                    }});
                }};
            }
        };
        this.sqlSessionTemplate.insert(this.namespace + "insUserBookRepeatComment", userBookRepeatComment);
        User user = this.get(new User(){
            @Override
            public Integer getId() {
                return userId;
            }
        });
//        if(user.getId().intValue() != userBookRepeat.getUserBook().getUser().getId().intValue()){
//
//        }
        // 发送评论消息通知
        CommentPush commentPush = new CommentPush();
        commentPush.setComment(userBookRepeatId, userBookRepeatComment.getId(), 0);
        commentPush.setUser(user.getId(),user.getHead(),user.getNickname());
        commentPush.setMessage((text+(audio == null ? "" : "[音频内容]")));
        this.pushService.sendCommentPush(userBookRepeat.getUserBook().getUser().getId(), commentPush);
    }

    /**
     * 新增用户绘本跟读评论，子评论
     * @param userId
     * @param userBookRepeatId
     * @param quote
     * @param text
     * @param audio
     * @return
     */
    public Integer insUserBookRepeatCommentReply(Integer userId,Integer userBookRepeatId,Integer quote, String text,Map audio){
        if((StringUtils.isBlank(text) && audio == null) || quote == null){
            throw new KidbridgeSimpleException("非法的请求参数 ~");
        }
        // 父级评论信息
        UserBookRepeatComment userBookRepeatComment = this.getUserBookRepeatComment(quote);
        if(userBookRepeatComment == null){
            throw new KidbridgeSimpleException("未知的评论 ~");
        }
        // 用户跟读信息
        UserBookRepeat userBookRepeat = this.getUserBookRepeat(userBookRepeatId);
        if(userBookRepeat == null){
            throw new KidbridgeSimpleException("未知的绘本跟读 ~");
        }
        // 插入数据
        return this.sqlSessionTemplate.insert(this.namespace + "insUserBookRepeatCommentReply",new UserBookRepeatComment(){
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
            public UserBookRepeat getUserBookRepeat() {
                return new UserBookRepeat(){
                    @Override
                    public Integer getId() {
                        return userBookRepeatId;
                    }
                };
            }

            @Override
            public UserBookRepeatComment getQuote() {
                return new UserBookRepeatComment(){
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
                        this.put("time",audio == null ? 0 : audio.get("time"));
                    }});
                }};
            }
        });
    }

    /**
     * 新增用户课程跟读评论
     * @param userId
     * @param userCourseRepeatId
     * @param text
     * @param audio
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    public void insUserCourseRepeatComment(Integer userId,Integer userCourseRepeatId,String text,Map audio) throws APIConnectionException, APIRequestException, JsonProcessingException {
        if(StringUtils.isBlank(text) && audio == null){
            throw new KidbridgeSimpleException("未知的评论内容 ~");
        }
        UserCourseRepeat userCourseRepeat = this.getUserCourseRepeat(userCourseRepeatId);
        if(userCourseRepeat == null){
            throw new KidbridgeSimpleException("未知的课程跟读 ~");
        }
        UserCourseRepeatComment userCourseRepeatComment = new UserCourseRepeatComment(){
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
            public UserCourseRepeat getUserCourseRepeat() {
                return new UserCourseRepeat(){
                    @Override
                    public Integer getId() {
                        return userCourseRepeatId;
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
        };
        User user = this.get(new User(){
            @Override
            public Integer getId() {
                return userId;
            }
        });
        this.sqlSessionTemplate.insert(this.namespace + "insUserCourseRepeatComment",userCourseRepeatComment);
        if(user.getId().intValue() != userCourseRepeat.getUserCourse().getUser().getId().intValue()){

        }
        // 评论通知推送
        CommentPush commentPush = new CommentPush();
        commentPush.setComment(userCourseRepeatId,userCourseRepeatComment.getId(),1);
        commentPush.setUser(user.getId(),user.getHead(),user.getNickname());
        commentPush.setMessage((text+(audio== null ? "" : "[音频内容]")));
        this.pushService.sendCommentPush(userCourseRepeat.getUserCourse().getUser().getId(),commentPush);
    }

    /**
     * 获取用户课程跟读评论详情
     * @param userCourseRepeatCommentId
     * @return
     */
    public UserCourseRepeatComment getUserCourseRepeatComment(Integer userCourseRepeatCommentId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getUserCourseRepeatComment",userCourseRepeatCommentId);
    }

    /**
     * 新增用户课程跟读评论，子评论
     * @param userId
     * @param userCourseRepeatId
     * @param quote
     * @param text
     * @param audio
     * @return
     */
    public Integer insUserCourseRepeatCommentReply(Integer userId,Integer userCourseRepeatId,Integer quote, String text,Map audio){
        if((StringUtils.isBlank(text) && audio == null) || quote == null){
            throw new KidbridgeSimpleException("非法的请求参数 ~");
        }
        UserCourseRepeatComment userCourseRepeatComment = this.getUserCourseRepeatComment(quote);
        if(userCourseRepeatComment == null){
            throw new KidbridgeSimpleException("未知的评论 ~");
        }
        UserCourseRepeat userCourseRepeat = this.getUserCourseRepeat(userCourseRepeatId);
        if(userCourseRepeat == null){
            throw new KidbridgeSimpleException("未知的课程跟读 ~");
        }
        return this.sqlSessionTemplate.insert(this.namespace + "insUserCourseRepeatCommentReply",new UserCourseRepeatComment(){
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
            public UserCourseRepeat getUserCourseRepeat() {
                return new UserCourseRepeat(){
                    @Override
                    public Integer getId() {
                        return userCourseRepeatId;
                    }
                };
            }

            @Override
            public UserCourseRepeatComment getQuote() {
                return new UserCourseRepeatComment(){
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

    /**
     * 用户注册，发送短信验证码
     * @param phone
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    public synchronized void registerVerification(String phone) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        User user = this.get(new User(){
            @Override
            public String getPhone() {
                return phone;
            }
        });
        if(user != null){
            throw new KidbridgeSimpleException("该手机号码已被注册 ~");
        }
        this.smsService.verificationCode(phone);
    }

    /**
     * 用户修改密码，发送短信验证码
     * @param phone
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    public synchronized void updatePasswordVerification(String phone) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        User user = this.get(new User(){
            @Override
            public String getPhone() {
                return phone;
            }
        });
        if(user == null){
            throw new KidbridgeSimpleException("该手机号码未被注册 ~");
        }
        this.smsService.verificationCode(phone);
    }

    /**
     * 修改头像
     * @param userId
     * @param head
     * @return
     */
    public Integer updateHead(Integer userId, String head){
        if(StringUtils.isBlank(head)){
            throw new KidbridgeSimpleException("头像信息为空 ~");
        }
        return this.update(new User(){
            @Override
            public Integer getId() {
                return userId;
            }

            @Override
            public String getHead() {
                return head;
            }
        });
    }

    /**
     * 修改昵称
     * @param userId
     * @param nickname
     * @return
     */
    public Integer updateNickname(Integer userId,String nickname){
        if(StringUtils.isBlank(nickname)){
            throw new KidbridgeSimpleException("昵称信息为空 ~");
        }
        return this.update(new User(){
            @Override
            public Integer getId() {
                return userId;
            }

            @Override
            public String getNickname() {
                return nickname;
            }
        });
    }

    /**
     * 修改签名
     * @param userId
     * @param signature
     * @return
     */
    public Integer updateSignature(Integer userId, String signature){
        return this.update(new User(){
            @Override
            public Integer getId() {
                return userId;
            }

            @Override
            public String getSignature() {
                return signature;
            }
        });
    }

    /**
     * 修改收货地址
     * @param userId
     * @param contact
     * @param phone
     * @param region
     * @param street
     * @return
     */
    public Integer updateReceiving(Integer userId,String contact,String phone,String region,String street){
        if(StringUtils.isBlank(contact)){
            throw new KidbridgeSimpleException("联系人信息为空 ~");
        }
        if(StringUtils.isBlank(phone)){
            throw new KidbridgeSimpleException("联系方式信息为空 ~");
        }
        if(StringUtils.isBlank(region)){
            throw new KidbridgeSimpleException("区域信息为空 ~");
        }
        if(StringUtils.isBlank(street)){
            throw new KidbridgeSimpleException("街道信息为空 ~");
        }
        return this.update(new User(){
            @Override
            public Integer getId() {
                return userId;
            }

            @Override
            public String getReceivingContact() {
                return contact;
            }

            @Override
            public String getReceivingPhone() {
                return phone;
            }

            @Override
            public String getReceivingRegion() {
                return region;
            }

            @Override
            public String getReceivingStreet() {
                return street;
            }
        });
    }

    /**
     * 修改出生日期
     * @param userId
     * @param birthday
     * @return
     */
    public Integer updateBirthday(Integer userId,String birthday){
        if(StringUtils.isBlank(birthday)){
            throw new KidbridgeSimpleException("生日信息为空 ~");
        }
        Date birthdayDate = null;
        try{
            birthdayDate = DateUtils.parseDate(birthday,"yyyyMMdd");
        } catch (ParseException e) {
            throw new KidbridgeSimpleException("日期格式有误 ~");
        }
        Date finalBirthdayDate = birthdayDate;
        return this.update(new User(){
            @Override
            public Integer getId() {
                return userId;
            }

            @Override
            public Date getBirthday() {
                return finalBirthdayDate;
            }
        });
    }

    /**
     * 修改密码
     * @param phone
     * @param code
     * @param password
     */
    public void updatePassword(String phone,String code,String password){
        if(StringUtils.isBlank(phone)){
            throw new KidbridgeSimpleException("请输入用户手机号码 ~");
        }
        if(StringUtils.isBlank(password)){
            throw new KidbridgeSimpleException("请输入新密码 ~");
        }
        if(StringUtils.isBlank(code)){
            throw new KidbridgeSimpleException("请输入短信验证码 ~");
        }
        String storageKey = String.format("%s:%s",Configuration.SYSTEM_SMS_NAMESPACE,phone);
        String storageCode = ((String)this.redisTemplate.opsForValue().get(storageKey));
        if(StringUtils.isBlank(storageCode)){
            throw new KidbridgeSimpleException("短信验证码已失效或未获取 ~");
        }
        if(!storageCode.equals(code)){
            throw new KidbridgeSimpleException("短信验证码输入有误 ~");
        }
        this.update(new User(){
            @Override
            public String getPhone() {
                return phone;
            }

            @Override
            public String getPassword() {
                return DigestUtils.md5Hex(String.format("%s:%s:%s",phone,password,Configuration.SYSTEM_SIGN_SALT));
            }
        });
        this.redisTemplate.delete(storageKey);
    }

    /**
     * 用户注册
     * @param phone
     * @param password
     * @param code
     * @param bind
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(String phone,String password,String code,String bind){
        if(StringUtils.isBlank(phone)){
            throw new KidbridgeSimpleException("请输入注册手机号码 ~");
        }
        if(StringUtils.isBlank(password)){
            throw new KidbridgeSimpleException("请输入注册密码 ~");
        }
        if(StringUtils.isBlank(code)){
            throw new KidbridgeSimpleException("请输入短信验证码 ~");
        }
        String storageKey = String.format("%s:%s",Configuration.SYSTEM_SMS_NAMESPACE,phone);
        // redis中存储的正确验证码
        String storageCode = ((String)this.redisTemplate.opsForValue().get(storageKey));
        if(StringUtils.isBlank(storageCode)){
            throw new KidbridgeSimpleException("短信验证码已失效或未获取 ~");
        }
        // 用户输入的验证码与正确的验证码比对
        if(!storageCode.equals(code)){
            throw new KidbridgeSimpleException("短信验证码输入有误 ~");
        }
        // 构建数据模型
        User user = new User(){
            @Override
            public String getPhone() {
                return phone;
            }

            @Override
            public String getPassword() {
                return DigestUtils.md5Hex(String.format("%s:%s:%s",phone,password,Configuration.SYSTEM_SIGN_SALT));
            }
        };
        // 新增用户数据
        this.sqlSessionTemplate.insert(this.namespace + "register",user);
        if(!StringUtils.isBlank(bind)){
            // 如果bind字段不为空，说明有绑定第三方授权信息
            Map authc = (HashMap) this.redisTemplate.opsForValue().get(String.format("%s:%s",Configuration.SYSTEM_AUTHC_NAMESPACE,bind));
            if(authc == null){
                throw new KidbridgeSimpleException("授权会话不存在或已过期 ~");
            }
            // 插入授权信息，与用户信息关联
            this.authcService.save(new Authc(){
                @Override
                public User getUser() {
                    return new User(){
                        @Override
                        public Integer getId() {
                            return user.getId();
                        }
                    };
                }
                @Override
                public String getCode() {
                    return (String) authc.get("code");
                }
                @Override
                public Integer getType() {
                    return (int) authc.get("type");
                }
            });
        }
        this.redisTemplate.delete(storageKey);
    }

    /**
     * 获取用户课程跟读列表
     * @param userId
     * @param pageRowBounds
     * @return
     */
    public List getUserCourseRepeatList(Integer userId, PageRowBounds pageRowBounds){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserCourseRepeatList",userId,pageRowBounds);
    }

    /**
     * 获取课程下的绘本跟读列表
     * @param userCourseId
     * @param pageRowBounds
     * @return
     */
    public List getUserCourseBookRepeatList(Integer userCourseId, PageRowBounds pageRowBounds){
        return this.sqlSessionTemplate.selectList(this.namespace + "getUserCourseBookRepeatList",userCourseId,pageRowBounds);
    }

    public void assetsUpdate(Integer userId, BigDecimal fee, Integer feeType,Integer billType){
        this.assetsUpdate(userId, fee, feeType, billType,new HashMap());
    }

    /**
     * 水滴到余额兑换
     * @param userId
     * @param bonus
     */
    @Transactional(rollbackFor = Exception.class)
    public void balanceRatio(Integer userId,Integer bonus){
        if(bonus == null || bonus.intValue() <= 0){
            throw new KidbridgeSimpleException("非法的水滴数量 ~");
        }
        String ratio = Configuration.property(Configuration.BONUS_TO_BALANCE_RATIO);
        if(bonus.intValue() < Integer.valueOf(ratio).intValue()){
            throw new KidbridgeSimpleException("水滴数量最少为：" + ratio);
        }
        BigDecimal balance = (new BigDecimal(String.valueOf(bonus)).divide(new BigDecimal(ratio)));
        // 更新剩余水滴信息
        this.assetsUpdate(userId,new BigDecimal(bonus).negate(),1,5);
        // 更新余额信息
        this.assetsUpdate(userId,balance,0,6);
    }

    /**
     * 用户余额或水滴更新
     * @param userId
     * @param fee
     * @param feeType
     * @param billType
     * @param option
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void assetsUpdate(Integer userId, BigDecimal fee, Integer feeType,Integer billType,Map option){
        // 获取用户信息
        User user = this.get(new User() {
            @Override
            public Integer getId() {
                return userId;
            }
        });
        // 0余额 1水滴
        switch (feeType.intValue()){
            case 0:
                if(fee.compareTo(new BigDecimal("0")) == -1){
                    if (user.getBalance().compareTo(fee.abs()) == -1) {
//                        throw new KidbridgeSimpleException(MessageType.INSUFFICIENT_BALANCE, "用户余额不足 ~");
                        throw new KidbridgeSimpleException("用户余额不足 ~");
                    }
                }
                this.update(new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }

                    @Override
                    public BigDecimal getBalance() {
                        return fee;
                    }
                });
                break;
            case 1:
                if(fee.compareTo(new BigDecimal("0")) == -1){
                    if (user.getBonus().compareTo(Integer.valueOf(fee.abs().intValue())) == -1) {
//                        throw new KidbridgeSimpleException(MessageType.INSUFFICIENT_BONUS, "用户余额不足 ~");
                        throw new KidbridgeSimpleException("用户余额不足 ~");
                    }
                }
                this.update(new User(){
                    @Override
                    public Integer getId() {
                        return userId;
                    }

                    @Override
                    public Integer getBonus() {
                        return fee.intValue();
                    }
                });
                break;
            default:
                throw new KidbridgeSimpleException("未知的消费类别 ~");
        }

        // 记录收支明细信息
        this.billService.add(new Bill(){
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
            public BigDecimal getFee() {
                return fee;
            }

            @Override
            public Integer getFeeType() {
                return feeType;
            }

            @Override
            public Integer getBillType() {
                return billType;
            }

            @Override
            public Map getOption() {
                return option;
            }
        });
    }

    /**
     * 用户绘本跟读打赏
     * @param userId
     * @param fee
     * @param userBookReoeatId
     */
    public void userBookRepeatReward(Integer userId, BigDecimal fee,Integer userBookReoeatId){
        if(fee.compareTo(new BigDecimal("0")) <= 0){
            throw new KidbridgeSimpleException("请输入正确的打赏金额 ~");
        }
        UserBookRepeat userBookRepeat = this.getUserBookRepeat(userBookReoeatId);
        if(userBookReoeatId == null){
            throw new KidbridgeSimpleException("未知的被打赏绘本跟读 ~");
        }
        this.assetsUpdate(userId,fee.negate(),0,3,new HashMap(){{
            this.put("user",userBookRepeat.getUserBook().getUser().getId());
            this.put("type",2);
            this.put("target",userBookReoeatId);
        }});
        this.assetsUpdate(userBookRepeat.getUserBook().getUser().getId(),fee,0,4,new HashMap(){{
            this.put("user",userId);
            this.put("type",2);
            this.put("target",userBookReoeatId);
        }});
    }

    /**
     * 用户课程跟读打赏
     * @param userId
     * @param fee
     * @param userCourseRepeatId
     */
    public void userCourseRepeatReward(Integer userId, BigDecimal fee,Integer userCourseRepeatId){
        if(fee.compareTo(new BigDecimal("0")) <= 0){
            throw new KidbridgeSimpleException("请输入正确的打赏金额 ~");
        }
        UserCourseRepeat userCourseRepeat = this.getUserCourseRepeat(userCourseRepeatId);
        if(userCourseRepeat == null){
            throw new KidbridgeSimpleException("未知的被打赏课程跟读 ~");
        }
        this.assetsUpdate(userId,fee.negate(),0,3,new HashMap(){{
            this.put("user",userCourseRepeat.getUserCourse().getUser().getId());
            this.put("type",3);
            this.put("target",userCourseRepeatId);
        }});
        this.assetsUpdate(userCourseRepeat.getUserCourse().getUser().getId(),fee,0,4,new HashMap(){{
            this.put("user",userId);
            this.put("type",3);
            this.put("target",userCourseRepeatId);
        }});
    }


}
