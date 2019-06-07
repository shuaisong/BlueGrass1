package com.reeching.bluegrass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.BaseApplication;
import com.reeching.adapter.HechaAdapter;
import com.reeching.bean.HechaInfobean;
import com.reeching.utils.HttpApi;
import com.reeching.utils.LogUtils;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class WaitForHeChaActivity extends Activity {
    private List<HechaInfobean.Infos> mInfos;
    private HechaAdapter adapter;
    private ProgressDialog progressDialog;
    private TextView mNo_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_he_cha);
        ListView lv = (ListView) findViewById(R.id.fragment_waitforhecha_lv);
        mInfos = new ArrayList<>();
        adapter = new HechaAdapter(mInfos, WaitForHeChaActivity.this);
        lv.setAdapter(adapter);
        TextView comeBack
                = (TextView) findViewById(R.id.comeback);
        mNo_data = (TextView) findViewById(R.id.no_data);
        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitForHeChaActivity.this.finish();
            }
        });
        initdata();
    }

    private void initdata() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(WaitForHeChaActivity.this);
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
        HttpUtils hu = new HttpUtils();
//        hu.configSoTimeout(10 * 1000);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());

        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.waitforhecha, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(WaitForHeChaActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
                                .show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        HechaInfobean infobean = JSON.parseObject(arg0.result,
                                HechaInfobean.class);
                        progressDialog.dismiss();
                        if (infobean.getResult().equals("1")) {
                            mInfos.clear();
                            mInfos.addAll(infobean.getInfos());
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(WaitForHeChaActivity.this, getString(R.string.no_more_data));
                            mInfos.clear();
                            adapter.notifyDataSetChanged();
                            mNo_data.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 331) {
            LogUtils.d("onActivityResult");
            initdata();
        }
    }

}
