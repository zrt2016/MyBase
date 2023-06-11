package com.zrt.mybase.customviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseActivity;
import com.zrt.mybase.base.BaseListAdapter;
import com.zrt.mybase.customview.SurfaceItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理记录表单单项输入框自定义view
 */
public class SurfaceItemActivity extends BaseActivity {
    SurfaceItemView surface_item;
    ListView surface_list;
    List<SurfaceInfo> mList = new ArrayList<SurfaceInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_surface_item;
    }

    @Override
    protected void initView() {
        surface_item = findViewById(R.id.surface_item);
        surface_list = findViewById(R.id.surface_list);
    }

    @Override
    protected void initData() {
        surface_item.setTitleText("体温：");
        addData("血压：");
        addData("脉搏：");
        addData("呼吸：");
        addData("心率：");

        SurfaceAdapter surfaceAdapter = new SurfaceAdapter(this, mList, R.layout.list_surface_item);
        surface_list.setAdapter(surfaceAdapter);
    }

    class SurfaceInfo{
        String title = "";
        String content = "";

        public SurfaceInfo(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    public class SurfaceAdapter extends BaseListAdapter<SurfaceInfo>{

        public SurfaceAdapter(Context context, List<SurfaceInfo> t, int layoutID) {
            super(context, t, layoutID);
        }

        @Override
        public void convert(ViewHolder mHolder, SurfaceInfo surfaceInfo, final int position) {
            mHolder.setPosition(position);
            SurfaceItemView surfaceItemView = mHolder.findById(R.id.surface_view_item);
            surfaceItemView.setTitleText(surfaceInfo.title);
            surfaceItemView.setContentTag(position);
            final EditText surface_item_content = surfaceItemView.getItem_content();
            surface_item_content.setText(surfaceInfo.content);
            surface_item_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (position == ((int) surface_item_content.getTag())){
                        mList.get(position).content = s.toString();
                    }
                }
            });
        }
    }

    public void addData(String title){
        for (int i=1; i<=10; i++){
            mList.add(new SurfaceInfo(title+"-"+i, title+i));
        }
    }
}