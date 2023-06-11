package com.zrt.mybase.demo;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.zrt.mybase.R;
import com.zrt.mybase.base.BaseListAdapter;

import java.util.List;

/**
 * @author：Zrt
 * @date: 2021/8/1
 */
public class ListDemoAdapter extends BaseListAdapter<String> {

    public ListDemoAdapter(Context context, List<String> t, int layoutID) {
        super(context, t, layoutID);
    }

    @Override
    public void convert(ViewHolder mHolder, String strings, final int position) {
//        mHolder.setText(R.id.text, strings);
        mHolder.setPosition(position);
        final EditText edittext = mHolder.findById(R.id.edittext);
//        edittext.setTag(position);
        edittext.setText(getItem(((int) edittext.getTag())));
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (position == ((int) edittext.getTag())){
                    mList.add(position, s.toString());

                }
            }
        });
    }
}
