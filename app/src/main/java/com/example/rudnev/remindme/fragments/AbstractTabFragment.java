package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.rudnev.remindme.viewmodels.FragmentsViewModel;


public class AbstractTabFragment extends Fragment {

    private String title;
    protected View view;
    private Context context;
    protected FragmentsViewModel mViewModel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);
    }

    protected Context getCurrentContext(){
        return context == null ? getContext() : context;
    }

    protected void setCurrentContext(Context context){
        this.context = context;
    }
}
