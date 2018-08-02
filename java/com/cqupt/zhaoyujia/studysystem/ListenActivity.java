package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.adapter.AudioAdapter;
import com.cqupt.zhaoyujia.studysystem.adapter.NewsAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.AudioFile;
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
public class ListenActivity extends Activity {

    private RecyclerView mRecyclerView;
    private AudioAdapter mAdapter;
    private ArrayList<AudioFile> mDatas;
    private Context context;
    private BmobQuery<AudioFile> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setTitle("我要听");
        setContentView(R.layout.listen);

        context=this;
        init();
        getDatas();

    }

    private void init(){
        mDatas = new ArrayList<AudioFile>();
        mRecyclerView = (RecyclerView) findViewById(R.id.listen_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mAdapter=new AudioAdapter(context,mDatas);
                mRecyclerView.setAdapter(mAdapter);

    }
    private void getDatas(){
        query = new BmobQuery<AudioFile>();
        query.order("-updatedAt");
        query.findObjects(this, new FindListener<AudioFile>() {
            @Override
            public void onSuccess(List<AudioFile> object) {
                mDatas.clear();
                for (AudioFile file: object) {
                    mDatas.add(file);
                }
                mAdapter.notifyData(mDatas);
                mAdapter.setOnItemClickListener(new AudioAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, ListenPieceActivity.class);
                        intent.putExtra("audioid", mDatas.get(position).getObjectId());
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
