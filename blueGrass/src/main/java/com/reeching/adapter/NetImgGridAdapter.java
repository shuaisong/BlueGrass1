package com.reeching.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NetImgGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> mList;
    private Context mContext;

    public NetImgGridAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
        mContext = context;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int arg0) {
        return mList.get(arg0);
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
        Picasso.with(mContext)
                .load(HttpApi.picip + mList.get(position))
                .resize(900, 900)
                .placeholder(R.drawable.loading)              //添加占位图片
                .error(R.drawable.error)
                .config(Bitmap.Config.RGB_565)
                .centerInside()
                .into(holder.image);
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}