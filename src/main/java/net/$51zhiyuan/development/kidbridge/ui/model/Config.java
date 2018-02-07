package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据库表参数配置
 */
public class Config implements Serializable {

    private Integer id;
    private String key;
    private String value;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public Config setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Config setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Config setValue(String value) {
        this.value = value;
        return this;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public Config setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Config setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Config setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
