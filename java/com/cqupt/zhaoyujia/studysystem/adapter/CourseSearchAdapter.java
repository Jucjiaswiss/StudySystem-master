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
import com.cqupt.zhaoyujia.studysystem.bean.Course;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class CourseSearchAdapter extends RecyclerView.Adapter<CourseSearchAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Course> courses;

    public CourseSearchAdapter(Context c, ArrayList<Course> courses){
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
                context).inflate(R.layout.course_searchitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_cname.setText(courses.get(position).getCname());
        holder.tv_major.setText(courses.get(position).getMajor());
        holder.tv_subject.setText(courses.get(position).getSubject());
        holder.tv_intro.setText(courses.get(position).getIntro());
        /**
         * 网络下载图片显示
         */
        BmobFile file=courses.get(position).getPic();
        HttpUtils.displayImgViaVolley(holder.pic_view,file.getFileUrl(context),context);

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView pic_view;
        TextView tv_cname;
        TextView tv_major;
        TextView tv_subject;
        TextView tv_intro;

        public MyViewHolder(View view) {
            super(view);
            tv_cname = (TextView) view.findViewById(R.id.course_s_item_cname);
            tv_major = (TextView) view.findViewById(R.id.course_s_item_major);
            tv_subject = (TextView) view.findViewById(R.id.course_s_item_subject);
            tv_intro = (TextView) view.findViewById(R.id.course_s_item_intro);
            pic_view= (ImageView) view.findViewById(R.id.course_s_item_pic);
            LinearLayout layout= (LinearLayout) view.findViewById(R.id.course_s_item);
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



