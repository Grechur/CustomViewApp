package com.grechur.customviewapp.view.tabIndication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static android.widget.FrameLayout.LayoutParams.*;

/**
 * Created by zhouzhu on 2018/6/9.
 */

public class IndicatorGroupView extends FrameLayout {
    //指示器条目集合布局
    private LinearLayout mIndicatorGroup;
    //底部跟踪器
    private View mBottomTrackView;
    //底部跟踪器的LayoutParams
    FrameLayout.LayoutParams mTrackParams;

    private int mItemWidth;
    private int mInitLeftMargin;

    public IndicatorGroupView(@NonNull Context context) {
        this(context,null);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorGroup = new LinearLayout(context);
        addView(mIndicatorGroup);

    }

    public void addItemView(View view) {
        mIndicatorGroup.addView(view);
    }

    public View getItemAt(int position) {
        return mIndicatorGroup.getChildAt(position);
    }

    public void getBottomTrackView(View bottomTrackView,int itemWidth) {
        if(bottomTrackView == null){
            return;
        }
        this.mBottomTrackView = bottomTrackView;
        this.mItemWidth = itemWidth;
        mBottomTrackView.setBackgroundColor(Color.RED);
        addView(mBottomTrackView);
        mTrackParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        mTrackParams.gravity = Gravity.BOTTOM;
        int trackWidth = mTrackParams.leftMargin;
        if(mTrackParams.width == MATCH_PARENT){
            trackWidth = mItemWidth;
        }

        if(trackWidth>mItemWidth){
            trackWidth = mItemWidth;
        }
        mTrackParams.width = trackWidth;
        mInitLeftMargin = (mItemWidth - trackWidth)/2;
        mTrackParams.leftMargin = mInitLeftMargin;
    }

    public void scrollBottomTrack(int position, float positionOffset) {
        if(mBottomTrackView == null){
            return;
        }
        int leftMargin = (int) ((position+positionOffset)*mItemWidth)+mInitLeftMargin;
        mTrackParams.leftMargin = leftMargin;
        mBottomTrackView.setLayoutParams(mTrackParams);
    }

    public void smoothScrollBottomTrack(int position) {
        if(mBottomTrackView == null){
            return;
        }
        int endLeftMargin = (int) (position*mItemWidth)+mInitLeftMargin;

        int currentLeftMargin = mTrackParams.leftMargin;

        //移动的距离
        int distance = endLeftMargin - currentLeftMargin;
        final ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin,endLeftMargin);
        animator.setDuration((long) (Math.abs(distance)*0.4));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) animator.getAnimatedValue();
                mTrackParams.leftMargin = (int) value;
                mBottomTrackView.setLayoutParams(mTrackParams);
            }
        });
        animator.start();
    }
}
