package net.$51zhiyuan.development.kidbridge.ui.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.$51zhiyuan.development.kidbridge.module.Configuration;
import net.$51zhiyuan.development.kidbridge.module.KidbridgePageRowBounds;
import net.$51zhiyuan.development.kidbridge.service.BookSegmentService;
import net.$51zhiyuan.development.kidbridge.service.BookService;
import net.$51zhiyuan.development.kidbridge.service.SearchRecordService;
import net.$51zhiyuan.development.kidbridge.service.UserBookService;
import net.$51zhiyuan.development.kidbridge.ui.model.*;
import org.apache.commons.lang3.StringUtils;
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
 * 绘本
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private BookSegmentService bookSegmentService;

    @Autowired
    private SearchRecordService searchRecordService;

    /**
     * 绘本列表
     * @param param fit：适合年龄段 0：3-5, 1：6-8, 2：9-12
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    Message list(@RequestBody Map<String,Object> param){
        // 条件过滤模型
        Book book = new Book();
        book.setUser(new User(){
            @Override
            public Integer getId() {
                // 当前用户id
                return (int)SecurityUtils.getSubject().getPrincipal();
            }
        });
        // 是否在首页显示
        book.setActive(true);
        if(param.get("fit") != null){
            // 年龄段
            book.setFit((int) param.get("fit"));
        }
        // 绘本列表
        List<Book> bookList = this.bookService.list(book,new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(bookList);
    }

    /**
     * 绘本信息详情
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/get")
    Message get(@RequestBody Map<String,Object> param){
        // 绘本详情
        Book book =  this.bookService.get(new Book(){
            @Override
            public Integer getId() {
                // 绘本编号
                return (int) param.get("id");
            }
        });
        // 当前用户是否已解锁该绘本
        UserBook belong = this.userBookService.get(new UserBook(){
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
            public Book getBook() {
                return new Book(){
                    @Override
                    public Integer getId() {
                        return (int) param.get("id");
                    }
                };
            }
        });
        return new Message(new HashMap(){{
            this.put("book",book); // 绘本信息
            this.put("belong",(belong == null ? -1 : belong.getId())); // 当前用户是否已解锁该绘本,0未解锁 1已解锁
        }});
    }

    /**
     * 绘本赏析H5富文本页面
     * @param id 绘本编号
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
        // 富文本视图
        ModelAndView view = new ModelAndView("/richtext/book");
        // 获取绘本信息
        Book book = this.bookService.get(new Book(){
            @Override
            public Integer getId() {
                return Integer.valueOf(id);
            }
        });
        // 获取七牛云存储的富文本
        String html = (id.equals("-1") ? "暂无内容"  : this.bookService.getRichtext(book.getRichText()));
        view.addObject("book",book); // 绘本信息
        view.addObject("html",html.replace("data-w","width")); // 富文本信息
        return view;
    }

    /**
     * 绘本检索
     * @param param keyword：搜索关键词
     * @return
     */
    @ResponseBody
    @RequestMapping("/search")
    Message search(@RequestBody Map<String,Object> param){
        if(param.get("keyword") != null && !StringUtils.isBlank(param.get("keyword").toString())){
            // 记录搜索关键词
            this.searchRecordService.add(new SearchRecord(){
                @Override
                public User getUser() {
                    return new User(){
                        @Override
                        public Integer getId() {
                            return (int) SecurityUtils.getSubject().getPrincipal();
                        }
                    };
                }

                @Override
                public String getKeyword() {
                    return (String) param.get("keyword");
                }
            });
        }
        // 绘本列表
        List<Book> bookList = this.bookService.search((String) param.get("keyword"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(bookList);
    }

    /**
     * 绘本解锁
     * @param param id：待解锁的绘本编号
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping("/unlock")
    Message unlock(@RequestBody Map<String,Object> param) throws APIConnectionException, APIRequestException, JsonProcessingException {
        this.bookService.unlock((int) SecurityUtils.getSubject().getPrincipal(),(int) param.get("id"));
        return new Message();
    }

    /**
     * 绘本跟读，段落信息
     * @param param repeat：绘本跟读token
     * @return
     */
    @ResponseBody
    @RequestMapping("/segment")
    Message segment(@RequestBody Map<String,Object> param){
        // 绘本详情
        Book book = this.bookService.segment(
                (int) SecurityUtils.getSubject().getPrincipal(),
                (String) param.get("repeat")
        );
        return new Message(book);
    }

    /**
     * 绘本赏析
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation")
    Message appreciation(@RequestBody Map<String,Object> param){
        // 绘本详情
        Book book = this.bookService.getAppreciation((int)param.get("id"));
        return new Message(new HashMap(){{
            this.put("book",book);
        }});
    }

    /**
     * 绘本赏析，评论信息提交
     * @param param id：绘本编号，text：评论文本，audio评论音频
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation/comment")
    Message appreciation_comment(@RequestBody Map<String,Object> param){
        this.bookService.insAppreciationComment((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 绘本赏析评论信息列表
     * @param param id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation/comment/list")
    Message appreciation_comment_list(@RequestBody Map<String,Object> param){
        List<BookComment> bookCommentList = this.bookService.getAppreciationCommentList((int)param.get("id"),new KidbridgePageRowBounds((Integer)param.get(Configuration.SYSTEM_PAGINATION_OFFSET),(Integer)param.get(Configuration.SYSTEM_PAGINATION_LIMIT)));
        return new Message(new HashMap(){{
            this.put("comment",bookCommentList); // 评论列表
        }});
    }

    /**
     * 绘本赏析，子回复评论提交
     * @param param id：绘本编号，quote：父评论id，text：评论文本，audio评论音频
     * @return
     */
    @ResponseBody
    @RequestMapping("/appreciation/comment/reply")
    Message appreciation_comment_reply(@RequestBody Map<String,Object> param){
        this.bookService.insAppreciationCommentReply((int)SecurityUtils.getSubject().getPrincipal(),(int)param.get("id"),(int)param.get("quote"),(String)param.get("text"),(HashMap) param.get("audio"));
        return new Message();
    }

    /**
     * 绘本打赏
     * @param param fee：打赏金额，id：绘本编号
     * @return
     */
    @ResponseBody
    @RequestMapping("/reward")
    Message reward(@RequestBody Map<String,Object> param){
        this.bookService.reward((int)SecurityUtils.getSubject().getPrincipal(), BigDecimal.valueOf(Double.parseDouble(param.get("fee").toString())),(int)param.get("id"));
        return new Message();
    }

}
