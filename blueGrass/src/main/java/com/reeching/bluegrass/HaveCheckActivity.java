package com.reeching.bluegrass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import com.reeching.activity.SearchExhibition;
import com.reeching.adapter.HaveCheckAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

//已检查列表
public class HaveCheckActivity extends Activity implements View.OnClickListener {
    private List<ExhibitionBean.InfosBean> alllists;
    private HaveCheckAdapter adapter;
    HttpUtils hu = new HttpUtils();
    private int pageNo = 1;
    private ProgressDialog progressDialog;
    private PullToRefreshListView mLV_AfterSalesInfor;
    private TextView mNo_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_have_check);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        TextView search = (TextView) findViewById(R.id.search);
        mNo_data = (TextView) findViewById(R.id.no_data);
        mLV_AfterSalesInfor = (PullToRefreshListView) findViewById(R.id.Hualang_havecheck_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        alllists = new ArrayList<>();
        adapter = new HaveCheckAdapter(alllists, this);
        mLV_AfterSalesInfor.setAdapter(adapter);
        initdata();
        comeback.setOnClickListener(this);
        search.setOnClickListener(this);

        mLV_AfterSalesInfor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(HaveCheckActivity.this,
                        HaveCheckInfoActivity.class);
                ExhibitionBean.InfosBean infos = alllists.get(position - 1);
                intent.putExtra("id", infos.getId());
                startActivity(intent);
            }

        });
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

    private void initdata() {
        if (pageNo == 1) {
            progressDialog = new ProgressDialog(HaveCheckActivity.this);
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        final RequestParams params = new RequestParams();
        String id = BaseApplication.getInstance().getId();
        params.addQueryStringParameter("userId", id);
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNo + "");
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.havecheck, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(HaveCheckActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
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
                        ExhibitionBean showing = JSON.parseObject(arg0.result,
                                ExhibitionBean.class);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        if (showing.getResult().equals("1")) {
                            if (pageNo == 1) alllists.clear();
                            pageNo++;
                            alllists.addAll(showing.getInfos());
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(alllists.size() - 10);
                        } else {
                            ToastUtil.showToast(HaveCheckActivity.this, getString(R.string.no_more_data));
                            if (pageNo == 1) {
                                alllists.clear();
                                adapter.notifyDataSetChanged();
                                mNo_data.setVisibility(View.VISIBLE);
                            }
                        }
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comeback:
                finish();
                break;
            case R.id.search:
                Intent intent = new Intent(this, SearchExhibition.class);
                intent.putExtra("NO", 5);
                startActivity(intent);
                break;
        }
    }
}
