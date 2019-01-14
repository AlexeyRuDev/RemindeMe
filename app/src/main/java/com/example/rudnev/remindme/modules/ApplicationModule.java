package com.example.rudnev.remindme.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides
    public Application application(){return application;}
}
