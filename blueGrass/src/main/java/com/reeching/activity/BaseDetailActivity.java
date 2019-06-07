package com.reeching.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.reeching.adapter.GlideLoader;
import com.reeching.adapter.ImgGridAdapter;
import com.reeching.adapter.VideoGridAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.bean.ExhibitionInfo;
import com.reeching.bean.VideoEntity;
import com.reeching.bluegrass.NoScrollGridView;
import com.reeching.bluegrass.R;
import com.reeching.listener.Add_Button_Listener;
import com.reeching.listener.ImgItemClickListener;
import com.reeching.listener.VideoItemClickListener;
import com.reeching.utils.DialogUtils;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.JsonCallback;
import com.reeching.utils.ToastUtil;
import com.reeching.utils.Utils;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/11/5.
 * auther:lenovo
 * Date：2018/11/5
 */
public abstract class BaseDetailActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    protected LinearLayout ll_popup;
    protected PopupWindow pop;
    protected Boolean ispixel = false, isPhote = true;
    protected ArrayList<String> pathPhoto = new ArrayList<>();
    protected ImgGridAdapter adapter;
    protected VideoGridAdapter adapter_video;
    protected NoScrollGridView gv_share_photo, gv_share_video;
    protected ArrayList<VideoEntity> mUploadVideoList = new ArrayList<>(3);
    protected ArrayList<Integer> checkedIds = new ArrayList<>(3);
    protected ProgressDialog mProgressDialog;
    protected ArrayList<VideoEntity> compressVideo;
    protected RadioGroup mGroupSHD;
    protected RadioGroup mGroupDismissTrue;
    protected Dialog mCameraDialog;
    protected TextView mButtonTrue, mButtonDismiss;
    protected ImageView add_file;
    private AlertDialog mAlertDialog;
    private Dialog mVideoDialog;
    private RadioGroup mRadioGroup_video;
    protected String id;
    protected ExhibitionBean.InfosBean infos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getActivityLayout());
        BimpHandler.tempSelectBitmap.clear();
        BimpHandler.mPhotoNum.clear();
        BimpHandler.haveCompress.clear();
        id = getIntent().getStringExtra("id");
        initView();
        initListView();
        if (id != null && !id.isEmpty()) {
            getExhibitionInfo();
        } else initdata();
    }

    protected void getExhibitionInfo() {
        OkGo.<ExhibitionInfo>post(HttpApi.ip + HttpApi.getExhibitionInfo).params("id", id)
                .execute(new JsonCallback<ExhibitionInfo>(ExhibitionInfo.class) {
                    @Override
                    public void onSuccess(Response<ExhibitionInfo> response) {
                        if (response != null && response.body() != null) {
                            if ("1".equals(response.body().getResult())) {
                                infos = response.body().getInfos();
                                initdata();
                            } else {
                                ToastUtil.showToast(BaseDetailActivity.this, response.body().getMsg());
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void finish() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.finish();
    }

    private void initVideoDialog() {
        mVideoDialog = DialogUtils.showPromptDailog(this,
                LayoutInflater.from(this), R.layout.vido_type_select);
        mRadioGroup_video = mVideoDialog.findViewById(R.id.radioGroup_video);
        TextView video_cancel = mVideoDialog.findViewById(R.id.video_cancel);
        TextView video_sure = mVideoDialog.findViewById(R.id.video_sure);
        video_sure.setOnClickListener(this);
        video_cancel.setOnClickListener(this);
    }

    private void initAddFile() {
        add_file = findViewById(R.id.add_file);
        add_file.setOnClickListener(
                new Add_Button_Listener(this, mCameraDialog, mVideoDialog));
    }

    protected void initCameraDialog() {
        mCameraDialog = DialogUtils.showPromptDailog(this,
                LayoutInflater.from(this), R.layout.cameradialog);
        mGroupSHD = mCameraDialog.findViewById(R.id.radiogroup_HSD);
        mGroupDismissTrue = mCameraDialog.findViewById(R.id.radiogroup_dismisstrue);
        mButtonDismiss = mCameraDialog.findViewById(R.id.radiobutton_dismiss);
        mButtonTrue = mCameraDialog.findViewById(R.id.radiobutton_true);
        mButtonTrue.setOnClickListener(this);
        mButtonDismiss.setOnClickListener(this);
    }

    protected abstract void initdata();

    protected void initView() {
        initPopupWindow();
        initCameraDialog();
        initVideoDialog();
        initAddFile();
        gv_share_photo = findViewById(R.id.share_photo);
        gv_share_video = findViewById(R.id.share_video);
    }

    protected void initListView() {
        adapter = new ImgGridAdapter(this);
        adapter_video = new VideoGridAdapter(this, mUploadVideoList);
        gv_share_photo.setAdapter(adapter);
        gv_share_video.setAdapter(adapter_video);
        gv_share_photo.setOnItemLongClickListener(this);
        gv_share_video.setOnItemLongClickListener(this);
        gv_share_video.setOnItemClickListener(new VideoItemClickListener(this));
        gv_share_photo.setOnItemClickListener(new ImgItemClickListener(this));
    }

    protected abstract int getActivityLayout();

    private void initPopupWindow() {
        pop = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(
                R.layout.item_popupwindows, null);
        ll_popup = view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        Button bt2 = view.findViewById(R.id.item_popupwindows_Photo);
        Button cancel = view.findViewById(R.id.item_popupwindows_cancel);
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


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.share_photo:
                BimpHandler.tempSelectBitmap.remove(position);
                pathPhoto.remove(position);
                BimpHandler.haveCompress.remove(position);
                BimpHandler.mPhotoNum.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.share_video:
                mUploadVideoList.remove(position);
                adapter_video.notifyDataSetChanged();
                return true;
        }
        return false;
    }

    protected static ArrayList<Integer> nomarlVideoID = new ArrayList<>();
    protected static ArrayList<Integer> errorVideoID = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1000 || requestCode == 1001) && resultCode == RESULT_OK && data != null) {
//            mUploadVideoList.clear();
            ArrayList<VideoEntity> checkedVideos = (ArrayList<VideoEntity>) data.getSerializableExtra("checked");
            LogUtils.d(checkedVideos.size() + "checkedVideos");
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
            }
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
                    switch (requestCode) {
                        case 1000://正常
                            video.type = 0;
                            nomarlVideoID.add(video.ID);
                            break;
                        case 1001://问题
                            video.type = 1;
                            errorVideoID.add(video.ID);
                            break;
                    }
                } else {
                    String path = FileUtils.getFilePath(this, "video" + File.separator +
                            "compress") + video.filePath.substring(video.filePath.lastIndexOf(File.separator));
                    if (new File(path).exists()) {
                        video.filePath = path;
                    }
                    video.size = Utils.ReadVideoSize(video.filePath);
                    if (errorVideoID.contains(video.ID)) {
                        video.type = 1;
                    } else {
                        video.type = 0;
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

    protected List<VideoEntity> errorCompress;
    protected List<VideoEntity> needCompress;
    int mSize;

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
                                    FileUtils.compressVideos(BaseDetailActivity.this, needCompress);
                                }
                            }).setItems(new String[]{"存在视频压缩失败，请点击确定重新压缩"}, null);
                    mAlertDialog = builder.create();
                }
                mAlertDialog.show();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                this, R.anim.activity_translate_in));
        pop.showAtLocation(this.findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radiobutton_true:
                if (mGroupSHD.getCheckedRadioButtonId() == -1 || mGroupDismissTrue.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(BaseDetailActivity.this, "两项都要选择", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mGroupSHD.getCheckedRadioButtonId() == R.id.radiobutton_one && mGroupDismissTrue.getCheckedRadioButtonId() == R.id.radiobutton_three) {
                    ispixel = true;
                    isPhote = true;
                } else if (mGroupSHD.getCheckedRadioButtonId() == R.id.radiobutton_one && mGroupDismissTrue.getCheckedRadioButtonId() == R.id.radiobutton_four) {
                    ispixel = true;
                    isPhote = false;
                } else if (mGroupSHD.getCheckedRadioButtonId() == R.id.radiobutton_two && mGroupDismissTrue.getCheckedRadioButtonId() == R.id.radiobutton_three) {
                    ispixel = false;
                    isPhote = true;
                } else if (mGroupSHD.getCheckedRadioButtonId() == R.id.radiobutton_two && mGroupDismissTrue.getCheckedRadioButtonId() == R.id.radiobutton_four) {
                    ispixel = false;
                    isPhote = false;
                }
                mCameraDialog.dismiss();
                openCameraPopupWindow();
                break;
            case R.id.radiobutton_dismiss:
                mCameraDialog.dismiss();
                break;
            case R.id.video_sure:
                Intent intent = new Intent(this, VideoGridActivity.class);
                checkedIds.clear();
                for (VideoEntity video :
                        mUploadVideoList) {
                    checkedIds.add(video.ID);
                }
                intent.putExtra("checked", checkedIds);
                int requestcode;
                if (mRadioGroup_video.getCheckedRadioButtonId() == R.id.normal_video) {
                    requestcode = 1000;//正常
                } else if (mRadioGroup_video.getCheckedRadioButtonId() == R.id.error_video) {
                    requestcode = 1001;//问题
                } else {
                    return;
                }
                startActivityForResult(intent, requestcode);
                mVideoDialog.dismiss();
                break;
            case R.id.video_cancel:
                mVideoDialog.dismiss();
                break;
        }
    }

}
