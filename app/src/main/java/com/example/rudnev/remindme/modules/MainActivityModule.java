package com.example.rudnev.remindme.modules;

import android.content.Context;

import com.example.rudnev.remindme.MainActivity;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.fragments.CalendarFragment;
import com.example.rudnev.remindme.fragments.TodayFragment;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class MainActivityModule {
    private final MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Provides
    public TabFragmentAdapter tabFragmentAdapter(Context context, TodayFragment todayFragment, CalendarFragment calendarFragment){
        return new TabFragmentAdapter(context, mainActivity.getSupportFragmentManager(), todayFragment, calendarFragment);
    }

    @Provides
    public TodayFragment todayFragment(Context context){
        return TodayFragment.getInstance(context);
    }

    @Provides
    public CalendarFragment calendarFragment(Context context){
        return CalendarFragment.getInstance(context);
    }

}
