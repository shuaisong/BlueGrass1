package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.BaseApplication;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HuaLangSerchActivity extends Activity {
    private EditText et;
    private JSONArray array;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    private List<String> mlist = new ArrayList<>();
    private TextView comeback;
    private ListView lv;
    private HttpUtils hu = new HttpUtils();
    private HashMap<String, String> hm = new HashMap<String, String>();

    private String mFragment = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hua_lang_serch);
        et = (EditText) findViewById(R.id.hualangserch_et);
        comeback = (TextView) findViewById(R.id.comeback);
        BaseApplication.getInstance().setInitflag(false);
        lv = (ListView) findViewById(R.id.hualangserch_lv);
        adapter = new ArrayAdapter<String>(HuaLangSerchActivity.this,
                R.layout.serchactivity_lvitem, R.id.serchactivity_tv,
                mlist);
        lv.setAdapter(adapter);
        mFragment = getIntent().getStringExtra("MapFragment");
        init();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("result", mlist.get(position));
                intent.putExtra("id", hm.get(mlist.get(position)));
                if ("mapFragment".equals(mFragment)) {
                    setResult(004, intent);
                } else {
                    setResult(003, intent);
                }
                HuaLangSerchActivity.this.finish();
            }
        });
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(003);
                HuaLangSerchActivity.this.finish();
            }
        });
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mlist.clear();
                    mlist.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    findpart(s.toString());
                }
            }
        });
    }

    private void init() {


//        if (null==object||null == object.getString("result")) {
        hu.configSoTimeout(20 * 1000);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallhualangname,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(HuaLangSerchActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        JSONObject object = JSON.parseObject(arg0.result,
                                JSONObject.class);
                        //  Toast.makeText(HuaLangSerchActivity.this, "更新数据完成！", Toast.LENGTH_SHORT).show();
                        //  BaseApplication.getInstance().setObj(object);
                        if (object.getString("result").equals("1")) {
                            array = object.getJSONArray("infos");
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                list.add(jsonObject.getString("name"));
                                hm.put(jsonObject.getString("name"), jsonObject.getString("id"));
                            }
                        } else {
                            Toast.makeText(HuaLangSerchActivity.this, "无数据！", Toast.LENGTH_LONG).show();
                        }

                    }
                });
//        }

    }


    private void findpart(final String s) {
        List<String> llist = new ArrayList<>();
        for (String ss : list) {

            if (ss.toLowerCase().contains(s.toLowerCase())) {
                llist.add(ss);
            }
        }
        mlist.clear();
        mlist.addAll(llist);
        adapter.notifyDataSetChanged();
    }


}
