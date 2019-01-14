package com.example.rudnev.remindme.modules;

import android.app.Application;

import com.example.rudnev.remindme.annotations.RemindMeAppliacationScope;
import com.example.rudnev.remindme.repositories.NoteRepository;
import com.example.rudnev.remindme.repositories.RemindMeRepository;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApplicationModule.class})
public class RepositoryModel {

    @RemindMeAppliacationScope
    @Provides
    public RemindMeRepository remindMeRepository(Application application){
        return new RemindMeRepository(application);
    }

    @RemindMeAppliacationScope
    @Provides
    public NoteRepository noteRepository(Application application){
        return new NoteRepository(application);
    }
}
