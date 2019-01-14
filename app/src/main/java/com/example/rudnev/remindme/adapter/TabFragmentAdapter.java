package com.example.rudnev.remindme.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.fragments.AbstractTabFragment;
import com.example.rudnev.remindme.fragments.CalendarFragment;
import com.example.rudnev.remindme.fragments.TodayFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private final static int NUM_SIZE = 2;


    public TabFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        initTabsMap(context);
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return NUM_SIZE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    private void initTabsMap(Context context){
        tabs = new TreeMap<>();
        tabs.put(0, TodayFragment.getInstance(context));
        tabs.put(1, CalendarFragment.getInstance(context));
    }


    public void showEditDialog(){
        ((TodayFragment)tabs.get(0)).showEditDialog();
    }
}
