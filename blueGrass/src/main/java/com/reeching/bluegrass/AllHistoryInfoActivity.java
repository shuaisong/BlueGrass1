package com.reeching.bluegrass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.adapter.NetVideoGridAdapter;
import com.reeching.bean.ExhibitionBean;

import java.util.ArrayList;
import java.util.Arrays;


public class AllHistoryInfoActivity extends AppCompatActivity {
    private ExhibitionBean.InfosBean infos;
    private TextView tvtheme;
    private TextView tvpeople;
    private TextView tvauthor;
    private TextView tvtimes;
    private TextView tvtimee;
    private TextView tvauthorinfo;
    private ImageView ivlevel;
    private NoScrollGridView lin, lin_video;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> list_video = new ArrayList<>();
    private NetImgGridAdapter adapter2;
    private TextView mInfoHuaLang;
    private TextView show_quantity;
    private TextView mWorksCount;
    private TextView mVideoCount;
    private TextView mSculptureCount;
    private TextView mDeviceCount;
    private TextView mOtherDesc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getActivityLayout());
        initView();
        initdata();
    }

    protected void initView() {
        tvauthor = findViewById(R.id.allhistoryinfo_author);
        infos = (ExhibitionBean.InfosBean) getIntent().getSerializableExtra("info");
        lin = findViewById(R.id.allhistoryinfo_lin);
        lin_video = findViewById(R.id.allhistoryinfo_lin_video);
        tvtheme = findViewById(R.id.allhistoryinfo_theme);
        tvpeople = findViewById(R.id.allhistoryinfo_people);
        TextView comeback = findViewById(R.id.comeback);
        tvtimee = findViewById(R.id.allhistoryinfo_timee);
        tvtimes = findViewById(R.id.allhistoryinfo_times);
        tvauthorinfo = findViewById(R.id.allhistoryinfo_authorinfo);
        ivlevel = findViewById(R.id.allhistoryinfo_level);
        mInfoHuaLang = findViewById(R.id.havereportedinfo_hualang);
        show_quantity = findViewById(R.id.show_quantity);

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(AllHistoryInfoActivity.this,
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
                AllHistoryInfoActivity.this.finish();
            }
        });
        mWorksCount = findViewById(R.id.worksCount);
        mVideoCount = findViewById(R.id.videoCount);
        mSculptureCount = findViewById(R.id.sculptureCount);
        mDeviceCount = findViewById(R.id.deviceCount);
        mOtherDesc = findViewById(R.id.otherDesc);
    }

    protected int getActivityLayout() {
        return R.layout.activity_all_history_info;
    }

    protected void initdata() {
        show_quantity.setText(infos.getRemarks());
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        String dateBegin = infos.getDateBegin();
        mInfoHuaLang.setText(infos.getGalleryName());

        tvauthor.setText(infos.getAuthor());
        tvtimes.setText(dateBegin);
        String dateEnd = infos.getDateEnd();
        tvtimee.setText(dateEnd);
        tvauthorinfo.setText(infos.getManagerIntroduction());
        switch (infos.getCareLevel()) {
            case "0":
                ivlevel.setImageResource(R.drawable.green);
                break;
            case "1":
                ivlevel.setImageResource(R.drawable.yellow);
                break;
            default:
                ivlevel.setImageResource(R.drawable.red);
                break;
        }
        if (!"".equals(infos.getPhoto())) {
            String sourceStr = infos.getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            list.addAll(Arrays.asList(sourceStrArray).subList(1, sourceStrArray.length));
        }

        if (infos.getVideoUrl() != null && !"".equals(infos.getVideoUrl())) {
            String sourceStr = infos.getVideoUrl();
            String[] sourceStrArray = sourceStr.split("\\|");
            list_video.addAll(Arrays.asList(sourceStrArray).subList(1, sourceStrArray.length));
        }
        adapter2 = new NetImgGridAdapter(AllHistoryInfoActivity.this, list);
        lin.setAdapter(adapter2);
        final NetVideoGridAdapter adapter_video = new NetVideoGridAdapter(this, list_video);
        lin_video.setAdapter(adapter_video);
        mVideoCount.setText(infos.getVideoCount());
        mSculptureCount.setText(infos.getSculptureCount());
        mDeviceCount.setText(infos.getDeviceCount());
        mWorksCount.setText(infos.getWorksCount());
        mOtherDesc.setText(infos.getOtherDesc());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }

}
