package com.reeching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yancy.imageselector.BimpHandler;
import com.reeching.bluegrass.R;

public class ImgGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    public ImgGridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return BimpHandler.tempSelectBitmap.size();
    }

    public Object getItem(int arg0) {
        return BimpHandler.tempSelectBitmap.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_photo_gvitem,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageBitmap(BimpHandler.tempSelectBitmap.get(
                position).getBitmap());
        return convertView;
    }


    public class ViewHolder {
        public ImageView image;
    }
}

