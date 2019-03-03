package com.moyacs.canary.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/3
 * @email 252774645@qq.com
 */
public class AppUtils {


    /**
     * 获取当前进程名称
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess
                : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
