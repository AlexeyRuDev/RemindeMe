package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;


public class BirthdaysFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.birthdays_fragment;


    public static BirthdaysFragment getInstance(Context context){
        Bundle args = new Bundle();
        BirthdaysFragment birthdaysFragment = new BirthdaysFragment();
        birthdaysFragment.setArguments(args);
        birthdaysFragment.setContext(context);
        birthdaysFragment.setTitle(context.getString(R.string.birthdays_tab));
        return birthdaysFragment;
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
