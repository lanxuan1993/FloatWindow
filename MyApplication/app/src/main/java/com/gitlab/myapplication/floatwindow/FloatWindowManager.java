package com.gitlab.myapplication.floatwindow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * @author: created by ZhaoBeibei on 2020-05-13 10:13
 * @describe: 悬浮窗管理器
 */
public class FloatWindowManager {
    private static WindowManager mWindowManager;
    private static FloatView mFloatView;
    private static WindowManager.LayoutParams mLayoutParams;
    private static boolean mHasShown;

    /**
     * 获取WindowManager服务
     * 注:context不能是Activity的,Activity会返回它专享的WindowManager，而Activity的窗口级别是属于应用层的
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 创建悬浮窗
     * @param context
     */
    public static void createFloatWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        mFloatView = new FloatView(context);
        mLayoutParams = new WindowManager.LayoutParams();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= 24) {
            /*android7.0不能用TYPE_TOAST*/
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            String packname = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
            if (permission) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }

        //设置图片格式，效果为背景透明
        mLayoutParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置 (例如为左侧置顶: mLayoutParams.gravity = Gravity.START | Gravity.TOP;)
        mLayoutParams.gravity = Gravity.CENTER;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mLayoutParams.x = screenWidth;
        mLayoutParams.y = screenHeight;

        //设置悬浮窗口长宽数据
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mFloatView.setParams(mLayoutParams);
        // 将悬浮窗控件添加到WindowManager
        windowManager.addView(mFloatView, mLayoutParams);
        mHasShown = true;
    }

    /**
     * 移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = mFloatView.isAttachedToWindow();
        }
        if (mHasShown && isAttach && mWindowManager != null) {
            mWindowManager.removeView(mFloatView);
        }

    }

    /**
     * 隐藏悬浮窗
     */
    public static void hide() {
        if (mHasShown) {
            mWindowManager.removeViewImmediate(mFloatView);
        }
        mHasShown = false;
    }

    /**
     * 展示悬浮窗
     */
    public static void show() {
        if (!mHasShown) {
            mWindowManager.addView(mFloatView, mLayoutParams);
        }
        mHasShown = true;
    }
}

