package com.reeching.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reeching.bean.VideoEntity;
import com.reeching.bluegrass.R;
import com.reeching.utils.ImageCache;
import com.reeching.utils.ImageResizer;
import com.reeching.utils.Utils;
import com.reeching.view.RecyclingImageView;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 绍轩 on 2018/4/10.
 */

public class VideoGridFragment extends Fragment implements View.OnClickListener {
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private VideoAdapter mAdapter;
    private ImageResizer mImageResizer;
    List<VideoEntity> mList;
    ArrayList<Integer> mCheckedList;
    ArrayList<VideoEntity> mVideoCheckeds;
    private TextView mTitle_right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        mList = new ArrayList<>();
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
        // app memory

        // The ImageFetcher takes care of loading images into our ImageView
        // children asynchronously
        mImageResizer = new ImageResizer(getActivity(), mImageThumbSize);
        mImageResizer.setLoadingImage(R.drawable.em_empty_photo);
        mImageResizer.addImageCache(getActivity().getSupportFragmentManager(),
                cacheParams);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_image_grid,
                container, false);
        final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        final TextView title_text = (TextView) v.findViewById(R.id.title_text);
        final LinearLayout back = (LinearLayout) v.findViewById(R.id.back);
        mTitle_right = (TextView) v.findViewById(R.id.title_right);
        mTitle_right.setOnClickListener(this);
        back.setOnClickListener(this);
        title_text.setOnClickListener(this);
        title_text.setText("视频");
        getVideoFile();
        mAdapter = new VideoAdapter(getActivity());
        mGridView.setAdapter(mAdapter);
        Bundle arguments = getArguments();
        mCheckedList = arguments.getIntegerArrayList("checked");
        mVideoCheckeds = new ArrayList<>(3);
        mTitle_right.setText(String.format(getString(R.string.finish_num), mCheckedList.size()));
//        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView,
                                             int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help
                    // with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageResizer.setPauseWork(true);
                    }
                } else {
                    mImageResizer.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        final int numColumns = (int) Math.floor(mGridView
                                .getWidth()
                                / (mImageThumbSize + mImageThumbSpacing));
                        if (numColumns > 0) {
                            final int columnWidth = (mGridView.getWidth() / numColumns)
                                    - mImageThumbSpacing;
                            mAdapter.setItemHeight(columnWidth);
                            if (Utils.hasJellyBean()) {
                                mGridView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            } else {
                                mGridView.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        }
                    }
                });
        return v;

    }


    @Override
    public void onResume() {
        super.onResume();
        mImageResizer.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageResizer.closeCache();
        mImageResizer.clearCache();
    }

    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建保存录制得到的视频文件
     *
     * @return
     * @throws IOException
     */
    private File createMediaFile() throws IOException {
        if (isSdcardExist()) {
            File mediaStorageDir = new
                    File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "video" + File.separator);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("Tag", "failed to create directory");
                    return null;
                }
            }
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";

            return new File(mediaStorageDir + File.separator + imageFileName + suffix);
        }
        return null;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.title_right:
                Intent intent = getActivity().getIntent().putExtra("checked", mVideoCheckeds);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                break;
        }
    }

    private class VideoAdapter extends BaseAdapter {

        private final Context mContext;
        private int mItemHeight = 0;
        private RelativeLayout.LayoutParams mImageViewLayoutParams;

        VideoAdapter(Context context) {
            mContext = context;
            mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        }

        @Override
        public int getCount() {
            return mList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return (position == 0) ? null : mList.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.em_choose_griditem, container, false);
                holder.imageView = (RecyclingImageView) convertView.findViewById(R.id.imageView);
                holder.video_check = (ImageView) convertView.findViewById(R.id.video_check);
                holder.icon = (ImageView) convertView.findViewById(R.id.video_icon);
                holder.tvDur = (TextView) convertView.findViewById(R.id.chatting_length_iv);
                holder.tvSize = (TextView) convertView.findViewById(R.id.chatting_size_iv);
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.imageView.setLayoutParams(mImageViewLayoutParams);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageResizer.setPauseWork(true);

                    if (position == 0) {
                        Uri fileUri = null;
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.addCategory("android.intent.category.DEFAULT");
                        try {
                            fileUri = Uri.fromFile(createMediaFile()); // create a file to save the video
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 2 * 60 * 1000);
                        // start the Video Capture Intent
                        startActivityForResult(intent, 100);

                    } else {
                        VideoEntity vEntty = mList.get(position - 1);
                        if (vEntty.checked) {
                            boolean remove = mCheckedList.remove(Integer.valueOf(vEntty.ID));
                            Log.d("remove", remove + "");
                            vEntty.checked = false;
                            if (mVideoCheckeds.contains(vEntty)) mVideoCheckeds.remove(vEntty);
                            holder.video_check.setImageResource(R.mipmap.imageselector_select_uncheck);
                        } else {
                            if (mCheckedList.size() >= 3) return;
                            vEntty.checked = true;
                            mCheckedList.add(vEntty.ID);
                            if (!mVideoCheckeds.contains(vEntty)) mVideoCheckeds.add(vEntty);
                            holder.video_check.setImageResource(R.mipmap.imageselector_select_checked);
                        }
                        mTitle_right.setText(String.format(getString(R.string.finish_num), mCheckedList.size()));

                    }
                }
            });
            // Check the height matches our calculated column width
            if (holder.imageView.getLayoutParams().height != mItemHeight) {
                holder.imageView.setLayoutParams(mImageViewLayoutParams);
            }

            // Finally load the image asynchronously into the ImageView, this
            // also takes care of
            // setting a placeholder image while the background thread runs
            String st1 = "拍摄录像";
            if (position == 0) {
                holder.icon.setVisibility(View.GONE);
                holder.tvDur.setVisibility(View.GONE);
                holder.tvSize.setText(st1);
                holder.video_check.setVisibility(View.GONE);
                holder.imageView.setImageResource(R.drawable.em_actionbar_camera_icon);
            } else {
                holder.icon.setVisibility(View.VISIBLE);
                VideoEntity entty = mList.get(position - 1);
                holder.tvDur.setVisibility(View.VISIBLE);
                holder.video_check.setVisibility(View.VISIBLE);
//                mImageResizer.loadImage(entty.filePath, holder.imageView);
                if (mCheckedList.contains(entty.ID)) {
                    entty.checked = true;
                    if (!mVideoCheckeds.contains(entty)) mVideoCheckeds.add(entty);
                    holder.video_check.setImageResource(R.mipmap.imageselector_select_checked);
                } else {
                    if (mVideoCheckeds.contains(entty)) mVideoCheckeds.remove(entty);
                    holder.video_check.setImageResource(R.mipmap.imageselector_select_uncheck);
                }
                holder.tvDur.setText(toTime(entty.duration));
                holder.tvSize.setText(getDataSize(entty.size));
                holder.imageView.setImageResource(R.drawable.em_empty_photo);
//                holder.imageView.setImageBitmap(BitmapUtils.getVideoBitmap1(entty.filePath));
                Glide.with(getContext()).load(mList.get(position).filePath).placeholder(R.drawable.em_empty_photo).skipMemoryCache(false).into(holder.imageView);
            }
            return convertView;
            // END_INCLUDE(load_gridview_item)
        }

        /**
         * Sets the item height. Useful for when we know the column width so the
         * height can be set to match.
         *
         * @param height
         */
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams = new RelativeLayout.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT, mItemHeight);
            mImageResizer.setImageSize(height);
            notifyDataSetChanged();
        }

        class ViewHolder {

            RecyclingImageView imageView;
            ImageView video_check;
            ImageView icon;
            TextView tvDur;
            TextView tvSize;
        }
    }

    public static String toTime(int var0) {
        var0 /= 1000;
        int var1 = var0 / 60;
        boolean var2 = false;
        if (var1 >= 60) {
            int var4 = var1 / 60;
            var1 %= 60;
        }

        int var3 = var0 % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var3)});
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 0L ? "error" : (var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F)) + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F)) + "MB" : var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F)) + "GB")));
    }

    private void getVideoFile() {
        ContentResolver mContentResolver = getActivity().getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.DEFAULT_SORT_ORDER);
        mList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // ID:MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));

                // title：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // path：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                // duration：MediaStore.Audio.Media.DURATION
                int duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                // 大小：MediaStore.Audio.Media.SIZE
                int size = (int) cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                VideoEntity entty = new VideoEntity();
                entty.ID = id;
                entty.title = title;
                entty.filePath = url;
                entty.duration = duration;
                entty.size = size;
                mList.add(entty);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getData();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(uri.getEncodedPath());
                Uri fromFile = Uri.fromFile(file);
                intent.setData(fromFile);
                getActivity().sendBroadcast(intent);
                getVideoFile();
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
