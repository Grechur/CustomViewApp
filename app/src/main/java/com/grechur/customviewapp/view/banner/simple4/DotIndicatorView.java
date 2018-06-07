package com.grechur.customviewapp.view.banner.simple4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zz on 2018/6/7.
 */

public class DotIndicatorView extends View{
    private Drawable mDrawable;
    public DotIndicatorView(Context context) {
        this(context,null);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mDrawable!=null){
            mDrawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
            mDrawable.draw(canvas);
        }

    }
}
