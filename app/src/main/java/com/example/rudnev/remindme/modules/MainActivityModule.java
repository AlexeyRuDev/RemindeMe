package com.example.rudnev.remindme.modules;

import android.content.Context;

import com.example.rudnev.remindme.MainActivity;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class MainActivityModule {
    private final MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Provides
    public TabFragmentAdapter tabFragmentAdapter(Context context){
        return new TabFragmentAdapter(context, mainActivity.getSupportFragmentManager());
    }
}
