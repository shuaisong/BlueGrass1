package com.reeching.listener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import com.reeching.activity.VideoActivity;
import com.reeching.bean.VideoEntity;

public class VideoItemClickListener implements AdapterView.OnItemClickListener {
    private Context mContext;

    public VideoItemClickListener(Context context) {
        mContext = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Uri parse = Uri.parse("file://" + ((VideoEntity) parent.getAdapter().getItem(position)).filePath);
//        Intent intent  = new Intent(Intent.ACTION_VIEW);
//        intent .setDataAndType(parse, "video/*");
        Intent intent = new Intent(mContext, VideoActivity.class);
        intent.putExtra("path", "file://" + ((VideoEntity) parent.getAdapter().getItem(position)).filePath);
        mContext.startActivity(intent);

    }
}