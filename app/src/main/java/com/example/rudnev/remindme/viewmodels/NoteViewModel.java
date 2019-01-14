package com.example.rudnev.remindme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.components.DaggerRemindMeComponent;
import com.example.rudnev.remindme.components.RemindMeComponent;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.modules.ApplicationModule;
import com.example.rudnev.remindme.repositories.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;

    private LiveData<List<Notes>> mAllNotes;


    public NoteViewModel(Application application) {
        super(application);
        RemindMeComponent remindMeComponent = DaggerRemindMeComponent.builder().applicationModule(new ApplicationModule(application))
                .build();
        mRepository = remindMeComponent.getNoteRepository();
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Notes>> getAllNotes() { return mAllNotes; }

    public void insert(Notes note) { mRepository.insert(note); }

    public void update(Notes note) { mRepository.update(note); }

    public void delete(Notes note) { mRepository.delete(note); }

}
