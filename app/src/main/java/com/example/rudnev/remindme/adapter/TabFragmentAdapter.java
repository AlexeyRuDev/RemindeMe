package com.example.rudnev.remindme.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.fragments.AbstractTabFragment;
import com.example.rudnev.remindme.fragments.NotesFragment;
import com.example.rudnev.remindme.fragments.TodayFragment;
import com.example.rudnev.remindme.fragments.CalendarFragment;
import com.example.rudnev.remindme.fragments.ArchiveFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class TabFragmentAdapter extends FragmentStatePagerAdapter implements TabLayout.OnTabSelectedListener {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;
    private final static int NUM_SIZE = 4;

    private TodayFragment todayFragment;

    private List<RemindDTO> datas;

    public TabFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.datas = new ArrayList<>();
        initTabsMap(context);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("GETITEM", " " + position);
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
        todayFragment = TodayFragment.getInstance(context, datas);
        tabs.put(0, todayFragment);
        tabs.put(1, CalendarFragment.getInstance(context));
        tabs.put(2, ArchiveFragment.getInstance(context));
        tabs.put(3, NotesFragment.getInstance(context));
    }

    public void setDatas(List<RemindDTO> datas) {
        this.datas = datas;
        todayFragment.refreshData(datas);
    }


    public List<RemindDTO> getDatas() {
        return datas;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition()==0){
            updateFragmentState(tab.getPosition(), true);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private void updateFragmentState(int pos, boolean current) {
        final Fragment fragment = tabs.get(pos);
        if (fragment != null && fragment instanceof TabSelectedListener) {
            ((TabSelectedListener) fragment).onFragmentBecomesCurrent(current);
        }
    }

   public interface TabSelectedListener {
        void onFragmentBecomesCurrent(boolean current);
    }
}
