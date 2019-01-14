package com.example.rudnev.remindme.modules;

import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.activities.NotesActivity;
import com.example.rudnev.remindme.adapter.NotesListAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class NoteActivityModule {

    private final NotesActivity notesActivity;
    private final RemindItemClickListener remindItemClickListener;

    public NoteActivityModule(NotesActivity notesActivity, RemindItemClickListener remindItemClickListener){
        this.notesActivity = notesActivity;
        this.remindItemClickListener = remindItemClickListener;
    }

    @Provides
    public NotesListAdapter notesListAdapter(){
        return new NotesListAdapter(remindItemClickListener);
    }
}
