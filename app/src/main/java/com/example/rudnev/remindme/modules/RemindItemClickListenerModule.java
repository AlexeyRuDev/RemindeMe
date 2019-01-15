package com.example.rudnev.remindme.modules;

import com.example.rudnev.remindme.RemindItemClickListener;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RemindItemClickListenerModule {

    private final RemindItemClickListener remindItemClickListener;

    public RemindItemClickListenerModule(RemindItemClickListener remindItemClickListener){
        this.remindItemClickListener = remindItemClickListener;
    }

    @Provides
    public RemindItemClickListener remindItemClickListener(){
        return remindItemClickListener;
    }

}
