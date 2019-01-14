package com.example.rudnev.remindme.components;

import android.app.Application;

import com.example.rudnev.remindme.modules.RepositoryModel;
import com.example.rudnev.remindme.repositories.NoteRepository;
import com.example.rudnev.remindme.repositories.RemindMeRepository;

import dagger.Component;

@Component(modules = {RepositoryModel.class})
public interface RemindMeComponent {
    NoteRepository getNoteRepository();
    RemindMeRepository getRemindMeRepository();
}
