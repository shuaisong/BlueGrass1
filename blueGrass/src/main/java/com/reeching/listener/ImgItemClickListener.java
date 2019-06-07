package com.reeching.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.reeching.bluegrass.PicViewActivityTemp;

public class ImgItemClickListener implements AdapterView.OnItemClickListener {

    private Context mContext;

    public ImgItemClickListener(Context context) {
        mContext = context;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext,
                PicViewActivityTemp.class);
        intent.putExtra("position", "1");
        intent.putExtra("ID", position);
        mContext.startActivity(intent);
    }
}