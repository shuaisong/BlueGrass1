package com.reeching.bluegrass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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
import com.reeching.bean.HechaInfobean.Infos;
import com.reeching.bean.VideoEntity;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 开始核查
 */
public class BeginToHecha extends BaseDetailActivity {
    private TextView tvtheme;
    private TextView tvaddress;
    private TextView tvaire;
    private TextView tvpeople;
    private TextView info_author;
    private TextView tvtimes;
    private TextView tvtimee;
    private TextView tvintroduction;
    private ImageView iv;
    private EditText ethistorydescribe, etdescribe, tvremark;
    private Infos infos;
    private RadioGroup group;
    private String recheckStatus;
    private NetImgGridAdapter adapter2;
    private NoScrollGridView lin, lin_video;
    private ProgressDialog progressDialog;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> list_video = new ArrayList<>();
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
        tvtheme.setText(infos.getExhibition().getTheme());
        tvaddress.setText(infos.getExhibition().getAddress());
        tvaire.setText(String.format("%s平方米", infos.getExhibition().getArea()));
        tvpeople.setText(infos.getExhibition().getManager());
        tvremark.setText(infos.getExhibition().getRemarks());
        String ss = infos.getExhibition().getDateBegin();
        tvtimes.setText(ss);
        String sss = infos.getExhibition().getDateEnd();
        tvtimee.setText(sss);
        if (!"".equals(infos.getExhibition().getPhoto())) {
            list.clear();
            String sourceStr = infos.getExhibition().getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        adapter2.notifyDataSetChanged();
        if (infos.getExhibition().getVideoUrl() != null && !"".equals(infos.getExhibition().getVideoUrl())) {
            String sourceStr = infos.getExhibition().getVideoUrl();
            list_video.clear();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list_video.add(sourceStrArray[ii]);
            }
        }
        mAdapter_video.notifyDataSetChanged();
        if (infos.getExhibition().getCareLevel().equals("0")) {
            iv.setImageResource(R.drawable.green);
        } else if (infos.getExhibition().getCareLevel().equals("1")) {
            iv.setImageResource(R.drawable.yellow);
        } else {
            iv.setImageResource(R.drawable.red);
        }
        String author = infos.getExhibition().getAuthor();
        info_author.setText(author);
        if (infos.getExhibition().getIsFocus() == 1) {
            String focusName = infos.getExhibition().getFocusName();
            SpannableString spannableString = new SpannableString(author);
            spannableString.setSpan(new BackgroundColorSpan(Color.parseColor("#E85643")), author.indexOf(focusName), author.indexOf(focusName) + focusName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            info_author.setText(spannableString);
        } else info_author.setText(author);

        tvintroduction.setText(infos.getExhibition()
                .getExhibitionIntroduction());

        if (null != infos.getCheckInfo()) {
            ethistorydescribe.setVisibility(View.VISIBLE);
            ethistorydescribe.setText(infos.getCheckInfo()
                    .getCheckDescription());
        }
        if (infos.getExhibition().getVideoCount() != null)
            mVideoCount.setText(infos.getExhibition().getVideoCount().trim().equals("0") ? "" : infos.getExhibition().getVideoCount());
        if (infos.getExhibition().getSculptureCount() != null)
            mSculptureCount.setText(infos.getExhibition().getSculptureCount().trim().equals("0") ? "" : infos.getExhibition().getSculptureCount());
        if (infos.getExhibition().getDeviceCount() != null)
            mDeviceCount.setText(infos.getExhibition().getDeviceCount().trim().equals("0") ? "" : infos.getExhibition().getDeviceCount());
        if (infos.getExhibition().getWorksCount() != null)
            mWorksCount.setText(infos.getExhibition().getWorksCount().trim().equals("0") ? "" : infos.getExhibition().getWorksCount());
        mOtherDesc.setText(infos.getExhibition().getOtherDesc());
    }

    @Override
    protected void initView() {
        super.initView();
        tvtheme = findViewById(R.id.begintohecha_theme);
        tvaddress = findViewById(R.id.begintohecha_address);
        tvaire = findViewById(R.id.begintohecha_aire);
        tvpeople = findViewById(R.id.begintohecha_people);
        info_author = findViewById(R.id.info_author);
        tvtimes = findViewById(R.id.begintohecha_times);
        tvtimee = findViewById(R.id.begintohecha_timee);
        tvremark = findViewById(R.id.begintohecha_remark);
        tvintroduction = findViewById(R.id.begintohecha_introduction);
        iv = findViewById(R.id.begintohecha_iv);
        ethistorydescribe = findViewById(R.id.begintohecha_history_describe);
        etdescribe = findViewById(R.id.begintohecha_describe);
        Button btn = findViewById(R.id.begintohecha_btn);
        infos = (Infos) getIntent().getSerializableExtra("info");
        lin = findViewById(R.id.begintohecha_lin);
        lin_video = findViewById(R.id.info_lin_video);
        group = findViewById(R.id.begintohecha_rgp);
        TextView comeback = findViewById(R.id.comeback);

        etdescribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 解决scrollView中嵌套EditText导致其不能上下滑动的问题
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        comeback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BeginToHecha.this.finish();
            }
        });
        ethistorydescribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 解决scrollView中嵌套EditText导致其不能上下滑动的问题
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        initrgp();
        mWorksCount = findViewById(R.id.worksCount);
        mVideoCount = findViewById(R.id.videoCount);
        mSculptureCount = findViewById(R.id.sculptureCount);
        mDeviceCount = findViewById(R.id.deviceCount);
        mOtherDesc = findViewById(R.id.otherDesc);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
        Addimageview();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_begin_to_hecha;
    }

    private void Addimageview() {
        mAdapter_video = new NetVideoGridAdapter(this, list_video);
        lin_video.setAdapter(mAdapter_video);
        adapter2 = new NetImgGridAdapter(BeginToHecha.this, list);
        lin.setAdapter(adapter2);
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(BeginToHecha.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    //   Toast.makeText(BeginToHecha.this,"加载中，请稍后。。。",Toast.LENGTH_SHORT).show();
                    adapter2.notifyDataSetChanged();
                }
            }
        });
    }

    private void report() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BeginToHecha.this);
            progressDialog.setMessage("上传中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
        hu.configSoTimeout(2 * 60 * 1000);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("exhibitionId", infos.getExhibition()
                .getId());
        params.addQueryStringParameter("createBy", BaseApplication.getInstance()
                .getId());
        params.addQueryStringParameter("checkInfoId", null == infos
                .getCheckInfo() ? "" : infos.getCheckInfo().getId());
       /* if (BimpHandler.mPhotoNum.contains("0") || errorVideoID.size() > 0) {
            recheckStatus = "0";
        }*/
        params.addQueryStringParameter("recheckStatus", recheckStatus);
        params.addQueryStringParameter("recheckDescription", etdescribe
                .getText().toString());
        params.addQueryStringParameter("remarks", tvremark
                .getText().toString());
        String workCount = mWorksCount.getText().toString().trim();
        params.addQueryStringParameter("worksCount", workCount.isEmpty() ? "0" : workCount);
        String videoCount = mVideoCount.getText().toString().trim();
        params.addQueryStringParameter("videoCount", videoCount.isEmpty() ? "0" : videoCount);
        String sculptureCount = mSculptureCount.getText().toString().trim();
        params.addQueryStringParameter("sculptureCount", sculptureCount.isEmpty() ? "0" : sculptureCount);
        String deivceCount = mDeviceCount.getText().toString().trim();
        params.addQueryStringParameter("deviceCount", deivceCount.isEmpty() ? "0" : deivceCount);
        params.addQueryStringParameter("otherDesc", mOtherDesc.getText().toString());
        params.addQueryStringParameter("questionRemarks", infos.getExhibition().getQuestionRemarks());
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
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reportwaitforhecha,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject = JSONObject
                                .parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(BeginToHecha.this, "保存成功！", Toast.LENGTH_SHORT)
                                    .show();
                            BimpHandler.tempSelectBitmap.clear();
                            progressDialog.dismiss();
                            setResult(331);
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
                            BeginToHecha.this.finish();
                        } else {
                            Toast.makeText(BeginToHecha.this, "保存失败！", Toast.LENGTH_SHORT)
                                    .show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private void initrgp() {
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.begintohecha_rbt1:
                        recheckStatus = 0 + "";
                        break;

                    case R.id.begintohecha_rbt2:
                        recheckStatus = 1 + "";
                        break;
                }
            }
        });
        group.check(R.id.begintohecha_rbt1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10 || requestCode == 100) {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
            BimpHandler.tempSelectBitmap.clear();
        }
        return super.onKeyDown(keyCode, event);
    }
}
