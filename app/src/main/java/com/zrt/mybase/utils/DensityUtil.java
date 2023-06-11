package com.zrt.mybase.utils;

import android.content.Context;

/**
 * @author：Zrt
 * @date: 2022/2/22
 * C588 density: 2.0
 * px：像素（例：分辨率480x800及有480x800个像素点）
 * dp（dip ）（全名Density-independent pixels）：Android中的长度单位(抽象的像素）
 *         如果品目的dpi为160，则1dp=1px
 * sp：Android中的字体大小单位
 *          px = dp*(dpi/160)（480*800 dpi=240)(320*480  dpi=160作为Android屏幕的基准线）
 * DPI:就是屏幕的密度，单位面积内的像素密度
 * DPI的计算公式 根号下（长度像素的平方+宽度像素的平方）/屏幕对角线英寸数
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        MyLogger.Log().i("##dp-scale="+scale);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
