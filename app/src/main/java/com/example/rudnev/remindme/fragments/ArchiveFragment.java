package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.CreateItemDialog;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.viewmodels.ArchiveViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ArchiveFragment extends AbstractTabFragment implements CreateItemDialog.EditNameDialogListener,
        RemindItemClickListener, TabFragmentAdapter.TabSelectedListener {

    private static final int LAYOUT = R.layout.archive_fragment;
    private static final int REQUEST_ARCHIVE = 3;

    private List<RemindDTO> datas;
    private ArchiveListAdapter adapter;
    RecyclerView rv;


    private ArchiveViewModel mArchiveViewModel;

    public static ArchiveFragment getInstance(Context context, List<RemindDTO> datas) {
        Bundle args = new Bundle();
        ArchiveFragment archiveFragment = new ArchiveFragment();
        archiveFragment.setArguments(args);
        archiveFragment.setData(datas);
        archiveFragment.setContext(context);
        archiveFragment.setTitle(context.getString(R.string.archive_tab));
        return archiveFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(LAYOUT, container, false);
        rv = (RecyclerView) view.findViewById(R.id.recyclerViewArchive);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ArchiveListAdapter(datas, this);
        rv.setAdapter(adapter);
        mArchiveViewModel = ViewModelProviders.of(this).get(ArchiveViewModel.class);
        mArchiveViewModel.getAllReminds().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                // Update the cached copy of the words in the adapter.
                adapter.setData(reminds);
                datas = reminds;
            }
        });
        return view;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public void setData(List<RemindDTO> data) {
        this.datas = data;
    }


    @Override
    public void remindListRemoveClicked(View v, int position) {
        mArchiveViewModel.delete(datas.get(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datas.get(position).getDate());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_ARCHIVE);
        createItemDialog.setDateField(calendar);
        createItemDialog.setmUpdateRemindItem(datas.get(position));
        createItemDialog.show(fm, "create_item_dialog");

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

    }

    @Override
    public void onFinishEditDialog(RemindDTO remindItem, boolean fromEditDialog) {
        if (fromEditDialog) {
            mArchiveViewModel.update(remindItem);
        } else {
            mArchiveViewModel.insert(remindItem);
        }
    }


}
