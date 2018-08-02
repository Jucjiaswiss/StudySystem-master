package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.News;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class NewsPieceActivity extends Activity {

    private Context context;
    private String news_id;
    private TextView news_author;
    private TextView news_time;
    private TextView news_title;
    private WebView news_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);
        setContentView(R.layout.news_piece);

        context=this;
        news_id=getIntent().getExtras().getString("newsid");
      //  Utils.toastLong(context,news_id);
        news_author= (TextView) findViewById(R.id.news_piece_author);
        news_time= (TextView) findViewById(R.id.news_piece_time);
        news_title= (TextView) findViewById(R.id.news_piece_title);
        news_webview= (WebView) findViewById(R.id.news_piece_content);
        WebSettings webSettings = news_webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        //webSettings.setBuiltInZoomControls(true);
        webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        initDatas();
    }

    private void initDatas() {
        BmobQuery<News> query = new BmobQuery<News>();
        query.getObject(context, news_id, new GetListener<News>() {
            @Override
            public void onSuccess(News news) {
                String s=Utils.formatDate("yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日 HH:mm",
                       news.getCreatedAt() );
                String html=news.getContent();

                news_author.setText(news.getAuthor());
                news_time.setText(s);
                news_title.setText(news.getTitle());
                //加载网页
                //news_webview.loadUrl(url);
                //news_webview.loadData(html, "text/html", "UTF-8");//API提供的标准用法，无法解决乱码问题
                news_webview.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解码
                //设置Web视图
                news_webview.setWebViewClient(new webViewClient ());
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
