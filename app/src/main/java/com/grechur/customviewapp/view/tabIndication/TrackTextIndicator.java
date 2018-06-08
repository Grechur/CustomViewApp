package com.grechur.customviewapp.view.tabIndication;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.grechur.customviewapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 2018/6/8.
 */

public class TrackTextIndicator extends HorizontalScrollView{

    private TrackAdapter mAdapter;
    //指示器条目集合布局
    private LinearLayout mIndicatorGroup;
    //设置标题显示的条数
    private int mVisibleIndicatorNum = 0;
    //设置title——item条目宽度
    private int mItemWidth = 0;

    private ViewPager mViewPager;

    private List<View> mIndicators;

    //
    private int mCurrentPosition = 0;

    public TrackTextIndicator(Context context) {
        this(context,null);
    }

    public TrackTextIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TrackTextIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context,attrs);
        mIndicatorGroup = new LinearLayout(context);
        addView(mIndicatorGroup);
        mIndicators = new ArrayList<>();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TrackTextIndicator);
        mVisibleIndicatorNum = array.getInt(R.styleable.TrackTextIndicator_visibleIndicatorNum,0);
        array.recycle();
    }

    public void setAdapter(TrackAdapter adapter){
        if(adapter == null) {
            throw new NullPointerException("adapter不能为空");
        }
        mAdapter = adapter;
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i,this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.leftMargin = 5;
            params.rightMargin = 5;
            view.setLayoutParams(params);
            switchViewClick(view,i);
            mIndicators.add(view);
            mIndicatorGroup.addView(view);
        }

    }

    private void switchViewClick(View view, final int i) {
        if(mViewPager!=null) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(i);
                }
            });
        }
    }

    //当前指示器居中
    public void setAdapter(TrackAdapter adapter, ViewPager viewPager){
        mViewPager = viewPager;
        if(mViewPager == null){
            throw new NullPointerException("viewPager不能为空");
        }

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //字体颜色和viewpager同步
                ColorTrackTextView leftTrack = (ColorTrackTextView) mIndicators.get(position);
                leftTrack.setDirection(ColorTrackTextView.Direction.DIRECTION_RIGHT);
                leftTrack.setCurrentProgress(1-positionOffset);

                try {
                    ColorTrackTextView rightTrack = (ColorTrackTextView) mIndicators.get(position+1);
                    rightTrack.setDirection(ColorTrackTextView.Direction.DIRECTION_LEFT);
                    rightTrack.setCurrentProgress(positionOffset);
                } catch (Exception e){

                }

                scrollCurrentIndicator(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //将上一个位置重置
                mAdapter.restoreIndicator(mViewPager.getChildAt(mCurrentPosition));
                //设置当前位置
                mCurrentPosition = position;
                //设置当前位置点亮
                mAdapter.highlightIndicator(mViewPager.getChildAt(mCurrentPosition));
            }
        });
        setAdapter(adapter);
    }

    private void scrollCurrentIndicator(int position, float positionOffset) {
        //当前移动的距离
        float totleWidth = (position+positionOffset)*mItemWidth;
        //左边的偏移量
        float leftWidth = (getWidth() - mItemWidth)/2;
        //左边看不到的距离
        int leftOffset = (int) (totleWidth - leftWidth);
        scrollTo(leftOffset,0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if(changed){
            mItemWidth = getItemWidth();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                View view = mIndicatorGroup.getChildAt(i);
                ViewGroup.LayoutParams params= view.getLayoutParams();
                params.width = mItemWidth;
                view.setLayoutParams(params);
//                mIndicatorGroup.getChildAt(i).getLayoutParams().width = mItemWidth;
            }
        }
    }

    private int getItemWidth() {
        int parentWidth = getWidth();
        if(mVisibleIndicatorNum!=0){
            return parentWidth/mVisibleIndicatorNum;
        }
        //指定的item
        int itemWidth = 0;
        //最宽的item
        int maxItemWidth = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            int currentWidth = mIndicatorGroup.getChildAt(i).getMeasuredWidth();
            maxItemWidth = Math.max(maxItemWidth,currentWidth);
        }
        //获取到了最宽的宽度
        itemWidth = maxItemWidth;
        int allWidth = itemWidth*mAdapter.getCount();
        if(allWidth<parentWidth){
            itemWidth = parentWidth/mAdapter.getCount();
        }
        return itemWidth;
    }
}
