package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.AudioFile;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class ListenPieceActivity extends Activity {

    private Context context;
    private String audio_id;
    private TextView listen_title;
    private TextView listen_type;
    private TextView listen_text;
    private ImageButton play_btn;
    private ImageButton stop_btn;
    private boolean isPlaying = false;
    private boolean isPlayerCreated= false;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setContentView(R.layout.listen_piece);

        context = this;
        audio_id = getIntent().getExtras().getString("audioid");
        listen_title = (TextView) findViewById(R.id.listen_piece_title);
        listen_type = (TextView) findViewById(R.id.listen_piece_type);
        listen_text = (TextView) findViewById(R.id.listen_piece_text);
        play_btn = (ImageButton) findViewById(R.id.listen_piece_play);
        stop_btn = (ImageButton) findViewById(R.id.listen_piece_stop);
        listen_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        initDatas();
    }

    private void initDatas() {
        BmobQuery<AudioFile> query = new BmobQuery<AudioFile>();
        query.getObject(context, audio_id, new GetListener<AudioFile>() {
            @Override
            public void onSuccess(final AudioFile file) {

                listen_title.setText(file.getTitle());
                listen_type.setText(file.getType());
                listen_text.setText(file.getText());
                play_btn.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View v) {
                        /**
                         * 播放mp3
                          */
                         if(!isPlaying){
                             isPlaying=true;
                             play_btn.setImageResource(R.mipmap.pause);
                             if(player==null){
                                 player=createPlayer(file.getAudio());
                                 isPlayerCreated=true;
                                 player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                                      @Override
                                      public void onCompletion(MediaPlayer mp) {
                                          mp.release();//释放音频资源
                                          stop_btn.setEnabled(false);
                                      }
                                 });
                                 try {
                                     //在播放音频资源之前，必须调用Prepare方法完成些准备工作
                                     player.prepare();
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                             try {
                                 //不能放在这，不然每次都不能继续播放
                                 //if(isPlayerCreated) player.prepare();
                                 player.start();
                                 Utils.toastShort(context,"开始播放...");
                             } catch (IllegalStateException e) {
                                 e.printStackTrace();
                             }
                         }else{
                             if(player!=null){
                                 player.pause();//暂停
                                 Utils.toastShort(context,"暂停");
                             }
                             isPlaying=false;
                             play_btn.setImageResource(R.mipmap.play_btn);
                         }
                        stop_btn.setEnabled(true);
                    }
                }
                );
                stop_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * 停止播放
                         */
                        Utils.toastShort(context,"停止");
                        stopMp3();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void stopMp3(){
        if(player!=null){
            player.stop();//停止播放
            player.release();//释放资源
            player=null;
            isPlaying=false;
            isPlayerCreated=false;
            play_btn.setImageResource(R.mipmap.play_btn);
            stop_btn.setEnabled(false);
        }
    }

    public MediaPlayer createPlayer(String url){
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        MediaPlayer mp=new MediaPlayer();
        try {
            mp.setDataSource(url);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalStateException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return mp;
    }

    /**
     * 页面关闭时自动停止播放
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopMp3();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMp3();
    }
}
