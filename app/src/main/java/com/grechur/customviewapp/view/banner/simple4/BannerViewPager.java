package com.grechur.customviewapp.view.banner.simple4;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.grechur.customviewapp.R;
import com.grechur.customviewapp.view.banner.simple3.BannerScroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by zz on 2018/6/7.
 */

public class BannerViewPager extends ViewPager{
    //1.自定义adapter
    private BannerAdapter mAdapter;

    //2.实现自动轮播-->handler
    private MyHandler mHandler;
    //2.实现自动轮播-->handler消息标志
    private final int MSG_WHAT = 0x0011;
    //2.实现自动轮播-->handler消息时间
    private int mDelayTime = 2000;
    //3.改变viewpager切换的速率-->自定义的scroller
    private BannerScroller mScroller;

    //5.复用view
//    private View mConvertView;//这个会有问题，滑动很快时会崩溃
    private Set<View> mConvertViews;

    private Activity mActivity;
    //添加监听
    private BannerClickListener mListener;

    public void setBannerListener(BannerClickListener listener){
        this.mListener = listener;
    }

    public BannerViewPager(@NonNull Context context) {
        super(context,null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        WeakReference<BannerViewPager> weakReference = new WeakReference<BannerViewPager>(this);
        mHandler = new MyHandler(weakReference);

        //3.改变viewpager切换的速率
        mScroller = new BannerScroller(context);
        try {
            //通过反射拿到mScroller这个属性
            Field filed = ViewPager.class.getDeclaredField("mScroller");
            //解除私有限制
            filed.setAccessible(true);
            filed.set(this,mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mConvertViews = new HashSet<>();

        mActivity = (Activity) context;

        mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycle);

    }

    public void setScrollerDuration(int scrollerDuration){
        mScroller.setScrollerDuration(scrollerDuration);
    }

    /**
     *设置自定义adapter
     * @param adapter
     */
    public void setAdapter(@Nullable BannerAdapter adapter) {
        this.mAdapter = adapter;
        //设置view pager的adapter
        setAdapter(new ViewpagerAdapter());
//        startRoll();
    }

    public class ViewpagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;

        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }


        /**
         * 创建item
         * @param container
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mAdapter.getCount()==0) return null;
//            View bannerView = mAdapter.getView(position%mAdapter.getCount(),mConvertView);
            final int index = position%mAdapter.getCount();
            View bannerView = mAdapter.getView(index,getConvertView(index));
            container.addView(bannerView);
            bannerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener == null) return;
                    mListener.onBannerListener(view,index);
                }
            });
            return bannerView;
        }

        /**
         * 销毁item
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
//            mConvertView = (View) object;
            mConvertViews.add((View) object);
        }
    }

    private View getConvertView(int index) {
//        Log.e("TAG","mConvertViews.size:"+mConvertViews.size());
        if(mConvertViews.size() < mAdapter.getCount())return  null;
        for (View mConvertView : mConvertViews) {
//            Log.e("TAG",mConvertView.toString());
            if(mConvertView.getParent() == null){
                if(mConvertView.getTag(R.id.image).equals(mAdapter.getUrl(index))) {
                    return mConvertView;
                }
            }
        }
        return null;
    }

    /**
     * 开启轮播
     */
    public void startRoll(){
        //为了防止多次调用该方法，一次转动多张图，先清除再发送
        mHandler.removeMessages(MSG_WHAT);
        //发送消息播放下一页
        mHandler.sendEmptyMessageDelayed(MSG_WHAT,mDelayTime);
//        Log.e("TAG","startRoll");
    }

    /**
     * 停止轮播
     */
    private void stopRoll() {
        mHandler.removeMessages(MSG_WHAT);
    }

    /**
     * 销毁轮播
     */
    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(MSG_WHAT);
        mHandler = null;
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        super.onDetachedFromWindow();

    }


    //2.实现自动轮播（使用handle作为轮询器）
    public static class MyHandler extends Handler {
        //使用软引用，防止内存泄漏
        private WeakReference<BannerViewPager> mReference;
        public MyHandler(WeakReference<BannerViewPager> reference){
            this.mReference = reference;
        }

        @Override
        public void handleMessage(Message msg) {
            BannerViewPager bannerViewPager = mReference.get();
            if (bannerViewPager == null) return;
            //每隔*s切换一次页面
            bannerViewPager.setCurrentItem(bannerViewPager.getCurrentItem()+1);
            //重新发送消息
            bannerViewPager.startRoll();
        }
    }

    /**
     * 添加activity的生命周期的监听
     */
    private Application.ActivityLifecycleCallbacks mActivityLifecycle = new DefaultActivityLifecycleCallbacks(){
        @Override
        public void onActivityResumed(Activity activity) {
            //是否是当前页面
            if(activity == mActivity){
                //开启轮播
                startRoll();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if(activity == mActivity){
                //停止轮播
                stopRoll();
            }
        }
    };


}
