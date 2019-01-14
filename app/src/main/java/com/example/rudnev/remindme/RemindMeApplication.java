package com.example.rudnev.remindme;

import android.app.Activity;
import android.app.Application;

import com.example.rudnev.remindme.components.DaggerRemindMeComponent;
import com.example.rudnev.remindme.components.RemindMeComponent;
import com.example.rudnev.remindme.modules.ApplicationModule;

public class RemindMeApplication extends Application {

    private static RemindMeApplication remindMeApplication;
    private RemindMeComponent remindMeComponent;

    public static RemindMeApplication get(){
        return remindMeApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        remindMeApplication = this;
        remindMeComponent = DaggerRemindMeComponent.builder().applicationModule(new ApplicationModule(remindMeApplication)).build();

    }

    public RemindMeComponent getRemindMeComponent(){
        return remindMeComponent;
    }
}
