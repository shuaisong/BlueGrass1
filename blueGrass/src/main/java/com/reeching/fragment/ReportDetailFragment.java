package com.reeching.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
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
import com.reeching.activity.VideoGridActivity;
import com.reeching.adapter.GlideLoader;
import com.reeching.adapter.ImgGridAdapter;
import com.reeching.adapter.VideoGridAdapter;
import com.reeching.bean.VideoEntity;
import com.reeching.bluegrass.HuaLangSerchActivity;
import com.reeching.bluegrass.MatterActivity;
import com.reeching.bluegrass.NoScrollGridView;
import com.reeching.bluegrass.R;
import com.reeching.listener.Add_Button_Listener;
import com.reeching.listener.ImgItemClickListener;
import com.reeching.listener.VideoItemClickListener;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.DBHelper;
import com.reeching.utils.DialogUtils;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.SPUtil;
import com.reeching.utils.UserDao;
import com.reeching.utils.Utils;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yancy.imageselector.ImageSelectorFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//上报页面
public class ReportDetailFragment extends Fragment implements ImageSelectorFragment.SelectorFragment,
        AdapterView.OnItemLongClickListener, OnClickListener {
    /**
     * pop窗口
     */
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private ImgGridAdapter adapter;
    private VideoGridAdapter adapter_video;
    private NoScrollGridView gv_share_photo, gv_share_video;
    private ProgressDialog progressDialog;
    private EditText myEditText;
    private EditText ettheme;
    private EditText etcehua;
    private EditText etgaiyao;
    private EditText etremark;
    private EditText etauthor;
    private EditText etauthorshuoming;
    private EditText etcehuashuoming;
    private Button btn;
    private TextView starttime, etstatus, endtime;
    private DatePickerDialog dpdialog;
    private String[] status = new String[]{"已完成", "进行中", "布展中"};
    private String[] statu = new String[]{"1", "0", "2"};
    private String state = "";
    private static String id = "";
    private String careLevel = "";
    private RadioGroup rgp;
    private int myear, mmonth, mday;
    private Boolean timeflag = false;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private Boolean ispixel = false, isPhote = true;
    UserDao userdao;
    Cursor cur;
    private Dialog mCameraDialog;
    private RadioGroup mGroupSHD;
    private RadioGroup mGroupDismissTrue;
    private ProgressDialog mProgressDialog;
    private List<VideoEntity> compressVideo;
    private Dialog mVideoDialog;
    private RadioGroup mRadioGroup_video;
    private int mSize;
    private AlertDialog mAlertDialog;
    private EditText worksCount;
    private EditText videoCount;
    private EditText sculptureCount;
    private EditText deviceCount;
    private EditText otherDesc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailreport, container, false);
        userdao = UserDao.getInstance(BaseApplication.getInstance());
        cur = userdao.queryUserList();
        Button btnrest = (Button) view.findViewById(R.id.detailreport_rest);
        gv_share_photo = (NoScrollGridView) view.findViewById(R.id.share_photo);
        gv_share_video = (NoScrollGridView) view.findViewById(R.id.share_video);
        final ImageView add_file = (ImageView) view.findViewById(R.id.add_file);

        etauthorshuoming = (EditText) view
                .findViewById(R.id.detailreport_authorshuoming);
        etcehuashuoming = (EditText) view
                .findViewById(R.id.detailreport_cehuashuoming);
        rgp = (RadioGroup) view.findViewById(R.id.detailreport_rgroup);
        myEditText = (EditText) view.findViewById(R.id.detailreport_myedittext);
        ettheme = (EditText) view.findViewById(R.id.detailreport_theme);
        etcehua = (EditText) view.findViewById(R.id.detailreport_cehuapeople);
        starttime = (TextView) view.findViewById(R.id.detailreport_starttime);
        endtime = (TextView) view.findViewById(R.id.detailreport_endtime);
        etgaiyao = (EditText) view.findViewById(R.id.detailreport_gaiyao);
        etremark = (EditText) view.findViewById(R.id.detailreport_remark);
        EditText etcontent = (EditText) view.findViewById(R.id.detailreport_content);
        btn = (Button) view.findViewById(R.id.detailreport_report);
        etstatus = (TextView) view.findViewById(R.id.detailreport_status);
        etauthor = (EditText) view.findViewById(R.id.detailreport_author);
        worksCount = (EditText) view.findViewById(R.id.worksCount);
        videoCount = (EditText) view.findViewById(R.id.videoCount);
        sculptureCount = (EditText) view.findViewById(R.id.sculptureCount);
        deviceCount = (EditText) view.findViewById(R.id.deviceCount);
        otherDesc = (EditText) view.findViewById(R.id.otherDesc);
        ImageSelectorFragment selectorFragment = new ImageSelectorFragment();
        selectorFragment.setSelectorFragment(this);
        if (mCameraDialog == null) initCameraDialog();
        if (mVideoDialog == null) initVideoDialog();
        add_file.setOnClickListener(new Add_Button_Listener(getActivity(), mCameraDialog, mVideoDialog));
        etcontent.setOnTouchListener(new View.OnTouchListener() {
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
        etremark.setOnTouchListener(new View.OnTouchListener() {
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
        btnrest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        etgaiyao.setOnTouchListener(new View.OnTouchListener() {
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
        etauthorshuoming.setOnTouchListener(new View.OnTouchListener() {
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
        etcehuashuoming.setOnTouchListener(new View.OnTouchListener() {
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

        etstatus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Builder builder = new Builder(getActivity());
                builder.setTitle("请选择展览状态");
                builder.setSingleChoiceItems(status, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                etstatus.setText(status[which]);
                                state = statu[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });


        myEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        HuaLangSerchActivity.class);
//                getRootFragment().startActivityForResult(intent, 001);
                startActivityForResult(intent, 1);
            }
        });
        rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.detailreport_rbtn1:
                        careLevel = "2";
                        break;
                    case R.id.detailreport_rbtn2:
                        careLevel = "1";
                        break;
                    case R.id.detailreport_rbtn3:
                        careLevel = "0";
                        break;
                }
            }
        });
        mreport();
        initstarttime();
        initendtime();
        initPopupWindow();
        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initVideoDialog() {
        mVideoDialog = DialogUtils.showPromptDailog(getContext(),
                LayoutInflater.from(getActivity()), R.layout.vido_type_select);
        mRadioGroup_video = (RadioGroup) mVideoDialog.findViewById(R.id.radioGroup_video);
        TextView video_cancel = (TextView) mVideoDialog.findViewById(R.id.video_cancel);
        TextView video_sure = (TextView) mVideoDialog.findViewById(R.id.video_sure);
        video_sure.setOnClickListener(this);
        video_cancel.setOnClickListener(this);
    }

    private void initCameraDialog() {
        mCameraDialog = DialogUtils.showPromptDailog(getContext(),
                LayoutInflater.from(getActivity()), R.layout.cameradialog);
        mGroupSHD = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_HSD);
        mGroupDismissTrue = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_dismisstrue);
        TextView buttonDismiss = (TextView) mCameraDialog.findViewById(R.id.radiobutton_dismiss);
        TextView buttonTrue = (TextView) mCameraDialog.findViewById(R.id.radiobutton_true);
        buttonDismiss.setOnClickListener(this);
        buttonTrue.setOnClickListener(this);
    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), R.anim.activity_translate_in));
        pop.showAtLocation(getActivity().findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
    }

    private int getPixelRadioButton() {
        return mGroupSHD.getCheckedRadioButtonId();
    }

    private int getErrorRadioButton() {
        return mGroupDismissTrue.getCheckedRadioButtonId();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    private void initstarttime() {
        starttime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year1 = calendar.get(Calendar.YEAR);
                int month1 = calendar.get(Calendar.MONTH);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                dpdialog = new DatePickerDialog(getActivity(),
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker arg0, int year,
                                                  int month, int day) {
                                // setTitle(year + "年" + (month + 1) + "月" + day
                                // + "日");
                                if (month < 9) {
                                    if (day <= 9) {
                                        starttime
                                                .setText(year + "-" + "0"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                    } else {
                                        starttime.setText(year + "-" + "0"
                                                + (month + 1) + "-" + day);
                                    }
                                } else {
                                    if (day <= 9) {
                                        starttime
                                                .setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                    } else {
                                        starttime.setText(year + "-"
                                                + (month + 1) + "-" + day);
                                    }
                                }
                                myear = year;
                                mmonth = month;
                                mday = day;
                                timeflag = true;
                            }
                        }, year1, month1, day1);

                dpdialog.show();
                endtime.setText("");
            }
        });

    }

    private void initendtime() {

        endtime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (timeflag == false) {

                    Toast.makeText(getActivity(), "请先设置开始时间",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    int year2 = calendar.get(Calendar.YEAR);
                    int month2 = calendar.get(Calendar.MONTH);
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    dpdialog = new DatePickerDialog(getActivity(),
                            new OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker arg0,
                                                      int year, int month, int day) {

                                    // setTitle(year + "年" + (month + 1) + "月" +
                                    // day + "日");
                                    if (myear < year) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }
                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }
                                    } else if (myear == year && mmonth < month) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }

                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday < day) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }

                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }

                                    } else if (myear == year && mmonth == month
                                            && mday == day) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }

                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday > day) {
                                        if (month < 9) {
                                            if (mday <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + mday);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + mday);
                                            }

                                        } else {
                                            if (mday <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + mday);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + mday);
                                            }

                                        }
                                    } else if (myear == year && mmonth > month) {
                                        if (month < 9) {
                                            if (mday <= 9) {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + "0" + mday);
                                            } else {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + mday);
                                            }

                                        } else {
                                            if (mday <= 9) {
                                                endtime.setText(myear + "-"
                                                        + +(mmonth + 1) + "-"
                                                        + "0" + mday);
                                            } else {
                                                endtime.setText(myear + "-"
                                                        + +(mmonth + 1) + "-"
                                                        + mday);
                                            }

                                        }
                                    } else if (myear > year) {
                                        if (month < 9) {
                                            if (mday <= 9) {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + "0" + mday);
                                            } else {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + mday);
                                            }

                                        } else {
                                            if (mmonth < 9) {
                                                if (mday <= 9) {
                                                    endtime.setText(myear + "-"
                                                            + "0"
                                                            + (mmonth + 1)
                                                            + "-" + "0" + mday);
                                                } else {
                                                    endtime.setText(myear + "-"
                                                            + "0"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                                }

                                            } else {
                                                if (mday <= 9) {
                                                    endtime.setText(myear + "-"
                                                            + (mmonth + 1)
                                                            + "-" + "0" + mday);
                                                } else {
                                                    endtime.setText(myear + "-"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                                }

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

    private void report() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("上传中,请稍候...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
        hu.configTimeout(10 * 1000);
        hu.configSoTimeout(2 * 60 * 1000);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("galleryId", id);
        params.addQueryStringParameter("theme", ettheme.getText().toString());
        params.addQueryStringParameter("status", state);
        params.addQueryStringParameter("dateBegin", starttime.getText()
                .toString());
        params.addQueryStringParameter("dateEnd", endtime.getText().toString());
        params.addQueryStringParameter("careLevel", careLevel);
        params.addQueryStringParameter("exhibitionIntroduction", etgaiyao
                .getText().toString());
        params.addQueryStringParameter("remarks", etremark.getText().toString());
        params.addQueryStringParameter("author", etauthor.getText().toString());
        params.addQueryStringParameter("authorIntroduction", etauthorshuoming
                .getText().toString());
        params.addQueryStringParameter("manager", etcehua.getText().toString());
        params.addQueryStringParameter("managerIntroduction", etcehuashuoming
                .getText().toString());
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        String trim_workCount = worksCount.getText().toString().trim();
        params.addQueryStringParameter("worksCount", trim_workCount.isEmpty() ? "0" : trim_workCount);
        String trim_videoCount = videoCount.getText().toString().trim();
        params.addQueryStringParameter("videoCount", trim_videoCount.isEmpty() ? "0" : trim_videoCount);
        String trim_sculptureCount = sculptureCount.getText().toString().trim();
        params.addQueryStringParameter("sculptureCount", trim_sculptureCount.isEmpty() ? "0" : trim_sculptureCount);
        String trim_deviceCount = deviceCount.getText().toString().trim();
        params.addQueryStringParameter("deviceCount", trim_deviceCount.isEmpty() ? "0" : trim_deviceCount);
        params.addQueryStringParameter("otherDesc", otherDesc.getText().toString());

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
                        btn.setEnabled(true);
                        Toast.makeText(getActivity(), "传输失败！", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        btn.setEnabled(true);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                            btn.setEnabled(true);
                            for (cur.moveToFirst(); !cur.isAfterLast(); cur
                                    .moveToNext()) {
                                int id = cur.getColumnIndex(DBHelper.COLUMN_ID);
                                String id2 = cur.getString(id);
                                userdao.delete(id2);
                            }
                            Intent intent = null;
                            if (!errorVideoID.isEmpty() || BimpHandler.mPhotoNum.contains("0")) {
                                intent = new Intent(getActivity(), MatterActivity.class);
                            }
                            clear();
                            FileUtils.deleteCompresses();
                            FileUtils.deleteImgCache();
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();
                            if (intent != null)
                                startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            btn.setEnabled(true);
                        }
                    }
                });
    }

    private void mreport() {
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btn.setEnabled(false);
                if (id.equals("") || "".equals(myEditText.getText().toString()) || "".equals(ettheme.getText().toString())
                        || "".equals(myEditText.getText().toString())
                        || "".equals(state)
                        || "".equals(starttime.getText().toString())
                        || "".equals(endtime.getText().toString())
                        || careLevel.equals("")
                        || (rgp.getCheckedRadioButtonId() == -1)) {
                    Toast.makeText(getActivity(), "请填写必填项信息！", Toast.LENGTH_SHORT).show();
                    btn.setEnabled(true);
                } else {

                    if (!BaseApplication.getInstance().isHasnet()) {
                        report();
                    } else {
                        btn.setEnabled(true);
                        saveinfo();
                    }
                }
            }
        });

    }

    private void initPopupWindow() {
        pop = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        // 点击父布局消失框pop
        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // pop消失 清除动画
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        // 选择相册
        bt2.setOnClickListener(new OnClickListener() {
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
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        adapter = new ImgGridAdapter(getActivity());
        adapter_video = new VideoGridAdapter(getActivity(), mUploadVideoList);
        gv_share_photo.setAdapter(adapter);
        gv_share_video.setAdapter(adapter_video);
        gv_share_photo.setOnItemLongClickListener(this);
        gv_share_video.setOnItemLongClickListener(this);
        gv_share_video.setOnItemClickListener(new VideoItemClickListener(getContext()));
        gv_share_photo.setOnItemClickListener(new ImgItemClickListener(getContext()));
    }

    private static ArrayList<Integer> nomarlVideoID = new ArrayList<>();
    private static ArrayList<Integer> errorVideoID = new ArrayList<>();

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3 && data != null) {
            myEditText.setText(data.getStringExtra("result"));
            id = data.getStringExtra("id");
        }
        if (resultCode == Activity.RESULT_OK && data != null) {//高清，标清图片压缩未区分
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

            } else {
//                mUploadVideoList.clear();
                ArrayList<VideoEntity> checkedVideos = (ArrayList<VideoEntity>) data.getSerializableExtra("checked");
                LogUtils.d(checkedVideos.size() + "checkedVideos");
                //mUploadVideoList.addAll(checkedVideos);
                if (mProgressDialog == null)
                    mProgressDialog = new ProgressDialog(getActivity());
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
                        String path = FileUtils.getFilePath(getActivity(), "video" + File.separator +
                                "compress") + video.filePath.substring(video.filePath.lastIndexOf(File.separator));
                        if (new File(path).exists())
                            video.filePath = path;
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
                    FileUtils.compressVideos(getActivity(), needCompress);
                } else {
                    mUploadVideoList.clear();
                    mUploadVideoList.addAll(checkedVideos);
                    adapter_video.notifyDataSetChanged();
                }

            }
        }

    }

    @Override
    public void delTempList() {
        BimpHandler.tempAddPhoto.clear();
    }

    private ArrayList<VideoEntity> mUploadVideoList = new ArrayList<>(3);
    private ArrayList<Integer> checkedIds = new ArrayList<>(3);

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.share_photo:
                BimpHandler.tempSelectBitmap.remove(position);
                BimpHandler.haveCompress.remove(position);
                pathPhoto.remove(position);
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        adapter_video.notifyDataSetChanged();
    }

    private void clear() {
        myEditText.setText("");
        id = "";
        state = "";
        etstatus.setText("");
        pathPhoto.clear();
        ettheme.setText("");
        etcehua.setText("");
        btn.setEnabled(true);
        starttime.setText("");
        endtime.setText("");
        etauthor.setText("");
        etauthorshuoming.setText("");
        etcehuashuoming.setText("");
        etremark.setText("");
        etgaiyao.setText("");
        worksCount.setText("");
        videoCount.setText("");
        sculptureCount.setText("");
        deviceCount.setText("");
        otherDesc.setText("");
        rgp.clearCheck();
        careLevel = "";
        BimpHandler.mPhotoNum.clear();
        BimpHandler.haveCompress.clear();
        BimpHandler.tempSelectBitmap.clear();
        adapter.notifyDataSetChanged();
        mUploadVideoList.clear();
        if (compressVideo != null)
            compressVideo.clear();
        errorVideoID.clear();
        nomarlVideoID.clear();
        if (errorCompress != null)
            errorCompress.clear();
        adapter_video.notifyDataSetChanged();

    }

    private void saveinfo() {
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            int id = cur.getColumnIndex(DBHelper.COLUMN_ID);
            String id2 = cur.getString(id);
            userdao.delete(id2);
        }
        String[] picArrayPath = new String[BimpHandler.tempSelectBitmap.size()];
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < BimpHandler.tempSelectBitmap.size(); i++) {
            picArrayPath[i] = BimpHandler.tempSelectBitmap.get(i)
                    .getImagePath();
            BimpHandler.tempSaveBitmap.add(BimpHandler.tempSelectBitmap.get(i));
            String ss = picArrayPath[i];
            str.append(ss).append(",");
        }
        String path = str.toString();
        path = path.substring(0, path.length() - 1);
        userdao.addList(id, ettheme.getText().toString(), state, starttime
                        .getText().toString(), endtime.getText().toString(), careLevel,
                etauthor.getText().toString(), etauthorshuoming.getText()
                        .toString(), etcehua.getText().toString(),
                etcehuashuoming.getText().toString(), BaseApplication
                        .getInstance().getId(), etgaiyao.getText().toString(),
                etremark.getText().toString(), path);
        clear();
        Toast.makeText(getActivity(), "已缓存！", Toast.LENGTH_SHORT).show();
        SPUtil.putflag(true, getActivity());
    }

    private List<VideoEntity> errorCompress;
    private List<VideoEntity> needCompress;

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
                    Builder builder = new Builder(getActivity());
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
                                    FileUtils.compressVideos(getActivity(), needCompress);
                                }
                            }).setItems(new String[]{"存在视频压缩失败，请点击确定重新压缩"}, null);
                    mAlertDialog = builder.create();
                }
                mAlertDialog.show();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        ImageSelector.open(this, imageConfig);   // 开启图片选择器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radiobutton_dismiss:
                mCameraDialog.dismiss();
                break;
            case R.id.radiobutton_true:
                if (getPixelRadioButton() == -1 || getErrorRadioButton() == -1) {
                    Toast.makeText(getActivity(), "两项都要选择", Toast.LENGTH_SHORT).show();
                } else if (getPixelRadioButton() == R.id.radiobutton_one && getErrorRadioButton() == R.id.radiobutton_three) {
                    ispixel = true;//标清
                    isPhote = true;//正常
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                } else if (getPixelRadioButton() == R.id.radiobutton_one && getErrorRadioButton() == R.id.radiobutton_four) {
                    ispixel = true;
                    isPhote = false;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                } else if (getPixelRadioButton() == R.id.radiobutton_two && getErrorRadioButton() == R.id.radiobutton_three) {
                    ispixel = false;
                    isPhote = true;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                } else if (getPixelRadioButton() == R.id.radiobutton_two && getErrorRadioButton() == R.id.radiobutton_four) {
                    ispixel = false;
                    isPhote = false;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                }
                break;
            case R.id.video_sure:
                Intent intent = new Intent(getActivity(), VideoGridActivity.class);
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
