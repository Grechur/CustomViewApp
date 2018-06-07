package com.grechur.customviewapp.view.banner.simple4;

import android.view.View;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zz on 2018/6/7.
 */

public abstract class BannerAdapter<T> {
    private List<T> mData;
    public BannerAdapter(List<T> data){
        mData = data;
    }

    public BannerAdapter(T[] data){
        if(data!=null)
        mData = Arrays.asList(data);
    }

    public abstract View getView(int position,View convertView);

    public int getCount(){
        return mData!=null?mData.size():0;
    }

    /**
     * 根据位置得到广告介绍
     * @param position
     */
    public String setBannerDesc(int position) {
        return "";
    }

    public T getUrl(int position){
        return mData.get(position);
    }
}
