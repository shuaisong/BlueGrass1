package com.reeching.fragment;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.CheckAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.reeching.utils.SPUtil;
import com.reeching.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 待核查
 */
public class WaitForCheckFragment extends Fragment {

    private List<ExhibitionBean.InfosBean> allinfos;
    private CheckAdapter adapter;

    private TextView tv;
    private int pageNo = 1;
    private HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waitforcheck, container, false);
        mLV_AfterSalesInfor = (PullToRefreshListView) view
                .findViewById(R.id.waitforcheck_lv);
        tv = (TextView) view.findViewById(R.id.waitforcheck_tv);
        mLV_AfterSalesInfor.setMode(Mode.BOTH);
        allinfos = new ArrayList<>();
        adapter = new CheckAdapter(allinfos, getActivity());
        mLV_AfterSalesInfor.setAdapter(adapter);
        initdata();
        mLV_AfterSalesInfor
                .setOnRefreshListener(new PullToRefreshListView.OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        tv.setVisibility(View.GONE);
                        pageNo = 1;
                        initdata();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        initdata();
                    }
                });
        return view;
    }

    private void initdata() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("pageNo", pageNo + "");
        params.addQueryStringParameter("userId", SPUtil.getUserIdSp(getContext()));
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.waitforcheck, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        mLV_AfterSalesInfor.onRefreshComplete();
                        Toast.makeText(getActivity(), "请检查网络！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ExhibitionBean bean = JSON.parseObject(arg0.result,
                                ExhibitionBean.class);
                        if (bean.getResult().equals("1")) {
                            if (pageNo == 1) allinfos.clear();
                            allinfos.addAll(bean.getInfos());
                            pageNo++;
                            adapter.notifyDataSetChanged();
                            mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);
                        } else {
                            if (getParentFragment().getUserVisibleHint())
                                ToastUtil.showToast(getActivity(), getString(R.string.no_more_data));
                            if (pageNo == 1) {
                                allinfos.clear();
                                adapter.notifyDataSetChanged();
                                tv.setVisibility(View.VISIBLE);
                            }
                        }
                        mLV_AfterSalesInfor.onRefreshComplete();
                    }
                });

    }
}
