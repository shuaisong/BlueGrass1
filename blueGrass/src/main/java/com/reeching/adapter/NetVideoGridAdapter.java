package com.reeching.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.activity.VideoActivity;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ImageCache;
import com.reeching.utils.ImageResizer;
import com.reeching.view.RecyclingImageView;

import java.util.List;

public class NetVideoGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> mList;
    private final ImageResizer mImageResizer;

    public NetVideoGridAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        this.mList = list;
        int mImageThumbSize = context.getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        int mImageThumbSpacing = context.getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
        // app memory

        // The ImageFetcher takes care of loading images into our ImageView
        // children asynchronously
        mImageResizer = new ImageResizer(context, mImageThumbSize);
//        mImageResizer.setLoadingImage(R.drawable.em_empty_photo);
        mImageResizer.addImageCache(((AppCompatActivity) context).getSupportFragmentManager(), cacheParams);
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_photo_gvitem,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (RecyclingImageView) convertView
                    .findViewById(R.id.item_grida_image);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.video_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position) != null) {
                   /* Uri parse = Uri.parse(HttpApi.picip + mList.get(position));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(parse, "video/*");*/
                    Intent intent = new Intent(inflater.getContext(), VideoActivity.class);
                    intent.putExtra("path", HttpApi.picip + mList.get(position));
                    inflater.getContext().startActivity(intent);

                }
            }
        });
        holder.icon.setVisibility(View.VISIBLE);
        holder.image.setImageResource(R.drawable.loading);
        HttpProxyCacheServer proxy = BaseApplication.getProxy();

        if (proxy.isCached(HttpApi.picip + mList.get(position))) {
            String path = proxy.getProxyUrl(HttpApi.picip + mList.get(position));
            Glide.with(inflater.getContext()).load(path).placeholder(R.drawable.em_empty_photo).skipMemoryCache(false).into(holder.image);
            LogUtils.d("path" + path);
        } else {
            try {
                mImageResizer.setLoadingImage(R.drawable.downing);
                mImageResizer.loadImage(HttpApi.picip + mList.get(position), holder.image);
            } catch (Exception e) {
                holder.image.setImageResource(R.drawable.error);
                LogUtils.d(e.getMessage());
            }
        }
//        new Video_Img_Async(holder.image).execute(HttpApi.picip + mList.get(position));
        return convertView;
    }

    public class ViewHolder {
        public RecyclingImageView image;
        public ImageView icon;
    }
}
