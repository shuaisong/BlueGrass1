package com.reeching.bluegrass;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.fragment.AnjianFragment;
import com.reeching.fragment.HualangFragment;
import com.reeching.fragment.MineFragment;
import com.reeching.fragment.ReportFragment;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.MyViewPager;
import com.reeching.utils.ToastUtil;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.yancy.imageselector.BimpHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import static com.reeching.fragment.MapFragment.handler;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragmentsList;
    private MyViewPager mPager;
    private RadioGroup mRadioGroup;
    private byte[] bitmapByte;
    private AnjianFragment mAnjianFragment;
    private MainReceiver mMainReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(MainActivity.this);
        RadioButton rbt1 = (RadioButton) findViewById(R.id.rbtn_report);
        RadioButton rbt2 = (RadioButton) findViewById(R.id.rbtn_anjian);
        RadioButton rbt3 = (RadioButton) findViewById(R.id.rbtn_hualang);
        RadioButton rbt4 = (RadioButton) findViewById(R.id.rbtn_mine);
        rbt1.setOnClickListener(new MyOnClickListener(0));
        rbt2.setOnClickListener(new MyOnClickListener(1));
        rbt3.setOnClickListener(new MyOnClickListener(2));
        rbt4.setOnClickListener(new MyOnClickListener(3));
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.check(R.id.rbtn_report);
        // 注册广播
        mMainReceiver = new MainReceiver();
        IntentFilter filter = new IntentFilter("com.spice.spicytemptation.net.HttpOpearation.checkVersion");
        registerReceiver(mMainReceiver, filter);
        if (BaseApplication.getInstance().getQuanxian().equals("上报用户")) {
            rbt2.setVisibility(View.GONE);
        } else {
            rbt2.setVisibility(View.VISIBLE);
        }
        InitViewPager();
        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.onAppStart();

        pushAgent.addAlias(BaseApplication.getInstance().getId(),
                BaseApplication.getInstance().getId(), new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String message) {
                        LogUtils.d("isSuccess:" + isSuccess + "--" + message);
                    }
                });
//        检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        //BaseApplication.getRefWatcher(this).watch(this);
        String exhibitionid = getIntent().getStringExtra("exhibitionid");
        if (exhibitionid != null && !exhibitionid.equals("")) {
            Intent intent = new Intent(this, PlanInfoActivity.class);
            intent.putExtra("id", exhibitionid);
            startActivity(intent);
        }
    }

    private final String[] allPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private List<String> denyPermissions = new ArrayList<>(3);
    private List<String> needPermissions = new ArrayList<>(3);

    /**
     * 遍历权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        needPermissions.clear();
        for (String allPermission : allPermissions) {
            if (checkSelfPermission(allPermission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(allPermission);
            }
        }
        if (!needPermissions.isEmpty()) {
            requestPermissions(needPermissions.toArray(new String[needPermissions.size()]), 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            denyPermissions.clear();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(permissions[i]);
                }
            }
            if (!denyPermissions.isEmpty()) {
                ToastUtil.showToast(this, "应用缺少权限，请给与权限以便应用正常运行");
                toSelfSetting();
            }
        }
    }

    private void toSelfSetting() {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(mIntent);
        finish();
    }

    File filePath;

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 331) {
            mAnjianFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 得到bitmap的字节流。
    private byte[] getData(Bitmap bm, String fileName) {
        if (bm != null && filePath != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bitmapByte = baos.toByteArray();
        }
        return bitmapByte;
    }

    private void InitViewPager() {
        mPager = (MyViewPager) findViewById(R.id.act_framelayout);
        mPager.setCanScrollble(false);
        mPager.setOffscreenPageLimit(4);
        fragmentsList = new ArrayList<Fragment>();
        mAnjianFragment = new AnjianFragment();
        HualangFragment hualangFragment = new HualangFragment();
        MineFragment mineFragment = new MineFragment();
        ReportFragment reportFragment = new ReportFragment();
        fragmentsList.add(reportFragment);
        fragmentsList.add(mAnjianFragment);
        fragmentsList.add(hualangFragment);
        fragmentsList.add(mineFragment);
        mPager.setOffscreenPageLimit(4);
        mPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isPush = getIntent().getBooleanExtra("isPush", false);
        LogUtils.d(isPush + "isPush");
        if (isPush) {
            mPager.setCurrentItem(1);
            mRadioGroup.check(R.id.rbtn_anjian);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("确认退出?");
            builder.setNegativeButton("退出程序",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File("/temp/picture");
                            if (file.exists()) {
                                deleteAllFiles(file);
                            }
                            ExitApplication.getInstance().exit();
                        }
                    });
            builder.setPositiveButton("再看看",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();

        }
        return super.onKeyDown(keyCode, event);
    }

    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", BaseApplication.getInstance().getId());
        outState.putString("LoginName", BaseApplication.getInstance().getLoginName());
        outState.putString("Murl", BaseApplication.getInstance().getMurl());
        outState.putString("Quanxian", BaseApplication.getInstance().getQuanxian());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String id = savedInstanceState.getString("id");
            String LoginName = savedInstanceState.getString("LoginName");
            String Murl = savedInstanceState.getString("Murl");
            String Quanxian = savedInstanceState.getString("Quanxian");
            BaseApplication.getInstance().setLoginName(LoginName);
            BaseApplication.getInstance().setMurl(Murl);
            BaseApplication.getInstance().setQuanxian(Quanxian);
            BaseApplication.getInstance().setId(id);
        }
    }


    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            if (index == 0) {
                mRadioGroup.check(R.id.rbtn_report);
            } else if (index == 1) {
                mRadioGroup.check(R.id.rbtn_anjian);
            } else if (index == 2) {
                mRadioGroup.check(R.id.rbtn_hualang);
            } else {
                mRadioGroup.check(R.id.rbtn_mine);
            }

        }
    }

    ;

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        // 得到每个item
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMainReceiver);
    }

    class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BimpHandler.tempAddPhoto.clear();
        }
    }
}
