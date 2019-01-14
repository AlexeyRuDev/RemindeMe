package com.example.rudnev.remindme.components;

import com.example.rudnev.remindme.activities.ArchiveActivity;
import com.example.rudnev.remindme.modules.ArchiveActivityModule;

import dagger.Component;

@Component(modules = {ArchiveActivityModule.class})
public interface ArchiveActivityComponent {
    void injectArchiveActivity(ArchiveActivity archiveActivity);
}
