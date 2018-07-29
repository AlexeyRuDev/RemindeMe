package com.example.rudnev.remindme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.repositories.RemindMeRepository;

import java.util.List;

public class ArchiveViewModel extends AndroidViewModel {

    private RemindMeRepository mRepository;

    private LiveData<List<RemindDTO>> mAllReminds;

    public ArchiveViewModel (Application application) {
        super(application);
        mRepository = new RemindMeRepository(application);
        mAllReminds = mRepository.getRemindsForArchive();
    }

    public LiveData<List<RemindDTO>> getAllReminds() { return mAllReminds; }

    public void insert(RemindDTO remind) { mRepository.insert(remind); }

    public void update(RemindDTO remind) { mRepository.update(remind); }

    public void delete(RemindDTO remind) { mRepository.delete(remind); }
}
