package com.example.rudnev.remindme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.repositories.RemindMeRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class FragmentsViewModel extends AndroidViewModel {

    private RemindMeRepository mRepository;

    private LiveData<List<RemindDTO>> mAllReminds;

    HashSet<CalendarDay> dates;

    public FragmentsViewModel(Application application) {
        super(application);
        mRepository = new RemindMeRepository(application);
        mAllReminds = mRepository.getAllReminds();
        dates = new HashSet<>();
    }

    public LiveData<List<RemindDTO>> getAllReminds() { return mAllReminds; }

    public void insert(RemindDTO remind) { mRepository.insert(remind); }

    public void update(RemindDTO remind) { mRepository.update(remind); }

    public void delete(RemindDTO remind) { mRepository.delete(remind); }

    public HashSet<CalendarDay> updateCalendar(List<RemindDTO> datas) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dates.clear();
        if (datas != null)
            for (RemindDTO s : datas) {
                dates.add(CalendarDay.from(s.getDate()));
            }
        return dates;
    }
}