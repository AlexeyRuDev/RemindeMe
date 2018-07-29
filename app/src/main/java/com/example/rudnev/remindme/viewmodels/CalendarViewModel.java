package com.example.rudnev.remindme.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.repositories.RemindMeRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CalendarViewModel extends AndroidViewModel {

    private RemindMeRepository mRepository;

    HashSet<CalendarDay> dates;

    private LiveData<List<RemindDTO>> mAllReminds;

    public CalendarViewModel(Application application) {
        super(application);
        mRepository = new RemindMeRepository(application);
        mAllReminds = mRepository.getRemindsForCalendar();
        dates = new HashSet<>();
    }

    public LiveData<List<RemindDTO>> getAllReminds() {
        return mAllReminds;
    }


    public HashSet<CalendarDay> updateCalendar(List<RemindDTO>datas) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dates.clear();
        if (datas != null)
            for (RemindDTO s : datas) {
                try {
                    dates.add(CalendarDay.from(sdf.parse(s.getDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        return dates;
    }

}