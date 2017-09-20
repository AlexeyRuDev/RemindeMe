package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;


public class IdeasFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.ideas_fragment;


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
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }
}
