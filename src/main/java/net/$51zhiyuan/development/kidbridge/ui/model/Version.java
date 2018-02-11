package net.$51zhiyuan.development.kidbridge.ui.model;

import java.util.Date;

/**
 * Created by hkhl.cn on 2018/2/10.
 */
public class Version {

    private Integer id;
    private Integer device;
    private String content;
    private String number;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public Version setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getDevice() {
        return device;
    }

    public Version setDevice(Integer device) {
        this.device = device;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Version setContent(String content) {
        this.content = content;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Version setNumber(String number) {
        this.number = number;
        return this;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public Version setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Version setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Version setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "Version{" +
                "id=" + id +
                ", device=" + device +
                ", content='" + content + '\'' +
                ", number='" + number + '\'' +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
