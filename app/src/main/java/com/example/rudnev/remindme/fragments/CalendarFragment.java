package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rudnev.remindme.EventDecorator;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class CalendarFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.calendar_fragment;
    private MaterialCalendarView calendarView;
    HashSet<CalendarDay> dates;
    private RemindDBAdapter dbAdapter;


    public static CalendarFragment getInstance(Context context){
        Bundle args = new Bundle();
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setArguments(args);
        calendarFragment.setContext(context);
        calendarFragment.setTitle(context.getString(R.string.calendar_tab));
        return calendarFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ONCREATECALENDAR", "OnCreateCalendar");
        view = inflater.inflate(LAYOUT, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        dates = new HashSet<>();
        dbAdapter = new RemindDBAdapter(context);
        List<RemindDTO> datas = new ArrayList<>();
        datas = dbAdapter.getAllItems(2);
        Calendar cal = Calendar.getInstance();
        for(RemindDTO s : datas){
            dates.add(CalendarDay.from(s.getDate()));
            calendarView.addDecorator(new EventDecorator(R.color.colorPrimary, dates));
        }
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT).show();
                //dates.add(date);
                widget.addDecorator(new EventDecorator(R.color.colorPrimary, dates));

            }
        });
        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        Log.i("ONRESUMECALENDAR", "OnResumeCalendar");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ONPAUSECALENDAR", "OnPauseCalendar");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("ONSTOPCALENDAR", "OnStopCalendar");
    }
}
