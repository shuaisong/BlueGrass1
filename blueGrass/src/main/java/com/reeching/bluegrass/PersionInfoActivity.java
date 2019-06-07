package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.reeching.bluegrass.MyOrientaionListener.OnOrientationListener;
import com.reeching.utils.LogUtils;
import com.reeching.utils.ToastUtil;

public class PersionInfoActivity extends Activity {
    MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private boolean isFirst = true;
    private Double mlatitude;
    private Double mlongitude;
    private BitmapDescriptor micondirection;
    private MyOrientaionListener myOrientaionListener;
    private float mcurrentx;
    private BitmapDescriptor mMarker;
    private Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_persion_info);
        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String lng = intent.getStringExtra("lng");
        LogUtils.d("lat:" + lat + "lng:" + lng);
        try {
            if (lat.isEmpty() || lng.isEmpty()) {
                ToastUtil.showToast(this, "位置信息错误");
                this.lng = 116.40386;
                this.lat = 39.915498;
            } else {
                this.lat = Double.valueOf(lat);
                this.lng = Double.valueOf(lng);
            }
        } catch (Exception e) {
            ToastUtil.showToast(this, "位置信息错误");
            this.lng = 116.40386;
            this.lat = 39.915498;
        }

        Button btn = (Button) findViewById(R.id.map_persioninfo_location_returnlocation);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        TextView tvname = (TextView) findViewById(R.id.persioninfo_name);
        TextView tvphone = (TextView) findViewById(R.id.persioninfo_phone);
        TextView tvaddress = (TextView) findViewById(R.id.persioninfo_aire);
        mMapView = (MapView) findViewById(R.id.map_persioninfo_location_mapview);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mLocationClient = new LocationClient(this);
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setScanSpan(3000);// 设置发起定位请求的间隔时间为1000ms
        mMapView.showZoomControls(false);
        mLocationClient.setLocOption(option);
        micondirection = BitmapDescriptorFactory
                .fromResource(R.drawable.zhixiang);
        myOrientaionListener = new MyOrientaionListener(this);
        myOrientaionListener
                .setOnOrientationListener(new OnOrientationListener() {

                    @Override
                    public void onOrientationChanged(float x) {
                        mcurrentx = x;
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
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PersionInfoActivity.this.finish();
            }
        });
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        addoverlays();
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Bundle bundle = arg0.getExtraInfo();
                String name = bundle.getString("name");
                // Toast.makeText(getActivity(), name, 0).show();
                InfoWindow infoWindow;
                TextView tv = new TextView(PersionInfoActivity.this);
                tv.setBackgroundResource(R.drawable.location_tips);
                tv.setPadding(30, 20, 50, 30);
                tv.setText(name);
                final LatLng latLng = arg0.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
                p.y -= 47;
                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);

                infoWindow = new InfoWindow(tv, ll, -47);
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        });
        tvname.setText(intent.getStringExtra("name"));
        tvphone.setText(intent.getStringExtra("phone"));
        tvaddress.setText(intent.getStringExtra("address"));
    }

    private void addoverlays() {
        mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        latLng = new LatLng(lat, lng);
        options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);

        marker = (Marker) mBaiduMap.addOverlay(options);
        Bundle bundle = new Bundle();
        bundle.putString("name", "我的区域");
        marker.setExtraInfo(bundle);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);

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

        isFirst = true;

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
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        super.onDestroy();
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
}
