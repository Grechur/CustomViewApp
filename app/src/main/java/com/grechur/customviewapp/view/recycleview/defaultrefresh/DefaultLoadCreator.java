package com.grechur.customviewapp.view.recycleview.defaultrefresh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.grechur.customviewapp.R;
import com.grechur.customviewapp.view.recycleview.refresh_load.LoadViewCreator;

import static com.grechur.customviewapp.view.recycleview.refresh_load.LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING;
import static com.grechur.customviewapp.view.recycleview.refresh_load.LoadRefreshRecyclerView.LOAD_STATUS_NORMAL;
import static com.grechur.customviewapp.view.recycleview.refresh_load.LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_REFRESH;

/**
 * Created by zhouzhu on 2018/6/10.
 */

public class DefaultLoadCreator extends LoadViewCreator{
    private View mRefreshIv;
    private TextView loadText;

    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        loadText = refreshView.findViewById(R.id.load_tv);
        loadText.setVisibility(View.VISIBLE);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int loadViewHeight, int currentLoadStatus) {
//        float rotate = ((float) currentDragHeight) / loadViewHeight;
//        // 不断下拉的过程中不断的旋转图片
//        mRefreshIv.setRotation(rotate * 360);
        Log.e("TAG",currentDragHeight+"  "+loadViewHeight+"   "+currentLoadStatus);
        if(currentLoadStatus ==  LOAD_STATUS_PULL_DOWN_REFRESH){
            loadText.setText("正在加载");
        }
        if(currentLoadStatus ==  LOAD_STATUS_LOOSEN_LOADING){
            loadText.setVisibility(View.GONE);
            mRefreshIv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void failLoading() {
        loadText.setText("上拉加载更多");
    }

    @Override
    public void onLoading() {
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopLoad() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
        mRefreshIv.setVisibility(View.GONE);
        loadText.setVisibility(View.VISIBLE);
        loadText.setText("上拉加载更多");
    }
}
