package com.cqupt.zhaoyujia.studysystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqupt.zhaoyujia.studysystem.Utils.Utils;
import com.cqupt.zhaoyujia.studysystem.adapter.CourseAdapter;
import com.cqupt.zhaoyujia.studysystem.adapter.TestAdapter;
import com.cqupt.zhaoyujia.studysystem.bean.Course;
import com.cqupt.zhaoyujia.studysystem.bean.Data;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhaoyujia on 16/5/6.
 */
public class LearnActivity extends Activity {

    private RecyclerView mRecyclerView;
    private CourseAdapter mAdapter;
    private TestAdapter tAdapter;
    private ArrayList<Course> mDatas;
    private Context context;

    private TextView classify_btn;
    private ImageView classify_icon;
    private TextView major_text;
    private TextView subject_text;
    private ListView list_filter;
    private LinearLayout menu;
    private RadioGroup majorGroup;
    private RadioGroup orderGroup;
    private String[] majorsArrays;
    private String[] subjectsArrays = null;

    private FilterAdapter adapter;
    private BmobQuery<Course> query;

    private boolean isMenuOpen = false;
    private boolean isTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Data.BMOBID);

        setContentView(R.layout.learn);

        context = this;
        findViews();
        if (getIntent().getStringExtra("test") != null && getIntent().getStringExtra("test").equals("test")) {
            isTest = true;
            tAdapter = new TestAdapter(context, new ArrayList<Course>());
            mRecyclerView.setAdapter(tAdapter);
        } else {
            mAdapter = new CourseAdapter(context, new ArrayList<Course>());
            mRecyclerView.setAdapter(mAdapter);
        }
        init();
        addListener();

        getDatas("全部", "全部", true);

    }

    private void findViews() {
        classify_btn = (TextView) findViewById(R.id.learn_selector_classify);
        classify_icon = (ImageView) findViewById(R.id.learn_selector_classify_icon);
        major_text = (TextView) findViewById(R.id.learn_selector_major);
        subject_text = (TextView) findViewById(R.id.learn_selector_subject);
        list_filter = (ListView) findViewById(R.id.learn_selector_allsubject_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.learn_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        menu = (LinearLayout) findViewById(R.id.learn_selector_menu);
        majorGroup = (RadioGroup) findViewById(R.id.learn_selector_allmajor_radioGroup);
        orderGroup = (RadioGroup) findViewById(R.id.learn_selector_order_radioGroup);
    }

    private void init() {
        majorsArrays = getResources().getStringArray(R.array.rank_classify);
        mDatas = new ArrayList<Course>();

        major_text.setText("全部");
        subject_text.setText("全部");

        //首次添加adapter
        adapter = new FilterAdapter(new String[0]);
        list_filter.setAdapter(adapter);

        for (int i = 0; i < majorsArrays.length; i++) {
            /**
             * 动态添加所有专业到目录中
             */
            RadioButton rb = new RadioButton(context);
            rb.setId(i);
            rb.setBackground(null);
            rb.setButtonDrawable(null);
            rb.setGravity(Gravity.CENTER_VERTICAL);
            rb.setBackgroundResource(R.drawable.text_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dip2px(50));
            rb.setText(majorsArrays[i]);
            if (i == 0) rb.setChecked(true);

            majorGroup.addView(rb, params);
        }

        setSubject("全部");
    }

    private void addListener() {
        classify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMenuOpen) {
                    menu.setVisibility(View.VISIBLE);
                    classify_icon.setImageResource(R.drawable.img_triangle_down_gray);
                    isMenuOpen = true;
                } else {
                    menu.setVisibility(View.GONE);
                    classify_icon.setImageResource(R.drawable.img_triangle_right_gray);
                    isMenuOpen = false;
                }
            }
        });

        majorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                setSubject(rb.getText().toString());
                major_text.setText(rb.getText().toString());
                subject_text.setText("");
            }
        });

        orderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.learn_selector_rankpopular:
                        getDatas(major_text.getText().toString(), subject_text.getText().toString(), false);
                        break;
                    case R.id.learn_selector_ranktime:
                        getDatas(major_text.getText().toString(), subject_text.getText().toString(), true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setSubject(String major) {
        query = new BmobQuery<>();
        if (!major.equals("全部")) {
            query.addWhereEqualTo("major", major);
        }
        query.findObjects(context, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> list) {
                //用查询到的数组大小，可以节约长度
                if (list.size() == 0) {
                    subjectsArrays = new String[0];
                } else {
                    subjectsArrays = new String[list.size() + 1];
                    subjectsArrays[0] = "全部";
                    for (int i = 1; i <= list.size(); i++) {
                        subjectsArrays[i] = list.get(i - 1).getSubject();
                    }
                }
                List aslist = Arrays.asList(subjectsArrays);
                Set set = new HashSet(aslist);
                subjectsArrays=(String [])set.toArray(new String[0]);
                adapter.adddata(subjectsArrays); //么么哒 教你的
                //adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private void getDatas(String major, String subject, boolean isTime) {
        query = new BmobQuery<>();
        //添加筛选条件
        if (!major.equals("全部")) {
            query.addWhereEqualTo("major", major);
        }
        if (!subject.equals("全部")) {
            query.addWhereEqualTo("subject", subject);
        }
        if (isTime) {
            query.order("-createdAt");
        } else {
            query.order("-popular");
        }

        query.findObjects(this, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> object) {
                mDatas.clear();
                for (Course file : object) {
                    mDatas.add(file);
                }
                /**
                 * 判断是哪里个adapter
                 */
                if (isTest) {
                    tAdapter.notifyData(mDatas);
                } else {
                    mAdapter.notifyData(mDatas);
                    mAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(context, CoursePieceActivity.class);
                            intent.putExtra("courseid", mDatas.get(position).getObjectId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Utils.toastShort(context, "查询失败：" + msg);
            }
        });
    }

    protected Handler mHandler = new MyHandler(this);

    // 刷新UI
    private static class MyHandler extends Handler {
        private final WeakReference<LearnActivity> mWeakAct;

        public MyHandler(LearnActivity activity) {
            mWeakAct = new WeakReference<LearnActivity>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            final LearnActivity activity = mWeakAct.get();
            if (activity == null) {
                return;
            }
            activity.handleMsg(msg);
        }
    }

    public void handleMsg(Message msg) {

        switch (msg.what) {
            case FilterAdapter.MSG_SUBJECT_CHOSEN:
                String tag_id = msg.getData().getString(FilterAdapter.MSG_BUNDLE_KEY_SUBJECT);
                subject_text.setText(tag_id);
                adapter.notifyDataSetInvalidated();
                menu.setVisibility(View.GONE);

                if (list_filter.getChildCount() == 0) {
                    Utils.toastShort(context, "此科目无课程！");
                }
                /**
                 *进行bombQuery查询筛选课程,默认是按更新排序
                 */
                getDatas(major_text.getText().toString(), subject_text.getText().toString(), true);

        }

    }


    private class FilterAdapter extends BaseAdapter {
        /**
         * 传来的科目值
         */
        public static final String MSG_BUNDLE_KEY_SUBJECT = "msg_bundle_key_subject";
        /**
         * message接收到的数据类型
         */
        public static final int MSG_SUBJECT_CHOSEN = 0x00011;

        private String[] data;

        public FilterAdapter(String[] str) {
            super();
            data = str;
        }

        void adddata(String[] str) {
            data = str;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = generateItemLayout();
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.id01);
            final TextView tv = (TextView) convertView.findViewById(R.id.id02);
            ImageView image = (ImageView) convertView.findViewById(R.id.id03);
            image.setVisibility(View.GONE);
            if (subject_text.getText().toString().equals(data[position])) {
                image.setVisibility(View.VISIBLE);
            }
            tv.setText(data[position]);
            layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Message msg = Message.obtain();
                    msg.what = MSG_SUBJECT_CHOSEN;
                    Bundle data = new Bundle();
                    data.putString(MSG_BUNDLE_KEY_SUBJECT, tv.getText().toString());
                    msg.setData(data);
                    mHandler.sendMessage(msg);
                }
            });
            return convertView;
        }

        public View generateItemLayout() {

            RelativeLayout layout = new RelativeLayout(context);
            layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            layout.setBackgroundColor(getResources().getColor(R.color.bg_hui));
            layout.setId(R.id.id01);

            //类别文字
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(dip2px(15), 1, 1, 1);
            textView.setId(R.id.id02);
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, dip2px(40));
            textParams.setMargins(dip2px(2), dip2px(1), dip2px(2), dip2px(1));

            layout.addView(textView, textParams);

            //右侧对勾
            ImageView nikeView = new ImageView(context);
            nikeView.setImageResource(R.drawable.img_tick);
            RelativeLayout.LayoutParams nickParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            nickParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            nickParams.addRule(RelativeLayout.CENTER_VERTICAL);
            nickParams.setMargins(0, 0, dip2px(15), 0);
            nikeView.setId(R.id.id03);

            layout.addView(nikeView, nickParams);

            //底线
            View doteView = new View(context);
            doteView.setId(R.id.id04);
            doteView.setBackgroundResource(R.drawable.shape_dotted_line_gray);
            RelativeLayout.LayoutParams doteParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dip2px(1));
            doteParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layout.addView(doteView, doteParams);

            return layout;
        }
    }

    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
