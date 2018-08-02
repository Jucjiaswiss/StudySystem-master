package com.cqupt.zhaoyujia.studysystem.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhaoyujia on 16/5/7.
 */
public class News extends BmobObject {
    private String title;
    private String content;
    private String intro;
    private String author;
    private Integer read;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
