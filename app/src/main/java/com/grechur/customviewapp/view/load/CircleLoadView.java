package com.grechur.customviewapp.view.load;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.grechur.customviewapp.R;

/**
 * Created by zz on 2018/6/20.
 */

public class CircleLoadView extends View{
    //三个颜色的画笔
    private Paint mLeftPaint,mCenterPaint,mRightPaint;
    //自定义的颜色
    private int mLeftColor,mCenterColor,mRightColor;
    //圆半径
    private int mRadius;
    //圆距离
    private int mDistance;
    //最大距离
    private int mMaxDistance;
    //动画
    private ValueAnimator mAnimator;
    private ValueAnimator mExpendAnimal;

    public CircleLoadView(Context context) {
        this(context,null);
    }

    public CircleLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CircleLoadView);
        mLeftColor = array.getColor(R.styleable.CircleLoadView_leftColor, Color.YELLOW);
        mCenterColor = array.getColor(R.styleable.CircleLoadView_centerColor,Color.BLACK);
        mRightColor = array.getColor(R.styleable.CircleLoadView_rightColor,Color.RED);
        mLeftPaint = getPaintByColor(mLeftColor);
        mCenterPaint = getPaintByColor(mCenterColor);
        mRightPaint = getPaintByColor(mRightColor);

        array.recycle();
    }

    /**
     * 通过颜色获取画笔
     * @param color
     * @return
     */
    private Paint getPaintByColor(int color){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mRadius = width/32;
        mDistance = width/8+2*mRadius;
        mMaxDistance = width/8+2*mRadius;
        initAnimal();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画左边的圆
        canvas.drawCircle(getWidth()/2-mDistance,getHeight()/2,mRadius,mLeftPaint);
        //画中间的圆
        canvas.drawCircle(getWidth()/2,getHeight()/2,mRadius,mCenterPaint);
        //画右边的圆
        canvas.drawCircle(getWidth()/2+mDistance,getHeight()/2,mRadius,mRightPaint);


    }

    /**
     * 离开window时要关闭动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mAnimator.isStarted()){
            mAnimator.cancel();
        }
        if(mExpendAnimal.isStarted()){
            mExpendAnimal.cancel();
        }
    }

    /**
     * 绑定页面时加载动画
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initAnimal();

    }

    /**
     * 设置当前的距离
     * @param current
     */
    public void setDistance(int current){
        this.mDistance = current;
        invalidate();
    }
    /**
     * 收缩动画
     */
    private void initAnimal(){
        mAnimator = ObjectAnimator.ofFloat(mMaxDistance,0);
        mAnimator.setDuration(350);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setDistance((int) value);

            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int temp;
                temp = mLeftColor;
                mLeftColor = mCenterColor;
                mCenterColor = mRightColor;
                mRightColor = temp;
                mLeftPaint.setColor(mLeftColor);
                mCenterPaint.setColor(mCenterColor);
                mRightPaint.setColor(mRightColor);
                if(mExpendAnimal!=null){
                    mExpendAnimal.start();
                }else{
                    initExpendAnimal();
                }

            }
        });
        mAnimator.start();
    }

    /**
     * 扩散动画
     */
    private void initExpendAnimal(){
        mExpendAnimal = ObjectAnimator.ofFloat(0,mMaxDistance);
        mExpendAnimal.setDuration(350);
        mExpendAnimal.setInterpolator(new LinearInterpolator());
        mExpendAnimal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setDistance((int) value);
            }
        });
        mExpendAnimal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(mAnimator!=null){
                    mAnimator.start();
                }else{
                    initAnimal();
                }

            }
        });
        mExpendAnimal.start();
    }

}
