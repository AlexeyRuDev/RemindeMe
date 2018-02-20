package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.util.List;


public class NotesFragment extends AbstractTabFragment implements RemindItemClickListener, TabFragmentAdapter.TabSelectedListener, AbstractTabFragment.UpdateFragmentsLists{

    private static final int LAYOUT = R.layout.notes_fragment;

    private List<RemindDTO> datas;
    private ArchiveListAdapter adapter;
    RecyclerView rv;
    private RemindDBAdapter dbAdapter;


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

    @Override
    public void remindListRemoveClicked(View v, int position) {

    }

    @Override
    public void remindListUpdateClicked(View v, int position) {

    }

    @Override
    public void popupMenuItemClicked(View v, int position) {

    }

    @Override
    public void onFragmentBecomesCurrent(boolean current) {

    }

    @Override
    public void update() {

    }
}
