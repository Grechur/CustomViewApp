package com.grechur.customviewapp.view.load;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

/**
 * Created by zz on 2018/6/21.
 */

public class WebLoadView extends View{
    //画笔
    private Paint mPaint;

    //大圆半径
    private int mBigCircleRadius;
    //小圆半径
    private int mSmallCircleRadius;

    //当前的大圆半径
    private float mCurrentMergeRadius;

    //中心点位置
    private float centerX,centerY;

    int[] mColors = {Color.RED,Color.YELLOW,Color.BLUE};
    private double mCurrentAng = 0;
    //动画集合
    private AnimatorSet mAnimatorSet;
    public WebLoadView(Context context) {
        this(context,null);
    }

    public WebLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WebLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dip2px(2));
        //抗锯齿
        mPaint.setAntiAlias(true);
        //防抖动
        mPaint.setDither(true);
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e("TAG","onMeasure");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mBigCircleRadius = width/4;
        mSmallCircleRadius = mBigCircleRadius/8;
        centerX = width/2;
        centerY = height/2;
        mCurrentMergeRadius = mBigCircleRadius;
        mAnimatorSet = getAnimSet();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        double percentAng = Math.PI*2/mColors.length;
        for (int i = 0; i < mColors.length; i++) {
             mPaint.setColor(mColors[i]);
             double currentAng = mCurrentAng+i*percentAng;
             int cx = (int) (mCurrentMergeRadius*Math.cos(currentAng)+centerX);
             int cy = (int) (mCurrentMergeRadius*Math.sin(currentAng)+centerY);
             canvas.drawCircle(cx,cy,mSmallCircleRadius,mPaint);
        }

        if(mAnimatorSet!=null&&!mAnimatorSet.isStarted()){
            mAnimatorSet.start();
        }
    }



    private AnimatorSet getAnimSet(){
        AnimatorSet set = new AnimatorSet();
        ValueAnimator rotationAnim = ObjectAnimator.ofFloat(0,(float) Math.PI*2);
        rotationAnim.setRepeatCount(-1);
        rotationAnim.setDuration(2000);
        rotationAnim.setInterpolator(new LinearInterpolator());
        rotationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCurrentAng = value;
                invalidate();
            }
        });

        int maxDistance = getWindowWidth()/4;
        int minDistance = (int) Math.sqrt(2*mSmallCircleRadius*2*mSmallCircleRadius/2);
        ValueAnimator scalAnim = ObjectAnimator.ofFloat(mBigCircleRadius,minDistance,mBigCircleRadius);
        scalAnim.setDuration(2000);
        scalAnim.setRepeatCount(-1);
        scalAnim.setInterpolator(new LinearInterpolator());
        scalAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                Log.e("TAG","value:"+value);
                mCurrentMergeRadius = value;
//                Log.e("TAG","mCurrentMergeRadius:"+mCurrentMergeRadius);
                invalidate();
            }
        });

        set.playTogether(rotationAnim,scalAnim);
        return set;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        Log.e("TAG","onAttachedToWindow");

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("TAG","onDetachedFromWindow");
        if(mAnimatorSet!=null&&mAnimatorSet.isStarted()){
            mAnimatorSet.cancel();
        }
        clearAnimation();
    }
    private int getWindowWidth(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }


}
