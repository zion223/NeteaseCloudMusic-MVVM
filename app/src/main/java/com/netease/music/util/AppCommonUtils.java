package com.netease.music.util;

import android.app.ActivityManager;
import android.content.Context;

import com.kunminx.architecture.utils.Utils;

import java.util.List;

/**
 * @author Administrator
 */
public class AppCommonUtils {

    /**
     * 判断服务是否启动,context上下文对象 ，className服务的name
     *
     * @param className
     * @return true 正在运行
     */
    public static boolean isServiceRunning(String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) Utils.getApp()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            String className1 = serviceList.get(i).service.getClassName();
            if (className1.equalsIgnoreCase(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
