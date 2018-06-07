package com.grechur.customviewapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import static android.animation.ValueAnimator.INFINITE;

/**
 * Created by zz on 2018/6/6.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class LoadingView extends View{
    public static final long ROTATE_DURATION=2000;
    //小圆的颜色集合
    private final static int[] mColors = {Color.BLACK,Color.BLUE,Color.CYAN,Color.MAGENTA,Color.GREEN,Color.RED};
    //当前圆旋转的角度
    private float mCurrentRotationAngle;
    //是否初始化
    boolean mInitParams = false;
    //大圆半径（为了计算小圆旋转时的变化，来画小圆）
    private int mRotationRadius;
    //小圆半径
    private int mCircleRadius;
    //画笔
    private Paint mPaint;

    private LoadingState mLoadingState;
    //当前的大圆半径
    private float mCurrentMergeRadius = mRotationRadius;

    //对角线
    private float mDiagonalDist;
    //当前的半径
    private float mHoleRadius;
    //中心点位置
    private float centerX,centerY;
    //背景颜色
    private int mSplashColor = Color.WHITE;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!mInitParams) {
            initParams();
        }

        //旋转
        if(mLoadingState==null){
            mLoadingState = new RotationState();
        }
        mLoadingState.draw(canvas);
    }

    private void initParams() {
        mRotationRadius = getWidth()/4;
        //小圆是大圆半径的1/8
        mCircleRadius = mRotationRadius/8;
        
        centerX = getMeasuredWidth()/2;
        centerY = getMeasuredHeight()/2;

        mDiagonalDist = (int) Math.sqrt(centerX*centerX+centerY*centerY);

        mInitParams = true;
    }

    /**
     * 消失
     */
    public void disappear(){
        //关闭旋转动画
        if(mLoadingState instanceof RotationState){
            RotationState rotationState = (RotationState) mLoadingState;
            rotationState.cancel();
        }
        //开启聚合动画
        mLoadingState = new MergeState();
    }



    public abstract class LoadingState{
        abstract void draw(Canvas canvas);
    }

    /**
     * 旋转动画
     */
    public class RotationState extends LoadingState{
        ValueAnimator mRotationAnimator;

        public RotationState(){
            if(mRotationAnimator == null){
                mRotationAnimator = ObjectAnimator.ofFloat(0,(float)Math.PI*2);

                mRotationAnimator.setDuration(ROTATE_DURATION);
                mRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // 不断获取值 当前大圆旋转的角度
                        mCurrentRotationAngle = (float) animation.getAnimatedValue();
                        if(animation.getCurrentPlayTime() > ROTATE_DURATION){
                            disappear();
                        }
                        // 提醒View重新绘制
                        invalidate();
                    }
                });

                mRotationAnimator.setInterpolator(new LinearInterpolator());
                mRotationAnimator.setRepeatCount(-1);
                mRotationAnimator.start();
            }
        }

        @Override
        void draw(Canvas canvas) {
            canvas.drawColor(mSplashColor);
            //画圆
            double percentAng = Math.PI*2/mColors.length;
            for (int i = 0; i < mColors.length; i++) {
                mPaint.setColor(mColors[i]);
                double currentAng = percentAng*i+mCurrentRotationAngle;
                int cx = (int) (getWidth()/2+mRotationRadius*Math.cos(currentAng));
                int cy = (int) (getHeight()/2+mRotationRadius*Math.sin(currentAng));
                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
            }
        }
        public void cancel(){
            mRotationAnimator.cancel();
        }
    }

    /**
     * 聚合动画
     */
    public class MergeState extends LoadingState{
        private ValueAnimator mMergeAnimator;
        public MergeState(){
            if(mMergeAnimator == null){
                mMergeAnimator = ObjectAnimator.ofFloat(mRotationRadius,0);

                mMergeAnimator.setDuration(ROTATE_DURATION/2);
                mMergeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // 不断获取值 当前大圆旋转的角度
                        mCurrentMergeRadius = (float) animation.getAnimatedValue();
                        // 提醒View重新绘制
                        invalidate();
                    }
                });
                mMergeAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mLoadingState = new ExpendState();
                    }
                });
                mMergeAnimator.setInterpolator(new AnticipateInterpolator(3f));
                mMergeAnimator.start();
            }
        }

        @Override
        void draw(Canvas canvas) {
            canvas.drawColor(mSplashColor);
            //画圆
            double percentAng = Math.PI*2/mColors.length;
            for (int i = 0; i < mColors.length; i++) {
                mPaint.setColor(mColors[i]);
                double currentAng = percentAng*i+mCurrentRotationAngle;
                int cx = (int) (getWidth()/2+mCurrentMergeRadius*Math.cos(currentAng));
                int cy = (int) (getHeight()/2+mCurrentMergeRadius*Math.sin(currentAng));
                canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
            }
        }

    }

    /**
     * 扩散动画
     */
    public class ExpendState extends LoadingState{
        private ValueAnimator mExpendAnimator;
        public ExpendState(){
            if(mExpendAnimator == null){
                mExpendAnimator = ObjectAnimator.ofFloat(0,mDiagonalDist);

                mExpendAnimator.setDuration(ROTATE_DURATION/2);
                mExpendAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // 不断获取值 当前大圆旋转的角度
                        mHoleRadius = (float) animation.getAnimatedValue();
                        // 提醒View重新绘制
                        invalidate();
                    }
                });
                mExpendAnimator.setInterpolator(new LinearInterpolator());
                mExpendAnimator.start();
            }
        }

        @Override
        void draw(Canvas canvas) {
            //画笔的宽度
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(mSplashColor);
            //画空心圆
            mPaint.setStyle(Paint.Style.STROKE);
            float radius = strokeWidth/2+mHoleRadius;
            canvas.drawCircle(centerX,centerY,radius,mPaint);
        }
    }
}
