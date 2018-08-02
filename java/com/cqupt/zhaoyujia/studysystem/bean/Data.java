package com.cqupt.zhaoyujia.studysystem.bean;

/**
 * Created by zhaoyujia on 16/5/6.
 */

import android.app.Application;

public class Data extends Application{
    public final static String BMOBID="0ec7b48ef413cdda5915efe9cdfe49ca";
    public final static int REGIS_GO=0;
    private String Student_id;

    @Override
    public void onCreate(){
        super.onCreate();
        Student_id="";
    }

    public String getStudent_id() {
        return Student_id;
    }

    public void setStudent_id(String student_id) {
        Student_id = student_id;
    }
}