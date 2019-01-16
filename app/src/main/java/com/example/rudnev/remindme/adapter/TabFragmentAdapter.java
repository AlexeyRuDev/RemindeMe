package com.example.rudnev.remindme.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rudnev.remindme.fragments.AbstractTabFragment;
import com.example.rudnev.remindme.fragments.CalendarFragment;
import com.example.rudnev.remindme.fragments.TodayFragment;

import java.util.Map;
import java.util.TreeMap;


public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private final static int NUM_SIZE = 2;
    private TodayFragment todayFragment;
    private CalendarFragment calendarFragment;


    public TabFragmentAdapter(FragmentManager fm, TodayFragment todayFragment, CalendarFragment calendarFragment) {
        super(fm);
        this.todayFragment = todayFragment;
        this.calendarFragment = calendarFragment;
        initTabsMap();
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

    private void initTabsMap(){
        tabs = new TreeMap<>();
        tabs.put(0, todayFragment);
        tabs.put(1, calendarFragment);
    }


    public void showEditDialog(){
        ((TodayFragment)tabs.get(0)).showEditDialog();
    }
}
