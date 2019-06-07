package com.reeching.bluegrass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.reeching.adapter.ZhanLanAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ReportedDetailActivity extends Activity implements AdapterView.OnItemClickListener {
    private ZhanLanAdapter adapter;
    private List<ExhibitionBean.InfosBean> allinfos;
    private ProgressDialog progressDialog;
    private int pageNo = 1;
    HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;
    private TextView mNo_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        mLV_AfterSalesInfor = (PullToRefreshListView)
                findViewById(R.id.detailreported_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        TextView comBack = (TextView) findViewById(R.id.comeback);
        mNo_data = (TextView) findViewById(R.id.no_data);
        comBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportedDetailActivity.this.finish();
            }
        });
        allinfos = new ArrayList<>();
        adapter = new ZhanLanAdapter(allinfos, this);
        mLV_AfterSalesInfor.setAdapter(adapter);
        initdata();

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

        mLV_AfterSalesInfor.setOnItemClickListener(this);
    }


    private void initdata() {
        if (pageNo == 1 && mLV_AfterSalesInfor.getState() == PullToRefreshBase.State.RESET) {
            progressDialog = new ProgressDialog(ReportedDetailActivity.this);
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNo + "");
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallzhanlan, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(ReportedDetailActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ExhibitionBean bean = JSON.parseObject(arg0.result,
                                ExhibitionBean.class);
                        progressDialog.dismiss();
                        if (bean.getResult().equals("1")) {
                            if (pageNo == 1) allinfos.clear();
                            pageNo++;
                            allinfos.addAll(bean.getInfos());
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);
                        } else {
                            ToastUtil.showToast(ReportedDetailActivity.this, getString(R.string.no_more_data));
                            if (pageNo == 1) {
                                mNo_data.setVisibility(View.VISIBLE);
                                allinfos.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 331) {
            pageNo = 1;
            initdata();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExhibitionBean.InfosBean infos = allinfos.get(position - 1);
        Intent intent;
        if (BaseApplication.getInstance().getQuanxian().equals("系统管理员") || infos.getUserId().equals(BaseApplication.getInstance().getId())) {
            intent = new Intent(this,
                    PlanInfoActivity.class);
            intent.putExtra("id", infos.getId());
            startActivity(intent);
        } else {
            intent = new Intent(this,
                    HaveReportedActivity.class);
            intent.putExtra("info", infos);
            startActivity(intent);
        }
    }
}
