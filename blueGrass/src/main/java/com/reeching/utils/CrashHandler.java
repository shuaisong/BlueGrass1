package com.reeching.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.reeching.bluegrass.MainActivity;

/**
 * Created by longjing on 2017/4/5.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler instance;  //单例引用，这里我们做成单例的，因为我们一个应用程序里面只需要一个UncaughtExceptionHandler实例
    private Context context;

    private CrashHandler() {
    }

    public synchronized static CrashHandler getInstance() {  //同步方法，以免单例多线程环境下出现异常
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx) {  //初始化，把当前对象设置成UncaughtExceptionHandler处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = ctx;
    }

    public void uncaughtException(Thread thread, Throwable ex) {  //当有未处理的异常发生时，就会来到这里。。
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "程序出现异常，即将重新启动", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();
        Log.d("Sandy", "uncaughtException, thread: " + thread
                + " name: " + thread.getName() + " id: " + thread.getId() + "exception: "
                + ex);
        ex.printStackTrace();
        String threadName = thread.getName();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            LogUtils.d("uncaughtException"+e.getMessage());
            e.printStackTrace();
        }
        restartApp();
        if ("sub1".equals(threadName)) {
            Log.d("Sandy", "");

        } else {
            //这里我们可以根据thread name来进行区别对待，同时，我们还可以把异常信息写入文件，以供后来分析。
        }
    }

    /**
     * 重启应用
     */
    private void restartApp() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//再此之前可以做些退出等操作
    }

}
