package com.example.rudnev.remindme.modules;

import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.activities.NotesActivity;
import com.example.rudnev.remindme.adapter.NotesListAdapter;

import dagger.Module;
import dagger.Provides;

@Module(includes = RemindItemClickListenerModule.class)
public class NoteActivityModule {

    private final NotesActivity notesActivity;

    public NoteActivityModule(NotesActivity notesActivity){
        this.notesActivity = notesActivity;
    }

    @Provides
    public NotesListAdapter notesListAdapter(RemindItemClickListener remindItemClickListener){
        return new NotesListAdapter(remindItemClickListener);
    }
}
