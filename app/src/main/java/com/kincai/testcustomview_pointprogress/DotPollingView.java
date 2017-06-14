package com.kincai.testcustomview_pointprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Copyright (C) 2015 The KINCAI Open Source Project
 * .
 * Create By KINCAI
 * .
 * Time 2017-06-14 10:23
 * .
 * Desc 水平圆点进度条
 */

public class DotPollingView extends View {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * 进度当前圆点画笔和正常圆点画笔
     */
    private Paint mSelectedPaint, mNormalPaint;
    /**
     * 正常圆点颜色
     */
    private int mColor;
    /**
     * 变大圆点的颜色
     */
    private int mSelectedColor;
    /**
     * 圆点总数
     */
    private int mDotTotalCount = 3;
    /**
     * 正常圆点半径
     */
    private int mDotRadius;
    /**
     * 当前变化的圆点半径变化量 0 - (mDotMaxRadius - mDotRadius)之间
     */
    private float mDotCurrentRadiusChange;
    /**
     * 圆点大小变化率
     */
    private float mRadiusChangeRate;
    /**
     * 最大圆点半径
     */
    private int mDotMaxRadius;
    /**
     * 圆点最大间距
     */
    private int mDotSpacing;
    /**
     * 当前变大的圆点索引
     */
    private int mCurrentDot = 0;

    public DotPollingView(Context context) {
        this(context, null);
    }

    public DotPollingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotPollingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotPollingView, defStyleAttr, 0);
        init(typedArray);
        typedArray.recycle();
    }

    /**
     * 初始化
     * @param Attributes TypedArray
     */
    private void init(TypedArray Attributes) {
        mColor = Attributes.getColor(R.styleable.DotPollingView_dot_polling_color, ContextCompat.getColor(getContext(),R.color.colorPrimary));
        mSelectedColor = Attributes.getColor(R.styleable.DotPollingView_dot_polling_selected_color, ContextCompat.getColor(getContext(),R.color.colorAccent));
        mDotRadius = Attributes.getDimensionPixelSize(R.styleable.DotPollingView_dot_polling_radius,DensityUtils.dp2px(getContext(),3));
        mDotMaxRadius = Attributes.getDimensionPixelSize(R.styleable.DotPollingView_dot_polling_max_radius,DensityUtils.dp2px(getContext(),5));
        mDotSpacing = Attributes.getDimensionPixelSize(R.styleable.DotPollingView_dot_polling_spacing,DensityUtils.dp2px(getContext(),6));
        mDotTotalCount = Attributes.getInteger(R.styleable.DotPollingView_dot_polling_dot_count,3);
        mRadiusChangeRate = Attributes.getFloat(R.styleable.DotPollingView_dot_polling_change_rate,0.3F);
        mDotCurrentRadiusChange = 0;
        mSelectedPaint = new Paint();
        mSelectedPaint.setColor(mSelectedColor);
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mNormalPaint = new Paint();
        mNormalPaint.setColor(mColor);
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setStyle(Paint.Style.FILL);
    }

    public void show() {
        mCurrentDot = 0;
        mDotCurrentRadiusChange = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            Log.e(TAG, "onMeasure MeasureSpec.EXACTLY widthSize="+widthSize);
        } else {
            //指定最小宽度所有圆点加上间距的宽度, 以最小半径加上间距算总和再加上最左边和最右边变大后的距离
            width = (mDotTotalCount * mDotRadius * 2 + ((mDotTotalCount - 1) * mDotSpacing)) + (mDotMaxRadius - mDotRadius) * 2;
            Log.e(TAG, "onMeasure no MeasureSpec.EXACTLY widthSize="+widthSize+" width="+width);
            if(widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
                Log.e(TAG, "onMeasure MeasureSpec.AT_MOST width="+width);
            }

        }

        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
            Log.e(TAG, "onMeasure MeasureSpec.EXACTLY heightSize="+heightSize);
        } else {
            height = mDotMaxRadius * 2;
            Log.e(TAG, "onMeasure no MeasureSpec.EXACTLY heightSize="+heightSize+" height="+height);
            if(heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
                Log.e(TAG, "onMeasure MeasureSpec.AT_MOST height="+height);
            }

        }

        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("DotPollingView", "dot current radius change: " + mDotCurrentRadiusChange);
        //第一个圆点的圆心x坐标计算：控件宽度的一半减去(所有圆点直径的和以及所有间距的和相加的总和的一半)再加上一个半径大小，为什么要加上半径？因为我们起点要的是圆心，但算出来的是最左边x坐标
        int startPointX = getWidth() / 2 - (mDotTotalCount * mDotRadius * 2 + ((mDotTotalCount - 1) * mDotSpacing)) / 2 + mDotRadius;
        //所有圆点的圆心y坐标一致控件高度的一半
        int startPointY = getHeight() / 2;
        //先绘制所有圆点
        for (int i = 0; i < mDotTotalCount; i++) {
            canvas.drawCircle(startPointX + i * (mDotRadius * 2 + mDotSpacing), startPointY, mDotRadius, mNormalPaint);
        }
        mDotCurrentRadiusChange += mRadiusChangeRate;
        //画当前变大的圆点
        canvas.drawCircle(startPointX + mCurrentDot * (mDotRadius * 2 + mDotSpacing), startPointY, mDotRadius + mDotCurrentRadiusChange, mSelectedPaint);

        //上一个变大的圆点索引
        int beforeDot = mCurrentDot == 0 ? mDotTotalCount - 1 : mCurrentDot - 1;
        //画上一个圆点，从最大mDotMaxRadius变到最小mDotRadius
        canvas.drawCircle(startPointX + beforeDot
                    * (mDotRadius * 2 + mDotSpacing), startPointY, mDotMaxRadius - mDotCurrentRadiusChange, mNormalPaint);
        //当圆点变化率达到最大或超过最大半径和正常半径之差时 变化率重置0，当前变大圆点移至下一圆点
        if (mDotCurrentRadiusChange >= (mDotMaxRadius - mDotRadius)) {
            mDotCurrentRadiusChange = 0;
            mCurrentDot = mCurrentDot == mDotTotalCount - 1 ? 0 : mCurrentDot + 1;
        }

        invalidate();

    }
}
