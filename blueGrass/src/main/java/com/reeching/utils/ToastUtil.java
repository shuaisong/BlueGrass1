package com.reeching.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lenovo on 2018/11/2.
 * auther:lenovo
 * Date：2018/11/2
 */
public class ToastUtil {
    public static void showToast(Context mContext, String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }
}
