package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程评论
 */
public class CourseComment implements Serializable {

    private Integer id;
    private CourseComment quote;
    private List<CourseComment> replyList;
    private Course course;
    private User user;
    private Map content;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CourseComment getQuote() {
        return quote;
    }

    public void setQuote(CourseComment quote) {
        this.quote = quote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map getContent() {
        return content;
    }

    public void setContent(Map content) {
        this.content = content;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<CourseComment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<CourseComment> replyList) {
        this.replyList = replyList;
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
}
