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
import com.reeching.adapter.ShowingAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class HuaLangShowingActivity extends Activity {

    private List<ExhibitionBean.InfosBean> alllists;
    private ShowingAdapter adapter;
    private int pageNo = 1;
    private ProgressDialog progressDialog;
    private PullToRefreshListView mLV_AfterSalesInfor;
    private HttpUtils hu;
    private TextView mNo_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hua_lang_showing);
        TextView comeback = findViewById(R.id.comeback);
        mNo_data = findViewById(R.id.no_data);
        mLV_AfterSalesInfor = findViewById(R.id.Hualang_showing_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        alllists = new ArrayList<>();
        adapter = new ShowingAdapter(alllists,
                HuaLangShowingActivity.this);
        mLV_AfterSalesInfor.setAdapter(adapter);

        initdata();

        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HuaLangShowingActivity.this.finish();
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
        mLV_AfterSalesInfor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ExhibitionBean.InfosBean infos = alllists.get(position - 1);
                Intent intent;
                if (BaseApplication.getInstance().getQuanxian().equals("系统管理员") || infos.getUserId().equals(BaseApplication.getInstance().getId())) {
                    intent = new Intent(HuaLangShowingActivity.this,
                            ShowingInfoActivity.class);
                    intent.putExtra("id", infos.getId());
                    startActivity(intent);
                } else {
                    intent = new Intent(HuaLangShowingActivity.this,
                            HaveReportedActivity.class);
                    intent.putExtra("info", infos);
                    startActivity(intent);
                }
            }
        });
    }

    private void initdata() {
        if (pageNo == 1) {
            progressDialog = new ProgressDialog(HuaLangShowingActivity.this);
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        if (hu == null) {
            hu = new HttpUtils();
        }
        RequestParams params = new RequestParams();
        String id = BaseApplication.getInstance().getId();
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNo + "");
        params.addQueryStringParameter("userId", id);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.show, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(HuaLangShowingActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
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
                            if (pageNo == 1) {
                                alllists.clear();
                                adapter.notifyDataSetChanged();
                                mNo_data.setVisibility(View.VISIBLE);
                            }
                            ToastUtil.showToast(HuaLangShowingActivity.this, getString(R.string.no_more_data));
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

}
