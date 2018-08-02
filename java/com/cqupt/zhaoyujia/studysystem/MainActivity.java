package com.cqupt.zhaoyujia.studysystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cqupt.zhaoyujia.studysystem.Utils.BaseActivity;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.adapter.HomeAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends BaseActivity {

    private Context context;

    private RecyclerView mRecyclerView;
    private ArrayList<News> mDatas;
    private HomeAdapter mAdapter;
    private BmobQuery<News> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setContentView(R.layout.activity_main);

        context = this;

        init();
        setDrawer();
        getNews();

    }

    public void init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.homenews_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatas = new ArrayList<News>();
        mAdapter=new HomeAdapter(context,mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }


    protected void getNews() {
        query = new BmobQuery<News>();
        //分页查询
        //query.setLimit(5);
        query.order("-createdAt");
        query.findObjects(this, new FindListener<News>() {
            @Override
            public void onSuccess(List<News> object) {
                mDatas.clear();
                for (News item: object) {
                  mDatas.add(item);
                }
                mAdapter.notifyData(mDatas);
                mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, NewsPieceActivity.class);
                        intent.putExtra("newsid", mDatas.get(position).getObjectId());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });
        return true;
    }

    public void homeLearn(View v){
       Intent intent=new Intent(this,LearnActivity.class);
        startActivity(intent);
    }

    public void homeListen(View v){
        Intent intent=new Intent(this,ListenActivity.class);
        startActivity(intent);
    }

    public void homeTest(View v){
        Intent intent=new Intent(this,LearnActivity.class);
        intent.putExtra("test","test");
        startActivity(intent);
    }

}