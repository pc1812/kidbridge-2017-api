package net.$51zhiyuan.development.kidbridge.ui.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 书单
 */
public class Bookshelf implements Serializable {

    private Integer id;
    private String name;
    private String icon;
    private Map cover;
    private Integer fit;
    private List tag;
    private List<Book> bookList;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Map getCover() {
        return cover;
    }

    public void setCover(Map cover) {
        this.cover = cover;
    }

    public Integer getFit() {
        return fit;
    }

    public void setFit(Integer fit) {
        this.fit = fit;
    }

    public List getTag() {
        return tag;
    }

    public void setTag(List tag) {
        this.tag = tag;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
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
        return "Bookshelf{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", cover=" + cover +
                ", fit=" + fit +
                ", tag=" + tag +
                ", delFlag=" + delFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
