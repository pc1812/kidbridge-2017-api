package net.$51zhiyuan.development.kidbridge.ui.model;

import java.util.Date;

/**
 * 用户搜索关键词
 */
public class SearchRecord {

    private Integer id;
    private User user;
    private String keyword;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public SearchRecord setId(Integer id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public SearchRecord setUser(User user) {
        this.user = user;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public SearchRecord setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public SearchRecord setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public SearchRecord setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public SearchRecord setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "SearchRecord{" +
                "id=" + id +
                ", user=" + user +
                ", keyword='" + keyword + '\'' +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
