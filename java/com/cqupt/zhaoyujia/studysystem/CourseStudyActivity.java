package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.Student;
import com.cqupt.zhaoyujia.studysystem.bean.Study;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class CourseStudyActivity extends Activity {
    private String courseId;
    private Context context;
    private TextView textView;
    private String studentId;
    private Data data;
    boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setContentView(R.layout.course_study);

        context = this;
        courseId = getIntent().getExtras().getString("courseid");
        data = (Data) getApplication();
        studentId = data.getStudent_id();
        textView = (TextView) findViewById(R.id.course_study_text);

        initDatas();
    }

    private void initDatas() {
        BmobQuery<Course> query = new BmobQuery<Course>();
        query.getObject(context, courseId, new GetListener<Course>() {
            @Override
            public void onSuccess(final Course course) {
                textView.setText(course.getContent());
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }


    public void goTest(View v) {
        BmobQuery<Student> query = new BmobQuery<>();
        Course c = new Course();
        c.setObjectId(courseId);
        query.addWhereRelatedTo("studied", new BmobPointer(c));
        query.findObjects(context, new FindListener<Student>() {
            @Override
            public void onSuccess(List<Student> list) {
                if (list.size() != 0) {
                    for (Student s : list) {
                        if (s.getObjectId().equals(studentId)) {
                            isAdded = true;
                        }
                    }
                }
                if (isAdded) {
                    turnToTest();
                } else {
                    alertDialog();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void alertDialog() {
        new AlertDialog.Builder(context)
                .setTitle("开始测试默认课程加入学习,还要继续吗？")
                .setIcon(R.mipmap.icon)
                //.setView(new EditText(context))若需要输入信息
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!courseId.equals("") && !studentId.equals("")) {
                            Utils.addRelationToStudy(context, courseId, studentId, null);
                            turnToTest();
                        }else{
                            Utils.toastLong(context,"student id 为空！不能添加！");
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void turnToTest() {
        Intent intent = new Intent(context, TestPieceActivity.class);
        intent.putExtra("courseid", courseId);
        startActivity(intent);
    }
}
