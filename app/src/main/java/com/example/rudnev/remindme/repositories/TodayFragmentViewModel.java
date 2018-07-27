package com.example.rudnev.remindme.repositories;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.dto.RemindDTO;

import java.util.List;

public class TodayFragmentViewModel extends AndroidViewModel {

    private RemindMeRepository mRepository;

    private LiveData<List<RemindDTO>> mAllReminds;

    public TodayFragmentViewModel (Application application) {
        super(application);
        mRepository = new RemindMeRepository(application);
        mAllReminds = mRepository.getAllReminds();
    }

    public LiveData<List<RemindDTO>> getAllReminds() { return mAllReminds; }

    public void insert(RemindDTO remind) { mRepository.insert(remind); }

    public void update(RemindDTO remind) { mRepository.insert(remind); }
}
