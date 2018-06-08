package com.grechur.customviewapp.view.banner.simple4;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grechur.customviewapp.R;
import com.grechur.customviewapp.view.banner.simple4.transformer.CoverModeTransformer;

/**
 * Created by zz on 2018/6/7.
 * 添加viewpage指示器
 */
public class BannerView extends RelativeLayout{
    //banner
    private BannerViewPager mBvp;
    //指示器父页面
    private RelativeLayout rl_dashboard;
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

    //  自定义属性
    private int mDotIndicatorNormal;
    private int mDotIndicatorSelect;
    private int mDotLocation=-1;
    private int mDotSize= 12;
    private int mDotDistance = 10;
    private int mBottomBg = Color.parseColor("#5500d4e6");
    private boolean isLooper = false;
    private boolean hasIndication = true;
    private boolean mIsOpenMZEffect = true;

    //宽高比
    private float mAspectRatio = (float)8/3;

    //当前的位置
    private int mCurrentPosition = 0;

    private BannerClickListener mListener;

    public void setBannerListener(BannerClickListener listener){
        this.mListener = listener;

    }

    public BannerView(Context context) {
        this(context,null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        if(mIsOpenMZEffect){
            inflate(context,R.layout.banner_view_mz_layout,this);
        }else{
            inflate(context,R.layout.banner_view_normal_layout,this);
        }


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
        isLooper = array.getBoolean(R.styleable.BannerView_isLooper,isLooper);
        hasIndication = array.getBoolean(R.styleable.BannerView_hasIndication,hasIndication);
        mIsOpenMZEffect = array.getBoolean(R.styleable.BannerView_open_mz_mode,mIsOpenMZEffect);

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
        rl_dashboard = findViewById(R.id.rl_dashboard);
        tv_banner_desc = findViewById(R.id.tv_banner_desc);
        ll_dot_container = findViewById(R.id.ll_dot_container);
        mBvp.setBannerListener(new BannerClickListener() {
            @Override
            public void onBannerListener(View view, int position) {
                if (mListener==null) return;
                mListener.onBannerListener(view, position);
            }
        });
        mBvp.setIsLooper(isLooper);
        if(hasIndication) rl_dashboard.setVisibility(VISIBLE);
        else rl_dashboard.setVisibility(GONE);
    }

    /**
     * 是否开启魅族模式
     */
    private void setOpenMZEffect(){
        // 魅族模式
        if(mIsOpenMZEffect) {

            // 中间页面覆盖两边，和魅族APP 的banner 效果一样。
            mBvp.setPageTransformer(true, new CoverModeTransformer(mBvp));
        }
//        else{
//            // 中间页面不覆盖，页面并排，只是Y轴缩小
//            mBvp.setPageTransformer(false,new ScaleYTransformer());
//        }


    }

    /**
     * 设置适配器
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter){
        mAdapter = adapter;
        mBvp.setAdapter(adapter);

        if(mAdapter.getCount()<3){
            mIsOpenMZEffect = false;
        }
        setOpenMZEffect();
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
            rl_dashboard.setBackgroundColor(mBottomBg);

        }


    }

    /**
     * 添加拦截时间，按下时停止切换，抬起时开始切换
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isLooper){
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()){
            // 按住Banner的时候，停止自动轮播
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_DOWN:
                int paddingLeft = mBvp.getLeft();
                float touchX = ev.getRawX();
                // 如果是魅族模式，去除两边的区域
                if(touchX >= paddingLeft && touchX < getScreenWidth(getContext()) - paddingLeft){
                    mBvp.stopRoll();
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起重新开始切换
                mBvp.startRoll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    public static int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
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
