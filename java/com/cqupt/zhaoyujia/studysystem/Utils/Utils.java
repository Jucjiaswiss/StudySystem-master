package com.cqupt.zhaoyujia.studysystem.Utils;

import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Student;
import com.cqupt.zhaoyujia.studysystem.bean.Study;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class Utils {
    public static void toastLong(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
    public static void toastShort(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    public static String formatDate(String pat1,String pat2,String orgin){
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2) ;
        Date d = null ;
        try{
            d = sdf1.parse(orgin) ;
        }catch(Exception e){
            e.printStackTrace() ;
        }
        return sdf2.format(d);
    }

    public static void addRelationToStudy(final Context context,String courseId,final String studentId,final Button add_btn){
        BmobQuery<Course> query1 = new BmobQuery<>();
        query1.getObject(context, courseId, new GetListener<Course>() {
            @Override
            public void onSuccess(final Course course) {
                /**
                 * 将关系添加到课程表热；relation
                 */
                final Student student = new Student();
                student.setObjectId(studentId);
                /**
                 * 将选择的人数添加到课程表popular
                 */
                BmobQuery<Student> q=new BmobQuery<>();
                q.addWhereRelatedTo("studied",new BmobPointer(course));
                q.count(context, Student.class, new CountListener() {
                    @Override
                    public void onSuccess(int i) {
                        BmobRelation relation = new BmobRelation();
                        relation.add(student);
                        course.setStudied(relation);
                        course.setPopular(i+1);
                        course.update(context, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Utils.toastShort(context, "添加成功！");
                                if(add_btn!=null) {
                                    add_btn.setText("已添加到学习计划中");
                                    add_btn.setClickable(false);
                                    add_btn.setBackgroundColor(context.getResources().getColor(R.color.third_color));
                                }

                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Utils.toastLong(context, s);
                            }
                        });

                        /**
                         * 将关系添加到学生表
                         */
                        relation = new BmobRelation();
                        relation.add(course);
                        student.setStudying(relation);
                        student.update(context);

                        /**
                         * 往Study表中添加对应关系
                         */
                        Study study = new Study();
                        study.setStudent(student);
                        study.setCourse(course);
                        study.save(context);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
