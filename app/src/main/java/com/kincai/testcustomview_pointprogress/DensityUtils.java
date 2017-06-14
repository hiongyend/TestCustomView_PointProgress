package com.kincai.testcustomview_pointprogress;

import android.content.Context;
import android.util.TypedValue;

/**
 * Copyright (C) 2015 The KINCAI Open Source Project
 * .
 * Create By KINCAI
 * .
 * Time 2017-06-14 10:56
 * .
 * Desc 水平圆点进度条
 */

public class DensityUtils {
	
	private DensityUtils() {
		throw new UnsupportedOperationException("Cannot instantiate the object");
	}

	/**
	 * dp转px
	 */
	public static int dp2px(Context ctx, float dpVal) {
		// 设备像素缩放比
		return (int) (dpVal * (ctx.getResources().getDisplayMetrics().density) + 0.5f);// 4.9->5 4.4->4
		/*return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
				ctx.getResources().getDisplayMetrics());*/
	}
	/**
	 * sp转px
	 * 
	 * @param ctx
	 * @param spVal
	 * @return
	 */
	public static int sp2px(Context ctx, float spVal) {
//		return (int) (spVal * (ctx.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, ctx.getResources().getDisplayMetrics());
	}
	/**
	 * px转sp
	 * 
	 * @param ctx
	 * @param pxVal
	 * @return
	 */
	public static float px2sp(Context ctx, float pxVal) {
		return (pxVal / ctx.getResources().getDisplayMetrics().scaledDensity + 0.5f);
	}
	
	/**
	 * px转dp
	 */
	public static float px2dp(Context ctx, int px) {
		return px / (ctx.getResources().getDisplayMetrics().density  + 0.5f);
	}
}
