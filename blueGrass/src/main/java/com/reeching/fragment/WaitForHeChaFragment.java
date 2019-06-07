package com.reeching.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.reeching.adapter.HechaAdapter;
import com.reeching.bean.HechaInfobean;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.reeching.utils.LogUtils;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class WaitForHeChaFragment extends Fragment {
    private PullToRefreshListView lv;
    private List<HechaInfobean.Infos> mInfos;
    private HechaAdapter adapter;
    private TextView tv;
    private HttpUtils hu = new HttpUtils();
    private ProgressDialog progressDialog;
    private boolean isFirst = true;
    private boolean viewCreated = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waitforhecha, container, false);
        lv = (PullToRefreshListView) view.findViewById(R.id.fragment_waitforhecha_lv);
        tv = (TextView) view.findViewById(R.id.fragment_waitforhecha_tv);
//        initdata();
        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mInfos = new ArrayList<>();
        adapter = new HechaAdapter(mInfos, getActivity());
        lv.setAdapter(adapter);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                tv.setVisibility(View.VISIBLE);
                initdata();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 331) {
            initdata();
        }
    }

    private void initdata() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        LogUtils.d("lv.getState()"+lv.getState());
        if (lv.getState() == PullToRefreshBase.State.RESET)
            progressDialog.show();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.waitforhecha, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(getActivity(), "请检查网络！", Toast.LENGTH_SHORT).show();
                        lv.onRefreshComplete();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        HechaInfobean infobean = JSON.parseObject(arg0.result,
                                HechaInfobean.class);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        if (infobean.getResult().equals("1")) {
                            mInfos.clear();
                            mInfos.addAll(infobean.getInfos());
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(getActivity(), getString(R.string.no_more_data));
                            tv.setVisibility(View.VISIBLE);
                        }
                        lv.onRefreshComplete();
                    }
                });
    }

}
