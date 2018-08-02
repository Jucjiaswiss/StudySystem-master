package com.cqupt.zhaoyujia.studysystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import java.util.ArrayList;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<News> newses;

    public NewsAdapter(Context c, ArrayList<News> newses) {
        this.context = c;
        this.newses = newses;
    }

    public void notifyData(ArrayList<News> newses) {
        this.newses = newses;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.newsitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.MyViewHolder holder, int position) {
        holder.tv_title.setText(newses.get(position).getTitle());
        holder.tv_author.setText(newses.get(position).getAuthor());
        holder.tv_read.setText(newses.get(position).getRead() + "人已看");
    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title;
        TextView tv_author;
        TextView tv_read;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.newsitem_title);
            tv_author = (TextView) view.findViewById(R.id.newsitem_author);
            tv_read = (TextView) view.findViewById(R.id.newsitem_read);
            RelativeLayout l = (RelativeLayout) view.findViewById(R.id.newitem_cardview);
            l.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}