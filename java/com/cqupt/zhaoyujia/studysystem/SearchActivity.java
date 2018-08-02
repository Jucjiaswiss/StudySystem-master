package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.adapter.CourseAdapter;
import com.cqupt.zhaoyujia.studysystem.adapter.CourseSearchAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.Student;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class SearchActivity extends Activity {
    private String search_words;
    private EditText edit_search;
    private RecyclerView mRecyclerView;
    private CourseSearchAdapter mAdapter;
    private ArrayList<Course> mDatas;
    private Context context;
    private BmobQuery<Course> q1;
    private BmobQuery<Course> q2;
    private BmobQuery<Course> q3;
    private BmobQuery<Course> q4;
    private List<BmobQuery<Course>> queries;
    private BmobQuery<Course> mainQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setTitle("搜索");
        setContentView(R.layout.search);

        context=this;
        init();
    }

    public void init(){
        edit_search= (EditText) findViewById(R.id.edit_search);
        mDatas = new ArrayList<Course>();

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter=new CourseSearchAdapter(context,mDatas);
        mRecyclerView.setAdapter(mAdapter);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    mDatas.clear();
                    mAdapter.notifyData(mDatas);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void search(View view){
        search_words=edit_search.getText().toString();
        if(search_words.trim().equals("")){
            Utils.toastShort(context,"输入为空！");
            return;
        }
        search(search_words);
    }

    private void search(String word) {
        /**
         * 复合查询条件
         * */
        q1 = new BmobQuery<Course>();
        q2 = new BmobQuery<Course>();
        q3 = new BmobQuery<Course>();
        q4 = new BmobQuery<Course>();
        q1.addWhereContains("cname",word);
        q2.addWhereContains("major",word);
        q3.addWhereContains("subject",word);
        q4.addWhereContains("intro",word);

        queries = new ArrayList<BmobQuery<Course>>();
        queries.add(q1);
        queries.add(q2);
        queries.add(q3);
        queries.add(q4);

        mainQuery= new BmobQuery<Course>();
        mainQuery.or(queries);
        mainQuery.order("-updatedAt");
        mainQuery.findObjects(this, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> object) {
                mDatas.clear();
                if(object.size()==0){
                    Utils.toastLong(context,"无查询结果！");
                }else {
                    for (Course file : object) {
                        mDatas.add(file);
                        /**
                         * 匹配字符，将匹配字符变为其他颜色
                         */
                    }
                }
                mAdapter.notifyData(mDatas);
                mAdapter.setOnItemClickListener(new CourseSearchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, CoursePieceActivity.class);
                        intent.putExtra("courseid", mDatas.get(position).getObjectId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Utils.toastShort(context,"查询失败：" + msg);
            }
        });
    }
}
