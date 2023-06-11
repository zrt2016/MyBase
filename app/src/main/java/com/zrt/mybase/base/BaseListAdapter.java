package com.zrt.mybase.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import com.zrt.mybase.utils.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：Zrt
 * @date: 2021/7/26
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {


    public List<T> mList = new ArrayList<>();
    private Context context;
    private @LayoutRes int layoutID;

    public BaseListAdapter(Context context, List<T> t, @LayoutRes int layoutID){
        this.mList.addAll(t);
        this.context = context;
        this.layoutID = layoutID;
        MyLogger.Log().i("##mList="+mList.size()+"##layoutID="+layoutID);
    }

    public void refreshList(List<T> t){
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
    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = ViewHolder.get(context, convertView, parent, layoutID);
//        mHolder.setPosition(position);
        convert(mHolder, mList.get(position), position);
        return mHolder.getConvertViewItem();
    }

//    abstract View createViewItem(int position, View convertView, ViewGroup parent);
    public abstract void convert(ViewHolder mHolder, T t, int position);

    public static class ViewHolder{

        private View convertView;
        public int mPostion;
        public Map<Integer, View> views;

        private ViewHolder(View convertView){
            views = new HashMap<>();
            this.convertView = convertView;
            this.convertView.setTag(this);
        }
        public static ViewHolder get(Context context, View convertView,
                                     ViewGroup parent, @LayoutRes int layoutID){
            if (null == convertView){
                convertView = LayoutInflater.from(context).inflate(layoutID, parent, false);
                ViewHolder viewHolder = new ViewHolder(convertView);
                return viewHolder;
            }else {
                return (ViewHolder) convertView.getTag();
            }
        }
        public void setPosition(int position){
            this.mPostion = position;
        }

        public View getConvertViewItem(){
            return convertView;
        }

        /**
         * findById方法
         * @param id
         * @param <T>
         * @return
         */
        public <T extends View> T findById(int id) {
            View view = views.get(id);
            if (view == null) {
                view = convertView.findViewById(id);
                views.put(id, view);
            }
            view.setTag(mPostion);
            return (T) view;
        }

        /**
         * 设置TextView文本方法
         * @param id
         * @param txt
         */
        public void setText(int id, String txt) {
            TextView tv = findById(id);
            tv.setText(txt);
        }

    }
}
