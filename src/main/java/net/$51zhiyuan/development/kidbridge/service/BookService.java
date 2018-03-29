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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绘本
 */

@Service
public class BookService {

    private final Logger logger = LogManager.getLogger(BookService.class);

    private final HttpClient httpClient = new HttpClient();

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.book.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private BookSegmentService bookSegmentService;

    @Autowired
    private BillService billService;

    @Autowired
    private PushService pushService;

    /**
     * 获取单个绘本的详情信息
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Book get(Book param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<Book> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new Book());
    }

    @Deprecated
    public List<Book> list(Book param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    /**
     * 获取绘本列表
     * @param param
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Book> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 绘本信息检索
     * @param keyword
     * @param pageRowBounds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Book> search(String keyword, PageRowBounds pageRowBounds){
        return this.sqlSessionTemplate.selectList(this.namespace + "search",keyword,pageRowBounds);
    }

    /**
     * 获取绘本中的富文本信息，富文本内容存储在七牛云中
     * @param resKey
     * @return
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyManagementException
     * @throws KeyStoreException
     */
    @Transactional(rollbackFor = Exception.class)
    public String getRichtext(String resKey) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        String html = this.httpClient.doGet(String.format("http://%s/%s",Configuration.property(Configuration.QINIU_BUCKET_DOMAIN),resKey));
        return html;
    }

    /**
     * 绘本赏析
     * @param bookId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Book getAppreciation(Integer bookId){
        Book book = this.get(new Book(){
            @Override
            public Integer getId() {
                return bookId;
            }
        });
        if(book == null){
            throw new KidbridgeSimpleException("未知的绘本 ~");
        }
        return this.sqlSessionTemplate.selectOne(this.namespace + "getAppreciation", bookId);
    }

    /**
     * 绘本解锁
     * @param userId
     * @param bookId
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void unlock(Integer userId,Integer bookId) throws APIConnectionException, APIRequestException, JsonProcessingException {
        // 判断当前绘本是否已被用户解锁，如已解锁，跳过
        if(this.userBookService.exist(userId,bookId)){
            throw new KidbridgeSimpleException("已解锁该绘本 ~");
        }
        Book book = this.get(new Book() {
            @Override
            public Integer getId() {
                return bookId;
            }
        });
        // 添加用户解锁数据
        this.userBookService.add(userId,bookId,book.getPrice().compareTo(BigDecimal.valueOf(0)) == 0);
        // 消费，扣除相应余额
        this.userService.assetsUpdate(userId, book.getPrice().negate(),0,0,new HashMap(){{
            this.put("id",bookId);
        }});
        // 消息通知，通知用户已解锁绘本
        SystemPush systemPush = new SystemPush();
        systemPush.setMessage(String.format("您已解锁新绘本：《%s》，快快去跟读吧 ~",book.getName()));
        this.pushService.sendSystemPush(userId,systemPush);
    }

    /**
     * 获取某绘本的编号、名称、有效跟读时间
     * @param bookId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private Book getNameAndRepeatActiveTime(Integer bookId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getNameAndRepeatActiveTime",bookId);
    }

    /**
     * 只获取绘本的有效跟读时间
     * @param bookId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer getRepeatActiveTime(Integer bookId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "repeatActiveTime",bookId);
    }

    /**
     * 用户开始跟读，获取绘本的跟读段落
     * @param userId
     * @param token
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Book segment(Integer userId,String token){
        if(StringUtils.isBlank(token)){
            throw new KidbridgeSimpleException("非法的跟读会话 ~");
        }
        //UserBookRepeat userBookRepeat  = (UserBookRepeat) this.redisTemplate.opsForValue().get(String.format("%s:%s",Configuration.SYSTEM_REPEAT_NAMESPACE,token));
        Object userRepeat = this.redisTemplate.opsForValue().get(String.format("%s:%s",Configuration.SYSTEM_REPEAT_NAMESPACE,token));
        // 跟读token验证
        if(userRepeat == null){
            throw new KidbridgeSimpleException("无效的跟读会话 ~");
        }
        // 获取跟读的绘本编号
        int bookId = (userRepeat instanceof UserBookRepeat ? ((UserBookRepeat)userRepeat).getUserBook().getBook().getId() : ((UserCourseRepeat)userRepeat).getBook().getId());
        // 根据绘本编号查询出绘本的名称、跟读时间
        Book book = this.getNameAndRepeatActiveTime(bookId);
        if(book == null){
            throw new KidbridgeSimpleException("该绘本不存在 ~");
        }
        // 还需要验证课程中的绘本是否存在
        if(userRepeat instanceof UserBookRepeat){
            if(!this.userBookService.exist(userId, bookId)){
                throw new KidbridgeSimpleException("该绘本未解锁 ~");
            }
        }

        // 获取绘本跟读段落列表
        List<BookSegment> bookSegmentList = this.bookSegmentService.list(new BookSegment(){
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
        book.setBookSegmentList(bookSegmentList);
//        String repeatToken = UUID.randomUUID().toString().replace("-","");
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        hashOperations.put(Configuration.SYSTEM_REPEAT_TOKEN_NAMESPACE,repeatToken,new UserBook(){
//            @Override
//            public User getUser() {
//                return new User(){
//                    @Override
//                    public Integer getId() {
//                        return userId;
//                    }
//                };
//            }
//            @Override
//            public Book getBook() {
//                return new Book(){
//                    @Override
//                    public Integer getId() {
//                        return bookId;
//                    }
//                };
//            }
//
//            @Override
//            public Date getCreateTime() {
//                return new Date();
//            }
//        });
        return book;
    }

    /**
     * 获取绘本赏析区评论详情
     * @param bookCommentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BookComment getAppreciationComment(Integer bookCommentId){
        return this.sqlSessionTemplate.selectOne(this.namespace + "getAppreciationComment",bookCommentId);
    }


    /**
     * 新增绘本赏析区评论
     * @param userId
     * @param bookId
     * @param text
     * @param audio
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer insAppreciationComment(Integer userId,Integer bookId,String text,Map audio){
        // 内容校验
        if(StringUtils.isBlank(text) && audio == null){
            throw new KidbridgeSimpleException("未知的评论内容 ~");
        }
        // 获取到评论的绘本详情
        Book book = this.get(new Book(){
            @Override
            public Integer getId() {
                return bookId;
            }
        });
        // 参数校验，主要用于防治非法的数据提交
        if(book == null){
            throw new KidbridgeSimpleException("未知的绘本信息 ~");
        }
        // 插入到数据库
        return this.sqlSessionTemplate.insert(this.namespace + "insAppreciationComment",new BookComment(){
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
            public Book getBook() {
                return new Book(){
                    @Override
                    public Integer getId() {
                        return bookId;
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
     * 绘本打赏
     * @param userId
     * @param fee
     * @param bookId
     */
    @Transactional(rollbackFor = Exception.class)
    public void reward(Integer userId, BigDecimal fee,Integer bookId){
        if(fee.compareTo(new BigDecimal("0")) <= 0){
            throw new KidbridgeSimpleException("请输入正确的打赏金额 ~");
        }
        Book book = this.get(new Book(){
            @Override
            public Integer getId() {
                return bookId;
            }
        });
        if(book == null){
            throw new KidbridgeSimpleException("未知的被打赏绘本 ~");
        }
        if(book.getCopyright().getUser().getId().intValue() == -1){
            throw new KidbridgeSimpleException("未知的被打赏用户 ~");
        }
        // 打赏用户，扣除相应余额
        this.userService.assetsUpdate(userId,fee.negate(),0,3,new HashMap(){{
            // option扩展字段， 主要用于记录数据发生源信息
            this.put("user",book.getCopyright().getUser().getId());
            this.put("type",0);
            this.put("target",bookId);
        }});
        // 被打赏用户，新增相应余额
        this.userService.assetsUpdate(book.getCopyright().getUser().getId(),fee,0,4,new HashMap(){{
            // option扩展字段， 主要用于记录数据发生源信息
            this.put("user",userId);
            this.put("type",0);
            this.put("target",bookId);
        }});
    }

    /**
     * 新增绘本赏析区评论，子评论
     * @param userId
     * @param bookId
     * @param quote
     * @param text
     * @param audio
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer insAppreciationCommentReply(Integer userId,Integer bookId,Integer quote, String text,Map audio){
        if((StringUtils.isBlank(text) && audio == null) || quote == null){
            throw new KidbridgeSimpleException("非法的请求参数 ~");
        }
        // 查询出引用的父评论信息
        BookComment bookComment = this.getAppreciationComment(quote);
        if(bookComment == null){
            throw new KidbridgeSimpleException("未知的父评论 ~");
        }
        // 查询出评论的绘本信息
        Book book = this.get(new Book(){
            @Override
            public Integer getId() {
                return bookId;
            }
        });
        if(book == null){
            throw new KidbridgeSimpleException("未知的绘本信息 ~");
        }
        // 插入数据
        return this.sqlSessionTemplate.insert(this.namespace + "insAppreciationCommentReply",new BookComment(){
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
            public Book getBook() {
                return new Book(){
                    @Override
                    public Integer getId() {
                        return bookId;
                    }
                };
            }

            @Override
            public BookComment getQuote() {
                return new BookComment(){
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
     * 获取绘本赏析区的评论列表
     * @param bookId
     * @param page
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<BookComment> getAppreciationCommentList(Integer bookId,PageRowBounds page){
        return this.sqlSessionTemplate.selectList(this.namespace + "getAppreciationCommentList",new HashMap(){{
            this.put("bookId",bookId);
            this.put("page",page);
        }});
    }
}
