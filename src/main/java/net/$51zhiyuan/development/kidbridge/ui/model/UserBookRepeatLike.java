package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户绘本跟读点赞
 */
public class UserBookRepeatLike implements Serializable {

    private Integer id;
    private User user;
    private UserBookRepeat userBookRepeat;
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

    @Override
    public String toString() {
        return "UserBookRepeatLike{" +
                "id=" + id +
                ", user=" + user +
                ", userBookRepeat=" + userBookRepeat +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
