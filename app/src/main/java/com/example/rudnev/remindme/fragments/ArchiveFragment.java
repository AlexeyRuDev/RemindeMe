package com.example.rudnev.remindme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;
import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.util.Date;
import java.util.List;


public class ArchiveFragment extends AbstractTabFragment implements RemindItemClickListener, TabFragmentAdapter.TabSelectedListener{

    private static final int LAYOUT = R.layout.archive_fragment;

    private List<RemindDTO> datas;
    private ArchiveListAdapter adapter;
    RecyclerView rv;
    private RemindDBAdapter dbAdapter;
    private long mItemID;
    private Date mItemDate;

    public static ArchiveFragment getInstance(Context context, List<RemindDTO> datas){
        Bundle args = new Bundle();
        ArchiveFragment archiveFragment = new ArchiveFragment();
        archiveFragment.setArguments(args);
        archiveFragment.setData(datas);
        archiveFragment.setContext(context);
        archiveFragment.setTitle(context.getString(R.string.archive_tab));
        return archiveFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ONCREATEARCHIVE", "OnCreateArchive");
        view = inflater.inflate(LAYOUT, container, false);
        dbAdapter = new RemindDBAdapter(context);
        datas = dbAdapter.getAllItems(3, null);
        rv = (RecyclerView)view.findViewById(R.id.recyclerViewArchive);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ArchiveListAdapter(datas, this);
        rv.setAdapter(adapter);
        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public void setData(List<RemindDTO> data) {
        this.datas = data;
    }

    public void refreshData(List<RemindDTO>data){
        //adapter = new RemindListAdapter(data);
        //FIX NULL WHEN ADD FUTURE ITEM
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        Log.i("ONRESUMEARCHIVE", "OnResumeArchive");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ONPAUSEARCHIVE", "OnPauseArchive");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("ONSTOPARCHIVE", "OnStopArchive");
    }


    @Override
    public void remindListRemoveClicked(View v, int position) {
        //Toast.makeText(getContext(), " "+position, Toast.LENGTH_SHORT).show();
        dbAdapter = new RemindDBAdapter(context);
        dbAdapter.removeItem(adapter.getTitle(position));
        adapter.setData(dbAdapter.getAllItems(3, null));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {

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
    public void onFragmentBecomesCurrent(boolean current) {
        //Analog onResume
        dbAdapter = new RemindDBAdapter(context);
        datas = dbAdapter.getAllItems(3, null);
        setData(datas);
        if(adapter!=null){
            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }
    }
}
