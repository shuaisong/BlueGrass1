package com.reeching.bluegrass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.activity.BaseDetailActivity;
import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.adapter.NetVideoGridAdapter;
import com.reeching.bean.VideoEntity;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.SPUtil;
import com.reeching.utils.ToastUtil;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PlanInfoActivity extends BaseDetailActivity {
    private TextView tvtheme;
    private TextView tvtimes;
    private TextView tvtimee;

    private ArrayList<String> list_video = new ArrayList<>();
    private EditText etpeople, tvcezhanreninfo;
    private EditText mZuoZheText;
    private ImageView ivlevel;
    private ProgressDialog progressDialog;
    private NoScrollGridView lin, lin_video;
    private ArrayList<String> list = new ArrayList<>();
    //    private PlanInfoActivity.GridViewAdapter adapter2;
    private NetImgGridAdapter adapter2;
    private TextView mInfoHuaLang;
    private String id;
    private Button btnalter;
    private TextView show_quantity;
    private EditText mWorksCount;
    private EditText mVideoCount;
    private EditText mSculptureCount;
    private EditText mDeviceCount;
    private EditText mOtherDesc;
    private NetVideoGridAdapter mAdapter_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initdata() {
        tvtheme.setText(infos.getTheme());
        etpeople.setText(infos.getManager());
        String dateBegin = infos.getDateBegin();
        mInfoHuaLang.setText(infos.getGalleryName());
        mZuoZheText.setText(infos.getAuthor());
        tvtimes.setText(dateBegin);
        id = infos.getId();
        String dateEnd = infos.getDateEnd();
        tvtimee.setText(dateEnd);
        show_quantity.setText(infos.getRemarks());
        tvcezhanreninfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
            ivlevel.setImageResource(R.drawable.red);
        }
        if (!"".equals(infos.getPhoto())) {
            list.clear();
            String sourceStr = infos.getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        adapter2.notifyDataSetChanged();
        if (infos.getVideoUrl() != null && !"".equals(infos.getVideoUrl())) {
            list_video.clear();
            String sourceStr = infos.getVideoUrl();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list_video.add(sourceStrArray[ii]);
            }
        }
        mAdapter_video.notifyDataSetChanged();
        if (infos.getVideoCount() != null)
            mVideoCount.setText(infos.getVideoCount().trim().equals("0") ? "" : infos.getVideoCount());
        if (infos.getSculptureCount() != null)
            mSculptureCount.setText(infos.getSculptureCount().trim().equals("0") ? "" : infos.getSculptureCount());
        if (infos.getDeviceCount() != null)
            mDeviceCount.setText(infos.getDeviceCount().trim().equals("0") ? "" : infos.getDeviceCount());
        if (infos.getWorksCount() != null)
            mWorksCount.setText(infos.getWorksCount().trim().equals("0") ? "" : infos.getWorksCount());
        mOtherDesc.setText(infos.getOtherDesc());
    }

    @Override
    protected void initView() {
        super.initView();
        lin = (NoScrollGridView) findViewById(R.id.planinfo_lin);
        lin_video = (NoScrollGridView) findViewById(R.id.info_lin_video);
        TextView tv_photo = (TextView) findViewById(R.id.photo);
        TextView tv_video = (TextView) findViewById(R.id.video);
        tvtheme = (TextView) findViewById(R.id.planinfo_theme);
        etpeople = (EditText) findViewById(R.id.planinfo_people);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        mZuoZheText = (EditText) findViewById(R.id.planinfo_zuozhe);
        tvtimee = (TextView) findViewById(R.id.planinfo_timee);
        tvtimes = (TextView) findViewById(R.id.planinfo_times);
        tvcezhanreninfo = (EditText) findViewById(R.id.planinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.planinfo_level);
        mInfoHuaLang = (TextView) findViewById(R.id.planinfo_hualang);
        show_quantity = (TextView) findViewById(R.id.show_quantity);
        btnalter = (Button) findViewById(R.id.planinfo_report);
        adapter2 = new NetImgGridAdapter(PlanInfoActivity.this, list);
        lin.setAdapter(adapter2);
        mAdapter_video = new NetVideoGridAdapter(this, list_video);
        lin_video.setAdapter(mAdapter_video);
        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PlanInfoActivity.this.finish();
            }
        });

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(PlanInfoActivity.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {

                    adapter2.notifyDataSetChanged();
                }
            }
        });
        if (/*BaseApplication.getInstance().getQuanxian().equals("上报用户")*/ SPUtil.getUserQuanXian(this).equals("上报用户")) {
            gv_share_photo.setVisibility(View.GONE);
            btnalter.setVisibility(View.GONE);
            gv_share_video.setVisibility(View.GONE);
            add_file.setVisibility(View.GONE);
            tv_photo.setVisibility(View.GONE);
            tv_video.setVisibility(View.GONE);
        } else {
            btnalter.setVisibility(View.VISIBLE);
        }
        btnalter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
        mWorksCount = (EditText) findViewById(R.id.worksCount);
        mVideoCount = (EditText) findViewById(R.id.videoCount);
        mSculptureCount = (EditText) findViewById(R.id.sculptureCount);
        mDeviceCount = (EditText) findViewById(R.id.deviceCount);
        mOtherDesc = (EditText) findViewById(R.id.otherDesc);
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_plan_info;
    }

    private void report() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(PlanInfoActivity.this);
            progressDialog.setMessage("上传中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
        hu.configSoTimeout(2 * 60 * 1000);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("galleryId", infos.getGalleryId());
        params.addQueryStringParameter("theme", infos.getTheme());
        params.addQueryStringParameter("status", infos.getStatus());
        params.addQueryStringParameter("dateBegin", tvtimes.getText().toString());
        params.addQueryStringParameter("dateEnd", tvtimee.getText().toString());
        params.addQueryStringParameter("careLevel", infos.getCareLevel());
        params.addQueryStringParameter("exhibitionIntroduction", infos.getExhibitionIntroduction());
        params.addQueryStringParameter("remarks", infos.getRemarks());
        params.addQueryStringParameter("authorIntroduction", infos.getAuthorIntroduction());
        params.addQueryStringParameter("author", mZuoZheText.getText().toString());
        params.addQueryStringParameter("manager", etpeople.getText().toString());
        params.addQueryStringParameter("managerIntroduction", tvcezhanreninfo.getText().toString());
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        String workCount = mWorksCount.getText().toString().trim();
        params.addQueryStringParameter("worksCount", workCount.isEmpty() ? "0" : workCount);
        String videoCount = mVideoCount.getText().toString().trim();
        params.addQueryStringParameter("videoCount", videoCount.isEmpty() ? "0" : videoCount);
        String sculptureCount = mSculptureCount.getText().toString().trim();
        params.addQueryStringParameter("sculptureCount", sculptureCount.isEmpty() ? "0" : sculptureCount);
        String deivceCount = mDeviceCount.getText().toString().trim();
        params.addQueryStringParameter("deviceCount", deivceCount.isEmpty() ? "0" : deivceCount);
        params.addQueryStringParameter("otherDesc", mOtherDesc.getText().toString());
        params.addQueryStringParameter("questionRemarks", infos.getQuestionRemarks());
        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
            String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());
            params.addBodyParameter("postsPic" + (i + 1) + "_" + BimpHandler.mPhotoNum.get(i), new File(
                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
            params.addBodyParameter("uploadType" + (i + 1), uploadType);
        }
        for (int i = 0; i < (mUploadVideoList.size()); i++) {
            VideoEntity videoEntity = mUploadVideoList.get(i);
            String filePath = videoEntity.filePath;
            String key;
            if (videoEntity.type == 0) {
                key = "video" + i + ".v";
            } else {
                key = "video" + i + ".f";
            }
            params.addBodyParameter(key, new File(filePath));
        }
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reporthualang, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        btnalter.setEnabled(true);
                        progressDialog.dismiss();
                        ToastUtil.showToast(PlanInfoActivity.this, "上传失败，请重试");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(PlanInfoActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            BimpHandler.tempSelectBitmap.clear();
                            progressDialog.dismiss();
                            //setResult(331);
                            FileUtils.deleteImgCache();
                            FileUtils.deleteCompresses();
                            BimpHandler.mPhotoNum.clear();
                            BimpHandler.haveCompress.clear();

                            mUploadVideoList.clear();
                            if (compressVideo != null)
                                compressVideo.clear();
                            errorVideoID.clear();
                            nomarlVideoID.clear();
                            if (errorCompress != null)
                                errorCompress.clear();
                            FileUtils.deleteCompresses();
                            adapter.notifyDataSetChanged();
                            adapter_video.notifyDataSetChanged();
                            getExhibitionInfo();
//                            PlanInfoActivity.this.finish();
                        } else {
                            Toast.makeText(PlanInfoActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            btnalter.setEnabled(true);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BimpHandler.tempSelectBitmap.clear();
        System.gc();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
            BimpHandler.tempSelectBitmap.clear();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10 || requestCode == 100) && data != null) {
            final String fileName = String.valueOf(System.currentTimeMillis());
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList == null) {
                return;
            }
            final StringBuilder stringBuffer = new StringBuilder();
            for (String path : pathList) {
                if (!BimpHandler.tempAddPhoto.contains(path)) {
                    BimpHandler.tempAddPhoto.add(path);
                }
                stringBuffer.append(path).append("MMMMMMMMMM");
            }

            if (!"".equals(stringBuffer.toString())) {
                String[] splitAblum = stringBuffer.toString().split("MMMMMMMMMM");
                BimpHandler.tempSelectBitmap.clear();
                for (String aSplitAblum : splitAblum) {
                    // 保存到照片列表里
                    // 保存到文件夹
                    // 图片在保存时直接进行压缩
                    //  File file = BitmapUtils.commpressImage(splitAblum[i]);
                    File file2 = BitmapUtils.commpressImage2(aSplitAblum);
                    Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                    CameraImage takePhoto = new CameraImage();
                    takePhoto.setBitmap(bitmap);
                    if (ispixel && isPhote) {
                        if (!BimpHandler.haveCompress.contains(aSplitAblum))
                            BimpHandler.mPhotoNum.add("1");//1为正常图片，0为问题图片
                        takePhoto.setImagePath(file2.getAbsolutePath());
                    } else if (!ispixel && isPhote) {
                        takePhoto.setImagePath(file2.getAbsolutePath());
                        if (!BimpHandler.haveCompress.contains(aSplitAblum))
                            BimpHandler.mPhotoNum.add("1");
                    } else if (ispixel && !isPhote) {
                        takePhoto.setImagePath(file2.getAbsolutePath());
                        if (!BimpHandler.haveCompress.contains(aSplitAblum))
                            BimpHandler.mPhotoNum.add("0");
                    } else if (!ispixel && !isPhote) {
                        takePhoto.setImagePath(file2.getAbsolutePath());
                        if (!BimpHandler.haveCompress.contains(aSplitAblum))
                            BimpHandler.mPhotoNum.add("0");
                    }
                    if (!BimpHandler.haveCompress.contains(aSplitAblum)) {
                        BimpHandler.haveCompress.add(aSplitAblum);
                    }
                    BimpHandler.tempSelectBitmap.add(takePhoto);
                }
                for (String s :
                        BimpHandler.mPhotoNum) {
                    LogUtils.d("mPhotoNum" + s);
                }
                adapter.notifyDataSetChanged();
            }

        }
    }

}
