package com.cqupt.zhaoyujia.studysystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Study;
import com.cqupt.zhaoyujia.studysystem.bean.Test;

import java.util.ArrayList;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class MyResultsAdapter extends RecyclerView.Adapter<MyResultsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Test> tests;

    public MyResultsAdapter(Context c,ArrayList<Test> tests){
        this.context=c;
        this.tests=tests;
    }

    public void notifyData(ArrayList<Test> tests){
        this.tests=tests;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.myresults_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String s= Utils.formatDate("yyyy-MM-dd HH:mm:ss","yyyy/MM/dd HH:mm",tests.get(position).getUpdatedAt());

        holder.tv_title.setText(tests.get(position).getCourseName());
        holder.tv_time.setText(s);
        holder.tv_score.setText(tests.get(position).getScore()+"");

    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        TextView tv_time;
        TextView tv_score;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.myresults_item_cname);
            tv_time = (TextView) view.findViewById(R.id.myresults_item_time);
            tv_score = (TextView) view.findViewById(R.id.myresults_item_score);
        }
    }
}



