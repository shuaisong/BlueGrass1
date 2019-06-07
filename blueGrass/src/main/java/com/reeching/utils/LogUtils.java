package com.reeching.utils;

import android.util.Log;

/**
 * ******************************************
 * ******************************************
 */
public class LogUtils {
    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数

    private static String createLog(String log) {
        return methodName +
                "(" + className + ":" + lineNumber + ")" +
                "=" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void i(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void i(int message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message + ""));
    }

    public static void d(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void e(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }


}
