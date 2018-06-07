package com.grechur.customviewapp.view.banner.simple3;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.grechur.customviewapp.view.banner.simple1.BannerAdapter;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


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
        startRoll();
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
            View bannerView = mAdapter.getView(position);
            container.addView(bannerView);
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
            object=null;
        }
    }

    public void startRoll(){
        //为了防止多次调用该方法，一次转动多张图，先清除再发送
        mHandler.removeMessages(MSG_WHAT);
        //发送消息播放下一页
        mHandler.sendEmptyMessageDelayed(MSG_WHAT,mDelayTime);
    }

    //2.实现自动轮播-->页面销毁时销毁handler


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(MSG_WHAT);
        mHandler = null;
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
}
