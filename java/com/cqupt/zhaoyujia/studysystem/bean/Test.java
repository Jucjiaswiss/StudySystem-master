package com.cqupt.zhaoyujia.studysystem.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhaoyujia on 16/5/7.
 */
public class Test extends BmobObject {
    private Integer score;
    private Student student;
    private Course course;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
