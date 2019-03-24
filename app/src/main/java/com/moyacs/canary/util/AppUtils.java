package com.moyacs.canary.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.LinkedList;
import java.util.List;

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


    private final List<Activity> activities = new LinkedList<Activity>();

    private static AppUtils manager;

    private boolean isExist = false;// activity 存在标志

    public static Class<?> indexActivity; // 首页所在的activity所对应的类名，必须在打开首页设置此项

    public static List<Class<?>> bottomActivities = new LinkedList<Class<?>>();// 底部导航类集合

    /**
     * 获得 activity管理对象
     *
     * @return
     */
    public static AppUtils getActivityManager() {
        if (null == manager) {
            manager = new AppUtils();
        }
        return manager;
    }

    /**
     * 添加新的activity
     *
     * @param activity
     * @return
     */
    public boolean add(final Activity activity) {

        int position = 0;
        // 判断是否自动清除非子activity
//		if (AbsInitApplication.isUseActivityManager) {
        // 导航栏activity进栈，删除非导航栏activity
        if (isBottomActivity(activity)) {
            for (int i = 0; i < activities.size() - 1; i++) {

                if (!isBottomActivity(activities.get(i))) {
                    popActivity(activities.get(i));
                    i--;
                }
                if (i > 0) {
                    // 获得重复activity位置
                    if (activities.get(i).getClass()
                            .equals(activity.getClass())) {
                        isExist = true;
                        position = i;
                    }
                }
            }

        }
//		}

        if (!activities.add(activity)) {
            return false;
        }
        // 删除重复activity
        if (isExist) {
            isExist = false;
            activities.remove(position);
        }

        return true;
    }

    /**
     * 关闭除参数activity外的所有activity
     *
     * @param activity
     */
    public void finish(final Activity activity) {
        for (Activity iterable : activities) {
            if (activity != iterable) {
                iterable.finish();
            }
        }
    }

    public static boolean isAppBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * 删除指定activity
     *
     * @param activity
     */
    private void popActivity(final Activity activity) {

        if (activity != null) {
            activity.finish();
            activities.remove(activity);
        }

    }

    /**
     * 删除已经finish的activity
     *
     * @param sourceActivity
     */
    public void removeTemporaryActivities(final Class<Activity> targetclazz,
                                          final Activity sourceActivity) {
        if (targetclazz == null || sourceActivity == null) {
            return;
        }

        int begin = -1;
        int end = -1;
        Activity activity;

        for (int i = activities.size() - 1; i >= 0; i--) {
            activity = activities.get(i);
            if (activity.getClass() == targetclazz && end == -1) {
                end = i;
            }
            if (sourceActivity == activity && begin == -1) {
                begin = i;
            }
            if (begin != -1 && end != -1) {
                break;
            }
        }

        if (end != -1 && begin > end) {
            for (int i = begin; i > end; i--) {
                activity = activities.get(i);
                popActivity(activity);
            }
        }
    }

    /**
     * 获得当前activity
     *
     * @return
     */
    @SuppressWarnings("unused")
    public Activity currentActivity() {

        if (activities == null || activities.isEmpty()) {
            return null;
        }

        Activity activity = activities.get(activities.size() - 1);

        return activity;
    }

    /*
     * 是否存在指定的Activity
     * */
    public boolean isExistActivity(final Class<? extends Activity> targetclazz) {
        Activity activity;

        if (activities == null) return false;

        for (int i = activities.size() - 1; i >= 0; i--) {
            activity = activities.get(i);
            if (activity.getClass() == targetclazz) {
                return true;
            }
        }
        return false;
    }

    public boolean isTopActivity(final Activity activity) {

        Activity currentActivity = currentActivity();

        if (currentActivity == null) return false;

        return currentActivity.equals(activity);
    }

    /**
     * activity是否为底部导航
     *
     * @return
     */
    public boolean isBottomActivity(final Activity activity) {

        for (int i = 0; i < bottomActivities.size(); i++) {
            if (activity.getClass() == bottomActivities.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如需返回IndexActivity则返回IndexActivity
     *
     * @param context
     */
    public void backIndex(final Context context) {

        if (activities.size() <= 0) {
            return;
        }

        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (isBottomActivity(activity)) {
                Intent intent = new Intent();
                intent.setClass(context, indexActivity);
                context.startActivity(intent);
            } else {
                popActivity(activity);
            }
        }
    }

    /**
     * 如需返回IndexActivity则返回IndexActivity
     *
     * @param clazz
     */
    public <E extends Activity> boolean backToActivity(final Class<E> clazz) {
        boolean flag = false;
        if (activities.size() <= 0) {
            return flag;
        }

        for (int i = activities.size() - 1; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (activity.getClass() == clazz) {
                flag = true;
                break;
            }
        }
        if (flag) {
            for (int i = activities.size() - 1; i >= 0; i--) {
                Activity activity = activities.get(i);
                if (activity.getClass() != clazz) {
                    popActivity(activity);
                } else {
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 删除已经finish的activity
     *
     * @param activity
     */
    public void removeActivity(final Activity activity) {

        if (activity != null) {
            activities.remove(activity);
        }
    }

    /**
     * 初始化，存储底部导航类
     *
     * @param activityClass
     */
    public void setBottomActivities(final Class<?> activityClass) {
        if (activityClass != null) {
            bottomActivities.add(activityClass);
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }


}
