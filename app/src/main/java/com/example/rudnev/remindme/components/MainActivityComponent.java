package com.example.rudnev.remindme.components;

import com.example.rudnev.remindme.MainActivity;
import com.example.rudnev.remindme.modules.MainActivityModule;

import dagger.Component;

@Component(modules = {MainActivityModule.class})
public interface MainActivityComponent {

    void injectMainActivity(MainActivity mainActivity);
}
