package com.reeching.activity;

import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.bluegrass.R;
import com.reeching.utils.ImageCache;
import com.reeching.utils.ImageResizer;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity {

    private OrientationEventListener mOrientationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        int mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        int mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
        // app memory

        // The ImageFetcher takes care of loading images into our ImageView
        // children asynchronously
        ImageResizer mImageResizer = new ImageResizer(this, mImageThumbSize);
//        mImageResizer.setLoadingImage(R.drawable.em_empty_photo);
        mImageResizer.addImageCache((getSupportFragmentManager()), cacheParams);
        String path = getIntent().getStringExtra("path");
        LogUtils.d("path:" + path);
//        path = "file:///storage/emulated/0/video/cache/93b82b1e99a29f9bced47d11520d3dc4.mp4";
//        path = "http://video.yidianzixun.com/video/get-url?key=user_upload/15236013492601fb45228d47976c47f1cb7ba3bab470b.mp4";
        if (!path.startsWith("file")) {
            HttpProxyCacheServer proxy = BaseApplication.getProxy();
            path = proxy.getProxyUrl(path);
        }
        LogUtils.d("path:" + path);

        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                LogUtils.i(
                        "Orientation changed to " + orientation);
            }
        };

        if (mOrientationListener.canDetectOrientation()) {
            LogUtils.i("Can detect orientation");
            mOrientationListener.enable();
        } else {
            LogUtils.i("Cannot detect orientation");
            mOrientationListener.disable();
        }


        JZVideoPlayerStandard.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayerStandard.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        final JZVideoPlayerStandard playerStandard = findViewById(R.id.player);
//        playerStandard.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //将缩略图的scaleType设置为FIT_XY（图片全屏）
        playerStandard.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //设置容器内播放器高,解决黑边（视频全屏）
        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
        playerStandard.setUp(path, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
//        playerStandard.setUp(path, JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN);
        if (path.startsWith("file"))
            Glide.with(this).load(path).placeholder(R.drawable.em_empty_photo).skipMemoryCache(false).into(playerStandard.thumbImageView);
        else {
            try {
//                mImageResizer.setLoadingImage(R.drawable.em_empty_photo);
                mImageResizer.loadImage(path, playerStandard.thumbImageView);
            } catch (Exception e) {
                LogUtils.d(e.getMessage());
            }
        }
//            new Video_Img_Async(this, playerStandard.thumbImageView).execute(path);
    }


    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationListener.disable();
    }
}
