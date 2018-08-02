package com.cqupt.zhaoyujia.studysystem.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zhaoyujia on 16/5/7.
 */
public class Course extends BmobObject {
    private String cname;
    private String major;
    private String subject;
    private String content;
    private String testurl;
    private String intro;
    private BmobRelation studied;
    private BmobFile pic;
    private Integer popular;

    public Integer getPopular() {
        return popular;
    }

    public void setPopular(Integer popular) {
        this.popular = popular;
    }

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }

    public BmobRelation getStudied() {
        return studied;
    }

    public void setStudied(BmobRelation studied) {
        this.studied = studied;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTesturl() {
        return testurl;
    }

    public void setTesturl(String testurl) {
        this.testurl = testurl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
