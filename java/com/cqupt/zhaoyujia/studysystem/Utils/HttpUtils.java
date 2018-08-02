package com.cqupt.zhaoyujia.studysystem.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cqupt.zhaoyujia.studysystem.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhaoyujia on 16/4/8.
 */
public class HttpUtils {
    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl
                    .openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 创建volley请求队列
     */
    public static void displayImgViaVolley(ImageView view,String url, Context mContext){
        RequestQueue mQueue = Volley.newRequestQueue(mContext);

        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, R.mipmap.nopic, R.mipmap.nopic);
        //imageLoader.get(url, listener);
        //指定图片允许的最大宽度和高度
        imageLoader.get(url,listener,view.getMaxWidth(),view.getMaxHeight());
    }

    public static class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> cache;
        public BitmapCache() {
            cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }
        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }
        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }

}
