package com.grechur.customviewapp.view.banner.simple4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grechur.customviewapp.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by zz on 2018/6/7.
 * 添加viewpage指示器
 */
public class BannerView extends RelativeLayout{
    //banner
    private BannerViewPager mBvp;
    //广告介绍
    private TextView tv_banner_desc;
    //指示器
    private LinearLayout ll_dot_container;
    //banner的适配器
    private BannerAdapter mAdapter;

    private Context mContext;

    //默认指示点的图片
    private Drawable mNormalDrawable;
    //选中指示点的图片
    private Drawable mSelectDrawable;


    private int mDotIndicatorNormal;
    private int mDotIndicatorSelect;
    private int mDotLocation=-1;
    private int mDotSize= 12;
    private int mDotDistance = 10;
    private int mBottomBg = Color.parseColor("#5500d4e6");

    //宽高比
    private float mAspectRatio = (float)8/3;

    //当前的位置
    private int mCurrentPosition = 0;

    public BannerView(Context context) {
        this(context,null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context,R.layout.banner_view_layout,this);
        initAttrs(attrs);
        initView();

    }

    /**
     * 获取自定义属性
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.BannerView);
        mDotIndicatorNormal = array.getResourceId(R.styleable.BannerView_dotIndicatorNormal,R.drawable.indicator_normal_shape);
        mDotIndicatorSelect = array.getResourceId(R.styleable.BannerView_dotIndicatorSelect,R.drawable.indicator_select_shape);
        mDotLocation = array.getInt(R.styleable.BannerView_dotLocation,dip2px(mDotLocation));
        mDotDistance = array.getDimensionPixelOffset(R.styleable.BannerView_dotDistance,dip2px(mDotDistance));
        mDotSize = array.getDimensionPixelOffset(R.styleable.BannerView_dotSize,dip2px(mDotSize));
        mBottomBg = array.getColor(R.styleable.BannerView_bottomBackground,mBottomBg);
        mAspectRatio = array.getFloat(R.styleable.BannerView_aspectRatio,mAspectRatio);

        mNormalDrawable = getResources().getDrawable(mDotIndicatorNormal);
        mSelectDrawable = getResources().getDrawable(mDotIndicatorSelect);
        array.recycle();
    }

    /**
     * 根据宽高比计算控件高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();

        int height = (int) (width/mAspectRatio);

        getLayoutParams().height = height;

    }

    /**
     * 初始化view
     */
    private void initView() {
        mBvp = findViewById(R.id.bvp_custom4);
        tv_banner_desc = findViewById(R.id.tv_banner_desc);
        ll_dot_container = findViewById(R.id.ll_dot_container);
    }

    /**
     * 设置适配器
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter){
        mAdapter = adapter;
        mBvp.setAdapter(adapter);
        //要得到adapter才能去计算有多少个指示点
        initIndicator();

        mBvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }
        });
        //获取第一个广告的简介
        String firstDesc = mAdapter.setBannerDesc(0);
        tv_banner_desc.setText(firstDesc);
    }

    /**
     * 设置切换的动画时间
     * @param scrollerDuration
     */
    public void setScrollerDuration(int scrollerDuration){
        mBvp.setScrollerDuration(scrollerDuration);
    }


    private void initIndicator() {
        if (mAdapter==null) return;
        //得到指示器的数量
        int count = mAdapter.getCount();

//        ll_dot_container.setGravity(Gravity.RIGHT);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(getDotGravity(mDotLocation));
        ll_dot_container.setLayoutParams(layoutParams);
        for (int i = 0; i < count; i++) {
            DotIndicatorView indicatorView = new DotIndicatorView(mContext);
            //设置指示器的params
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize,mDotSize);
            //设置间距
            params.leftMargin = params.rightMargin = mDotDistance;

            indicatorView.setLayoutParams(params);
            if(i == 0){
                indicatorView.setDrawable(mSelectDrawable);
            }else{
                indicatorView.setDrawable(mNormalDrawable);
            }
            //添加到布局中
            ll_dot_container.addView(indicatorView);
            setBackgroundColor(mBottomBg);

        }


    }



    /**
     * 页面切换的回调
     */
    private void pageSelected(int position) {
        //把当前位置的点点亮，将默认的点设为普通
        //之前选中的点
        DotIndicatorView dotIndicatorView = (DotIndicatorView) ll_dot_container.getChildAt(mCurrentPosition%mAdapter.getCount());
        dotIndicatorView.setDrawable(mNormalDrawable);

        //现在选中的点
        mCurrentPosition = position;
        DotIndicatorView currentIndicatorView = (DotIndicatorView) ll_dot_container.getChildAt(mCurrentPosition%mAdapter.getCount());
        currentIndicatorView.setDrawable(mSelectDrawable);

        //根据位置得到广告介绍
        String bannerDesc = mAdapter.setBannerDesc(position);
        tv_banner_desc.setText(bannerDesc);
    }

    /**
     * dip转换为px
     * @param dip
     * @return
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip
                ,getResources().getDisplayMetrics());
    }

    /**
     * 获取点的位置
     * @param mDotLocation
     * @return
     */
    private int getDotGravity(int mDotLocation) {
        switch (mDotLocation){
            case -1:
                return RelativeLayout.ALIGN_PARENT_RIGHT;
            case 0:
                return RelativeLayout.CENTER_IN_PARENT;
            case 1:
                return RelativeLayout.ALIGN_PARENT_LEFT;
        }
        return RelativeLayout.ALIGN_PARENT_RIGHT;
    }
}
