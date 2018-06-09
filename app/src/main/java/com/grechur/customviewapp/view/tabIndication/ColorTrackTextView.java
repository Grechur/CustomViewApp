package com.grechur.customviewapp.view.tabIndication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import static com.grechur.customviewapp.view.tabIndication.ColorTrackTextView.Direction.LEFT_TO_RIGHT;
import static com.grechur.customviewapp.view.tabIndication.ColorTrackTextView.Direction.RIGHT_TO_LEFT;

/**
 * Created by zz on 2018/6/8.
 */

@SuppressLint("AppCompatCustomView")
public class ColorTrackTextView extends TextView {
    // 默认的字体颜色的画笔
    private Paint mDefaultPaint;
    // 改变的字体颜色的画笔
    private Paint mChangePaint;
    // 当前的进度
    private float mCurrentProgress = 0.0f;
    private String TAG = "CTTV";

    // 当前文本
    private String mText;

    // 当前朝向
    private Direction mDirection = LEFT_TO_RIGHT;
//    //是否时点击
//    private boolean isClick = false;
    //点击时文字的颜色类型
    private int mType;

    // 绘制的朝向枚举
    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }


    public ColorTrackTextView(Context context) {
        this(context,null);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorTrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取文字的范围
        Rect bounds = new Rect();
        mText = getText().toString();
        mDefaultPaint.getTextBounds(mText, 0, mText.length(), bounds);
        if(getMeasuredWidth()<bounds.width()){
            setMeasuredDimension(bounds.width(),getMeasuredHeight());
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mDefaultPaint = getPaintByColor(Color.BLACK);
        mChangePaint = getPaintByColor(Color.RED);
    }

    /**
     * 获取画笔根据不同的颜色
     */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        // 抗锯齿
        paint.setAntiAlias(true);
        // 仿抖动
        paint.setDither(true);
        paint.setColor(color);

        return paint;
    }

    /**
     * 这里肯定是自己去画  不能用super
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 字体的大小设置为TextView的文字大小
//        Log.e("TAG","getTextSize:"+getTextSize());

        mChangePaint.setTextSize(getTextSize());
        mDefaultPaint.setTextSize(getTextSize());
        // 获取当前文本
        mText = getText().toString();
        // 获取控件宽度
        int width = getWidth();
//        if(isClick){
////            isClick = false;
//            if(mType == 0)drawText(canvas, mDefaultPaint, 0, width);
//            else drawText(canvas, mChangePaint, 0, width);
//        }else {
            if (!TextUtils.isEmpty(mText)) {
                // 根据当前进度计算中间位置
                int middle = (int) (width * mCurrentProgress);

                // 根据不同的朝向去画字体
                if (mDirection == LEFT_TO_RIGHT) {
                    drawDefaultDirectionLeft(canvas, middle);
                    drawChangeDirectionLeft(canvas, middle);
                }
                if (mDirection == RIGHT_TO_LEFT) {
                    drawDefaultDirectionRight(canvas, middle);
                    drawChangeDirectionRight(canvas, middle);
                }
            }
//        }
    }

//    public void setTextColor(int color,int type){
//        isClick = true;
//        mType = type;
//        if(type == 0){
//            mDefaultPaint.setColor(color);
//        }else{
//            mChangePaint.setColor(color);
//        }
//        invalidate();
//    }

    /**
     * 画朝向右边变色字体
     */
    private void drawChangeDirectionRight(Canvas canvas, int middle) {

        drawText(canvas, mChangePaint, getWidth() - middle, getWidth());
    }

    /**
     * 画朝向左边默认色字体
     */
    private void drawDefaultDirectionRight(Canvas canvas, int middle) {
        drawText(canvas, mDefaultPaint, 0, getWidth() - middle);
    }

    /**
     * 画朝向左边变色字体
     */
    private void drawChangeDirectionLeft(Canvas canvas, int middle) {
        drawText(canvas, mChangePaint, 0, middle);
    }

    /**
     * 画朝向左边默认色字体
     */
    private void drawDefaultDirectionLeft(Canvas canvas, int middle) {
        drawText(canvas, mDefaultPaint, middle, getWidth());
    }


    /**
     * 绘制文本根据指定的位置
     *
     * @param canvas canvas画布
     * @param paint  画笔
     * @param startX 开始的位置
     * @param endX   结束的位置
     */
    private void drawText(Canvas canvas, Paint paint, int startX, int endX) {
        // 保存画笔状态
        canvas.save();
        // 截取绘制的内容，待会就只会绘制clipRect设置的参数部分
        canvas.clipRect(startX, 0, endX, getHeight());
        // 获取文字的范围
        Rect bounds = new Rect();
        mDefaultPaint.getTextBounds(mText, 0, mText.length(), bounds);

        // 获取文字的Metrics 用来计算基线
        Paint.FontMetricsInt fontMetrics = mDefaultPaint.getFontMetricsInt();
        // 获取文字的宽高
        int fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算基线到中心点的位置
        int offY = fontTotalHeight / 2 - fontMetrics.bottom;
        // 计算基线位置
        int baseline = (getMeasuredHeight() + fontTotalHeight) / 2 - offY;
//        Log.e("TAG",getMeasuredWidth()+":"+bounds.width());
        int offX = Math.abs(getMeasuredWidth() / 2 - bounds.width() / 2);
        canvas.drawText(mText, offX, baseline, paint);
        // 释放画笔状态
        canvas.restore();
    }

    /**
     * 设置当前的进度
     *
     * @param currentProgress 当前进度
     */
    public void setCurrentProgress(float currentProgress) {
        //滑动的话，说明不是点击
//        isClick = false;
        this.mCurrentProgress = currentProgress;
        // 重新绘制
        invalidate();
    }
    /**
     * 设置绘制方向，从右到左或者从左到右
     *
     * @param direction 绘制方向
     */
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

}
