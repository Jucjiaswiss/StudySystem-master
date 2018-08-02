package com.cqupt.zhaoyujia.studysystem.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cqupt.zhaoyujia.studysystem.AboutActivity;
import com.cqupt.zhaoyujia.studysystem.MainActivity;
import com.cqupt.zhaoyujia.studysystem.MyResultsActivity;
import com.cqupt.zhaoyujia.studysystem.MyStudyActivity;
import com.cqupt.zhaoyujia.studysystem.NewsActivity;
import com.cqupt.zhaoyujia.studysystem.R;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyujia on 16/5/12.
 */
public class BaseActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    protected Context context=this;
    private Intent intent;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setDrawer(){
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)this.findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)this.findViewById(R.id.navdrawer);


        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();



        /**
         * 自定义图片加上文字的adapter
         */

        SimpleAdapter madapter = new SimpleAdapter(this, getData(), R.layout.list_item_drawer, new String[] { "title",  "img" }, new int[] { R.id.title, R.id.img });
        mDrawerList.setAdapter(madapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(context, MyStudyActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, MyResultsActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(context, NewsActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        share.putExtra(Intent.EXTRA_SUBJECT,
                                getString(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT,
                                "分享给你们我的学习软件\n" +
                                 "安装链接：" +"studysystemzyj.bmob.cn");
                        startActivity(Intent.createChooser(share,
                                getString(R.string.app_name)));
                        break;
                    case 5:
                        intent = new Intent(context, AboutActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    private List<Map<String, Object>> getData() {


        String[] values = new String[]{
                "学习主页",
                "我的学习",
                "我的成绩",
                "校园新闻",
                "分享",
                "关于"
        };
        int[] values_img = new int[]{
                R.mipmap.home,
                R.mipmap.notebook,
                R.mipmap.write,
                R.mipmap.news,
                R.mipmap.share,
                R.mipmap.about
        };
        //map.put(参数名字,参数值)
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        for (int i=0;i<values.length;i++){

            map = new HashMap<String, Object>();
            map.put("title", values[i]);
            map.put("img",values_img[i]);
            list.add(map);

        }

        return list;
    }
}
