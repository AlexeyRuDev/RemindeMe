package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.rudnev.remindme.viewmodels.TodayFragmentViewModel;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public class TodayFragment extends AbstractTabFragment implements RemindItemClickListener,
        TabFragmentAdapter.TabSelectedListener, CreateItemDialog.EditNameDialogListener {

    private static final int LAYOUT = R.layout.today_fragment;
    private static final int REQUEST_TODAY = 1;
    Observer<List<RemindDTO>> observer;

    private static final String TAG = "TODAY_FRAGMENT";



    private FloatingActionButton fab;

    private RemindListAdapter adapter;
    RecyclerView rv;

    public static TodayFragment getInstance(Context context) {

        Bundle args = new Bundle();
        TodayFragment todayFragment = new TodayFragment();
        todayFragment.setArguments(args);
        todayFragment.setContext(context);
        todayFragment.setTitle(context.getString(R.string.today_tab));
        return todayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView ");
        view = inflater.inflate(LAYOUT, container, false);
        initFAB(view);
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(this);
        rv.setAdapter(adapter);
        /*observer = new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable List<RemindDTO> remindDTOS) {
                Log.d(TAG, "OnChanged ");
                if(remindDTOS!=null) {
                    filterListReminds(remindDTOS);
                    //adapter.setData(datas);
                }
            }
        };
        mViewModel.getAllReminds().observeForever(observer);*/
        mViewModel.getAllReminds().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                // Update the cached copy of the words in the adapter.
                Log.d(TAG, "OnChanged ");
                filterListReminds(reminds);
                //adapter.setData(datas);

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
                if (dateTimeComparator.compare(itemLocalDate.toDate(), localDate.toDate()) == 0) {
                    datas.add(item);
                }
            }
        }
        adapter.setData(datas);
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private void initFAB(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(TodayFragment.this, REQUEST_TODAY);
        createItemDialog.show(fm, "create_item_dialog");
    }

    @Override
    public void remindListRemoveClicked(View v, int position) {
        mViewModel.delete(adapter.getItemById(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_TODAY);
        createItemDialog.setmUpdateRemindItem(adapter.getItemById(position));

        createItemDialog.show(fm, "create_item_dialog");
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.getAllReminds().removeObserver(observer);
    }*/

    @Override
    public void popupMenuItemClicked(final View view, final int position) {
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
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (fromEditDialog) {
            mViewModel.update(remindItem);
        } else {
            mViewModel.insert(remindItem);
        }
    }
}
