package com.example.rudnev.remindme.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.rudnev.remindme.RemindMeApplication;
import com.example.rudnev.remindme.components.DaggerRemindMeComponent;
import com.example.rudnev.remindme.components.RemindMeComponent;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.modules.ApplicationModule;
import com.example.rudnev.remindme.repositories.NoteRepository;

import java.util.List;

import javax.inject.Inject;

public class NoteViewModel extends AndroidViewModel {

    @Inject
    NoteRepository mRepository;

    private LiveData<List<Notes>> mAllNotes;


    public NoteViewModel(Application application) {
        super(application);
        RemindMeApplication.get().getRemindMeComponent().injectNoteViewModel(this);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Notes>> getAllNotes() { return mAllNotes; }

    public void insert(Notes note) { mRepository.insert(note); }

    public void update(Notes note) { mRepository.update(note); }

    public void delete(Notes note) { mRepository.delete(note); }

}
