package com.reeching.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.util.LogUtils;
import com.reeching.bean.VideoEntity;
import com.reeching.bluegrass.R;
import com.reeching.utils.FileUtils;
import com.reeching.view.RecyclingImageView;

import java.util.List;

public class VideoGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<VideoEntity> mList;

    public VideoGridAdapter(Context context, List<VideoEntity> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
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
            convertView = inflater.inflate(R.layout.em_choose_griditem,
                    parent, false);
            holder = new ViewHolder();
            holder.imageView = (RecyclingImageView) convertView
                    .findViewById(R.id.imageView);
            holder.video_check = (ImageView) convertView.findViewById(R.id.video_check);
            holder.video_check.setVisibility(View.GONE);
            holder.icon = (ImageView) convertView.findViewById(R.id.video_icon);
            holder.tvDur = (TextView) convertView.findViewById(R.id.chatting_length_iv);
            holder.tvSize = (TextView) convertView.findViewById(R.id.chatting_size_iv);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setVisibility(View.VISIBLE);
        holder.tvDur.setText(FileUtils.toTime(mList.get(position).duration));
        holder.tvSize.setText(FileUtils.getDataSize(mList.get(position).size));
        Glide.with(inflater.getContext()).load(mList.get(position).filePath).placeholder(R.drawable.em_empty_photo).skipMemoryCache(false).into(holder.imageView);
//        }
        return convertView;
    }

    public class ViewHolder {
        RecyclingImageView imageView;
        ImageView video_check;
        ImageView icon;
        TextView tvDur;
        TextView tvSize;
    }
}
