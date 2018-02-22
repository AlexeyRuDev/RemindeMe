package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;


public class AbstractTabFragment extends Fragment {

    private String title;
    protected View view;
    protected Context context;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public interface UpdateFragmentsLists{
        void update();
    }

}
