package com.reeching.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.reeching.BaseApplication;
import com.reeching.adapter.WellDoneAdapter;
import com.reeching.bean.ZhanlanBean;
import com.reeching.bean.ZhanlanBean.Infos;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class WellDoneFragment extends Fragment {
    private List<Infos> alllist;
    private WellDoneAdapter adapter;
    private PullToRefreshListView mLV_AfterSalesInfor;
    private TextView tv;
    private int pageNO = 1;
    HttpUtils hu = new HttpUtils();
    private ProgressDialog progressDialog;
    private boolean isFirst = true;
    private boolean viewCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welldone, null);
        mLV_AfterSalesInfor = (PullToRefreshListView) view
                .findViewById(R.id.welldone_lv);
        tv = (TextView) view.findViewById(R.id.welldone_tv);
        mLV_AfterSalesInfor.setMode(Mode.BOTH);
        alllist = new ArrayList<>();
        adapter = new WellDoneAdapter(alllist, getActivity());
        mLV_AfterSalesInfor.setAdapter(adapter);
//        initdata();
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
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirst && viewCreated) {
            initdata();
            isFirst = false;
        }
    }

    private void initdata() {
        if (pageNO == 1 && mLV_AfterSalesInfor.getState() == PullToRefreshBase.State.RESET) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        hu.configSoTimeout(30 * 1000);
        hu.configTimeout(30 * 1000);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNO + "");
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.welldone, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        if (arg0.getExceptionCode() == 500) {
                            Toast.makeText(getContext(), "服务器出错！已完成案件无法显示", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "网络出错！请稍候再试", Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        String a = arg0.result;
                        ZhanlanBean bean = JSON.parseObject(a,
                                ZhanlanBean.class);
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        if (bean.getResult().equals("1")) {
                            if (pageNO == 1) alllist.clear();
                            alllist.addAll(bean.getInfos());
                            pageNO++;
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(alllist.size() - 10);
                        } else {
                            ToastUtil.showToast(getActivity(), getString(R.string.no_more_data));
                            if (pageNO == 1) {
                                tv.setVisibility(View.VISIBLE);
                                alllist.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }
                });

    }

}
