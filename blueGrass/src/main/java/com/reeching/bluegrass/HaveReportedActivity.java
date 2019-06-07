package com.reeching.bluegrass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.reeching.activity.BaseDetailActivity;
import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.adapter.NetVideoGridAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.bean.UploadBean;
import com.reeching.bean.ZhanlanBean;
import com.reeching.utils.FileUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.JsonCallback;
import com.reeching.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;


public class HaveReportedActivity extends AppCompatActivity {
    private ExhibitionBean.InfosBean infos;
    private TextView tvtheme;
    private TextView tvpeople;
    private TextView tvtimes;
    private TextView tvtimee;
    private TextView tvauthor;
    private TextView tvauthorinfo;
    private ImageView ivlevel;
    private NoScrollGridView lin, lin_video;
    private ArrayList<String> list = new ArrayList<String>();
    private NetImgGridAdapter adapter2;
    private ArrayList<String> list_video = new ArrayList<>();
    private TextView mHuaLang;
    private TextView mCurrentTime;
    private TextView show_quantity;
    private TextView mWorksCount;
    private TextView mVideoCount;
    private TextView mSculptureCount;
    private TextView mDeviceCount;
    private TextView mOtherDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getActivityLayout());
        initView();
        initdata();
    }

    protected void initdata() {
        show_quantity.setText(infos.getRemarks());
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        tvtimes.setText(infos.getDateBegin());
        tvtimee.setText(infos.getDateEnd());
        tvauthor.setText(infos.getAuthor());
        mHuaLang.setText(infos.getGalleryName());
        mCurrentTime.setText(infos.getCreateDate());
        tvauthorinfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else if (infos.getCareLevel().equals("2")) {
            ivlevel.setImageResource(R.drawable.red);
        }
        if (!"".equals(infos.getPhoto())) {
            String sourceStr = infos.getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        if (infos.getVideoUrl() != null && !"".equals(infos.getVideoUrl())) {
            String sourceStr = infos.getVideoUrl();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list_video.add(sourceStrArray[ii]);
            }
        }
        adapter2 = new NetImgGridAdapter(HaveReportedActivity.this, list);
        lin.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        final NetVideoGridAdapter adapter_video = new NetVideoGridAdapter(this, list_video);
        lin_video.setAdapter(adapter_video);
        mVideoCount.setText(infos.getVideoCount());
        mSculptureCount.setText(infos.getSculptureCount());
        mDeviceCount.setText(infos.getDeviceCount());
        mWorksCount.setText(infos.getWorksCount());
        mOtherDesc.setText(infos.getOtherDesc());
    }

    protected void initView() {
        infos = (ExhibitionBean.InfosBean) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.havereportedinfo_lin);
        lin_video = (NoScrollGridView) findViewById(R.id.info_lin_video);
        tvtheme = (TextView) findViewById(R.id.havereportedinfo_theme);
        tvpeople = (TextView) findViewById(R.id.havereportedinfo_people);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        tvauthor = (TextView) findViewById(R.id.havereportedinfo_author);
        tvtimee = (TextView) findViewById(R.id.havereportedinfo_timee);
        tvtimes = (TextView) findViewById(R.id.havereportedinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.havereportedinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.havereportedinfo_level);
        mHuaLang = (TextView) findViewById(R.id.havereported_hualang);
        mCurrentTime = (TextView) findViewById(R.id.history_currenttime);
        show_quantity = (TextView) findViewById(R.id.show_quantity);

        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HaveReportedActivity.this.finish();
            }
        });
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(HaveReportedActivity.this,
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

        mWorksCount = (TextView) findViewById(R.id.worksCount);
        mVideoCount = (TextView) findViewById(R.id.videoCount);
        mSculptureCount = (TextView) findViewById(R.id.sculptureCount);
        mDeviceCount = (TextView) findViewById(R.id.deviceCount);
        mOtherDesc = (TextView) findViewById(R.id.otherDesc);
    }

    protected int getActivityLayout() {
        return R.layout.activity_have_reported;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }

}
