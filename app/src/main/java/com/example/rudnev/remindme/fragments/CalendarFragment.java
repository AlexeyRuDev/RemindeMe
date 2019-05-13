package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rudnev.remindme.CalendarItemsDialog;
import com.example.rudnev.remindme.DateDecorator;
import com.example.rudnev.remindme.EventDecorator;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.activities.CreateItemActivity;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


public class CalendarFragment extends AbstractTabFragment {

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

        mViewModel.getRemindsForCalendar().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                datas = reminds;
                updateCalendar(datas);
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, date.getYear());
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.DATE, date.getDay());
                widget.addDecorator(new EventDecorator(R.color.colorPrimary, dates, getCurrentContext()));
                widget.addDecorator(new DateDecorator(R.color.colorPrimary, CalendarDay.today(), getCurrentContext()));

                if(haveItemsForConcreteDate(calendar)) {
                    CalendarItemsDialog calendarItemsDialog = CalendarItemsDialog.getInstance(getCurrentContext(), calendar.getTime(), mViewModel);
                    calendarItemsDialog.setTargetFragment(CalendarFragment.this, CALENDARFRAGMENT);
                    calendarItemsDialog.show(getFragmentManager(), "add_calendar_item");
                }else {
                    LocalDate localDate = LocalDateTime.fromDateFields(calendar.getTime()).toLocalDate();
                    LocalDate localDateNow = LocalDate.now();
                    if(localDate.compareTo(localDateNow) < 0){
                        Toast.makeText(getCurrentContext(), R.string.PreviousNotes, Toast.LENGTH_SHORT).show();
                    }else {
                        showAddItemActivity(calendar);
                    }
                }

            }
        });
        return view;
    }

    private boolean haveItemsForConcreteDate(Calendar calendar){
        LocalDateTime dateTime = LocalDateTime.fromDateFields(calendar.getTime());
        for(RemindDTO remindDto : datas){
            if(remindDto.getDate().toLocalDate().equals(dateTime.toLocalDate())){
                return true;
            }
        }
        return false;
    }

    private void showAddItemActivity(Calendar calendar){
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        intent.putExtra("mDateField", calendar);
        startActivityForResult(intent, CALENDARFRAGMENT);
    }

    public void setContext(Context context) {
        setCurrentContext(context);
    }

    private void updateCalendar(List<RemindDTO> datas) {
        dates.clear();
        calendarView.removeDecorators();
        dates = mViewModel.updateCalendar(datas);
        calendarView.addDecorator(new EventDecorator(R.color.colorPrimary, dates, getCurrentContext()));
        calendarView.addDecorator(new DateDecorator(R.color.colorPrimary, CalendarDay.today(), getCurrentContext()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0){
            RemindDTO mRemindItem = (RemindDTO) data.getParcelableExtra("mRemindItem");
            if(mRemindItem!=null){
                if(data.getBooleanExtra("updateItem", false)){
                    mViewModel.update(mRemindItem);
                }else{
                    mViewModel.insert(mRemindItem);
                }
            }
        }
    }

}
