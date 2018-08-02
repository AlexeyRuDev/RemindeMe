package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.Observer;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


public class CalendarFragment extends AbstractTabFragment implements TabFragmentAdapter.TabSelectedListener,
        CalendarItemsDialog.CalendarItemsUpdateListener {


    private static final String TAG = "CALENDAR_FRAGMENT";
    private static final int LAYOUT = R.layout.calendar_fragment;
    private static final int CALENDARFRAGMENT = 1;
    private MaterialCalendarView calendarView;
    HashSet<CalendarDay> dates;
    private List<RemindDTO> datas;


    public static CalendarFragment getInstance(Context context) {
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

        dates = new HashSet<CalendarDay>();

        mViewModel.getAllReminds().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                filterListReminds(reminds);
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, date.getYear());
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.DATE, date.getDay());
                widget.addDecorator(new EventDecorator(R.color.colorPrimary, dates, context));
                CalendarItemsDialog calendarItemsDialog = CalendarItemsDialog.getInstance(context, calendar.getTime(), mViewModel);
                calendarItemsDialog.setTargetFragment(CalendarFragment.this, CALENDARFRAGMENT);
                calendarItemsDialog.show(getFragmentManager(), "add_calendar_item");

            }
        });
        return view;
    }


    private void filterListReminds(List<RemindDTO> reminds) {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        LocalDate localDate = LocalDate.now();
        datas = new ArrayList<>();
        if (reminds != null) {
            for (RemindDTO item : reminds) {
                LocalDate itemLocalDate = LocalDate.fromDateFields(item.getDate());
                if (dateTimeComparator.compare(itemLocalDate.toDate(), localDate.toDate()) >= 0) {
                    datas.add(item);
                }
            }
        }
        updateCalendar(datas);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onFragmentBecomesCurrent(boolean current) {

    }

    private void updateCalendar(List<RemindDTO> datas) {

        dates.clear();
        calendarView.removeDecorators();
        dates = mViewModel.updateCalendar(datas);
        calendarView.addDecorator(new EventDecorator(R.color.colorPrimary, dates, context));
    }

    @Override
    public void onCloseDialog() {
        if(datas != null)
            updateCalendar(datas);
    }


}
