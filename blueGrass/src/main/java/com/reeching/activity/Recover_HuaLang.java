package com.reeching.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.reeching.BaseApplication;
import com.reeching.adapter.GlideLoader;
import com.reeching.adapter.NetImgGridAdapter;
import com.reeching.bean.HuaLangShowing;
import com.reeching.bean.UploadBean;
import com.reeching.bluegrass.MyOrientaionListener;
import com.reeching.bluegrass.NoScrollGridView;
import com.reeching.bluegrass.PicViewActivity;
import com.reeching.bluegrass.PicViewActivityTemp;
import com.reeching.bluegrass.R;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.JsonCallback;
import com.reeching.utils.ToastUtil;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Recover_HuaLang extends Activity implements View.OnClickListener {

    private Button recover;
    private NoScrollGridView lin;
    private NoScrollGridView gv_share_photo;
    private EditText etlivetime;
    private EditText ettheme;
    private EditText etaddress;
    private EditText etpeople;
    private EditText etphone;
    private EditText etexplain;
    private EditText etdutyman;
    private EditText etmanagetype;
    private EditText etarea;
    private EditText etstyle;
    private EditText etregisteraddress;
    private TextView etstate;
    private TextView ettype;
    private TextView mQujianText;
    private RadioGroup rgp;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private BitmapDescriptor micondirection;
    private BitmapDescriptor mMarker;
    private float mcurrentx;
    private double latitude;
    private String address;
    private String carelevel;
    private double mlatitude;
    private double mlongitude;
    private boolean isFirst;
    private PopupWindow pop;
    private LinearLayout ll_popup;
    private GridAdapter adapter;
    private String status;
    private String qujianHaoType;
    private String enterpriseType;
    private double mlat;
    private double mlng;
    private MyOrientaionListener myOrientaionListener;
    private ArrayList<String> list = new ArrayList<>();
    private String id;
    private double longitude;
    private ArrayList<String> mSplitAblum;
    private MyLocationListener mMyLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_recover_hua_lang);
        BimpHandler.tempSelectBitmap.clear();
        BimpHandler.mPhotoNum.clear();
        BimpHandler.haveCompress.clear();
        HuaLangShowing.Infos infos = (HuaLangShowing.Infos) getIntent().getSerializableExtra("info");
        initView();
        initData(infos);
        initPopupWindow();
    }

    private void initView() {
        recover = findViewById(R.id.alter_hualang_alter);
        TextView comeback = findViewById(R.id.comeback);
        lin = findViewById(R.id.alter_hualang_lin);
        gv_share_photo = findViewById(R.id.alter_hualang_share_photo);
        etlivetime = findViewById(R.id.alter_hualang_livetime);
        ettheme = findViewById(R.id.alter_hualang_name);
        etaddress = findViewById(R.id.alter_hualang_location);
        etpeople = findViewById(R.id.alter_hualang_people);
        etphone = findViewById(R.id.alter_hualang_phone);
        etexplain = findViewById(R.id.alter_hualang_explain);
        etdutyman = findViewById(R.id.alter_hualang_dutyman);
        etmanagetype = findViewById(R.id.alter_hualang_managetype);
        etarea = findViewById(R.id.alter_hualang_area);
        etstyle = findViewById(R.id.alter_hualang_style);
        etregisteraddress = findViewById(R.id.alter_hualang_ddress);
        etstate = findViewById(R.id.alter_hualang_state);
        ettype = findViewById(R.id.alter_hualang_type);
        mQujianText = findViewById(R.id.alter_hualang_qujian);
        rgp = findViewById(R.id.alter_hualang_rg);
        Button btnreturn = findViewById(R.id.map_alterhualang_returnlocation);
        mMapView = findViewById(R.id.map_alterhualang_mapview);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        mMapView.showZoomControls(true);
        mLocationClient.setLocOption(option);
        micondirection = BitmapDescriptorFactory
                .fromResource(R.drawable.zhixiang);
        myOrientaionListener = new MyOrientaionListener(this);
        etexplain.setOnTouchListener(new View.OnTouchListener() {
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
        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etstate.setOnClickListener(this);
        mQujianText.setOnClickListener(this);

        ettype.setOnClickListener(this);
        myOrientaionListener
                .setOnOrientationListener(new MyOrientaionListener.OnOrientationListener() {

                    @Override
                    public void onOrientationChanged(float x) {
                        mcurrentx = x;
                    }
                });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                latitude = arg0.latitude;
                longitude = arg0.longitude;
                System.out.println("latitude=" + latitude + ",longitude="
                        + longitude);
                // 先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(mMarker);
                // 在地图上添加Marker，并显示
                mBaiduMap.addOverlay(options);
                // 实例化一个地理编码查询对象
                GeoCoder geoCoder = GeoCoder.newInstance();
                // 设置反地理编码位置坐标
                ReverseGeoCodeOption op = new ReverseGeoCodeOption();
                op.location(arg0);
                // 发起反地理编码请求(经纬度->地址信息)
                geoCoder.reverseGeoCode(op);
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetReverseGeoCodeResult(
                            ReverseGeoCodeResult arg0) {
                        // 获取点击的坐标地址
                        address = arg0.getAddress();
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
            }
        });
        btnreturn.setOnClickListener(this);
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                InfoWindow infoWindow;
                TextView tv = new TextView(Recover_HuaLang.this);
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundResource(R.drawable.location_tips);
                tv.setPadding(30, 20, 45, 30);
                tv.setText(address);
                final LatLng latLng = arg0.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
                p.y -= 47;
                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
                infoWindow = new InfoWindow(tv, ll, -47);
                mBaiduMap.showInfoWindow(infoWindow);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.setMapStatus(u);
                etaddress.setText(address);
                return true;
            }
        });

        initrgp();
        recover.setOnClickListener(this);
    }

    private void initrgp() {
        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.alter_hualang_rbtnred:
                        carelevel = 2 + "";
                        break;
                    case R.id.alter_hualang_rbtnyellow:
                        carelevel = 1 + "";
                        break;
                    case R.id.alter_hualang_rbtn_green:
                        carelevel = 0 + "";
                        break;
                }
            }
        });
    }

    private void initData(HuaLangShowing.Infos infos) {
        id = infos.getId();
        ettheme.setText(infos.getName());
        etlivetime.setText(infos
                .getLiveTime());
        etaddress.setText(null == infos
                .getAddress() ? "" : infos
                .getAddress());
        etpeople.setText(infos
                .getLinkMan());
        etphone.setText(infos.getMobile());
        etexplain.setText(infos
                .getDescription());
        etdutyman.setText(infos
                .getLegalPerson());
        etarea.setText(infos.getArea());
        etstyle.setText(infos.getStyle());
        etregisteraddress.setText(infos
                .getRegistAddress());

        mQujianText.setText(qujianHao[Integer.parseInt(infos.getAreaNo()) - 1]);
        qujianHaoType = infos.getAreaNo();
        etmanagetype.setText(infos
                .getManageType());

        int i = Integer.parseInt(infos
                .getEnterpriseType());
        enterpriseType = i + "";
        ettype.setText(type[i]);

        int j = Integer.parseInt(infos
                .getStatus());
        etstate.setText(state[j]);
        status = j + "";

        switch (infos.getCareLevel()) {
            case "0":
                rgp.check(R.id.alter_hualang_rbtn_green);
                carelevel = "0" + "";
                break;
            case "1":
                rgp.check(R.id.alter_hualang_rbtnyellow);
                carelevel = "1" + "";
                break;
            default:
                rgp.check(R.id.alter_hualang_rbtnred);
                carelevel = "2" + "";
                break;
        }
        mlat = Double.parseDouble(infos
                .getMapLat());
        mlng = Double.parseDouble(infos
                .getMapLng());
        if (!"".equals(infos.getPhoto())) {
            String sourceStr = infos
                    .getPhoto();
            String[] sourceStrArray = sourceStr
                    .split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        addoverlays();
        final NetImgGridAdapter adapter2 = new NetImgGridAdapter(this, list);
        lin.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null) {
                    Intent intent = new Intent(Recover_HuaLang.this,
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
    }

    private void addoverlays() {
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        latLng = new LatLng(mlat, mlng);
        options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
        marker = (Marker) mBaiduMap.addOverlay(options);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10 || requestCode == 100) {
                final String fileName = String.valueOf(System.currentTimeMillis());

                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                if (pathList == null) {
                    return;
                }
                if (mSplitAblum == null) mSplitAblum = new ArrayList<>(9);
                else mSplitAblum.clear();
                for (String path : pathList) {
                    if (!BimpHandler.tempAddPhoto.contains(path)) {
                        BimpHandler.tempAddPhoto.add(path);
                    }
                    mSplitAblum.add(path);
                }

                if (mSplitAblum.size() > 0) {
                    for (String aSplitAblum : mSplitAblum) {
                        // 保存到照片列表里
                        // 保存到文件夹
                        // 图片在保存时直接进行压缩
                        File file2 = BitmapUtils.commpressImage2(aSplitAblum);
                        Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                        CameraImage takePhoto = new CameraImage();
                        takePhoto.setBitmap(bitmap);
                        takePhoto.setImagePath(file2.getAbsolutePath());
                        BimpHandler.tempSelectBitmap.add(takePhoto);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mcurrentx).accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);
            mlatitude = location.getLatitude();
            mlongitude = location.getLongitude();
            MyLocationConfiguration configuration = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL,
                    true, micondirection);
            mBaiduMap.setMyLocationConfigeration(configuration);
            if (isFirst) {
                LatLng latLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirst = false;
            }
        }
    }

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
        Button bt3 = view.findViewById(R.id.item_popupwindows_cancel);
        // 选择相册
        bt2.setOnClickListener(this);
        // 取消
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        adapter = new GridAdapter(this);
        gv_share_photo.setAdapter(adapter);
        gv_share_photo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == BimpHandler.tempSelectBitmap.size()) {
                    return false;
                } else {
                    BimpHandler.tempSelectBitmap.remove(position);
                    pathPhoto.remove(position);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
        });
        gv_share_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // 初次进来都为0
                if (position == BimpHandler.tempSelectBitmap.size()) {
                    openCameraPopupWindow();
                } else {
                    // 执行浏览照片操作
                    Intent intent = new Intent(Recover_HuaLang.this,
                            PicViewActivityTemp.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                this, R.anim.activity_translate_in));
        pop.showAtLocation(this.findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
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

    private ArrayList<String> pathPhoto = new ArrayList<>();
    private String[] state = new String[]{"歇业中", "运营中"};
    private String[] stateid = new String[]{"0", "1"};
    private String[] type = new String[]{"有限责任", "股份有限", "私营企业"};
    private String[] qujianHao = new String[]{"A区间", "B区间", "C区间", "D区间", "E区间", "F区间"};
    private String[] qujianHaoId = new String[]{"1", "2", "3", "4", "5", "6"};
    private String[] typeid = new String[]{"0", "1", "2"};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alter_hualang_state:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        this);
                builder.setTitle("请选择运营状态");
                builder.setSingleChoiceItems(state, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                etstate.setText(state[which]);
                                status = stateid[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
                break;
            case R.id.alter_hualang_qujian:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                        this);
                builder1.setTitle("请选择区间号");
                builder1.setSingleChoiceItems(qujianHao, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mQujianText.setText(qujianHao[which]);
                                qujianHaoType = qujianHaoId[which];
                                dialog.dismiss();
                            }
                        });
                builder1.show();
                break;
            case R.id.alter_hualang_type:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(
                        this);
                builder2.setTitle("请选择经营类型");
                builder2.setSingleChoiceItems(type, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ettype.setText(type[which]);
                                enterpriseType = typeid[which];
                                dialog.dismiss();
                            }
                        });
                builder2.show();
                break;
            case R.id.map_alterhualang_returnlocation:
                LatLng latLng = new LatLng(mlatitude, mlongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                break;
            case R.id.alter_hualang_alter:
                if ("".equals(ettheme.getText().toString())
                        || "".equals(etaddress.getText().toString())
                        || (rgp.getCheckedRadioButtonId() == -1)
                        || "".equals(etstate.getText().toString())
                        || "".equals(ettype.getText().toString())
                        || "".equals(mQujianText.getText().toString())) {
                    Toast.makeText(this, "请填写完整信息!", Toast.LENGTH_SHORT)
                            .show();
                    recover.setEnabled(true);
                } else if (latitude <= 1 || longitude <= 1) {
                    ToastUtil.showToast(Recover_HuaLang.this, "坐标不准确，请重新定位");
                } else {
                    recover();
                }
                break;
            case R.id.item_popupwindows_Photo:
                getPhoto(10);
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
        }
    }

    private void recover() {
        recover.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中,请稍侯...");
        progressDialog.show();
        PostRequest<UploadBean> request = OkGo.<UploadBean>post(HttpApi.ip + HttpApi.keeporalterhualang)
                .params("id", id)
                .params("name", ettheme.getText().toString())
                .params("linkMan", etpeople.getText().toString())
                .params("mobile", etphone.getText().toString())
                .params("mapLng", longitude + "")
                .params("mapLat", latitude + "")
                .params("address", etaddress.getText()
                        .toString())
                .params("status", status)
                .params("description", etexplain.getText()
                        .toString())
                .params("areaNo", qujianHaoType)
                .params("registAddress", etregisteraddress
                        .getText().toString())
                .params("legalPerson", etdutyman.getText()
                        .toString())
                .params("enterpriseType", enterpriseType)
                .params("manageType", etmanagetype.getText()
                        .toString())
                .params("area", etarea.getText().toString())
                .params("style", etstyle.getText().toString())
                .params("liveTime", etlivetime.getText()
                        .toString())
                .params("careLevel", carelevel)
                .params("delFlag", "00")
                .params("userId", BaseApplication.getInstance()
                        .getId());
        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
            String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());
            request.params("postsPic" + (i + 1), new File(
                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
            request.params("uploadType" + (i + 1), uploadType);
        }
        request.execute(new JsonCallback<UploadBean>(UploadBean.class) {
            @Override
            public void onSuccess(Response<UploadBean> response) {
                progressDialog.dismiss();
                recover.setEnabled(true);
                if (response.body() != null && response.body().getResult().equals("1")) {
                    Toast.makeText(Recover_HuaLang.this, "保存成功！",
                            Toast.LENGTH_SHORT).show();
                    recover.setEnabled(true);
                    BimpHandler.tempSelectBitmap.clear();
                    setResult(331);
                    finish();
                } else {
                    Toast.makeText(Recover_HuaLang.this, "保存失败！",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<UploadBean> response) {
                super.onError(response);
                recover.setEnabled(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
        myOrientaionListener.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        isFirst = true;
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 停止定位 并且设置不可定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //
        myOrientaionListener.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mLocationClient != null && mMyLocationListener != null)
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            if (BimpHandler.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (BimpHandler.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.share_photo_gvitem,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 和数量相等的position图片设置为添加样式 如果position等于9 说明已经最大 则隐藏
            if (position == BimpHandler.tempSelectBitmap.size()) {
                // 超过6张隐藏添加照片按钮
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                } else {
                    holder.image.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.icon_addpic_unfocused));
                }
            } else {
                holder.image.setImageBitmap(BimpHandler.tempSelectBitmap.get(
                        position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }
    }
}