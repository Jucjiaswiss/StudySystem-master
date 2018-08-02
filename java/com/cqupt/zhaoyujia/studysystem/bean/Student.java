package com.cqupt.zhaoyujia.studysystem.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class Student extends BmobObject {
    private String sname;
    private String spwd;
    private BmobRelation studying;

    public BmobRelation getStudying() {

        return studying;
    }

    public void setStudying(BmobRelation studying) {
        this.studying = studying;
    }

    public String getSpwd() {
        return spwd;
    }

    public void setSpwd(String spwd) {
        this.spwd = spwd;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
}
