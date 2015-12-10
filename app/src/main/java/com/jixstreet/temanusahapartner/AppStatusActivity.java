package com.jixstreet.temanusahapartner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.jixstreet.temanusahapartner.adapters.AppStatusPagerAdapter;

/**
 * Created by satryaway on 10/17/2015.
 * activity to handle application status module
 */
public class AppStatusActivity extends AppCompatActivity {
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.application_status_layout);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabstrip);
        viewPager = (ViewPager) findViewById(R.id.app_status_vp);

        ImageView iconIV = (ImageView) findViewById(R.id.option_menu_iv);
        TextView activityTitleTV = (TextView) findViewById(R.id.page_title_tv);
        iconIV.setImageResource(R.drawable.app_status);
        activityTitleTV.setText(R.string.application_status);

        initAdapter();
    }

    private void initAdapter() {
        AppStatusPagerAdapter pagerAdapter = new AppStatusPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabStrip.setViewPager(viewPager);
    }

    private void setCallBack() {

    }
}
