package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends AbstractTabFragment {

    private static final int LAYOUT = R.layout.history_fragment;


    public static HistoryFragment getInstance(Context context){

        Bundle args = new Bundle();
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setArguments(args);
        historyFragment.setContext(context);
        historyFragment.setTitle(context.getString(R.string.history_tab));
        return historyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new RemindListAdapter(createMockRemindListDatas()));
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private List<RemindDTO> createMockRemindListDatas() {
        List<RemindDTO> datas = new ArrayList<>();
        datas.add(new RemindDTO("Item1"));
        datas.add(new RemindDTO("Item2"));
        datas.add(new RemindDTO("Item3"));
        datas.add(new RemindDTO("Item4"));
        datas.add(new RemindDTO("Item5"));
        datas.add(new RemindDTO("Item6"));
        datas.add(new RemindDTO("Item7"));

        return datas;
    }
}
