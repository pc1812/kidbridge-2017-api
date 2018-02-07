package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户课程
 */
public class UserCourse implements Serializable {

    private Integer id;
    private User user;
    private Course course;
    private List<UserCourseRepeat> userCourseRepeatList;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<UserCourseRepeat> getUserCourseRepeatList() {
        return userCourseRepeatList;
    }

    public void setUserCourseRepeatList(List<UserCourseRepeat> userCourseRepeatList) {
        this.userCourseRepeatList = userCourseRepeatList;
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
        return "UserCourse{" +
                "id=" + id +
                ", user=" + user +
                ", course=" + course +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
