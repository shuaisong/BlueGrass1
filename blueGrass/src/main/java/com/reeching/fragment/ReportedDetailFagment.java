package com.reeching.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.reeching.bluegrass.HaveReportedActivity;
import com.reeching.bluegrass.PlanInfoActivity;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class ReportedDetailFagment extends Fragment implements AdapterView.OnItemClickListener {

    private ZhanLanAdapter adapter;
    private List<ExhibitionBean.InfosBean> allinfos;

    private int pageNO = 1;
    HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;
    private ProgressDialog progressDialog;
    private boolean isFirst = true;
    private boolean viewCreated = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailreported, container, false);

        mLV_AfterSalesInfor = (PullToRefreshListView) view
                .findViewById(R.id.detailreported_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.BOTH);
        allinfos = new ArrayList<>();
        adapter = new ZhanLanAdapter(allinfos, getActivity());
        mLV_AfterSalesInfor.setAdapter(adapter);
        //initdata();
        mLV_AfterSalesInfor
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        pageNO = 1;
                        initdata();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        initdata();
                    }
                });
        viewCreated = true;
        mLV_AfterSalesInfor.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser && viewCreated) {
            initdata();
            isFirst = false;
        }

    }
/* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 331) {
            i = 2;
            initdata();
        }
    }*/

    private void initdata() {
        if (pageNO == 1) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pageSize", 10 + "");
        params.addQueryStringParameter("pageNo", pageNO + "");
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallzhanlan, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        mLV_AfterSalesInfor.onRefreshComplete();
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(getActivity(), "请检查网络！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ExhibitionBean bean = JSON.parseObject(arg0.result,
                                ExhibitionBean.class);
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        if (bean.getResult().equals("1")) {
                            if (pageNO == 1) allinfos.clear();
                            allinfos.addAll(bean.getInfos());
                            pageNO++;
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);
                        } else {
                            Toast.makeText(getActivity(),
                                    "没有更多数据！", Toast.LENGTH_SHORT).show();
                        }
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }
                });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*Intent intent = new Intent(getActivity(),
                HaveReportedActivity.class);
        ExhibitionBean.InfosBean infos = allinfos.get(position - 1);
        intent.putExtra("info", infos);
        startActivity(intent);*/
        ExhibitionBean.InfosBean infos = allinfos.get(position - 1);
        Intent intent;
        if (BaseApplication.getInstance().getQuanxian().equals("系统管理员") || infos.getUserId().equals(BaseApplication.getInstance().getId())) {
            intent = new Intent(getActivity(),
                    PlanInfoActivity.class);
            intent.putExtra("id", infos.getId());
            startActivity(intent);
        } else {
            intent = new Intent(getActivity(),
                    HaveReportedActivity.class);
            intent.putExtra("info", infos);
            startActivity(intent);
        }
    }
}
