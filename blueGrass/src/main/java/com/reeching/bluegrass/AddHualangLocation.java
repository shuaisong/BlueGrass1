package com.reeching.bluegrass;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.BaseApplication;
import com.reeching.adapter.GlideLoader;
import com.reeching.bluegrass.MyOrientaionListener.OnOrientationListener;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;
import com.yancy.imageselector.BimpHandler;
import com.yancy.imageselector.CameraImage;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//添加画廊
public class AddHualangLocation extends Activity {
    MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private boolean isFirst = true;
    private Double mlatitude;
    private PopupWindow pop = null;
    private double latitude;
    private double longitude;
    private Double mlongitude;
    private LinearLayout ll_popup;
    private String address = "";
    private Button btn;
    private GridAdapter adapter;
    private BitmapDescriptor micondirection;
    private MyOrientaionListener myOrientaionListener;
    private float mcurrentx;
    private BitmapDescriptor mMarker;
    private EditText ettheme, etaddress, etpeople, etphone, etremark,
            etdutypeople, etarea, etstyle, etregisteraddress,
            etmanagetype;
    private TextView tvlivetime;
    private NoScrollGridView gv_share_photo;
    private TextView ettype, etstate;
    private RadioGroup rgp;
    private String careLevel;
    private String[] state = new String[]{"歇业中", "运营中"};
    private String[] stateid = new String[]{"0", "1"};
    private String[] type = new String[]{"有限责任", "股份有限", "私营企业"};
    private String[] typeid = new String[]{"0", "1", "2"};
    private String[] qujianHao = new String[]{"A区间", "B区间", "C区间", "D区间", "E区间", "F区间"};
    private String[] qujianHaoId = new String[]{"1", "2", "3", "4", "5", "6"};
    private String status, enterpriseType, qujianHaoType;
    private ProgressDialog progressDialog;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private TextView mQuanjianText;
    private ArrayList<String> mSplitAblum;
    private MyLocationListener mMyLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getApplicationContext());
        BimpHandler.tempSelectBitmap.clear();
        setContentView(R.layout.activity_add_hua_lang);
        BaseApplication.getInstance().setInitflag(true);
        TextView comeback = findViewById(R.id.comeback);
        gv_share_photo = findViewById(R.id.activity_addhualang_share_photo);
        tvlivetime = findViewById(R.id.activity_addhualang_livetime);
        ettheme = findViewById(R.id.activity_addhualang_theme);
        etaddress = findViewById(R.id.activity_addhualang_address);
        etpeople = findViewById(R.id.activity_addhualang_people);
        etphone = findViewById(R.id.activity_addhualang_phone);
        etremark = findViewById(R.id.activity_addhualang_remark);
        etdutypeople = findViewById(R.id.activity_addhualang_legalpeople);
        etarea = findViewById(R.id.activity_addhualang_area);
        etstyle = findViewById(R.id.activity_addhualang_style);
        etregisteraddress = findViewById(R.id.activity_addhualang_registeraddress);
        etmanagetype = findViewById(R.id.activity_addhualang_managetype);
        etstate = findViewById(R.id.activity_addhualang_state);
        ettype = findViewById(R.id.activity_addhualang_type);
        mQuanjianText = findViewById(R.id.add_hualang_qujian);
        rgp = findViewById(R.id.activity_addhualang_rgp);
        Button btnadd = findViewById(R.id.activity_addhualang_btnadd);
        btn = findViewById(R.id.map_hualanglocation_returnlocation);
        mMapView = findViewById(R.id.map_hualanglocation_mapview);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        mMapView.showZoomControls(true);
        mLocationClient.setLocOption(option);
        micondirection = BitmapDescriptorFactory
                .fromResource(R.drawable.zhixiang);

        initPopupWindow();

        mQuanjianText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Builder builder = new Builder(
                        AddHualangLocation.this);
                builder.setTitle("请选择区间号");
                builder.setSingleChoiceItems(qujianHao, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mQuanjianText.setText(qujianHao[which]);
                                qujianHaoType = qujianHaoId[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        tvlivetime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
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

        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AddHualangLocation.this.finish();
            }
        });
        etstate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Builder builder = new Builder(
                        AddHualangLocation.this);
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
            }
        });

        ettype.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Builder builder = new Builder(
                        AddHualangLocation.this);
                builder.setTitle("请选择经营类型");
                builder.setSingleChoiceItems(type, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ettype.setText(type[which]);
                                enterpriseType = typeid[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        myOrientaionListener = new MyOrientaionListener(this);

        myOrientaionListener
                .setOnOrientationListener(new OnOrientationListener() {

                    @Override
                    public void onOrientationChanged(float x) {
                        mcurrentx = x;
                    }
                });
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
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
                        // System.out.println("address=" + address);
                    }

                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult arg0) {
                    }
                });
            }
        });
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(mlatitude, mlongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
            }
        });
        btnadd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btn.setEnabled(false);
                if ("".equals(ettheme.getText().toString())
                        || "".equals(etaddress.getText().toString())
                        || (rgp.getCheckedRadioButtonId() == -1)
                        || "".equals(etstate.getText().toString())
                        || "".equals(ettype.getText().toString())
                        || "".equals(mQuanjianText.getText().toString())) {
                    Toast.makeText(AddHualangLocation.this, "请填写完整信息!", Toast.LENGTH_SHORT)
                            .show();
                    btn.setEnabled(true);
                } else if (latitude <= 0 || longitude <= 0) {
                    ToastUtil.showToast(AddHualangLocation.this, "坐标不准确，请重新定位");
                } else {
                    add();
                }

            }
        });

        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                InfoWindow infoWindow;
                TextView tv = new TextView(AddHualangLocation.this);
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

        rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.activity_addhualang_rb1:
                        careLevel = 2 + "";
                        break;
                    case R.id.activity_addhualang_rb2:
                        careLevel = 1 + "";
                        break;
                    case R.id.activity_addhualang_rb3:
                        careLevel = 0 + "";
                        break;
                }

            }
        });
//		rgp.check(R.id.begintocheck_rb1);

    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置可以定位 并开始定位
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
        // kaiqifangxaingchuanganqi
        myOrientaionListener.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
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

    private void initDate() {

        Calendar calendar = Calendar.getInstance();
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpdialog = new DatePickerDialog(AddHualangLocation.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month,
                                          int day) {

                        tvlivetime.setText(String.format(Locale.CHINESE, "%d-%d-%d", year, month + 1, day));

                    }
                }, year1, month1, day1);

        dpdialog.show();

    }

    private void add() {

        progressDialog = new ProgressDialog(AddHualangLocation.this);
        progressDialog.setMessage("上传中,请稍候...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("areaNo", qujianHaoType);
        params.addQueryStringParameter("name", ettheme.getText().toString());
        params.addQueryStringParameter("linkMan", etpeople.getText().toString());
        params.addQueryStringParameter("mobile", etphone.getText().toString());
        params.addQueryStringParameter("mapLng", longitude + "");
        params.addQueryStringParameter("mapLat", latitude + "");
        params.addQueryStringParameter("address", etaddress.getText()
                .toString());
        params.addQueryStringParameter("status", status);
        params.addQueryStringParameter("description", etremark.getText()
                .toString());
        params.addQueryStringParameter("registAddress", etregisteraddress
                .getText().toString());
        params.addQueryStringParameter("legalPerson", etdutypeople.getText()
                .toString());
        params.addQueryStringParameter("enterpriseType", enterpriseType);
        params.addQueryStringParameter("manageType", etmanagetype.getText()
                .toString());
        params.addQueryStringParameter("area", etarea.getText().toString());
        params.addQueryStringParameter("style", etstyle.getText().toString());
        params.addQueryStringParameter("liveTime", tvlivetime.getText()
                .toString());
        params.addQueryStringParameter("careLevel", careLevel);
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
            String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());
            params.addBodyParameter("postsPic" + (i + 1), new File(
                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
            params.addBodyParameter("uploadType" + (i + 1), uploadType);

        }
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.keeporalterhualang,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        btn.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        progressDialog.dismiss();
                        JSONObject object = JSON.parseObject(arg0.result);
                        if (object.getString("result").equals("1")) {
                            Toast.makeText(AddHualangLocation.this, "保存成功！", Toast.LENGTH_SHORT)
                                    .show();
                            BimpHandler.tempSelectBitmap.clear();
                            adapter.notifyDataSetChanged();
                            AddHualangLocation.this.finish();

                        } else {
                            Toast.makeText(AddHualangLocation.this, "保存失败！", Toast.LENGTH_SHORT)
                                    .show();
                            btn.setEnabled(true);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10) {
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
                BimpHandler.tempSelectBitmap.clear();
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

    private void initPopupWindow() {
        pop = new PopupWindow(AddHualangLocation.this);
        View view = LayoutInflater.from(AddHualangLocation.this).inflate(
                R.layout.item_popupwindows, null);
        ll_popup = view.findViewById(R.id.ll_popup);
        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = view.findViewById(R.id.parent);
        Button bt2 = view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = view.findViewById(R.id.item_popupwindows_cancel);
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
                getPhoto(10);
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
        adapter = new GridAdapter(AddHualangLocation.this);
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
        gv_share_photo.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // 初次进来都为0
                if (position == BimpHandler.tempSelectBitmap.size()) {
                    openCameraPopupWindow();
                } else {
                    // 执行浏览照片操作
                    Intent intent = new Intent(AddHualangLocation.this,
                            PicViewActivityTemp.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
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


        ImageSelector.open(AddHualangLocation.this, imageConfig);   // 开启图片选择器
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.share_photo_gvitem,
                        parent, false);
                holder = new ViewHolder();
                holder.image = convertView
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                this, R.anim.activity_translate_in));
        pop.showAtLocation(this.findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
    }

}