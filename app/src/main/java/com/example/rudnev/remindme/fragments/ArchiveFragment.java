package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.Observer;
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
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ArchiveFragment extends AbstractTabFragment implements CreateItemDialog.EditNameDialogListener,
        RemindItemClickListener, TabFragmentAdapter.TabSelectedListener {

    private static final int LAYOUT = R.layout.archive_fragment;
    private static final int REQUEST_ARCHIVE = 3;
    private static final String TAG = "ARCHIVE_FRAGMENT";

    private ArchiveListAdapter adapter;
    RecyclerView rv;


    public static ArchiveFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ArchiveFragment archiveFragment = new ArchiveFragment();
        archiveFragment.setArguments(args);
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
        adapter = new ArchiveListAdapter( this);
        rv.setAdapter(adapter);

        mViewModel.getAllReminds().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                filterListReminds(reminds);
            }
        });
        return view;
    }

    private void filterListReminds(List<RemindDTO> reminds) {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        LocalDate localDate = LocalDate.now();
        List<RemindDTO>datas = new ArrayList<>();

        if (reminds != null) {
            for (RemindDTO item : reminds) {
                LocalDate itemLocalDate = LocalDate.fromDateFields(item.getDate());
                if (dateTimeComparator.compare(itemLocalDate.toDate(), localDate.toDate()) < 0) {
                    datas.add(item);
                }
            }
        }
        adapter.setData(datas);
    }

    public void setContext(Context context) {
        this.context = context;
    }




    @Override
    public void remindListRemoveClicked(View v, int position) {
        mViewModel.delete(adapter.getItemById(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(adapter.getItemById(position).getDate());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_ARCHIVE);
        createItemDialog.setDateField(calendar);
        createItemDialog.setmUpdateRemindItem(adapter.getItemById(position));
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
            mViewModel.update(remindItem);
        } else {
            mViewModel.insert(remindItem);
        }
    }

}
