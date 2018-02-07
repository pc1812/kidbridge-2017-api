package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户课程跟读
 */
public class UserCourseRepeat implements Serializable {

    private Integer id;
    private UserCourse userCourse;
    private Book book;
    private Integer like;
    private List segment;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserCourse getUserCourse() {
        return userCourse;
    }

    public void setUserCourse(UserCourse userCourse) {
        this.userCourse = userCourse;
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

    public Book getBook() {
        return book;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List getSegment() {
        return segment;
    }

    public void setSegment(List segment) {
        this.segment = segment;
    }

    @Override
    public String toString() {
        return "UserCourseRepeat{" +
                "id=" + id +
                ", userCourse=" + userCourse +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }


}
