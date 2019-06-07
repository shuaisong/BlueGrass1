package com.reeching.bluegrass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.adapter.NetVideoGridAdapter;
import com.reeching.bean.ExhibitionBean;

import java.util.ArrayList;


public class HistoryInfoActivity extends AppCompatActivity {
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
    private TextView mInfoHuaLang;
    private ArrayList<String> list_video = new ArrayList<>();
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
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        tvtimes.setText(infos.getDateBegin());
        tvtimee.setText(infos.getDateEnd());
        tvauthor.setText(infos.getAuthor());
        mInfoHuaLang.setText(infos.getGalleryName());
        tvauthorinfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
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
        adapter2 = new NetImgGridAdapter(HistoryInfoActivity.this, list);
        lin.setAdapter(adapter2);
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
        lin = (NoScrollGridView) findViewById(R.id.historyinfo_lin);
        lin_video = (NoScrollGridView) findViewById(R.id.info_lin_video);
        tvtheme = (TextView) findViewById(R.id.historyinfo_theme);
        tvpeople = (TextView) findViewById(R.id.historyinfo_people);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        tvauthor = (TextView) findViewById(R.id.historyinfo_author);
        tvtimee = (TextView) findViewById(R.id.historyinfo_timee);
        tvtimes = (TextView) findViewById(R.id.historyinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.historyinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.historyinfo_level);
        mInfoHuaLang = (TextView) findViewById(R.id.havehistoryinfo_hualang);

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(HistoryInfoActivity.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    Toast.makeText(HistoryInfoActivity.this, "加载中，请稍候。。。", Toast.LENGTH_SHORT).show();
                    adapter2.notifyDataSetChanged();
                }
            }
        });

        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HistoryInfoActivity.this.finish();
            }
        });
        mWorksCount = findViewById(R.id.worksCount);
        mVideoCount = findViewById(R.id.videoCount);
        mSculptureCount = findViewById(R.id.sculptureCount);
        mDeviceCount = findViewById(R.id.deviceCount);
        mOtherDesc = findViewById(R.id.otherDesc);
    }

    protected int getActivityLayout() {
        return R.layout.activity_history_info;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }
}
