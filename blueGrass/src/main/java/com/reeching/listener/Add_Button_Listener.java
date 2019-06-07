package com.reeching.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by lenovo on 2018/11/5.
 * auther:lenovo
 * Date：2018/11/5
 */
public class Add_Button_Listener implements View.OnClickListener {
    private Activity mContext;
    private AlertDialog mDialog;
    private Dialog mCameraDialog;
    private Dialog mVideoDialog;

    public Add_Button_Listener(Activity context, Dialog cameraDialog, Dialog videoDialog) {
        mContext = context;
        mCameraDialog = cameraDialog;
        mVideoDialog = videoDialog;
    }

    @Override
    public void onClick(View v) {
        if (mDialog == null) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
            mBuilder.setTitle("文件类型选择：").setItems(new String[]{"图片", "视频"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            mDialog.dismiss();
                            mCameraDialog.show();
                            break;
                        case 1:
                            dialog.dismiss();
                            mVideoDialog.show();
                            break;
                    }
                }
            });
            mBuilder.setCancelable(true);
            mDialog = mBuilder.show();
        }
        mDialog.show();
    }
}
