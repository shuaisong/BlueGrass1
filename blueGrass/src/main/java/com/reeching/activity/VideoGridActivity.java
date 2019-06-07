package com.reeching.activity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.bean.VideoEntity;
import com.reeching.bluegrass.R;
import com.reeching.utils.FileUtils;
import com.reeching.utils.MediaScanner;
import com.reeching.utils.ToastUtil;
import com.reeching.utils.Utils;
import com.reeching.video.CameraActivity;
import com.reeching.view.RecyclingImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class VideoGridActivity extends Activity implements View.OnClickListener {

    private VideoAdapter mAdapter;
    List<VideoEntity> mList = new ArrayList<>(3);
    ArrayList<Integer> mCheckedList = new ArrayList<>(3);
    ArrayList<VideoEntity> mVideoCheckeds = new ArrayList<>(3);
    ;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private File mFile;//mp4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        mImageThumbSize = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(
                R.dimen.image_thumbnail_spacing);
        final GridView mGridView = (GridView) findViewById(R.id.gridView);
        final TextView title_text = (TextView) findViewById(R.id.title_text);
        final LinearLayout back = (LinearLayout) findViewById(R.id.back);
        TextView title_right = (TextView) findViewById(R.id.title_right);
        RelativeLayout bar = (RelativeLayout) findViewById(R.id.imageselector_title_bar_layout);
        bar.setBackgroundColor(getResources().getColor(R.color.blue));
        title_right.setOnClickListener(this);
        back.setOnClickListener(this);
        title_text.setText("视频");
        getVideoFile();
        mAdapter = new VideoAdapter(this);
        mGridView.setAdapter(mAdapter);
        if (getIntent() != null)
            mCheckedList.addAll(getIntent().getIntegerArrayListExtra("checked"));
        if (mCheckedList.contains(-1)) {
            mCheckedList.set(mCheckedList.indexOf(-1), mList.get(0).ID);
        }
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
    }

    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建保存录制得到的视频文件
     */
    private File createMediaFile() {
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
            DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssZ");
            String timeStamp = dateFormat.format(new Date());
            String imageFileName = "VID_" + timeStamp;
            String suffix = ".mp4";
            mFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mFile;
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
                finish();
                break;
            case R.id.title_right:
                Intent intent = getIntent().putExtra("checked", mVideoCheckeds);
                setResult(Activity.RESULT_OK, intent);
                finish();
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
                    if (position == 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Intent intent = new Intent(mContext, CameraActivity.class);
                            startActivityForResult(intent, 100);
                        } else {
                            Uri fileUri;
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            fileUri = Uri.fromFile(createMediaFile()); // create a file to save the video
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);//时长max 30秒
//                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);//时长max 30秒
                            // start the Video Capture Intent
                            startActivityForResult(intent, 100);
                        }

                    } else {
                        VideoEntity vEntty = mList.get(position - 1);
                        if (vEntty.duration > (31 * 1000) - 1) {
                            Toast.makeText(VideoGridActivity.this, "视频超过30秒，无法上传", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (vEntty.checked) {
                            vEntty.checked = false;
                            if (mVideoCheckeds.contains(vEntty)) mVideoCheckeds.remove(vEntty);
                            holder.video_check.setImageResource(R.mipmap.imageselector_select_uncheck);
                        } else {
                            vEntty.checked = true;
                            mCheckedList.add(vEntty.ID);
                            if (!mVideoCheckeds.contains(vEntty)) mVideoCheckeds.add(vEntty);
                            holder.video_check.setImageResource(R.mipmap.imageselector_select_checked);
                        }
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
                if (mCheckedList.contains(entty.ID)) {
                    entty.checked = true;
                    entty.compress = true;
                    if (!mVideoCheckeds.contains(entty)) mVideoCheckeds.add(entty);
                } /*else {
                    if (mVideoCheckeds.contains(entty)) mVideoCheckeds.remove(entty);
                }*/
                if (entty.checked) {
                    holder.video_check.setImageResource(R.mipmap.imageselector_select_checked);
                } else holder.video_check.setImageResource(R.mipmap.imageselector_select_uncheck);
                holder.tvDur.setText(FileUtils.toTime(entty.duration));
                holder.tvSize.setText(FileUtils.getDataSize(entty.size));
                holder.imageView.setImageResource(R.drawable.em_empty_photo);
//                holder.imageView.setImageBitmap(BitmapUtils.getVideoBitmap1(entty.filePath));
                Glide.with(mContext).load(entty.filePath).placeholder(R.drawable.em_empty_photo).skipMemoryCache(false).into(holder.imageView);
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


    private void getVideoFile() {
        ContentResolver mContentResolver = getContentResolver();
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
                LogUtils.d(url);
                if (url.startsWith(FileUtils.getFilePath(this, "video" + File.separator + "compress"))
                        || url.startsWith(FileUtils.getFilePath(this, "video" + File.separator + "cache"))) {
                    continue;
                }
                // duration：MediaStore.Audio.Media.DURATION
                int duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));//ms

                // 大小：MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                long date = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                VideoEntity entty = new VideoEntity();
                entty.ID = id;
                entty.title = title;
                entty.filePath = url;
                entty.duration = duration;
                entty.size = size;
                entty.date = date;
                mList.add(entty);
            } while (cursor.moveToNext());
        }
        Collections.sort(mList);
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(requestCode + resultCode + (data == null ? "null" : data.toString()));
        if (resultCode == Activity.RESULT_CANCELED) return;
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            /*     Uri uri = data.getData();
                 Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                 assert uri != null;
                 File file = new File(uri.getEncodedPath());
                 Uri fromFile = Uri.fromFile(file);
                 intent.setData(fromFile);
                 sendBroadcast(intent);//4.4之后不可用
                 getVideoFile();
                 mAdapter.notifyDataSetChanged();*/
            Uri uri = data.getData();
            if (uri != null) {
                String mVideoPath = uri.getPath();
                VideoEntity videoEntity = new VideoEntity();
                videoEntity.duration = Utils.getLongTime(mVideoPath);
                videoEntity.size = Utils.ReadVideoSize(mVideoPath);
                videoEntity.ID = -1;
                if (videoEntity.size == 0) {
                    ToastUtil.showToast(this, "视频保存失败，请重新录制");
                    return;
                }
                videoEntity.checked = true;
                videoEntity.filePath = mVideoPath;
                mList.add(0, videoEntity);
                mVideoCheckeds.add(videoEntity);
//                mCheckedList.add(0, videoEntity.ID);
                mAdapter.notifyDataSetChanged();
                new MediaScanner(BaseApplication.getInstance()).scanFile(new File(mVideoPath), "video/mp4", null);
            }
        }
    }

}
