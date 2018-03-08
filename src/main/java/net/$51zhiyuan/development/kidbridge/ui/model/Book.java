package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 绘本
 */
public class Book implements Serializable {

    private Integer id;
    private String name;
    private List icon;
    private BookCopyright copyright;
    private BigDecimal price;
    private Integer fit;
    private String outline;
    private String feeling;
    private String difficulty;
    private List tag;
    private Integer repeatActiveTime;
    private Boolean active;
    private List<BookSegment> bookSegmentList;
    private String richText;
    private String audio;
    private User user;
    private Integer lock;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;

    public BookCopyright getCopyright() {
        return copyright;
    }

    public Integer getLock() {
        return lock;
    }

    public void setLock(Integer lock) {
        this.lock = lock;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCopyright(BookCopyright copyright) {
        this.copyright = copyright;
    }

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

    public List getIcon() {
        return icon;
    }

    public void setIcon(List icon) {
        this.icon = icon;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getFit() {
        return fit;
    }

    public void setFit(Integer fit) {
        this.fit = fit;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List getTag() {
        return tag;
    }

    public void setTag(List tag) {
        this.tag = tag;
    }

    public Integer getRepeatActiveTime() {
        return repeatActiveTime;
    }

    public void setRepeatActiveTime(Integer repeatActiveTime) {
        this.repeatActiveTime = repeatActiveTime;
    }

    public List<BookSegment> getBookSegmentList() {
        return bookSegmentList;
    }

    public void setBookSegmentList(List<BookSegment> bookSegmentList) {
        this.bookSegmentList = bookSegmentList;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getAudio() {
        return audio;
    }

    public Book setAudio(String audio) {
        this.audio = audio;
        return this;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", copyright=" + copyright +
                ", price=" + price +
                ", fit=" + fit +
                ", outline='" + outline + '\'' +
                ", feeling='" + feeling + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", tag=" + tag +
                ", repeatActiveTime=" + repeatActiveTime +
                ", active=" + active +
                ", bookSegmentList=" + bookSegmentList +
                ", richText='" + richText + '\'' +
                ", audio='" + audio + '\'' +
                ", user=" + user +
                ", lock=" + lock +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
