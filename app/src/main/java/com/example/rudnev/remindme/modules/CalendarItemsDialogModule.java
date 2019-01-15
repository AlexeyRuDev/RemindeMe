package com.example.rudnev.remindme.modules;

import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.CalendarItemsListAdapter;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RemindItemClickListenerModule.class})
public class CalendarItemsDialogModule {

    public CalendarItemsDialogModule(){}

    @Provides
    public CalendarItemsListAdapter calendarItemsListAdapter(RemindItemClickListener remindItemClickListener){
        return new CalendarItemsListAdapter(remindItemClickListener);
    }
}
