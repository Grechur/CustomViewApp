package com.grechur.customviewapp.view.banner.simple1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zz on 2018/6/7.
 */

public class BannerViewPager extends ViewPager{

    private BannerAdapter mAdapter;

    public BannerViewPager(@NonNull Context context) {
        super(context,null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(@Nullable BannerAdapter adapter) {
        this.mAdapter = adapter;
        setAdapter(new ViewpagerAdapter());
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
}
