package com.reeching.bluegrass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.BaseApplication;
import com.reeching.utils.HttpApi;
import com.reeching.utils.LogUtils;
import com.reeching.utils.SPUtil;
import com.reeching.utils.StatusBarUtil;
import com.reeching.utils.ToastUtil;
import com.reeching.utils.UpdateAppManager;

import java.util.ArrayList;
import java.util.List;

public class SpActivity extends Activity {
    private String name, psd;
    private boolean flag;
    private String murl;
    //	UserDao userdao;
//	Cursor cur;
    private HttpUtils hu = new HttpUtils();
    private UpdateAppManager updateManager;
    private String version = BaseApplication.getInstance().getVersonnum();
    private static final long SPLASH_TIME = 3000;// splash界面显示的时间
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (flag) {
                String exhibitionid = getExhibitionid();
                String pushUserid = getPushUserid();
                Intent intent = new Intent(SpActivity.this, MainActivity.class);

                if (pushUserid != null && !"".equals(pushUserid)) {
                    intent.putExtra("isPush", true);
                } else if (exhibitionid != null && !"".equals(exhibitionid)) {
                    intent.putExtra("exhibitionid", exhibitionid);
                }
                startActivity(intent);
//                startActivity(new Intent(SpActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(SpActivity.this, LoginActivity.class));
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sp);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        name = SPUtil.getUserSP(this);
        psd = SPUtil.getPassSP(this);
//		userdao = UserDao.getInstance(SpActivity.this);
//		cur = userdao.queryUserList();
//        updata();
        //        检查权限

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else
//            updata();
        login();
    }

    private String getExhibitionid() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("exhibitionid");
        LogUtils.d(id + "exhibitionid");
        return id;
    }

    private String getPushUserid() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("userid");
        LogUtils.d(id + "userid");
        return id;
    }

    private final String[] allPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
        } else
            login();

//            updata();
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
            } else
                login();
//                updata();
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


    private void login() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("loginName", name);
        params.addQueryStringParameter("password", psd);
        params.addQueryStringParameter("mobileLogin", "true");
        hu.configTimeout(20 * 1000);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.login, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        flag = false;
                        handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        /*JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(SpActivity.this, "自动登录成功！", Toast.LENGTH_SHORT).show();
                            if (jsonObject.getString("roleName").equals("系统管理员")) {
                                BaseApplication.getInstance().setQuanxian("系统管理员");
                            } else if (jsonObject.getString("roleName").equals("普通用户")) {
                                BaseApplication.getInstance().setQuanxian("普通用户");
                            } else {
                                BaseApplication.getInstance().setQuanxian("上报用户");
                            }
                            SPUtil.putUserQuanXian(jsonObject.getString("roleName"), SpActivity.this);
                            BaseApplication.getInstance().setLoginName(name);
                            BaseApplication.getInstance().setId(jsonObject.getString("userId"));
                            flag = true;
                            handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
                        } else {
                            flag = false;
                            handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
                        }*/
                        Toast.makeText(SpActivity.this, "自动登录成功！", Toast.LENGTH_SHORT).show();
                        BaseApplication.getInstance().setQuanxian("系统管理员");
                        SPUtil.putUserQuanXian("系统管理员", SpActivity.this);
                        BaseApplication.getInstance().setLoginName(name);
                        BaseApplication.getInstance().setId("1");
                        flag = true;
                        handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
                    }
                });

    }

    private void updata() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version", version);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.updata, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Builder builder = new Builder(
                                SpActivity.this);
                        builder.setTitle("网络异常!");
                        builder.setMessage("请检查网络!");
                        builder.setNegativeButton("退出",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        System.exit(0);

                                    }
                                });
                        builder.setPositiveButton("手动登录!",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // method stub
                                        startActivity(new Intent(
                                                SpActivity.this,
                                                LoginActivity.class));
                                        finish();
                                    }
                                });
                        if (!SpActivity.this.isFinishing())
                            builder.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject object = JSON.parseObject(arg0.result);
                        if (object.getString("result").equals("2")) {
                            JSONObject obj = object.getJSONObject("info");
                            murl = obj.getString("downAddress");
                            BaseApplication.getInstance().setMurl(murl);
                            updateManager = new UpdateAppManager(
                                    SpActivity.this);
                            updateManager.checkUpdateInfo();
                        } else {
                            login();
                            //	initdate();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            updateManager.installApp();
        } else if (requestCode == 101) {
            updateManager.downloadApp();
        }
    }

    private void initdate() {

        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallhualangname,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(SpActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject object = JSON.parseObject(arg0.result,
                                JSONObject.class);
                        Toast.makeText(SpActivity.this, "更新数据完成！", Toast.LENGTH_SHORT).show();
                        BaseApplication.getInstance().setObj(object);
//						login();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
