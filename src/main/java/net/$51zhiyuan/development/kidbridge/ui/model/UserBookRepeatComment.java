package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户绘本跟读评论
 */
public class UserBookRepeatComment implements Serializable {

    private Integer id;
    private User user;
    private UserBookRepeat userBookRepeat;
    private List<UserBookRepeatComment> replyList;
    private Map content;
    private UserBookRepeatComment quote;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserBookRepeat getUserBookRepeat() {
        return userBookRepeat;
    }

    public void setUserBookRepeat(UserBookRepeat userBookRepeat) {
        this.userBookRepeat = userBookRepeat;
    }

    public Map getContent() {
        return content;
    }

    public void setContent(Map content) {
        this.content = content;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UserBookRepeatComment getQuote() {
        return quote;
    }

    public void setQuote(UserBookRepeatComment quote) {
        this.quote = quote;
    }

    public List<UserBookRepeatComment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<UserBookRepeatComment> replyList) {
        this.replyList = replyList;
    }

    @Override
    public String toString() {
        return "UserBookRepeatComment{" +
                "id=" + id +
                ", user=" + user +
                ", userBookRepeat=" + userBookRepeat +
                ", content='" + content + '\'' +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
