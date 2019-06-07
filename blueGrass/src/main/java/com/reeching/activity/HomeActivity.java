package com.reeching.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.reeching.bluegrass.MainActivity;
import com.reeching.bluegrass.R;
import com.reeching.utils.ExitApplication;


public class HomeActivity extends Activity implements View.OnClickListener {
    /* int[] images = new int[]{
            R.drawable.home_one, R.drawable.home_two1,
            R.drawable.home_three1, R.drawable.home_four1,
            R.drawable.home_five1, R.drawable.home_six1,
            R.drawable.add1,
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intView();
       // ExitApplication.getInstance().addActivity(HomeActivity.this);
    }

    private void intView() {
        ImageView iv_798 = findViewById(R.id.iv_798);
        iv_798.setOnClickListener(this);
        ImageView iv_prairie = findViewById(R.id.iv_prairie);
        iv_prairie.setOnClickListener(this);
        ImageView iv_winery = findViewById(R.id.iv_winery);
        iv_winery.setOnClickListener(this);
        ImageView iv_one = findViewById(R.id.iv_one);
        iv_one.setOnClickListener(this);
        ImageView iv_sever = findViewById(R.id.iv_sever);
        iv_sever.setOnClickListener(this);
        ImageView iv_ = findViewById(R.id.iv_);
        iv_.setOnClickListener(this);
        ImageView add = findViewById(R.id.add);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_798:
                Intent i = new Intent();
                i.setClass(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.iv_prairie:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_winery:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_one:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_sever:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
