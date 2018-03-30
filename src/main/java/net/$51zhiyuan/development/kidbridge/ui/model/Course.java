package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 课程
 */
public class Course implements Serializable {

    private Integer id;
    private CourseCopyright copyright;
    private Teacher teacher;
    private User user;
    private Integer lock;
    private String name;
    private List icon;
    private Integer fit;
    private Integer enter;
    private Integer limit;
    private Date startTime;
    private BigDecimal price;
    private String outline;
    private Integer cycle;
    private Integer status;
    private List tag;
    private List<CourseDetail> courseDetailList;
    private String richText;
    private Integer sort;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public CourseCopyright getCopyright() {
        return copyright;
    }

    public void setCopyright(CourseCopyright copyright) {
        this.copyright = copyright;
    }

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

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getIcon() {
        return icon;
    }

    public void setIcon(List icon) {
        this.icon = icon;
    }

    public Integer getFit() {
        return fit;
    }

    public void setFit(Integer fit) {
        this.fit = fit;
    }

    public Integer getEnter() {
        return enter;
    }

    public void setEnter(Integer enter) {
        this.enter = enter;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public List getTag() {
        return tag;
    }

    public void setTag(List tag) {
        this.tag = tag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CourseDetail> getCourseDetailList() {
        return courseDetailList;
    }

    public void setCourseDetailList(List<CourseDetail> courseDetailList) {
        this.courseDetailList = courseDetailList;
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

    public String getRichText() {
        return richText;
    }

    public void setRichText(String richText) {
        this.richText = richText;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", copyright=" + copyright +
                ", teacher=" + teacher +
                ", user=" + user +
                ", lock=" + lock +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", fit=" + fit +
                ", enter=" + enter +
                ", limit=" + limit +
                ", startTime=" + startTime +
                ", price=" + price +
                ", outline='" + outline + '\'' +
                ", cycle=" + cycle +
                ", status=" + status +
                ", tag=" + tag +
                ", courseDetailList=" + courseDetailList +
                ", richText='" + richText + '\'' +
                ", sort=" + sort +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
