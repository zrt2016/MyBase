package com.zrt.kotlinapp.utils

/**
 * @author：Zrt
 * @date: 2023/3/16
 * 禁用GPU硬件加速功能，
 *  ① application 层级禁用，在 AndroidManifest 文件中 applicationn 标签中添加如下属性：
 *      <applicationn android:hardwareAccelerated="true"/>
 *  ② activity 层级禁用，在 AndroidManifest 文件中， activity 标签中添加如下属性：
 *      <activity  android:hardwareAccelerated="true"/>
 *  ③ window 层级开起硬件加速（window层级上不支持关闭硬件加速）
 *      getWindow().setFlags(
 *          WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
 *          WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
 *  ④ 在 View 层级上使用如下代码关闭硬件加速（View 层级不支持开起硬件加速）
 *      setLayerType(View.LAYER_TYPE_SOFTWARE, null)
 *      或者使用 android:layerType="software" 关闭硬件加速
 */
