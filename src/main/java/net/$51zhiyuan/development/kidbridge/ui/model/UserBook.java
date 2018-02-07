package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户绘本
 */
public class UserBook implements Serializable {

    private Integer id;
    private User user;
    private Book book;
    private Boolean free;
    private List<UserBookRepeat> userBookRepeatList;
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public List<UserBookRepeat> getUserBookRepeatList() {
        return userBookRepeatList;
    }

    public void setUserBookRepeatList(List<UserBookRepeat> userBookRepeatList) {
        this.userBookRepeatList = userBookRepeatList;
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
        return "UserBook{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", free=" + free +
                ", userBookRepeatList=" + userBookRepeatList +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
