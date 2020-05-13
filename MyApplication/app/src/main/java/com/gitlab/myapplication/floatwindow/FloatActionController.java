package com.gitlab.myapplication.floatwindow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Author:
 * Date:2017.08.01
 * Des:与悬浮窗交互的控制类，真正的实现逻辑不在这
 */
public class FloatActionController {

    private static final FloatActionController sInstance = new FloatActionController();
    private FloatCallBack mFloatCallBack;

    private FloatActionController() {
    }

    public static FloatActionController getInstance() {
        return sInstance;
    }

    /**
     * 注册监听
     */
    public void registerFloatCallBack(FloatCallBack floatCallBack) {
        mFloatCallBack = floatCallBack;
    }


    /**
     * 开启服务悬浮窗
     */
    public void startFloatServer(Context context) {
        Intent intent = new Intent(context, FloatService.class);
        context.startService(intent);
    }

    /**
     * 关闭悬浮窗
     */
    public void stopFloatServer(Context context) {
        Intent intent = new Intent(context, FloatService.class);
        context.stopService(intent);
    }


    /**
     * 悬浮窗的显示
     */
    public void show() {
        if (mFloatCallBack == null){
            return;
        }
        mFloatCallBack.show();
    }

    /**
     * 悬浮窗的隐藏
     */
    public void hide() {
        if (mFloatCallBack == null){
            return;
        }
        mFloatCallBack.hide();
    }

    /**
     * 判断是否有悬浮窗权限
     * @param context
     * @return
     */
    public boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 跳转悬浮窗权限设置界面
     * @param context
     */
    public void showPermissionDialog(final Context context) {
        boolean isHasPermission = hasPermission(context);
        if (!isHasPermission) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("无法自动打开权限设置页面，请找到权限相关的页面，并授予悬浮框权限即可")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //请求悬浮窗权限
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            context.startActivity(intent);
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }


}
