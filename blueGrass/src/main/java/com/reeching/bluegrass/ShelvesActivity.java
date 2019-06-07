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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.BaseApplication;
import com.reeching.adapter.DeleteHuaLangAdapter;
import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ShelvesActivity extends Activity {
    private List<HuaLangShowing.Infos> allinfos;
    private DeleteHuaLangAdapter adapter;
    private int pageNo = 1;
    private ProgressDialog progressDialog;
    HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;
    private TextView mNo_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelves);
        mLV_AfterSalesInfor = (PullToRefreshListView) findViewById(R.id.fragment_maplist_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        mNo_data = (TextView) findViewById(R.id.no_data);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShelvesActivity.this.finish();
            }
        });
        allinfos = new ArrayList<>();
        adapter = new DeleteHuaLangAdapter(allinfos, this);
        mLV_AfterSalesInfor.setAdapter(adapter);
        initdata();
        mLV_AfterSalesInfor.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initdata();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 331) {
            pageNo = 1;
            initdata();
        }
    }

    private void initdata() {
        if (pageNo == 1) {
            progressDialog = new ProgressDialog(ShelvesActivity.this);
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNo + "");
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getdelHuangLang,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(ShelvesActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
                                .show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        HuaLangShowing allHualangInfo = JSON.parseObject(
                                arg0.result, HuaLangShowing.class);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        if (allHualangInfo.getResult().equals("1")) {
                            if (pageNo == 1) allinfos.clear();
                            pageNo++;
                            allinfos.addAll(allHualangInfo.getInfos());
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);
                        } else {
                            ToastUtil.showToast(ShelvesActivity.this, getString(R.string.no_more_data));
                            if (pageNo == 1) {
                                allinfos.clear();
                                adapter.notifyDataSetChanged();
                                mNo_data.setVisibility(View.VISIBLE);
                            }
                        }
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }
                });

    }

}
