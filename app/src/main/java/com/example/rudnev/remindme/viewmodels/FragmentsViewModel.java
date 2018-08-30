package com.example.rudnev.remindme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.repositories.RemindMeRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class FragmentsViewModel extends AndroidViewModel {

    private RemindMeRepository mRepository;

    private LiveData<List<RemindDTO>> mAllReminds;
    private LiveData<List<RemindDTO>> mRemindsForToday;
    private LiveData<List<RemindDTO>> mRemindsForArchive;
    private LiveData<List<RemindDTO>> mRemindsForCalendar;

    HashSet<CalendarDay> dates;

    public FragmentsViewModel(Application application) {
        super(application);
        mRepository = new RemindMeRepository(application);
        mAllReminds = mRepository.getAllReminds();
        mRemindsForToday = mRepository.getRemindsForToday();
        mRemindsForArchive = mRepository.getRemindsForArchive();
        mRemindsForCalendar = mRepository.getRemindsForCalendar();
        dates = new HashSet<>();
    }

    public LiveData<List<RemindDTO>> getAllReminds() { return mAllReminds; }

    public LiveData<List<RemindDTO>> getRemindsForToday() { return mRemindsForToday; }

    public LiveData<List<RemindDTO>> getRemindsForArchive() { return mRemindsForArchive; }

    public LiveData<List<RemindDTO>> getRemindsForCalendar() { return mRemindsForCalendar; }

    public LiveData<List<RemindDTO>> getRemindsForConcreteDate(LocalDateTime date) {
        return mRepository.getRemindsForConcreteDate(date);
    }

    public void insert(RemindDTO remind) { mRepository.insert(remind); }

    public void update(RemindDTO remind) { mRepository.update(remind); }

    public void delete(RemindDTO remind) { mRepository.delete(remind); }

    public HashSet<CalendarDay> updateCalendar(List<RemindDTO> datas) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dates.clear();
        if (datas != null)
            for (RemindDTO s : datas) {
                dates.add(CalendarDay.from(s.getDate().toDate()));
            }
        return dates;
    }
}