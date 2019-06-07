package com.reeching.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class ExitApplication {
    private List<Activity> activityList = new LinkedList<>();
    private static ExitApplication instance;

    private ExitApplication() {
    }

    // 单例模式中获取唯�?的ExitApplication实例
    public static ExitApplication getInstance() {
        if (null == instance) {
            synchronized (Exception.class) {
                if (null == instance)
                    instance = new ExitApplication();
            }
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历�?有Activity并finish

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }
}
