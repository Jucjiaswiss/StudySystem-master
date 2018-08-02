package com.cqupt.zhaoyujia.studysystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.R;
import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.AudioFile;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import java.util.ArrayList;

/**
 * Created by zhaoyujia on 16/5/8.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<AudioFile> audioes;

    public AudioAdapter(Context c, ArrayList<AudioFile> audioes){
        this.context=c;
        this.audioes=audioes;
    }

    public void notifyData(ArrayList<AudioFile> audioes){
        this.audioes=audioes;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.listen_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_listen_title.setText(audioes.get(position).getTitle());
        holder.tv_listen_type.setText(audioes.get(position).getType());
        holder.tv_listen_text.setText(audioes.get(position).getText().substring(0,50));

    }

    @Override
    public int getItemCount() {
        return audioes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_listen_title;
        TextView tv_listen_type;
        TextView tv_listen_text;

        public MyViewHolder(View view) {
            super(view);
            tv_listen_title= (TextView) view.findViewById(R.id.listenitem_title);
            tv_listen_type= (TextView) view.findViewById(R.id.listenitem_type);
            tv_listen_text= (TextView) view.findViewById(R.id.listenitem_text);
            LinearLayout layout= (LinearLayout) view.findViewById(R.id.listenitem_cardview);
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



