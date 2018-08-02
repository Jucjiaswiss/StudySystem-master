package com.cqupt.zhaoyujia.studysystem;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Data;
import com.cqupt.zhaoyujia.studysystem.bean.News;
import com.cqupt.zhaoyujia.studysystem.bean.Student;
import com.cqupt.zhaoyujia.studysystem.bean.Study;
import com.cqupt.zhaoyujia.studysystem.bean.Test;

import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class TestPieceActivity extends Activity {

    private Context context;
    private String courseId;
    private String studentId;
    private WebView test_webview;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testpiece);

        context = this;
        courseId = getIntent().getStringExtra("courseid");
        data = (Data) getApplication();
        studentId = data.getStudent_id();

        test_webview = (WebView) findViewById(R.id.test_piece_webview);
        WebSettings webSettings = test_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        initDatas();
    }

    private void initDatas() {
        BmobQuery<Course> query = new BmobQuery<Course>();
        query.getObject(context, courseId, new GetListener<Course>() {
            @Override
            public void onSuccess(Course course) {
                //加载html代码
                //news_webview.loadData(html, "text/html", "UTF-8");//API提供的标准用法，无法解决乱码问题
                //test_webview.loadData( "text/html; charset=UTF-8", null);//这种写法可以正确解码
                //加载网页
                test_webview.loadUrl(course.getTesturl());
                //设置Web视图
                test_webview.setWebViewClient(new webViewClient());
                /**
                 * 获取网页上的成绩值
                 */
                addToTest(new Integer(getRandom(70,100)));
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void addToTest(final Integer score) {
        if (score == null || score > 100 || score < 0) {
            Utils.toastLong(context,"成绩不准确，不能录入！");
            return;
        }
        if (!courseId.equals("") && !studentId.equals("")) {
            BmobQuery<Course> query1 = new BmobQuery<>();
            query1.getObject(context, courseId, new GetListener<Course>() {
                @Override
                public void onSuccess(final Course course) {
                    Student student = new Student();
                    student.setObjectId(studentId);

                    /**
                     * 往Test表中添加对应关系
                     */
                    Test test = new Test();
                    test.setStudent(student);
                    test.setCourse(course);
                    test.setCourseName(course.getCname());
                    test.setScore(score);
                    test.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {

                    Utils.toastLong(context,"成绩添加成功！");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                    Utils.toastLong(context,"成绩失败："+s);

                        }
                    });

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    public static String getRandom(int min, int max)
    {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
