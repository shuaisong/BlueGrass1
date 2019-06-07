package com.reeching.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.reeching.BaseApplication;
import com.reeching.adapter.AllHistoryAdapter;
import com.reeching.bean.ExhibitionBean;
import com.reeching.bluegrass.AllHistoryInfoActivity;
import com.reeching.bluegrass.HaveCheckInfoActivity;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;
import com.reeching.utils.JsonCallback;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/11/12.
 * auther:lenovo
 * Date：2018/11/12
 */
public class SearchExhibition extends AppCompatActivity implements View.OnClickListener {

    private EditText et_search;
    private ArrayList<ExhibitionBean.InfosBean> mList;
    private ProgressDialog mProgressDialog;
    private String mTheme;
    private AllHistoryAdapter mAdapter;
    private PullToRefreshListView mListView;
    private AlertDialog mAlertDialog;
    private String mIp_ex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_exhibition_search);
        et_search = findViewById(R.id.et_exhibition_search);
        final TextView title = findViewById(R.id.title);
        TextView comeback = findViewById(R.id.comeback);
        TextView search = findViewById(R.id.search);
        final int NO = getIntent().getIntExtra("NO", 5);
        switch (NO) {
            case 5:
                mIp_ex = HttpApi.havecheck;
                title.setText("已检查展览查询");
                break;
            case 6:
                mIp_ex = HttpApi.YEAREXHIBITIONCOUNT;
                title.setText("当年展览查询");
                break;
        }
        mListView = findViewById(R.id.hualangserch_lv);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList = new ArrayList<>();
        mAdapter = new AllHistoryAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (NO) {
                    case 5:
                        intent = new Intent(SearchExhibition.this, HaveCheckInfoActivity.class);
                        intent.putExtra("id", mList.get(position - 1).getId());
                        break;
                    case 6:
                        intent = new Intent(SearchExhibition.this, AllHistoryInfoActivity.class);
                        intent.putExtra("info", mList.get(position - 1));
                        break;
                    default:
                        intent = new Intent(SearchExhibition.this, AllHistoryInfoActivity.class);
                        intent.putExtra("info", mList.get(position - 1));
                        break;
                }
                startActivity(intent);
            }
        });
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initList(mTheme);
            }
        });
        search.setOnClickListener(this);
        comeback.setOnClickListener(this);
    }

    int index = 1;

    private void initList(final String theme) {
        String id = BaseApplication.getInstance().getId();
        OkGo.<ExhibitionBean>post(HttpApi.ip + mIp_ex)
                .params("theme", theme)
                .params("pageSize", 10)
                .params("pageNo", index)
                .params("userId", id)
                .tag(this)
                .execute(new JsonCallback<ExhibitionBean>(ExhibitionBean.class) {
                    @Override
                    public void onSuccess(Response<ExhibitionBean> response) {
                        if ("1".equals(response.body().getResult())) {
                            mList.addAll(response.body().getInfos());
                            mAdapter.notifyDataSetChanged();
                            index++;
                        } else {
                            if (index == 1) {
                                if (mAlertDialog == null) {
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchExhibition.this);
                                    mBuilder.setCancelable(true)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mAlertDialog.dismiss();
                                                }
                                            });
                                    mAlertDialog = mBuilder.create();
                                }
                                mAlertDialog.setMessage("没有\"" + theme + "\"相关的展览");
                                if (!SearchExhibition.this.isFinishing())
                                    mAlertDialog.show();
                            } else
                                Toast.makeText(SearchExhibition.this,
                                        "没有更多数据！", Toast.LENGTH_SHORT).show();
                        }
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        mListView.onRefreshComplete();
                    }

                    @Override
                    public void onError(Response<ExhibitionBean> response) {
                        super.onError(response);
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        mListView.onRefreshComplete();
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
                mTheme = et_search.getText().toString().trim();
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setMessage("数据加载中，请稍候...");
                }
                mProgressDialog.show();
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        OkGo.getInstance().cancelTag(this);
                    }
                });
                mList.clear();
                index = 1;
                initList(mTheme);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
