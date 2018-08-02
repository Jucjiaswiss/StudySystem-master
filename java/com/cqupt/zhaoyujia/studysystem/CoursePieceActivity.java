package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.Utils.HttpUtils;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.Student;


import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class CoursePieceActivity extends Activity {

    private Context context;
    private String courseId;
    private String studentId;
    private TextView course_title;
    private TextView course_major;
    private TextView course_subject;
    private TextView course_intro;
    private TextView course_time;
    private ImageView course_pic;
    private Data data;
    private Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setContentView(R.layout.course_piece);
        setTitle("课程学习");

        context = this;
        courseId = getIntent().getExtras().getString("courseid");
        data = (Data) getApplication();
        studentId = data.getStudent_id();
        init();
        initDatas();
    }

    private void init() {
        course_title= (TextView) findViewById(R.id.course_piece_cname);
        course_major= (TextView) findViewById(R.id.course_piece_major);
        course_subject= (TextView) findViewById(R.id.course_piece_subject);
        course_intro= (TextView) findViewById(R.id.course_piece_intro);
        course_time= (TextView) findViewById(R.id.course_piece_time);
        course_pic= (ImageView) findViewById(R.id.course_piece_pic);
        add_btn= (Button) findViewById(R.id.course_piece_add);

        setBtnState();
    }

    private void initDatas() {
        BmobQuery<Course> query = new BmobQuery<Course>();
        query.getObject(context, courseId, new GetListener<Course>() {
            @Override
            public void onSuccess(final Course course) {
                course_title.setText(course.getCname());
                course_major.setText(course.getMajor());
                course_subject.setText(course.getSubject());
                course_intro.setText(course.getIntro());
                course_time.setText(course.getUpdatedAt());
                /**
                 * 匹配图片，下载
                 */
                BmobFile file=course.getPic();
                HttpUtils.displayImgViaVolley(course_pic,file.getFileUrl(context),context);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    public void addStudy(View v) {
        if (!courseId.equals("") && !studentId.equals("")) {
            Utils.addRelationToStudy(context,courseId,studentId,add_btn);
        }else{
            Utils.toastLong(context,"student id 为空！不能添加！");
        }
    }

    private void setBtnState() {
        if (!courseId.equals("") && !studentId.equals("")) {
            BmobQuery<Student> query = new BmobQuery<>();
            Course c = new Course();
            c.setObjectId(courseId);
            query.addWhereRelatedTo("studied", new BmobPointer(c));
            query.findObjects(context, new FindListener<Student>() {
                @Override
                public void onSuccess(List<Student> list) {
                    for (Student s : list) {
                        if (s.getObjectId().equals(studentId)) {
                            add_btn.setText("已添加到学习计划中");
                            add_btn.setClickable(false);
                            add_btn.setBackgroundColor(getResources().getColor(R.color.third_color));
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

    public void goLearn(View v){
        Intent intent = new Intent(context, CourseStudyActivity.class);
        intent.putExtra("courseid",courseId);
        startActivity(intent);
    }

}
