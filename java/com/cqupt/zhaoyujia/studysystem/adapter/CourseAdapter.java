package com.cqupt.zhaoyujia.studysystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.Utils.HttpUtils;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Course;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Course> courses;

    public CourseAdapter(Context c, ArrayList<Course> courses){
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
                context).inflate(R.layout.courseitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String s=Utils.formatDate("yyyy-MM-dd HH:mm:ss","yyyy-MM-dd",courses.get(position).getUpdatedAt());
        Integer count=courses.get(position).getPopular();

        holder.tv_title.setText(courses.get(position).getCname());
        holder.tv_time.setText(s);
        if(count==null){
            count=new Integer(0);
        }
        holder.tv_chose.setText(count+"人已学习");
        /**
         * 网络下载图片显示
         */
        //holder.pic_view.setImageResource();
        BmobFile file=courses.get(position).getPic();
        HttpUtils.displayImgViaVolley(holder.pic_view,file.getFileUrl(context),context);

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView pic_view;
        TextView tv_title;
        TextView tv_time;
        TextView tv_chose;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.courseitem_title);
            tv_time = (TextView) view.findViewById(R.id.courseitem_time);
            tv_chose = (TextView) view.findViewById(R.id.courseitem_chose);
            pic_view= (ImageView) view.findViewById(R.id.courseitem_pic);
            LinearLayout layout= (LinearLayout) view.findViewById(R.id.courseitem_cardview);
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



