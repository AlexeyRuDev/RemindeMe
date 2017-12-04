package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.rudnev.remindme.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


public class IdeasFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.ideas_fragment;
    private MaterialCalendarView calendarView;


    public static IdeasFragment getInstance(Context context){
        Bundle args = new Bundle();
        IdeasFragment ideasFragment = new IdeasFragment();
        ideasFragment.setArguments(args);
        ideasFragment.setContext(context);
        ideasFragment.setTitle(context.getString(R.string.ideas_tab));
        return ideasFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ONCREATEIDEAS", "OnCreateIdeas");
        view = inflater.inflate(LAYOUT, container, false);
        calendarView = view.findViewById(R.id.calendarView);

        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        Log.i("ONRESUMEIDEAS", "OnResumeIdeas");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ONPAUSEIDEAS", "OnPauseIdeas");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("ONSTOPIDEAS", "OnStopIdeas");
    }
}
