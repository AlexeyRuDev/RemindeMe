package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.CalendarItemsDialog;
import com.example.rudnev.remindme.EventDecorator;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


public class CalendarFragment extends AbstractTabFragment implements TabFragmentAdapter.TabSelectedListener, CalendarItemsDialog.CalendarItemsUpdateListener, AbstractTabFragment.UpdateFragmentsLists {

    private static final int LAYOUT = R.layout.calendar_fragment;
    private static final int CALENDARFRAGMENT = 1;
    private MaterialCalendarView calendarView;
    HashSet<CalendarDay> dates;
    private RemindDBAdapter dbAdapter;
    private List<RemindDTO> datas;


    public static CalendarFragment getInstance(Context context){
        Bundle args = new Bundle();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setArguments(args);
        calendarFragment.setContext(context);
        calendarFragment.setTitle(context.getString(R.string.calendar_tab));
        return calendarFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setPagingEnabled(false);
        dbAdapter = new RemindDBAdapter(context);
        dates = new HashSet<>();
        Calendar cal = Calendar.getInstance();
        updateCalendar(datas);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT).show();
                //dates.add(date);
                widget.addDecorator(new EventDecorator(R.color.colorPrimary, dates, context));
                CalendarItemsDialog calendarItemsDialog = CalendarItemsDialog.getInstance(context, date.getDate());
                calendarItemsDialog.setTargetFragment(CalendarFragment.this, CALENDARFRAGMENT);
                calendarItemsDialog.show(getFragmentManager(), "add_calendar_item");

            }
        });
        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onFragmentBecomesCurrent(boolean current) {

        //updateCalendar(datas);
    }

    private void updateCalendar(List<RemindDTO>datas){
        dates.clear();
        calendarView.removeDecorators();
        for(RemindDTO s : datas){
            dates.add(CalendarDay.from(s.getDate()));
            calendarView.addDecorator(new EventDecorator(R.color.colorPrimary, dates, context));
        }
    }

    @Override
    public void onCloseDialog() {
        datas = dbAdapter.getAllItems(2, null);
        updateCalendar(datas);
    }

    @Override
    public void update() {
        dbAdapter = new RemindDBAdapter(context);
        datas = dbAdapter.getAllItems(2, null);
        if(calendarView != null || dates !=null)
            updateCalendar(datas);
    }
}
