package net.$51zhiyuan.development.kidbridge.ui.model;

import java.math.BigDecimal;
import java.util.Date;

public class IOSIAPProduct {

    private Integer id;
    private String name;
    private BigDecimal price;
    private String product;
    private String des;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDes() {
        return des;
    }

    public IOSIAPProduct setDes(String des) {
        this.des = des;
        return this;
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
        return "IOSIAPProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", product='" + product + '\'' +
                ", des='" + des + '\'' +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
