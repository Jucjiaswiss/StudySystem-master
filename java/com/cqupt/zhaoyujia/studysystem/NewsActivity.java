package com.cqupt.zhaoyujia.studysystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cqupt.zhaoyujia.studysystem.Utils.BaseActivity;
import com.cqupt.zhaoyujia.studysystem.Utils.DividerItemDecoration;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.adapter.NewsAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class NewsActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ArrayList<News> mDatas;
    private Context context;
    private BmobQuery<News> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setContentView(R.layout.news);
        setTitle("校园新闻");

        context = this;
        setDrawer();
        init();
        getDatas();
    }

    private void init() {
        mRecyclerView= (RecyclerView) this.findViewById(R.id.news_recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        mDatas = new ArrayList<News>();
        mAdapter=new NewsAdapter(context,mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getDatas() {
        query = new BmobQuery<News>();
        query.order("-createdAt");
        query.findObjects(this, new FindListener<News>() {
            @Override
            public void onSuccess(List<News> object) {
                mDatas.clear();
                for (News item: object) {
                    mDatas.add(item);
                }
                mAdapter.notifyData(mDatas);
                mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
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
}
