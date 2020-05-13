package com.gitlab.myapplication.floatwindow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author: created by ZhaoBeibei on 2020-05-12 10:48
 * @describe: 悬浮窗服务, 通过暴露接口FloatCallBack与Activity进行交互
 */
public class FloatService extends Service implements FloatCallBack{

    @Override
    public void onCreate() {
        super.onCreate();
        FloatActionController.getInstance().registerFloatCallBack(this);
        //初始化悬浮窗
        FloatWindowManager.createFloatWindow(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除悬浮窗
        FloatWindowManager.removeFloatWindowManager();
    }

    /**
     * 悬浮窗的隐藏
     */
    @Override
    public void hide() {
        FloatWindowManager.hide();
    }

    /**
     * 悬浮窗的显示
     */
    @Override
    public void show() {
        FloatWindowManager.show();
    }

}