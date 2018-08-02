package com.cqupt.zhaoyujia.studysystem;


import android.content.Context;
import android.os.Bundle;
import android.app.Activity;

import com.cqupt.zhaoyujia.studysystem.Utils.BaseActivity;


public class AboutActivity extends BaseActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        setTitle("关于");

        context = this;
        setDrawer();
    }

}
