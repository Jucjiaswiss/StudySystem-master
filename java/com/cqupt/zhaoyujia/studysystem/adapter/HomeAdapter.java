package com.cqupt.zhaoyujia.studysystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.SearchActivity;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<News> newses;

    public HomeAdapter(Context c, ArrayList<News> newses){
        this.context=c;
        this.newses=newses;
    }
    public void notifyData(ArrayList<News> newses){
        this.newses=newses;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.home_newsitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String s=Utils.formatDate("yyyy-MM-dd HH:mm:ss","yyyy/MM/dd",newses.get(position).getCreatedAt());

        holder.tv_title.setText(newses.get(position).getTitle());
        holder.tv_time.setText(s);

    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_title;
        TextView tv_time;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.homenews_title);
            tv_time = (TextView) view.findViewById(R.id.homenews_time);
            LinearLayout layout= (LinearLayout) view.findViewById(R.id.home_newsitem);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null){
                itemClickListener.onItemClick(v,getPosition());
            }
        }
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener=itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}



