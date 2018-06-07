package com.grechur.customviewapp.view.banner.simple3;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by zz on 2018/6/7.
 */

public class BannerScroller extends Scroller{
    private int mScrollerDuration = 1000;

    public void setScrollerDuration(int scrollerDuration){
        this.mScrollerDuration = scrollerDuration;
    }

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuration);
    }
}
