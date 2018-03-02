package net.$51zhiyuan.development.kidbridge.ui.model;

import java.math.BigDecimal;
import java.util.Date;

public class IOSIAPRecharge {

    private String id;
    private User user;
    private BigDecimal fee;
    private IOSIAPProduct iosiapProduct;
    private Integer quantity;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public IOSIAPProduct getIosiapProduct() {
        return iosiapProduct;
    }

    public void setIosiapProduct(IOSIAPProduct iosiapProduct) {
        this.iosiapProduct = iosiapProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
        return "IOSIAPRecharge{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", fee=" + fee +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
