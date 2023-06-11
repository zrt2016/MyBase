package com.zrt.mybase.demo;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zrt.mybase.R;
import com.zrt.mybase.base.TabInfo;
import com.zrt.mybase.base.TopTabActivity;
import com.zrt.mybase.utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

public class TabDemoActivity extends TopTabActivity {

    public ListView mListView;
    public List<String> mList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_top_demo;
    }

    @Override
    protected void initView() {
        super.initView();
        mListView = findViewById(R.id.top_demo_list);
    }

    @Override
    protected void initData() {
        super.initData();
        for (int i=0; i<30; i++) {
            mList.add(String.format("Demo-%d", i));
        }
//        final ListDemoAdapter listDemoAdapter = new ListDemoAdapter(this, mList, R.layout.edittext);
        final MyAdapter listDemoAdapter = new MyAdapter(this, mList);
        mListView.setAdapter(listDemoAdapter);
        top_tab_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(group.getCheckedRadioButtonId());
                List<String> s = new ArrayList<>();
                for (int i=0; i<30; i++) {
                    s.add(String.format("%s-%d", rb.getText(), i));
                }
                mList.clear();
                mList.addAll(s);
                listDemoAdapter.refreshList(s);
            }
        });
    }

    @Override
    public List<TabInfo> getTabList() {
        List<TabInfo> tabInfoList = new ArrayList<TabInfo>();
        tabInfoList.add(getTabInfo("待配液"));
        tabInfoList.add(getTabInfo("已配液"));
        tabInfoList.add(getTabInfo("已校对"));
        return tabInfoList;
    }

    class MyAdapter extends BaseAdapter{
        public List<String> mList;
        private Context context;

        public MyAdapter(Context context, List<String> t){
            this.mList = t;
            this.context = context;
        }

        public void refreshList(List<String> t){
            if (!mList.isEmpty()) {
                mList.clear();
            }
            if (t != null && !t.isEmpty()) {
                mList.addAll(t);
            }
            MyLogger.Log().i("##refreshList="+mList.size());
            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList == null ? null : mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView){
                convertView = LayoutInflater.from(context).inflate(R.layout.edittext, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder =  (ViewHolder) convertView.getTag();
            }
            viewHolder.setPosition(position);
            viewHolder.edittext.setText(mList.get(position));
            viewHolder.initData();
            return convertView;
        }
        class ViewHolder{
            EditText edittext;
            int position;
            public ViewHolder(View convertView) {
                edittext = convertView.findViewById(R.id.edittext);
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public void initData(){
                edittext.setTag(position);

                edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (position == ((int) edittext.getTag())){
                            mList.set(position, s.toString());
                        }
                    }
                });
            }
        }
    }
}