package com.jixstreet.temanusahapartner.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jixstreet.temanusahapartner.fragments.AppStatusFragment;

/**
 * Created by satryaway on 10/17/2015.
 * pager adapter for application status
 */
public class AppStatusPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    Context context;
    String[] tabTitles = {"In Progress", "Completed"};

    public AppStatusPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        AppStatusFragment fragment;
        fragment = AppStatusFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
