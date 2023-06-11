package com.zrt.mybase.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zrt.mybase.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author：Zrt
 * @date: 2022/2/11
 */
public class ScreenUtils {


    public static void onWindowFocusChanged(Activity mActivity, MyApplication appContext) {
//        super.onWindowFocusChanged(hasFocus);

        /**
         *  获取屏幕的宽/高(屏幕展示高度)：
         注：
         如果手机没有底部虚拟导航栏则：此高度==屏幕完整高度
         如果手机有底部虚拟导航栏则：此高度==屏幕完整高度-导航栏高度

         方法一
         */
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int screenH = dm.heightPixels;
        MyLogger.Log().i("##screenW="+screenW+"##screenH="+screenH);

        /**
         *方法二
         */
        WindowManager windowManager =
                (WindowManager)mActivity.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        MyLogger.Log().i("##width="+width+"##height="+height);
        /**
         *方法三
         */
        int width2 = mActivity.getResources().getDisplayMetrics().widthPixels;
        int height2 = mActivity.getResources().getDisplayMetrics().heightPixels;
        MyLogger.Log().i("##width2="+width2+"##height2="+height2);
        /**
         *方法四
         */
        int width3 = appContext.getResources().getDisplayMetrics().widthPixels;
        int height3 = appContext.getResources().getDisplayMetrics().heightPixels;
        MyLogger.Log().i("##width3="+width3+"##height3="+height3);
        /**
         *方法五
         */
        WindowManager windowManager2 =
                (WindowManager)appContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager2.getDefaultDisplay().getMetrics(dm);
        int width5 = dm.widthPixels;
        int height5 = dm.heightPixels;
        MyLogger.Log().i("##width5="+width5+"##height5="+height5);
        //以上五种方法中：
        //方法一二三是通过Activity获取当前APP的宽高。
        //方法四五是通过ApplicationContext来获取屏幕的宽高。
        //两者区别：
        //当处于分屏模式时，前者只是当前分屏APP的宽高。
        //后者是这个设备屏幕宽高。

        //注：
        //某些情况下（具体原因待研究）带有底部虚拟导航栏的设备--在使用方法三四五获取屏幕展示高度时
        //不包含状态栏，此时可以可以换用方法一二来获取。



        /**
         * 获取屏幕完整高度(屏幕展示高度+导航栏)[针对具有导航栏的手机]
         方法一
         */
        int realScreenH = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            realScreenH = dm.heightPixels;
        }

        /**
         * 方法二：通过反射获取
         */
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm2 = new DisplayMetrics();
        int realScreenH2 = 0;
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm2);
            realScreenH2 = dm2.heightPixels;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        /**
         * 状态栏高度
         */
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;



        /**
         * 标题栏高度
         */
        View v = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentTop = v.getTop();
        int titleBarHeight = contentTop - statusBarHeight;



        /**
         * 获取虚拟导航栏高度
         */
        int navigationBarH = realScreenH - screenH;


        MyLogger.Log().i("screenW:" + screenW + "\n"
                + "screenH:" + screenH + "\n"
                + "realScreenH:" + realScreenH + "\n"
                + "statusBarHeight:" + statusBarHeight + "\n"
                + "titleBarHeight:" + titleBarHeight + "\n"
                + "navigationBarH:" + navigationBarH);

    }
}
