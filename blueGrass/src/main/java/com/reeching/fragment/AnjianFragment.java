package com.reeching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.reeching.adapter.MyFragmentPagerAdapter;
import com.reeching.bluegrass.R;
import com.reeching.utils.MyViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AnjianFragment extends Fragment {
    private MyViewPager mPager;
    private RadioGroup mRadioGroup;
    private WaitForHeChaFragment mWaitForHeChaFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anjian, null);
        RadioButton rbt1 = (RadioButton) view.findViewById(R.id.fragment_anjian_rbt1);
        RadioButton rbt2 = (RadioButton) view.findViewById(R.id.fragment_anjian_rbt2);
        RadioButton rbt3 = (RadioButton) view.findViewById(R.id.fragment_anjian_rbt3);
        rbt1.setOnClickListener(new MyOnClickListener(0));
        rbt2.setOnClickListener(new MyOnClickListener(1));
        rbt3.setOnClickListener(new MyOnClickListener(2));

        mRadioGroup = (RadioGroup) view
                .findViewById(R.id.fragment_anjian_radiogroups);
        InitViewPager(view);
        mRadioGroup.check(R.id.fragment_anjian_rbt1);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 331) {
            mWaitForHeChaFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            if (index == 0) {
                mRadioGroup.check(R.id.fragment_anjian_rbt1);
            } else if (index == 1) {
                mRadioGroup.check(R.id.fragment_anjian_rbt2);
            } else {
                mRadioGroup.check(R.id.fragment_anjian_rbt3);
            }
        }
    }

    ;

    private void InitViewPager(View parentView) {
        mPager = parentView
                .findViewById(R.id.fragment_anjiandetail);
        mPager.setCanScrollble(false);
        ArrayList<Fragment> fragmentsList = new ArrayList<>();
        WaitForCheckFragment waitForCheckFragment = new WaitForCheckFragment();
        mWaitForHeChaFragment = new WaitForHeChaFragment();
        WellDoneFragment wellDoneFragment = new WellDoneFragment();
        fragmentsList.add(waitForCheckFragment);
        fragmentsList.add(mWaitForHeChaFragment);
        fragmentsList.add(wellDoneFragment);
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
                fragmentsList));
        mPager.setCurrentItem(0);

    }

}