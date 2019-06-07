package com.reeching.bluegrass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.HistoryAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class HualangHistoryActivity extends Activity {
    private List<ExhibitionBean.InfosBean> alllists;
    private int pageNO = 1;
    private String id;
    HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;
    private HistoryAdapter adapter;
    private TextView mTextNull;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hualang_history);
        TextView comeback = (TextView) findViewById(R.id.comeback);
        mTextNull = (TextView) findViewById(R.id.fragment_waitforhecha_tv);
        mLV_AfterSalesInfor = (PullToRefreshListView) findViewById(R.id.Hualang_history_lv);
        mLV_AfterSalesInfor.setMode(Mode.MANUAL_REFRESH_ONLY);
        id = getIntent().getStringExtra("id");
        alllists = new ArrayList<>();
        adapter = new HistoryAdapter(alllists, this);
        mLV_AfterSalesInfor.setAdapter(adapter);
        initdata();
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HualangHistoryActivity.this.finish();
            }
        });
        mLV_AfterSalesInfor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(HualangHistoryActivity.this,
                        HistoryInfoActivity.class);
                ExhibitionBean.InfosBean infos = alllists.get(position - 1);
                intent.putExtra("info", infos);
                startActivity(intent);
            }

        });
        mLV_AfterSalesInfor
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        initdata();
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 331) {
            pageNO = 1;
            initdata();
        }
    }

    private void initdata() {
        if (pageNO == 1) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNO + "");
        params.addQueryStringParameter("galleryId", id);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.gethistory, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(HualangHistoryActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ExhibitionBean history = JSON.parseObject(arg0.result,
                                ExhibitionBean.class);
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        if (history.getResult().equals("1")) {
                            if (pageNO == 1) alllists.clear();
                            alllists.addAll(history.getInfos());
                            pageNO++;
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(alllists.size() - 10);
                        } else {
                            ToastUtil.showToast(HualangHistoryActivity.this, getString(R.string.no_more_data));
                            mLV_AfterSalesInfor.onRefreshComplete();
                            if (pageNO == 1) {
                                mTextNull.setVisibility(View.VISIBLE);
                            }
                        }
                        if (!alllists.isEmpty()) {
                            mLV_AfterSalesInfor.setMode(Mode.PULL_FROM_END);
                        }
                    }
                });
    }
}
