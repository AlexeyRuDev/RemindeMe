package com.example.rudnev.remindme.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.fragments.AbstractTabFragment;
import com.example.rudnev.remindme.fragments.BirthdaysFragment;
import com.example.rudnev.remindme.fragments.HistoryFragment;
import com.example.rudnev.remindme.fragments.IdeasFragment;
import com.example.rudnev.remindme.fragments.ToDoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class TabFragmentAdapter extends FragmentStatePagerAdapter implements TabLayout.OnTabSelectedListener {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;
    private final static int NUM_SIZE = 4;

    private HistoryFragment historyFragment;

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
        historyFragment = HistoryFragment.getInstance(context, datas);
        tabs.put(0, historyFragment);
        tabs.put(1, IdeasFragment.getInstance(context));
        tabs.put(2, ToDoFragment.getInstance(context));
        tabs.put(3, BirthdaysFragment.getInstance(context));
    }

    public void setDatas(List<RemindDTO> datas) {
        this.datas = datas;
        historyFragment.refreshData(datas);
    }


    public List<RemindDTO> getDatas() {
        return datas;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Toast.makeText(context, "Position selected = " + tab.getPosition(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Toast.makeText(context, "Position unselected = " + tab.getPosition(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Toast.makeText(context, "Position reselected = " + tab.getPosition(), Toast.LENGTH_SHORT).show();
    }

    private void updateFragmentState(int pos, boolean current) {
        /*final Fragment fragment = map.get(pos);
        if (fragment != null && fragment instanceof TabSelectedListener) {
            ((TabSelectedListener) fragment).onFragmentBecomesCurrent(current);
        }*/
    }

   public interface TabSelectedListener {
        void onFragmentBecomesCurrent(boolean current);
    }
}
