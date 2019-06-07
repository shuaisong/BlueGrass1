package com.reeching.bluegrass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.reeching.BaseApplication;
import com.reeching.activity.VideoGridActivity;
import com.reeching.adapter.GlideLoader;
import com.reeching.adapter.ImgGridAdapter;
import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.adapter.NetVideoGridAdapter;
import com.reeching.adapter.VideoGridAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.bean.ExhibitionInfo;
import com.reeching.bean.VideoEntity;
import com.reeching.listener.ImgItemClickListener;
import com.reeching.listener.VideoItemClickListener;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.DialogUtils;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.JsonCallback;
import com.reeching.utils.ToastUtil;
import com.reeching.utils.Utils;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE;
import static android.text.Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL;

//问题展览详情页
public class HaveVerificationInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ExhibitionBean.InfosBean infos;
    private TextView tvtheme;
    private TextView tvpeople;
    private TextView tvtimes;
    private TextView tvtimee;
    private TextView tvauthorinfo;
    private TextView tvauthor;
    private EditText questremark;
    private ImageView ivlevel;
    private NoScrollGridView lin, lin_video;
    private ArrayList<String> list = new ArrayList<String>();
    private NetImgGridAdapter adapter2;
    private TextView mInfoHuaLang;
    private TextView show_quantity;
    private Button btn;
    private ArrayList<String> list_video = new ArrayList<>();
    private TextView mWorksCount;
    private TextView mVideoCount;
    private TextView mSculptureCount;
    private TextView mDeviceCount;
    private TextView mOtherDesc;
    private AlertDialog mDialog;
    private PopupWindow pop;
    private LinearLayout ll_popup;
    private Dialog mCameraDialog;
    private RadioGroup mGroupSHD;
    private boolean ispixel;
    private ImgGridAdapter adapter;
    private VideoGridAdapter adapter_video;
    protected ArrayList<VideoEntity> mUploadVideoList = new ArrayList<>(3);
    protected ArrayList<Integer> checkedIds = new ArrayList<>(3);
    protected ArrayList<String> pathPhoto = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private ArrayList<VideoEntity> needCompress;
    private ArrayList<VideoEntity> compressVideo;
    private int mSize;
    protected static ArrayList<Integer> errorVideoID = new ArrayList<>();
    private ArrayList<VideoEntity> errorCompress;
    private AlertDialog mAlertDialog;
    private ProgressDialog progressDialog;
    private NetVideoGridAdapter mNet_adapter_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getActivityLayout());
        EventBus.getDefault().register(this);
        BimpHandler.tempSelectBitmap.clear();
        BimpHandler.haveCompress.clear();
        initView();
        initPopupWindow();
        initdata();
    }

    @Override
    public void finish() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.finish();
    }

    protected void initdata() {
        show_quantity.setText(infos.getRemarks());
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        tvauthor.setText(infos.getAuthor());
        //questremark.setText(infos.getRemarks());
        String dateBegin = infos.getDateBegin();
        mInfoHuaLang.setText(infos.getGalleryName());
        tvtimes.setText(dateBegin);
        String questionRemarks = infos.getQuestionRemarks();
        if (questionRemarks != null) {
            Spanned spanned;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spanned = Html.fromHtml(questionRemarks, Html.FROM_HTML_MODE_LEGACY);
            } else {
//            String replace = "测试啊本月已成功邀请 <strong><font color=\"#FF0000\">" + 100 + "</font><strong>人";
                String replace;
                if (questionRemarks.contains("style=\"color:")) {
                    replace = questionRemarks.replace("style=\"color:", "color=\"");
                    replace = replace.replace(";", "");
                    replace = replace.replaceAll("span", "font");
                } else replace = questionRemarks;
//            String replace = "2017年9月8日对该画廊作品审核时发现作品名为（十个月光）的作品，<strong><font color=\"#FF0000\">内容暴力</span></strong>";
                spanned = Html.fromHtml(replace);
            }
            questremark.setText(spanned);
        } else questremark.setText("");


        tvtimee.setText(infos.getDateEnd());
        //  }
        tvauthorinfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
            ivlevel.setImageResource(R.drawable.red);
        }
        if (!"".equals(infos.getPhotoFalse())) {
            String sourceStr = infos.getPhotoFalse();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        if (infos.getVideoUrlfalse() != null && !"".equals(infos.getVideoUrlfalse())) {
            String sourceStr = infos.getVideoUrlfalse();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list_video.add(sourceStrArray[ii]);
            }
        }
        adapter2.notifyDataSetChanged();
        mNet_adapter_video.notifyDataSetChanged();
        mVideoCount.setText(infos.getVideoCount());
        mSculptureCount.setText(infos.getSculptureCount());
        mDeviceCount.setText(infos.getDeviceCount());
        mWorksCount.setText(infos.getWorksCount());
        mOtherDesc.setText(infos.getOtherDesc());
    }

    protected void initView() {
        infos = (ExhibitionBean.InfosBean) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.haveverificationinfo_lin);
        lin_video = (NoScrollGridView) findViewById(R.id.info_lin_video);

        adapter2 = new NetImgGridAdapter(HaveVerificationInfoActivity.this, list);
        lin.setAdapter(adapter2);
        mNet_adapter_video = new NetVideoGridAdapter(this, list_video);
        lin_video.setAdapter(mNet_adapter_video);

        tvtheme = (TextView) findViewById(R.id.haveverificationinfo_theme);
        tvpeople = (TextView) findViewById(R.id.haveverificationinfo_people);
        btn = (Button) findViewById(R.id.haveverificationinfo_xiada);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        questremark = (EditText) findViewById(R.id.haveverificationinfo_remark);
        tvauthor = (TextView) findViewById(R.id.haveverificationinfo_author);
        tvtimee = (TextView) findViewById(R.id.haveverificationinfo_timee);
        tvtimes = (TextView) findViewById(R.id.haveverificationinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.haveverificationinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.haveverificationinfo_level);
        mInfoHuaLang = (TextView) findViewById(R.id.havereportedinfo_hualang);
        show_quantity = (TextView) findViewById(R.id.show_quantity);
        RadioGroup color_check = findViewById(R.id.color_check);
        CheckBox bold_check = findViewById(R.id.bold_check);
        questremark.getText().setSpan(new UnderlineSpan(), 0, questremark.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        bold_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    questremark.getText().setSpan(new StyleSpan(Typeface.BOLD),
                            questremark.getSelectionStart(), questremark.getSelectionEnd()
                            , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                else {
                    StyleSpan styleSpans[] = questremark.getText().
                            getSpans(questremark.getSelectionStart(), questremark.getSelectionEnd(), StyleSpan.class);
                    for (StyleSpan s :
                            styleSpans) {
                        if (s.getStyle() == Typeface.BOLD) {
                            questremark.getText().removeSpan(s);
                        }
                    }
                }
            }
        });
        color_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ForegroundColorSpan foregroundColorSpan;
                switch (checkedId) {
                    case R.id.black_check:
                        foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                        break;
                    case R.id.red_check:
                        foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                        break;
                    case R.id.yellow_check:
                        foregroundColorSpan = new ForegroundColorSpan(Color.YELLOW);
                        break;
                    case R.id.blue_check:
                        foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
                        break;
                    default:
                        foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                        break;
                }
                questremark.getText().setSpan(foregroundColorSpan,
                        questremark.getSelectionStart(), questremark.getSelectionEnd()
                        , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        });
        questremark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LogUtils.d(Html.toHtml(s, TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                } else {
                    LogUtils.d(Html.toHtml(s));
                }
            }
        });
        mCameraDialog = DialogUtils.showPromptDailog(this,
                LayoutInflater.from(this), R.layout.cameradialog);
        mGroupSHD = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_HSD);
        RadioGroup mGroupDismissTrue = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_dismisstrue);
        mGroupDismissTrue.setVisibility(View.GONE);
        mCameraDialog.findViewById(R.id.photo_type).setVisibility(View.GONE);
        mCameraDialog.findViewById(R.id.radiobutton_dismiss).setOnClickListener(this);
        mCameraDialog.findViewById(R.id.radiobutton_true).setOnClickListener(this);

        NoScrollGridView gv_share_photo = (NoScrollGridView) findViewById(R.id.share_photo);
        NoScrollGridView gv_share_video = (NoScrollGridView) findViewById(R.id.share_video);
        adapter = new ImgGridAdapter(this);
        adapter_video = new VideoGridAdapter(this, mUploadVideoList);
        gv_share_photo.setAdapter(adapter);
        gv_share_video.setAdapter(adapter_video);
        gv_share_photo.setOnItemLongClickListener(this);
        gv_share_video.setOnItemLongClickListener(this);
        gv_share_video.setOnItemClickListener(new VideoItemClickListener(this));
        gv_share_photo.setOnItemClickListener(new ImgItemClickListener(this));
        findViewById(R.id.add_file).setOnClickListener(this);
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(HaveVerificationInfoActivity.this,
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

        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HaveVerificationInfoActivity.this.finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
        mWorksCount = (TextView) findViewById(R.id.worksCount);
        mVideoCount = (TextView) findViewById(R.id.videoCount);
        mSculptureCount = (TextView) findViewById(R.id.sculptureCount);
        mDeviceCount = (TextView) findViewById(R.id.deviceCount);
        mOtherDesc = (TextView) findViewById(R.id.otherDesc);
    }

    protected int getActivityLayout() {
        return R.layout.activity_have_verification_info;
    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                this, R.anim.activity_translate_in));
        pop.showAtLocation(this.findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
    }

    private void initPopupWindow() {
        pop = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        // 选择相册
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ispixel) {
                    getPhoto(100);
                } else {
                    getPhoto(10);
                }
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    protected void getPhoto(int num) {
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

        ImageSelector.open(this, imageConfig);   // 开启图片选择器
    }

    private void report() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(HaveVerificationInfoActivity.this);
            progressDialog.setMessage("上传中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", infos.getId());
        params.addQueryStringParameter("galleryId", infos.getGalleryId());
        params.addQueryStringParameter("theme", infos.getTheme());
        params.addQueryStringParameter("status", infos.getStatus());
        params.addQueryStringParameter("dateBegin", tvtimes.getText().toString());
        params.addQueryStringParameter("dateEnd", tvtimee.getText().toString());
        params.addQueryStringParameter("careLevel", infos.getCareLevel());
        params.addQueryStringParameter("exhibitionIntroduction", infos.getExhibitionIntroduction());
        params.addQueryStringParameter("remarks", infos.getRemarks());
        params.addQueryStringParameter("authorIntroduction", infos.getAuthorIntroduction());
        params.addQueryStringParameter("author", infos.getAuthor());
        params.addQueryStringParameter("manager", infos.getManager());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            params.addQueryStringParameter("questionRemarks", Html.toHtml(questremark.getText(), TO_HTML_PARAGRAPH_LINES_CONSECUTIVE));
        } else {
            params.addQueryStringParameter("questionRemarks", Html.toHtml(questremark.getText()).trim());
        }
        params.addQueryStringParameter("managerIntroduction", infos.getManagerIntroduction());
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        params.addQueryStringParameter("worksCount", mWorksCount.getText().toString());
        params.addQueryStringParameter("videoCount", mVideoCount.getText().toString());
        params.addQueryStringParameter("sculptureCount", mSculptureCount.getText().toString());
        params.addQueryStringParameter("deviceCount", mDeviceCount.getText().toString());
        params.addQueryStringParameter("otherDesc", mOtherDesc.getText().toString());
        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
          /*  String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());*/
            params.addBodyParameter("postsPic" + (i + 1) + "_" + 0, new File(
                    imagePath));
//            params.addBodyParameter("uploadType" + (i + 1), uploadType);
        }
        for (int i = 0; i < (mUploadVideoList.size()); i++) {
            String filePath = mUploadVideoList.get(i).filePath;
            params.addBodyParameter("video" + i + ".f", new File(filePath));
        }
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reporthualang, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        btn.setEnabled(true);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(HaveVerificationInfoActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            //setResult(331);
                            progressDialog.dismiss();
                            BimpHandler.tempSelectBitmap.clear();
                            FileUtils.deleteImgCache();
                            FileUtils.deleteCompresses();
                            BimpHandler.haveCompress.clear();
                            mUploadVideoList.clear();
                            if (compressVideo != null)
                                compressVideo.clear();
                            errorVideoID.clear();
                            if (errorCompress != null)
                                errorCompress.clear();
                            adapter.notifyDataSetChanged();
                            adapter_video.notifyDataSetChanged();
                            getExhibitionInfo();
//                            HaveVerificationInfoActivity.this.finish();
                        } else {
                            Toast.makeText(HaveVerificationInfoActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                            btn.setEnabled(true);
                        }
                    }
                });

    }

    private void getExhibitionInfo() {
        OkGo.<ExhibitionInfo>post(HttpApi.ip + HttpApi.getExhibitionInfo).params("id", infos.getId())
                .execute(new JsonCallback<ExhibitionInfo>(ExhibitionInfo.class) {
                    @Override
                    public void onSuccess(Response<ExhibitionInfo> response) {
                        if (response != null && response.body() != null) {
                            if ("1".equals(response.body().getResult())) {
                                infos = response.body().getInfos();
                                list.clear();
                                list_video.clear();
                                initdata();
                            } else {
                                ToastUtil.showToast(HaveVerificationInfoActivity.this, response.body().getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ExhibitionInfo> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radiobutton_true:
                if (mGroupSHD.getCheckedRadioButtonId() == R.id.radiobutton_one) {
                    ispixel = true;
                } else if (mGroupSHD.getCheckedRadioButtonId() == R.id.radiobutton_two) {
                    ispixel = false;
                } else {
                    ToastUtil.showToast(this, "请选择图片清晰度");
                    return;
                }
                mCameraDialog.dismiss();
                openCameraPopupWindow();
                break;
            case R.id.radiobutton_dismiss:
                mCameraDialog.dismiss();
                break;
            case R.id.add_file:
                if (mDialog == null) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(HaveVerificationInfoActivity.this);
                    mBuilder.setTitle("文件类型选择：").setItems(new String[]{"图片", "视频"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    mDialog.dismiss();
                                    mCameraDialog.show();
                                    break;
                                case 1:
                                    mDialog.dismiss();
                                    Intent intent = new Intent(HaveVerificationInfoActivity.this, VideoGridActivity.class);
                                    checkedIds.clear();
                                    for (VideoEntity video :
                                            mUploadVideoList) {
                                        checkedIds.add(video.ID);
                                    }
                                    intent.putExtra("checked", checkedIds);
                                    startActivityForResult(intent, 1001);
                                    break;
                            }
                        }
                    });
                    mBuilder.setCancelable(true);
                    mDialog = mBuilder.show();
                }
                mDialog.show();
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        if (ispixel) {
                            takePhoto.setImagePath(file2.getAbsolutePath());
                        } else if (!ispixel) {
                            takePhoto.setImagePath(file2.getAbsolutePath());
                        }
                        if (!BimpHandler.haveCompress.contains(aSplitAblum)) {
                            BimpHandler.haveCompress.add(aSplitAblum);
                        }
                        BimpHandler.tempSelectBitmap.add(takePhoto);
                    }
                    adapter.notifyDataSetChanged();
                }
            } else if (requestCode == 1001) {
//            mUploadVideoList.clear();
                ArrayList<VideoEntity> checkedVideos = (ArrayList<VideoEntity>) data.getSerializableExtra("checked");
                LogUtils.d(checkedVideos.size() + "checkedVideos");
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
                if (compressVideo != null) compressVideo.clear();
                else compressVideo = new ArrayList<>(3);
                if (needCompress == null) needCompress = new ArrayList<>(3);
                else needCompress.clear();
                for (VideoEntity video :
                        checkedVideos) {
                    if (!video.compress) {
                        if (Utils.ReadVideoSize(video.filePath) > 5 * 1024 * 1024)
                            needCompress.add(video);
                        else {
                            video.compress = true;
                            compressVideo.add(video);
                        }
                        video.type = 1;
                        errorVideoID.add(video.ID);
                    } else {
                        String path = FileUtils.getFilePath(this, "video" + File.separator +
                                "compress") + video.filePath.substring(video.filePath.lastIndexOf(File.separator));
                        if (new File(path).exists()) {
                            video.filePath = path;
                        }
                        video.size = Utils.ReadVideoSize(video.filePath);
                        if (errorVideoID.contains(video.ID)) {
                            video.type = 1;
                        }
                        compressVideo.add(video);
                    }
                }
                if (needCompress.size() > 0) {
                    mSize = needCompress.size();
                    mProgressDialog.setMessage("压缩进度0/" + mSize);
                    mProgressDialog.show();
                    FileUtils.compressVideos(this, needCompress);
                } else {
                    mUploadVideoList.clear();
                    mUploadVideoList.addAll(checkedVideos);
                    adapter_video.notifyDataSetChanged();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VideoEntity event) {
        LogUtils.d(event.filePath);
        if (errorCompress == null) {
            errorCompress = new ArrayList<>(3);
        }
        if (!event.compress) {
            errorCompress.add(event);
        } else {
            if (!compressVideo.contains(event))
                compressVideo.add(event);
            mProgressDialog.setMessage("压缩进度" + compressVideo.size() + "/" + mSize);
        }

        if (compressVideo.size() + errorCompress.size() >= mSize) {
            mUploadVideoList.clear();
            mUploadVideoList.addAll(compressVideo);
            mProgressDialog.dismiss();
            adapter_video.notifyDataSetChanged();
            if (errorCompress != null && errorCompress.size() != 0) {
                if (mAlertDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("警告：")
                            .setCancelable(true)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    needCompress.clear();
                                    needCompress.addAll(errorCompress);
                                    compressVideo.clear();
                                    errorCompress.clear();
                                    mAlertDialog.dismiss();
                                    mSize = needCompress.size();
                                    mProgressDialog.setMessage("压缩进度0/" + mSize);
                                    mProgressDialog.show();
                                    FileUtils.compressVideos(HaveVerificationInfoActivity.this, needCompress);
                                }
                            }).setItems(new String[]{"存在视频压缩失败，请点击确定重新压缩"}, null);
                    mAlertDialog = builder.create();
                }
                mAlertDialog.show();
            }

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.share_photo:
                BimpHandler.tempSelectBitmap.remove(position);
                pathPhoto.remove(position);
                BimpHandler.haveCompress.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.share_video:
                mUploadVideoList.remove(position);
                adapter_video.notifyDataSetChanged();
                return true;
        }
        return false;
    }
}
