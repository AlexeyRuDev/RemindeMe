package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;


public class NotesFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.notes_fragment;


    public static NotesFragment getInstance(Context context){
        Bundle args = new Bundle();
        NotesFragment notesFragment = new NotesFragment();
        notesFragment.setArguments(args);
        notesFragment.setContext(context);
        notesFragment.setTitle(context.getString(R.string.notes_tab));
        return notesFragment;
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
