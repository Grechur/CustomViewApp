package com.grechur.customviewapp.view.tabIndication;

import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zz on 2018/6/8.
 */

public abstract class TrackAdapter<T> {

    private List<T> mData;
    public TrackAdapter(List<T> data){
        mData = data;
    }

    public TrackAdapter(T[] data){
        if(data!=null)
            mData = Arrays.asList(data);
    }

    public abstract View getView(int position, View convertView);

    public int getCount(){
        return mData!=null?mData.size():0;
    }

    //重置当前位置
    public void restoreIndicator(View view){

    }

    //高亮当前位置
    public void highlightIndicator(View view){

    }
}
