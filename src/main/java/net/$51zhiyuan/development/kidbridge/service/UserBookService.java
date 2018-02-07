package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.Book;
import net.$51zhiyuan.development.kidbridge.ui.model.User;
import net.$51zhiyuan.development.kidbridge.ui.model.UserBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户绘本
 */
@Service
public class UserBookService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.userBook.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 获取用户解锁的绘本
     * @param param
     * @return
     */
    public UserBook get(UserBook param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<UserBook> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new UserBook());
    }

    @Deprecated
    public List<UserBook> list(UserBook param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<UserBook> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

    /**
     * 用户解锁绘本
     * @param userId
     * @param bookId
     * @param free
     * @return
     */
    public Integer add(Integer userId, Integer bookId, Boolean free){
        return this.sqlSessionTemplate.insert(this.namespace + "add",new UserBook(){
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
            public Boolean getFree() {
                return free;
            }
        });
    }

    /**
     * 获取用户解锁的绘本列表
     * @param userId
     * @param free
     * @param pageRowBounds
     * @return
     */
    public List<Book> userBook(Integer userId,int free,PageRowBounds pageRowBounds){
        return this.sqlSessionTemplate.selectList(this.namespace + "userBook",new UserBook(){
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
            public Boolean getFree() {
                return (free == 1);
            }
        },pageRowBounds);
    }

    /**
     * 判断用户是否已解锁某一绘本
     * @param userId
     * @param bookId
     * @return
     */
    public Boolean exist(Integer userId,Integer bookId){
        return (((int)this.sqlSessionTemplate.selectOne(this.namespace + "exist",new UserBook(){
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
        })) > 0);
    }
}
