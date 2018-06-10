package com.grechur.customviewapp;

import android.content.Context;

import com.grechur.customviewapp.view.recycleview.common.CommonRecyclerAdapter;
import com.grechur.customviewapp.view.recycleview.common.ViewHolder;

import java.util.List;

/**
 * Created by zhouzhu on 2018/6/10.
 */

public class MyAdapter extends CommonRecyclerAdapter<Integer>{
    public MyAdapter(Context context, List<Integer> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Integer item) {
        holder.setText(R.id.tv_item,"这是"+item);
    }
}
