package com.example.rudnev.remindme.adapter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.fragments.AbstractTabFragment;
import com.example.rudnev.remindme.fragments.ArchiveFragment;
import com.example.rudnev.remindme.fragments.CalendarFragment;
import com.example.rudnev.remindme.fragments.NotesFragment;
import com.example.rudnev.remindme.fragments.TodayFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class TabFragmentAdapter extends FragmentStatePagerAdapter implements TabLayout.OnTabSelectedListener {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;
    private final static int NUM_SIZE = 3;

    private TodayFragment todayFragment;
    private ArchiveFragment archiveFragment;

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
        return NUM_SIZE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    private void initTabsMap(Context context){
        tabs = new TreeMap<>();
        todayFragment = TodayFragment.getInstance(context, datas);
        archiveFragment = ArchiveFragment.getInstance(context, datas);
        tabs.put(0, todayFragment);
        tabs.put(1, CalendarFragment.getInstance(context));
        tabs.put(2, archiveFragment);
        //tabs.put(3, NotesFragment.getInstance(context));
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateFragmentState(tab.getPosition(), true);
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
