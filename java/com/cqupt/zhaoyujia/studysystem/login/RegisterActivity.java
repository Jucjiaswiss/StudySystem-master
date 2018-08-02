package com.cqupt.zhaoyujia.studysystem.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.Student;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class RegisterActivity extends Activity {
    private EditText name_edit;
    private EditText pwd_edit;
    private String name;
    private String password;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        name_edit = (EditText) findViewById(R.id.regi_name);
        pwd_edit = (EditText) findViewById(R.id.regi_pwd);
        context = this;

    }

    public void register(View v) {
        name = name_edit.getText().toString();
        password = pwd_edit.getText().toString();

        /**
         * 添加注册的判断逻辑
         * 1.为空
         * 2.长度限制
         * 3.是否已经有该名字了
         */
        if (name.trim().equals("") || password.trim().equals("")) {
            Utils.toastShort(context, "输入不能为空！");
            return;
        }
        if (name.length() < 5 || name.length() > 10) {
            Utils.toastShort(context, "名字长度范围：5～10");
            return;
        }
        if (password.length() < 5 || password.length() > 10) {
            Utils.toastShort(context, "密码长度范围：5～10");
            return;
        }
        BmobQuery<Student> query = new BmobQuery<Student>();
        query.addWhereEqualTo("sname", name);
        query.findObjects(this, new FindListener<Student>() {
            @Override
            public void onSuccess(List<Student> list) {
                // TODO Auto-generated method stub
                if(list.size()==0){
                   saveStudent();
                }else{
                    for (Student stu : list) {
                       Utils.toastLong(context, "该用户名" + name + "已被使用\n创建于:" + stu.getCreatedAt());
                       return;
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Utils.toastLong(context, "注册查询失败"+s);
            }
        });
    }

    public void saveStudent(){
        final Student s1 = new Student();
        s1.setSname(name);
        s1.setSpwd(password);
        s1.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Utils.toastShort(context, "注册成功，返回objectId为：" + s1.getObjectId());
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("sname_regi",name);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Utils.toastLong(context, "注册失败：" + msg);
            }
        });
    }
}
