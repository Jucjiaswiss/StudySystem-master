package com.cqupt.zhaoyujia.studysystem.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cqupt.zhaoyujia.studysystem.MainActivity;
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
public class LoginActivity extends Activity {
    private EditText name_edit;
    private EditText pwd_edit;
    private String name;
    private String password;
    private Context context;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        name_edit = (EditText) findViewById(R.id.log_name);
        pwd_edit = (EditText) findViewById(R.id.log_pwd);
        data= (Data) getApplication();
        context = this;

        name_edit.setText("12345");
        pwd_edit.setText("12345");

    }

    public void login(View v) {
        name = name_edit.getText().toString();
        password = pwd_edit.getText().toString();

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
        query.findObjects(context, new FindListener<Student>() {
            @Override
            public void onSuccess(List<Student> list) {
                // TODO Auto-generated method stub
                if(list.size()==0){
                    Utils.toastShort(context, "用户名错误！");
                    return;
                }
                for (Student stu : list) {
                    if (password.equals(stu.getSpwd())) {
                        Utils.toastShort(context, "登录成功");
                        data.setStudent_id(stu.getObjectId());
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("sid", stu.getObjectId());
                        intent.putExtra("sname", stu.getSname());
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Utils.toastShort(context, "登录失败：" + s);
                return;
            }
        });
    }

    public void goregister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, Data.REGIS_GO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Bundle b=data.getExtras(); //data为B中回传的Intent
                name_edit.setText(b.getString("sname_regi"));
                pwd_edit.setFocusable(true);
                pwd_edit.setFocusableInTouchMode(true);
                pwd_edit.requestFocus();
                pwd_edit.findFocus();
                break;
            default:
                break;
        }
    }
}
