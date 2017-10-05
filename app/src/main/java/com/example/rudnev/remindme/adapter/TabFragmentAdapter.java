package com.example.rudnev.remindme.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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


public class TabFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

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

        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    private void initTabsMap(Context context){
        tabs = new HashMap<>();
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

    public void updateRVAdapter(){
        historyFragment.updateRV();
    }

    public List<RemindDTO> getDatas() {
        return datas;
    }
}
