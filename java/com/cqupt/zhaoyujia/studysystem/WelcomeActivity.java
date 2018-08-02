package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cqupt.zhaoyujia.studysystem.Utils.HttpUtils;
import com.cqupt.zhaoyujia.studysystem.login.LoginActivity;

public class WelcomeActivity extends Activity {

    private ImageView welcomeImage;
    private Thread t1, t2;
    private Handler handler;
    private Animation animation;
    private Bitmap mDownloadImage;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);

        welcomeImage = (ImageView) findViewById(R.id.welcome_bg);
        animation = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        mContext=getApplicationContext();

        /**
         get pic from the Internet(here两种方法)
         1.AysncTask:可以自己设置任何任务，设置的是imageview的背景，符合要求
         2.Volley(ImageLoader)：只能设置src，不能达到缩放效果
         两者速度相当，显示图片资源时，可以使用volly
         */
        new downloadImageTask().execute("http://7xsqmo.com1.z0.glb.clouddn.com/welcome_bg4.jpg");
        //HttpUtils.displayImgViaVolley(welcomeImage,"http://7xsqmo.com1.z0.glb.clouddn.com/welcome_bg5.jpeg",mContext);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    startAnimation();
                }
            }
        };
        t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        });
        t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    turnPage();// 跳转界面
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }

    /**
     * 异步网络下载图片
     */
    class downloadImageTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            mDownloadImage = HttpUtils.getNetWorkBitmap(params[0]);
            return true;
        }

        // 下载完成回调
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            welcomeImage.setBackgroundDrawable(new BitmapDrawable(mDownloadImage));
            super.onPostExecute(result);
        }

        // 更新进度回调
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

    }

    /**
     * turn to main activity
     */
    private void turnPage() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    /**
     * change the background of welcome page
     */
    private void startAnimation() {
        welcomeImage.setAnimation(animation);
        welcomeImage.startAnimation(animation);
        t2.start();
    }
}
