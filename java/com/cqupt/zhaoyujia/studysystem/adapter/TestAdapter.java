package com.cqupt.zhaoyujia.studysystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.TestPieceActivity;
import com.cqupt.zhaoyujia.studysystem.Utils.HttpUtils;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Course;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Course> courses;

    public TestAdapter(Context c, ArrayList<Course> courses){
        this.context=c;
        this.courses=courses;
    }

    public void notifyData(ArrayList<Course> courses){
        this.courses=courses;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.testitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String s=Utils.formatDate("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd",courses.get(position).getUpdatedAt());

        holder.tv_title.setText(courses.get(position).getCname());
        holder.tv_time.setText(s);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TestPieceActivity.class);
                intent.putExtra("courseid", courses.get(position).getObjectId());
                context.startActivity(intent);
            }
        });

        BmobFile file=courses.get(position).getPic();
        HttpUtils.displayImgViaVolley(holder.pic_view,file.getFileUrl(context),context);

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView pic_view;
        TextView tv_title;
        TextView tv_time;
        Button btn;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.testitem_title);
            tv_time = (TextView) view.findViewById(R.id.testitem_time);
            pic_view= (ImageView) view.findViewById(R.id.testitem_pic);
            btn=(Button)view.findViewById(R.id.testitem_btn);
        }
    }
}



