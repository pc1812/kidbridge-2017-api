package net.$51zhiyuan.development.kidbridge.service;

import com.github.pagehelper.PageRowBounds;
import net.$51zhiyuan.development.kidbridge.ui.model.UserBookRepeatComment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户绘本跟读点赞
 */
@Service
@Deprecated
public class UserBookRepeatLikeService {

    private final Logger logger = LogManager.getLogger(UserService.class);

    private final String namespace = "net.$51zhiyuan.development.kidbridge.dao.userBookRepeatLike.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Deprecated
    public UserBookRepeatComment get(UserBookRepeatComment param){
        return this.sqlSessionTemplate.selectOne(this.namespace + "get",param);
    }

    @Deprecated
    public List<UserBookRepeatComment> list(){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",new UserBookRepeatComment());
    }

    @Deprecated
    public List<UserBookRepeatComment> list(UserBookRepeatComment param){
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param);
    }

    @Deprecated
    public List<UserBookRepeatComment> list(Object param, PageRowBounds page) {
        return this.sqlSessionTemplate.selectList(this.namespace + "list",param,page);
    }

}
