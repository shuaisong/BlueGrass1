package com.reeching.bluegrass;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
import com.reeching.adapter.GlideLoader;
import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.adapter.NetVideoGridAdapter;
import com.reeching.bean.VideoEntity;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BeginToCheck extends BaseDetailActivity {
    private RadioGroup rgp;
    private LinearLayout lin;
    private String checkStatus;
    private TextView tvtheme, tvlocation, tvaire, tvpeople, info_author, tvtimes, tvtimee,
            tvintroduction, tvtimestart, tvtimeend, comeback;
    private ImageView ivlevel;
    private EditText etdescrible, tvremark;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private int myear, mmonth, mday;
    private boolean timeflag = false;
    private DatePickerDialog dpdialog;
    private ProgressDialog progressDialog;
    private ArrayList<String> list = new ArrayList<>();
    private NetImgGridAdapter adapter2;
    private ArrayList<String> list_video = new ArrayList<>();
    private NoScrollGridView lin_video, lin_photo;
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

    @Override
    protected void initView() {
        super.initView();
        tvremark = findViewById(R.id.begintocheck_remark);
        Button btn = findViewById(R.id.begintocheck_btn);
        rgp = findViewById(R.id.begintocheck_rgp);
        comeback = findViewById(R.id.comeback);
        lin = (LinearLayout) findViewById(R.id.begintocheck_timelin);
        lin_photo = findViewById(R.id.begintocheck_lin);
        lin_video = findViewById(R.id.info_lin_video);
        tvtheme = findViewById(R.id.begintocheck_theme);
        tvlocation = findViewById(R.id.begintocheck_location);
        tvaire = findViewById(R.id.begintocheck_aire);
        tvpeople = findViewById(R.id.begintocheck_people);
        info_author = findViewById(R.id.info_author);
        tvtimes = findViewById(R.id.begintocheck_times);
        tvtimee = findViewById(R.id.begintocheck_timee);
        tvintroduction = findViewById(R.id.begintocheck_introduction);
        tvtimestart = findViewById(R.id.begintocheck_timestart);
        tvtimeend = findViewById(R.id.begintocheck_timeend);
        ivlevel = findViewById(R.id.begintocheck_level);
        etdescrible = findViewById(R.id.begintocheck_describe);
        tvtimestart = findViewById(R.id.begintocheck_timestart);
        tvtimeend = findViewById(R.id.begintocheck_timeend);

        inittimestart();
        inittimeend();
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BeginToCheck.this.finish();
            }
        });
        etdescrible.setOnTouchListener(new View.OnTouchListener() {
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
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkStatus.equals("1")) {
                    if (tvtimestart.getText().toString().equals("")
                            || tvtimeend.getText().toString().equals("")) {
                        Toast.makeText(BeginToCheck.this, "请选择核查时间！", Toast.LENGTH_SHORT).show();
                    } else {
                        initreport();
                    }
                } else {
                    initreport();
                }
            }
        });
        rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.begintocheck_rb1:
                        lin.setVisibility(View.GONE);
                        checkStatus = 0 + "";
                        break;

                    case R.id.begintocheck_rb2:
                        lin.setVisibility(View.VISIBLE);
                        checkStatus = 1 + "";
                        break;
                }

            }
        });
        rgp.check(R.id.begintocheck_rb1);
        mWorksCount = findViewById(R.id.worksCount);
        mVideoCount = findViewById(R.id.videoCount);
        mSculptureCount = findViewById(R.id.sculptureCount);
        mDeviceCount = findViewById(R.id.deviceCount);
        mOtherDesc = findViewById(R.id.otherDesc);
        Addimageview();

    }

    private void initreport() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BeginToCheck.this);
            progressDialog.setMessage("上传中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("exhibitionId", infos.getId());
        // params.addQueryStringParameter("checkPhoto", "");
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
        params.addQueryStringParameter("checkStatus", checkStatus);
        params.addQueryStringParameter("checkBegin", tvtimestart.getText()
                .toString());
        params.addQueryStringParameter("checkEnd", tvtimeend.getText()
                .toString());
        params.addQueryStringParameter("checkDescription", etdescrible
                .getText().toString());
        params.addQueryStringParameter("remarks", tvremark
                .getText().toString());
        params.addQueryStringParameter("questionRemarks", infos.getQuestionRemarks());
        params.addQueryStringParameter("createBy", BaseApplication.getInstance()
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
        // params.addQueryStringParameter("questionRemarks", infos.getQuestionRemarks());
/*
        params.addQueryStringParameter("questionRemarks", BaseApplication.getInstance()
                .getId());
*/
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reportwaitforcheck,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(BeginToCheck.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(BeginToCheck.this, "保存成功！", Toast.LENGTH_SHORT)
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
//                            adapter.notifyDataSetChanged();
//                            adapter_video.notifyDataSetChanged();
//                            getExhibitionInfo();
                            BeginToCheck.this.finish();
                        } else {
                            Toast.makeText(BeginToCheck.this, "保存失败！", Toast.LENGTH_SHORT)
                                    .show();
                            progressDialog.dismiss();
                        }
                    }
                });

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

    protected void initdata() {
        tvtheme.setText(null == infos.getTheme() ? "" : infos.getTheme());
        tvlocation
                .setText(null == infos.getAddress() ? "" : infos.getAddress());
        tvpeople.setText(null == infos.getManager() ? "" : infos.getManager());
        tvaire.setText(null == infos.getArea() ? "0" : infos.getArea() + "平方米");
        tvtimes.setText(null == infos.getDateBegin() ? "" : infos
                .getDateBegin());
        tvremark.setText(infos.getRemarks());
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
        tvtimee.setText(null == infos.getDateEnd() ? "" : infos.getDateEnd());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);

        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
            ivlevel.setImageResource(R.drawable.red);
        }
        info_author.setText(null == infos.getAuthor() ? "" : infos.getAuthor());
        tvintroduction.setText(null == infos.getExhibitionIntroduction() ? ""
                : infos.getExhibitionIntroduction());
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

    private void Addimageview() {
        mAdapter_video = new NetVideoGridAdapter(this, list_video);
        lin_video.setAdapter(mAdapter_video);
        adapter2 = new NetImgGridAdapter(BeginToCheck.this, list);
        lin_photo.setAdapter(adapter2);
        lin_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(BeginToCheck.this,
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

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_begin_to_check;
    }

    private void inittimestart() {

        tvtimestart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year1 = calendar.get(Calendar.YEAR);
                int month1 = calendar.get(Calendar.MONTH);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                dpdialog = new DatePickerDialog(BeginToCheck.this,

                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker arg0, int year,
                                                  int month, int day) {
                                // setTitle(year + "年" + (month + 1) + "月" + day
                                // + "日");
                                if (month < 9) {
                                    tvtimestart.setText(year + "-" + "0"
                                            + (month + 1) + "-" + day);
                                } else {
                                    tvtimestart.setText(year + "-"
                                            + (month + 1) + "-" + day);
                                }
                                myear = year;
                                mmonth = month;
                                mday = day;
                                timeflag = true;
                            }
                        }, year1, month1, day1);

                dpdialog.show();
            }
        });

    }


    private void inittimeend() {

        tvtimeend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (timeflag == false) {

                    Toast.makeText(BeginToCheck.this, "请先设置开始时间",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    int year2 = calendar.get(Calendar.YEAR);
                    int month2 = calendar.get(Calendar.MONTH);
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    dpdialog = new DatePickerDialog(BeginToCheck.this,

                            new OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker arg0,
                                                      int year, int month, int day) {

                                    // setTitle(year + "年" + (month + 1) + "月" +
                                    // day + "日");
                                    if (myear < year) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                    } else if (myear == year && mmonth < month) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday < day) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                        tvtimeend.setText(year + "-"
                                                + (month + 1) + "-" + day);
                                    } else if (myear == year && mmonth == month
                                            && mday == day) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday > day) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + mday);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + mday);
                                        }
                                    } else if (myear == year && mmonth > month) {
                                        if (month < 9) {
                                            tvtimeend
                                                    .setText(myear + "-" + "0"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                        } else {
                                            tvtimeend
                                                    .setText(myear + "-"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                        }
                                    } else if (myear > year) {
                                        if (month < 9) {
                                            tvtimeend
                                                    .setText(myear + "-" + "0"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                        } else {
                                            if (mmonth < 9) {
                                                tvtimeend.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + mday);
                                            } else {
                                                tvtimeend.setText(myear + "-"
                                                        + (mmonth + 1) + "-"
                                                        + mday);
                                            }
                                        }
                                    }
                                }
                            }, year2, month2, day2);

                    dpdialog.show();
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
            BimpHandler.tempSelectBitmap.clear();
        }
        return super.onKeyDown(keyCode, event);

    }

    public void getPhoto(int num) {
        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
//                        .crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(9)
                // 已选择的图片路径
                .pathList(pathPhoto)
                // 拍照后存放的图片路径（默认 /temp/picture）
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(num)
                .build();
        ImageSelector.open(BeginToCheck.this, imageConfig);   // 开启图片选择器
    }
}
