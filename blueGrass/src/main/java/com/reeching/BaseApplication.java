package com.reeching;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.danikula.videocache.HttpProxyCacheServer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.reeching.bean.AllHualangInfo.Infos;
import com.reeching.bluegrass.MainActivity;
import com.reeching.bluegrass.PlanInfoActivity;
import com.reeching.utils.CrashHandler;
import com.reeching.utils.FileUtils;
import com.reeching.utils.LogUtils;
import com.reeching.utils.SPUtil;
import com.reeching.utils.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class BaseApplication extends Application {
    private static BaseApplication application;
    private List<Infos> info;
    private String loginName;
    private String murl;
    private String versonnum = "2.3.5";//发布新版本后，来此处更改为对应版本号，必改，否则无限更新。
    private boolean hasnet, initflag;
    private String id;
    private JSONObject obj;
    private String quanxian = "上报用户";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        info = new ArrayList<>();
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        initOkGo();
        initPush();
//        SDKInitializer.initialize(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        stepLeakCanary();
    }

    private void stepLeakCanary() {
        LeakCanary.install(this);
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy() {
        BaseApplication instance = getInstance();
        return instance.proxy == null ? (instance.proxy = instance.newProxy()) : instance.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        final File dir = new File(FileUtils.getFilePath(this, "video" + File.separator + "cache"));
//        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "video" + File.separator + "cache");

        return new HttpProxyCacheServer.Builder(this).maxCacheFilesCount(20).cacheDirectory(dir).build();
    }

    private void setHUAWEIIconBadgeNum(int count) throws Exception {
        Bundle bunlde = new Bundle();
        bunlde.putString("package", getPackageName());
        bunlde.putString("class", "com.reeching.bluegrass.SpActivity");
        bunlde.putInt("badgenumber", count);
        getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
    }

    private void initPush() {
        UMConfigure.setLogEnabled(false);
        UMConfigure.setEncryptEnabled(true);
        UMConfigure.init(this, "58d8eb42f29d986d8b00075a", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "3a63bcbfa9b25ebe8fe24be734e41db0");
        final PushAgent pushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        pushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("dkey", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }

        });
        //自定义行为
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                if (Utils.isAppRunning(context)) {
                    if (uMessage.extra.containsKey("userid")) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("isPush", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PlanInfoActivity.class);
                        String exhibitionid = uMessage.extra.get("exhibitionid");
                        intent.putExtra("id", exhibitionid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } else super.launchApp(context, uMessage);
            }
        };
        pushAgent.setNotificationClickHandler(notificationClickHandler);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 通知的回调方法
             * @param context
             * @param msg
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
//                LogUtils.d("dealWithNotificationMessage:" + msg.message_id + ":" + msg.title);
//                LogUtils.d("dealWithNotificationMessage:" + msg.msg_id + ":" + msg.title);
//                LogUtils.d("dealWithNotificationMessage" + msg.activity);
//                LogUtils.d("dealWithNotificationMessage" + msg.text);
//                LogUtils.d("dealWithNotificationMessage" + msg.after_open);
//                LogUtils.d("dealWithNotificationMessage" + msg.custom);
//                LogUtils.d("dealWithNotificationMessage" + msg.alias + msg.alias.length());
//                LogUtils.d("dealWithNotificationMessage" + msg.extra);
                 /*try {
                    setHUAWEIIconBadgeNum(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                //调用super则会走通知展示流程，不调用super则不展示通知
                LogUtils.d("dealWithNotificationMessage" + quanxian);
                LogUtils.d("dealWithNotificationMessage" + SPUtil.getUserQuanXian(application));
                if ("系统管理员".equals(quanxian) || "系统管理员".equals(SPUtil.getUserQuanXian(application))) {
                    if (msg.extra.containsKey("userid")) {
                        if (SPUtil.getUserIdSp(application).equals(msg.extra.get("userid"))) {
                            super.dealWithNotificationMessage(context, msg);
                        }
                    } else {
                        super.dealWithNotificationMessage(context, msg);
                    }

                }

            }

            @Override
            public Notification getNotification(Context context, UMessage msg) {
                LogUtils.d("getNotification:" + msg.message_id + ":" + msg.title + ":" + msg.custom);
                LogUtils.d("getNotification:" + msg.builder_id);
                return super.getNotification(context, msg);
            }
        };
        pushAgent.setMessageHandler(messageHandler);
        //通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。
        pushAgent.setDisplayNotificationNumber(0);

        pushAgent.setMuteDurationSeconds(5);

        if ("系统管理员".equals(quanxian) || "系统管理员".equals(SPUtil.getUserQuanXian(this))) {
            pushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
            pushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
            pushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        } else {
            pushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
            pushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
            pushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // Enable MultiDex.
    }

    private void initOkGo() {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BASIC);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.WARNING);                               //log颜色级别，决定了log在控制台显示的颜色
        mBuilder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //超时时间设置，默认60秒
        mBuilder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        mBuilder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        mBuilder.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        OkGo.getInstance().init(this)
                .setOkHttpClient(mBuilder.build())
                .setRetryCount(3)
                .setCacheMode(CacheMode.DEFAULT)
                .setCacheTime(100 * 60 * 60 * 24);
    }

    public String getId() {
        return id;
    }

    public boolean isInitflag() {
        return initflag;
    }

    public void setInitflag(boolean initflag) {
        this.initflag = initflag;
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getVersonnum() {
        return versonnum;
    }

    public void setVersonnum(String versonnum) {
        this.versonnum = versonnum;
    }

    public static BaseApplication getInstance() {
        return application;
    }

    public List<Infos> getInfo() {
        return info;
    }

    public void setInfo(List<Infos> info) {
        this.info = info;
    }

    public boolean isHasnet() {
        return hasnet;
    }

    public void setHasnet(boolean hasnet) {
        this.hasnet = hasnet;
    }

    public String getQuanxian() {
        return quanxian;
    }

    public void setQuanxian(String quanxian) {
        this.quanxian = quanxian;
    }
}
