package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.CreateItemDialog;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.util.List;


public class HistoryFragment extends AbstractTabFragment implements RemindItemClickListener, TabFragmentAdapter.TabSelectedListener{

    private static final int LAYOUT = R.layout.history_fragment;


    private List<RemindDTO> data;
    private RemindListAdapter adapter;
    RecyclerView rv;
    private RemindDBAdapter dbAdapter;

    public static HistoryFragment getInstance(Context context, List<RemindDTO> datas){

        Bundle args = new Bundle();
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setArguments(args);
        historyFragment.setData(datas);
        historyFragment.setContext(context);
        historyFragment.setTitle(context.getString(R.string.history_tab));
        return historyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT, container, false);
        dbAdapter = new RemindDBAdapter(context);
        data = dbAdapter.getAllItems();
        rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(data, this);
        rv.setAdapter(adapter);
        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void setData(List<RemindDTO> data) {
        this.data = data;
    }

    public void refreshData(List<RemindDTO>data){
        //adapter = new RemindListAdapter(data);
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    /*@Override
    public void onResume() {
        Log.i("ONRESUMEHISTORY", "OnResume");
        refreshData(data);
        adapter.notifyDataSetChanged();
        super.onResume();
    }*/

    @Override
    public void remindListRemoveClicked(View v, int position) {
        //Toast.makeText(getContext(), " "+position, Toast.LENGTH_SHORT).show();
        dbAdapter = new RemindDBAdapter(context);
        dbAdapter.removeItem(adapter.getTitle(position));
        adapter.setData(dbAdapter.getAllItems());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        Bundle args = new Bundle();
        args.putString("title", data.get(position).getTitle());
        args.putString("note", data.get(position).getNote());
        args.putString("date", data.get(position).getDate());
        //fix to real id
        args.putLong("itemID", data.get(position).getId());
        createItemDialog.setArguments(args);
        createItemDialog.show(fm, "create_item_dialog");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void popupMenuItemClicked(final View view, final int position) {
        // pass the imageview id
        View menuItemView = view.findViewById(R.id.ib_popup_menu);
        PopupMenu popup = new PopupMenu(view.getContext(), menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.popup_cardview_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        remindListUpdateClicked(view, position);
                        break;
                    case R.id.delete:
                        remindListRemoveClicked(view, position);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ONPAUSEHISTORY", "OnPauseHistory");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("ONSTOPHISTORY", "OnStopHistory");
    }

    @Override
    public void onFragmentBecomesCurrent(boolean current) {
        //Analog onResume
        dbAdapter = new RemindDBAdapter(context);
        data = dbAdapter.getAllItems();
        setData(data);
    }
}
