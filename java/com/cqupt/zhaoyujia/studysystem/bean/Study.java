package com.cqupt.zhaoyujia.studysystem.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhaoyujia on 16/5/7.
 */
public class Study extends BmobObject {
    private Student student;
    private Course course;


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
