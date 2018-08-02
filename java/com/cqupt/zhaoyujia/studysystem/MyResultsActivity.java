package com.cqupt.zhaoyujia.studysystem;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cqupt.zhaoyujia.studysystem.Utils.BaseActivity;
import com.cqupt.zhaoyujia.studysystem.adapter.MyResultsAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.Student;
import com.cqupt.zhaoyujia.studysystem.bean.Test;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class MyResultsActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MyResultsAdapter mAdapter;
    private ArrayList<Test> tests;
    private Context context;
    private Data data;
    private  BmobQuery<Test> query1;
    private Student s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);

        setContentView(R.layout.my_results);
        setTitle("我的成绩");

        context = this;
        setDrawer();
        data= (Data) getApplication();
        init();
        getDatas();
    }


    private void init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.myresults_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tests = new ArrayList<Test>();
        mAdapter=new MyResultsAdapter(context,tests);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getDatas(){
        s=new Student();
        s.setObjectId(data.getStudent_id());
        /**
         * 查询Study对应的成绩
         */
        query1 = new BmobQuery<Test>();
        query1.addWhereEqualTo("student",s);
        query1.order("-updatedAt");
        query1.findObjects(context, new FindListener<Test>() {
            @Override
            public void onSuccess(List<Test> object) {
                tests.clear();
                for(Test test:object){
                    Integer score=test.getScore();
                    if(score!=null&&score<=100&&score>=0)
                        tests.add(test);
                }
                mAdapter.notifyData(tests);
            }

            @Override
            public void onError(int code, String msg) {
            }
        });
    }
}
