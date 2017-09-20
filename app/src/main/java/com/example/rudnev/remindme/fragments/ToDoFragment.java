package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;


public class ToDoFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.todo_fragment;

    public static ToDoFragment getInstance(Context context){
        Bundle args = new Bundle();
        ToDoFragment todoFragment = new ToDoFragment();
        todoFragment.setArguments(args);
        todoFragment.setContext(context);
        todoFragment.setTitle(context.getString(R.string.todo_tab));
        return todoFragment;
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
