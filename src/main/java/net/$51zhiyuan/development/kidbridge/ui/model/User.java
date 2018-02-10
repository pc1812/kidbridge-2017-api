package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
public class User implements Serializable {

    private Integer id;
    private String phone;
    private String password;
    private String head;
    private String nickname;
    private String age;
    private Date birthday;
    private String address;
    private Integer bonus;
    private BigDecimal balance;
    private String signature;
    private Boolean delFlag;
    private Teacher teacher;
    private List<Bill> billList;
    private List<BookCopyright> bookCopyrightList;
    private List<Feedback> feedbackList;
    private String receivingContact;
    private String receivingPhone;
    private String receivingRegion;
    private String receivingStreet;
    private Date createTime;
    private Date updateTime;

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReceivingContact() {
        return receivingContact;
    }

    public void setReceivingContact(String receivingContact) {
        this.receivingContact = receivingContact;
    }

    public String getReceivingPhone() {
        return receivingPhone;
    }

    public void setReceivingPhone(String receivingPhone) {
        this.receivingPhone = receivingPhone;
    }

    public String getPhone() {
        return phone;
    }

    public String getReceivingRegion() {
        return receivingRegion;
    }

    public void setReceivingRegion(String receivingRegion) {
        this.receivingRegion = receivingRegion;
    }

    public String getReceivingStreet() {
        return receivingStreet;
    }

    public void setReceivingStreet(String receivingStreet) {
        this.receivingStreet = receivingStreet;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    public List<BookCopyright> getBookCopyrightList() {
        return bookCopyrightList;
    }

    public void setBookCopyrightList(List<BookCopyright> bookCopyrightList) {
        this.bookCopyrightList = bookCopyrightList;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
	public String toString() {
		return "User [id=" + id + ", phone=" + phone + ", password=" + password + ", head=" + head + ", nickname="
				+ nickname + ", birthday=" + birthday + ", address=" + address + ", bonus=" + bonus + ", balance="
				+ balance + ", delFlag=" + delFlag + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
