package com.example.rudnev.remindme.modules;

import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.activities.ArchiveActivity;
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;

import dagger.Module;
import dagger.Provides;

@Module(includes = RemindItemClickListenerModule.class)
public class ArchiveActivityModule {
    private final ArchiveActivity archiveActivity;

    public ArchiveActivityModule(ArchiveActivity archiveActivity){
        this.archiveActivity = archiveActivity;
    }

    @Provides
    public ArchiveListAdapter archiveListAdapter(RemindItemClickListener remindItemClickListener){
        return new ArchiveListAdapter(remindItemClickListener);
    }
}
