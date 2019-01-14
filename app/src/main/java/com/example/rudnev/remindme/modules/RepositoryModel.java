package com.example.rudnev.remindme.modules;

import android.app.Application;

import com.example.rudnev.remindme.repositories.NoteRepository;
import com.example.rudnev.remindme.repositories.RemindMeRepository;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class RepositoryModel {

    @Provides
    public RemindMeRepository remindMeRepository(Application application){
        return new RemindMeRepository(application);
    }

    @Provides
    public NoteRepository noteRepository(Application application){
        return new NoteRepository(application);
    }
}
