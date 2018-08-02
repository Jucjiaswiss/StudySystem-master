package com.cqupt.zhaoyujia.studysystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cqupt.zhaoyujia.studysystem.Utils.BaseActivity;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.adapter.MyStudyAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.Student;
import com.cqupt.zhaoyujia.studysystem.bean.Study;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class MyStudyActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MyStudyAdapter mAdapter;
    private ArrayList<Course> courses;
    private Context context;
    private Data data;
    private BmobQuery<Course> query;
    private Student student;
    private Course course;
    private String studentId;
    private BmobQuery<Study> q1;
    private BmobQuery<Study> q2;
    private BmobQuery<Study> q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);

        setContentView(R.layout.my_study);
        setTitle("我的学习");

        context = this;
        setDrawer();
        data = (Data) getApplication();
        studentId=data.getStudent_id();
        init();
        getDatas();
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mystudy_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courses = new ArrayList<Course>();

        mAdapter = new MyStudyAdapter(context, courses);
        mRecyclerView.setAdapter(mAdapter);

        course=new Course();
        student = new Student();
        student.setObjectId(studentId);
    }

    private void getDatas() {
        query = new BmobQuery<>();
        //student = new Student();
        //student.setObjectId(studentId);
        query.addWhereRelatedTo("studying", new BmobPointer(student));
        query.findObjects(context, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> list) {
                courses.clear();
                for (Course c : list) {
                    courses.add(c);
                }
                mAdapter.notifyData(courses);
                mAdapter.setOnItemClickListener(new MyStudyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /**
                         * 在我的学习中的课程，点击之后去哪里？
                         *
                         */
                        Intent intent = new Intent(context, CoursePieceActivity.class);
                        intent.putExtra("courseid", courses.get(position).getObjectId());
                        startActivity(intent);
                    }
                });
                mAdapter.setOnItemLongClickListener(new MyStudyAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, final int position) {
                        /**
                         * 长按出现删除选项
                         */
                        new AlertDialog.Builder(context)
                                .setTitle("确认删除《"+courses.get(position).getCname()+"》吗？")
                                .setIcon(R.mipmap.icon)
                                //.setView(new EditText(context))若需要输入信息
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /**
                                         * 删除
                                         */
                                        deleteOneStudy(courses.get(position).getObjectId());
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Utils.toastLong(context,"长按课程就能选择删除啦！");
                return false;
            }
        });
        return true;
    }

    private void deleteOneStudy(final String courseId) {
        course.setObjectId(courseId);

        q1=new BmobQuery<>();
        q2=new BmobQuery<>();
        q1.addWhereEqualTo("student",student);
        q2.addWhereEqualTo("course",course);

        List<BmobQuery<Study>> andQuerys = new ArrayList<BmobQuery<Study>>();
        andQuerys.add(q1);
        andQuerys.add(q2);

        q=new BmobQuery<>();
        q.and(andQuerys);
        q.findObjects(context, new FindListener<Study>() {
            @Override
            public void onSuccess(List<Study> list) {
                //Utils.toastLong(context,list.size()+"");
                if(list.size()!=0){
                    list.get(0).delete(context);
                }
                deleteRelation();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void deleteRelation() {
        BmobRelation studieds= new BmobRelation();
        BmobRelation studyings= new BmobRelation();
        studieds.remove(student);
        studyings.remove(course);

        student.setStudying(studyings);
        course.setStudied(studieds);

        student.update(context);
        course.update(context);

        getDatas();
    }

}
