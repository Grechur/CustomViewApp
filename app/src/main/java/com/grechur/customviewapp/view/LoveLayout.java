package com.grechur.customviewapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grechur.customviewapp.R;

import java.util.Random;

/**
 * Created by zz on 2018/6/6.
 */

public class LoveLayout extends RelativeLayout{
    //图片集合
    private int[] mImages;
    //随机数生成器
    private Random mRandom;

    //控件的宽高
    private int mWidth;
    private int mHeight;

    //图片的宽高
    private int mDrawableWidth;
    private int mDrawableHeight;

    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImages = new int[]{R.mipmap.like_fill_black,R.mipmap.like_fill_blue,R.mipmap.like_fill_green
                ,R.mipmap.like_fill_pink,R.mipmap.like_fill_red
                ,R.mipmap.like_fill_yellow};
        mRandom = new Random();
        Drawable drawable = getResources().getDrawable(R.mipmap.like_fill_blue);
        mDrawableWidth = drawable.getIntrinsicWidth();
        mDrawableHeight = drawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //拿到控件宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);


    }

    /**
     * 添加图片
     */
    public void addLove(){
        //添加一个ImageView
        final ImageView loveImg = new ImageView(getContext());
        loveImg.setImageResource(mImages[mRandom.nextInt(mImages.length-1)]);
        //添加到底部中心
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);
        loveImg.setLayoutParams(layoutParams);
        addView(loveImg);

        //添加动画，有放大和透明度
        AnimatorSet animationSet = getAnimationSet(loveImg);
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(loveImg);
            }
        });
        animationSet.start();
    }

    private AnimatorSet getAnimationSet(ImageView imageView) {
        AnimatorSet allAnimatorSet = new AnimatorSet();

        AnimatorSet innerAnimationSet = new AnimatorSet();
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(imageView,"alpha",0.3f,1f);
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(imageView,"scaleX",0.3f,1f);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(imageView,"scaleY",0.3f,1f);
        innerAnimationSet.setDuration(1000);
        innerAnimationSet.playTogether(alphaAnimation,scaleXAnimation,scaleYAnimation);

        allAnimatorSet.playSequentially(innerAnimationSet,getBeizerAnimation(imageView));

        return allAnimatorSet;
    }

    private Animator getBeizerAnimation(final ImageView imageView) {

        PointF point0 = new PointF(mWidth/2-mDrawableWidth/2,mHeight - mDrawableHeight);
        PointF point1 = new PointF(mRandom.nextInt(mWidth),mRandom.nextInt(mHeight/2));
        PointF point2 = new PointF(mRandom.nextInt(mWidth),mRandom.nextInt(mHeight/2)+mHeight/2);
        PointF point3 = new PointF(mRandom.nextInt(mWidth-mDrawableWidth),0);

        LoveEvaluator loveEvaluator = new LoveEvaluator(point1,point2);

        ValueAnimator animator = ObjectAnimator.ofObject(loveEvaluator,point0,point3);
        animator.setDuration(3000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
            }
        });
        return animator;
    }
}
