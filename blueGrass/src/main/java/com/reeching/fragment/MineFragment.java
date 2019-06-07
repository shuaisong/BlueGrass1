package com.reeching.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.reeching.BaseApplication;
import com.reeching.bluegrass.HaveCheckActivity;
import com.reeching.bluegrass.HaveVerificationActivity;
import com.reeching.bluegrass.HuaLangAllHistoryActivity;
import com.reeching.bluegrass.HuaLangPlanActivity;
import com.reeching.bluegrass.HuaLangShowingActivity;
import com.reeching.bluegrass.LoginActivity;
import com.reeching.bluegrass.MatterActivity;
import com.reeching.bluegrass.PersionInfoActivity;
import com.reeching.bluegrass.R;
import com.reeching.bluegrass.ReportedDetailActivity;
import com.reeching.bluegrass.ShelvesActivity;
import com.reeching.bluegrass.WaitForHeChaActivity;
import com.reeching.utils.HttpApi;
import com.reeching.utils.LogUtils;
import com.reeching.utils.SPUtil;
import com.reeching.utils.ToastUtil;
import com.reeching.utils.UpdateAppManager;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

public class MineFragment extends Fragment implements OnClickListener {
    private TextView tvname;
    private TextView tvphone;
    private TextView tvaddress;
    private TextView tvone;
    private TextView tvtwo;
    private TextView tvthree;
    private TextView tvfour;
    private TextView tvfive;
    private TextView tvsix;
    private TextView tvseven;
    private String name, address, phone, lat, lng;
    private String murl;
    private UpdateAppManager updateManager;
    HttpUtils hu = new HttpUtils(20000);//20s超时
    private TextView mTveight;
    private TextView mTvnine;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.fragment_mine_iv);
        tvname = (TextView) view.findViewById(R.id.fragment_mine_name);
        tvphone = (TextView) view.findViewById(R.id.fragment_mine_phonenum);
        tvaddress = (TextView) view.findViewById(R.id.fragment_mine_address);
        tvone = (TextView) view.findViewById(R.id.fragment_mine_one);
        tvtwo = (TextView) view.findViewById(R.id.fragment_mine_two);
        tvthree = (TextView) view.findViewById(R.id.fragment_mine_three);
        tvfour = (TextView) view.findViewById(R.id.fragment_mine_four);
        TextView tvnine = (TextView) view.findViewById(R.id.fragment_mine_tvnine);
        tvfive = (TextView) view.findViewById(R.id.fragment_mine_five);
        tvsix = (TextView) view.findViewById(R.id.fragment_mine_six);
        tvseven = (TextView) view.findViewById(R.id.fragment_mine_seven);
        mTveight = (TextView) view.findViewById(R.id.fragment_mine_eight);
        mTvnine = (TextView) view.findViewById(R.id.fragment_mine_nine);
        TextView tvten = (TextView) view.findViewById(R.id.fragment_mine_tvfive);
        TextView tvelven = (TextView) view.findViewById(R.id.fragment_mine_tvsix);
        TextView tvtwelve = (TextView) view.findViewById(R.id.fragment_mine_tvseven);
        TextView tvexit = (TextView) view.findViewById(R.id.fragment_exit);
        TextView tvupdated = (TextView) view.findViewById(R.id.fragment_updated);
        tvupdated.setText(String.format(getResources().getString(R.string.updated), BaseApplication.getInstance().getVersonnum()));
        tvupdated.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                updata();
            }
        });
        tvone.setOnClickListener(this);
        tvtwo.setOnClickListener(this);
        tvthree.setOnClickListener(this);
        tvfour.setOnClickListener(this);
        tvfive.setOnClickListener(this);
        tvsix.setOnClickListener(this);
        tvseven.setOnClickListener(this);
        mTveight.setOnClickListener(this);
        mTvnine.setOnClickListener(this);

        tvexit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("退出登录!");
                builder.setMessage("确定要退出登录吗?");
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        });
                builder.setPositiveButton("退出",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                PushAgent pushAgent = PushAgent.getInstance(MineFragment.this.getActivity());
                                pushAgent.deleteAlias(BaseApplication.getInstance().getId(), BaseApplication.getInstance().getId(), new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean b, String s) {
                                        LogUtils.d("deleteAlias:" + b + ":" + s);
                                    }
                                });
                                SPUtil.putPassSP("", getActivity());
                                SPUtil.putUserSP("", getActivity());
                                BaseApplication.getInstance().setQuanxian("上报用户");
                                SPUtil.putUserQuanXian("上报用户", getActivity());
                                startActivity(new Intent(getActivity(),
                                        LoginActivity.class));
                                getActivity().finish();
                            }
                        });
                builder.show();

            }
        });

        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (tvname.getText().toString().equals(getString(R.string.no_log_in))) {
                    startActivity(new Intent(getActivity(),
                            LoginActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(),
                            PersionInfoActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("phone", phone);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    startActivity(intent);
                }
            }
        });

        getpersionfo();
        if (BaseApplication.getInstance().getQuanxian().equals("系统管理员")) {
            mTvnine.setVisibility(View.VISIBLE);
            tvfive.setVisibility(View.VISIBLE);
            tvsix.setVisibility(View.VISIBLE);
            tvseven.setVisibility(View.VISIBLE);
            tvnine.setVisibility(View.VISIBLE);
            tvten.setVisibility(View.VISIBLE);
            tvelven.setVisibility(View.VISIBLE);
            tvtwelve.setVisibility(View.VISIBLE);
        } else if (BaseApplication.getInstance().getQuanxian().equals("普通用户")) {
            mTvnine.setVisibility(View.GONE);
            tvnine.setVisibility(View.GONE);
        } else {
            mTvnine.setVisibility(View.GONE);
            tvfive.setVisibility(View.GONE);
            // tvsix.setVisibility(View.GONE);
            tvseven.setVisibility(View.GONE);
            tvnine.setVisibility(View.GONE);
            tvten.setVisibility(View.GONE);
//            tvelven.setVisibility(View.GONE);
            tvtwelve.setVisibility(View.GONE);
        }
        if (BaseApplication.getInstance().getId() == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initcount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            updateManager.downloadApp();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            initcount();
    }

    private void getpersionfo() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("loginName", BaseApplication
                .getInstance().getLoginName());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.personalinfo, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Log.i("aaa", arg1);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            JSONObject jsonObject2 = jsonObject
                                    .getJSONObject("info");
                            name = jsonObject2.getString("name");
                            tvname.setText(name);
                            phone = jsonObject2.getString("phone");
                            tvphone.setText(phone);
                            address = jsonObject2.getString("manageArea");
                            tvaddress.setText(address);
                            lat = jsonObject2.getString("mapLat");
                            lng = jsonObject2.getString("mapLng");
                            String ss = jsonObject2.getString("id");
                            BaseApplication.getInstance().setId(ss);
//                            initcount();
                        } else {
                            Log.i("aaa", arg0.result);
                        }

                    }

                });
    }

    private void initcount() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());

        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.count, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject object = JSONObject.parseObject(arg0.result);
                        if (object.getString("result").equals("1")) {
                            tvone.setText(object.getString("beingCount"));
                            tvtwo.setText(object.getString("willCount"));
                            tvthree.setText(object.getString("hadCount"));
                            tvfour.setText(object.getString("reportCount"));
                            tvfive.setText(object.getString("checkCount"));
                            tvsix.setText(object.getString("recheckCount"));
                            tvseven.setText(object.getString("reCount"));
                            mTveight.setText(object.getString("delGalleryCount"));
                            mTvnine.setText(object.getString("questionExhibitionCount"));
                        }
                    }
                });

    }

    private void updata() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("version", BaseApplication.getInstance()
                .getVersonnum());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.updata, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        ToastUtil.showToast(getActivity(), "请检查网络");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject object = JSON.parseObject(arg0.result);
                        if (object.getString("result").equals("2")) {
                            JSONObject obj = object.getJSONObject("info");
                            murl = obj.getString("downAddress");
                            BaseApplication.getInstance().setMurl(murl);
                            updateManager = new UpdateAppManager(getActivity());
                            updateManager.checkUpdateInfo();
                        } else {
                            Toast.makeText(getActivity(), "已是最新版本！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_mine_one:
                Intent intent = new Intent(getActivity(), HuaLangShowingActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_two:
                intent = new Intent(getActivity(), HuaLangPlanActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_three:
                intent = new Intent(getActivity(), HuaLangAllHistoryActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_four:
                intent = new Intent(getActivity(), ReportedDetailActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_five:
                intent = new Intent(getActivity(), HaveCheckActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_six:
                intent = new Intent(getActivity(), HaveVerificationActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_seven:
                intent = new Intent(getActivity(), WaitForHeChaActivity.class);
                startActivity(intent);
//                initcount();
                break;
            case R.id.fragment_mine_eight:
                intent = new Intent(getActivity(), ShelvesActivity.class);
                startActivity(intent);
                initcount();
                break;
            case R.id.fragment_mine_nine:
                intent = new Intent(getActivity(), MatterActivity.class);
                startActivity(intent);
//                initcount();
                break;
        }
    }
}
