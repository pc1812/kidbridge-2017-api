package net.$51zhiyuan.development.kidbridge.ui.model;

import java.util.Date;

public class IOSIAPProduct {

    private Integer id;
    private String name;
    private String product;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public IOSIAPProduct setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IOSIAPProduct setName(String name) {
        this.name = name;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public IOSIAPProduct setProduct(String product) {
        this.product = product;
        return this;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public IOSIAPProduct setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public IOSIAPProduct setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public IOSIAPProduct setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "IOSIAP{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", product='" + product + '\'' +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
