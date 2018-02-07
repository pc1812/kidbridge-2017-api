package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 评论推送信息
 */
public class CommentPush implements Serializable {

    private Map comment;
    private Map message;
    private Map user;

    public Map getComment() {
        return comment;
    }

    public void setComment(Integer pid, Integer sid, Integer type) {
        this.comment = new HashMap();
        this.comment.put("pid", pid);
        this.comment.put("sid", sid);
        this.comment.put("type", type);
    }

    public Map getMessage() {
        return message;
    }

    public void setMessage(String text) {
        this.message = new HashMap();
        this.message.put("text", text);
        this.message.put("createTime", new Date());
    }

    public Map getUser() {
        return user;
    }

    public void setUser(Integer id, String head, String nickname) {
        this.user = new HashMap();
        this.user.put("id", id);
        this.user.put("head", head);
        this.user.put("nickname", nickname);
    }
}
