package com.reeching.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.reeching.bluegrass.R;

import java.lang.ref.WeakReference;

/**
 * Created by lenovo on 2018/10/21.
 * auther:lenovo
 * Date：2018/10/21
 */
public class Video_Img_Async extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<ImageView> mImageView;
//    private final ImageResizer mImageResizer;

    public Video_Img_Async(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
      /*  int mImageThumbSize = mContext.getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        int mImageThumbSpacing = mContext.getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
        // app memory

        // The ImageFetcher takes care of loading images into our ImageView
        // children asynchronously
        mImageResizer = new ImageResizer(mContext, mImageThumbSize);
        mImageResizer.setLoadingImage(R.drawable.em_empty_photo);
        mImageResizer.addImageCache(((AppCompatActivity) mContext).getSupportFragmentManager(),
                cacheParams);*/
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
//        mImageResizer.loadImage(strings[0], mImageView.get());
        return BitmapUtils.getVideoBitmap2(strings[0]);
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mImageView.get().setImageBitmap(bitmap);
    }
}
