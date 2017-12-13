package com.example.rudnev.remindme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.util.List;

public class CalendarItemsDialog extends DialogFragment implements RemindItemClickListener{

    private List<RemindDTO> datas;
    private RecyclerView listViewItems;
    private Button addItem;
    private RemindListAdapter adapter;
    private RemindDBAdapter dbAdapter;
    private Context context;

    public static CalendarItemsDialog getInstance(Context context, List<RemindDTO> datas){
        Bundle args = new Bundle();
        CalendarItemsDialog calendarFragment = new CalendarItemsDialog();
        calendarFragment.setArguments(args);
        calendarFragment.setData(datas);
        calendarFragment.setContext(context);
        return calendarFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_items_dialog, container);
        addItem = view.findViewById(R.id.addCalItem);
        listViewItems = view.findViewById(R.id.recyclerViewCalItems);
        listViewItems.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(datas, this);
        listViewItems.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
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

    public void setData(List<RemindDTO> data) {
        this.datas = data;
    }
}
