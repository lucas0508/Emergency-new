package com.tjmedicine.emergency.common.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author QiZai
 * @desc
 * @date 2018/6/1 11:21
 */

public class ActivityManager {

    public static ActivityManager instance;

    /**
     * 存放Activity的map
     */
    private List<Activity> mActivities = new ArrayList<>();

    private ActivityManager() {
    }

    /**
     * 采用单例模式初始化ActivityManager，使只初始化一次
     *
     * @return
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (!mActivities.contains(activity)) {
            mActivities.add(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrent() {
        return mActivities.get(mActivities.size() - 1);
    }

    /**
     * 关闭指定的Activity
     *
     * @param activity
     */
    public void closeActivity(Activity activity) {
        if (activity != null) {
            if (mActivities.contains(activity)) {
                mActivities.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 将activity全部关闭掉
     */
    public void closeAll() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 将activity全部关闭掉,除IndexActivity
     */
    public void clearOther() {
        for (Activity activity : mActivities) {
            if (activity.getClass().getSimpleName().equals("IndexActivity")) {
                continue;
            }
            activity.finish();
        }
    }

    /**
     * 关闭Activity列表中的所有Activity
     * 并杀死该应用进程
     */
    public void kill() {
        closeAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
