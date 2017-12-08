package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;


public class ArchiveFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.archive_fragment;

    public static ArchiveFragment getInstance(Context context){
        Bundle args = new Bundle();
        ArchiveFragment archiveFragment = new ArchiveFragment();
        archiveFragment.setArguments(args);
        archiveFragment.setContext(context);
        archiveFragment.setTitle(context.getString(R.string.archive_tab));
        return archiveFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ONCREATEARCHIVE", "OnCreateArchive");
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        Log.i("ONRESUMEARCHIVE", "OnResumeArchive");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ONPAUSEARCHIVE", "OnPauseArchive");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("ONSTOPARCHIVE", "OnStopArchive");
    }
}
