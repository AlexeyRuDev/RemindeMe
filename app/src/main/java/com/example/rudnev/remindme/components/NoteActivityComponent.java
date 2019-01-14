package com.example.rudnev.remindme.components;


import com.example.rudnev.remindme.activities.NotesActivity;
import com.example.rudnev.remindme.modules.NoteActivityModule;

import dagger.Component;

@Component(modules = {NoteActivityModule.class})
public interface NoteActivityComponent {
    void injectNotesActivity(NotesActivity activity);
}
