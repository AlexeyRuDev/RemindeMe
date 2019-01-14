package com.example.rudnev.remindme.modules;

import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.activities.ArchiveActivity;
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class ArchiveActivityModule {
    private final ArchiveActivity archiveActivity;
    private final RemindItemClickListener remindItemClickListener;

    public ArchiveActivityModule(ArchiveActivity archiveActivity, RemindItemClickListener remindItemClickListener){
        this.archiveActivity = archiveActivity;
        this.remindItemClickListener = remindItemClickListener;
    }

    @Provides
    public ArchiveListAdapter archiveListAdapter(){
        return new ArchiveListAdapter(remindItemClickListener);
    }
}
