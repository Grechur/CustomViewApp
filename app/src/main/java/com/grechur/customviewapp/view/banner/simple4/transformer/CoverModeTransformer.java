package com.grechur.customviewapp.view.banner.simple4.transformer;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.grechur.customviewapp.view.banner.simple4.BannerViewPager;

/**
 * Created by zz on 2018/6/8.
 */
//5.添加魅族模式
public class CoverModeTransformer  implements ViewPager.PageTransformer {
    private ViewPager mViewPager;
    //根据左右两边的距离计算偏移了几个页面
    private float offsetPosition = 0f;
    private float itemWidth = 0;
    private float reduceX;
    //缩放的最大和最小
    private float mScaleMax = 1.0f;
    private float mScaleMin = 0.9f;

    private int mCoverWidth;

    public CoverModeTransformer(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        if(offsetPosition == 0){
            float paddingLeft = mViewPager.getPaddingLeft();
            float paddingRight = mViewPager.getPaddingRight();
            float width = mViewPager.getMeasuredWidth();
            offsetPosition = paddingLeft/(width - paddingLeft-paddingRight);
        }
        float currentPos = position - offsetPosition;
        if(itemWidth == 0){
            itemWidth = page.getWidth();
            //由于左右边的缩小而减小的x的大小的一半
            reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f;
        }
        if (currentPos <= -1.0f) {
            page.setTranslationX(reduceX + mCoverWidth);
            page.setScaleX(mScaleMin);
            page.setScaleY(mScaleMin);
        } else if (currentPos <= 1.0) {
            float scale = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos));
            float translationX = currentPos * -reduceX;
            if (currentPos <= -0.5) {//两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                page.setTranslationX(translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
            } else if (currentPos <= 0.0f) {
                page.setTranslationX(translationX);
            } else if (currentPos >= 0.5) {//两个view中间的临界，这时两个view在同一层
                page.setTranslationX(translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f);
            } else {
                page.setTranslationX(translationX);
            }
            page.setScaleX(scale + mScaleMin);
            page.setScaleY(scale + mScaleMin);
        } else {
            page.setScaleX(mScaleMin);
            page.setScaleY(mScaleMin);
            page.setTranslationX(-reduceX - mCoverWidth);
        }
    }
}
