package com.example.rudnev.remindme.fragments;

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

import java.util.List;


public class TodayFragment extends AbstractTabFragment implements RemindItemClickListener,
        TabFragmentAdapter.TabSelectedListener, CreateItemDialog.EditNameDialogListener{

    private static final int LAYOUT = R.layout.today_fragment;
    private static final int REQUEST_TODAY = 1;

    private TodayFragmentViewModel mTodayFragmentViewModel;


    private FloatingActionButton fab;
    private List<RemindDTO> datas;
    private RemindListAdapter adapter;
    RecyclerView rv;

    public static TodayFragment getInstance(Context context, List<RemindDTO> datas){

        Bundle args = new Bundle();
        TodayFragment todayFragment = new TodayFragment();
        todayFragment.setArguments(args);
        todayFragment.setData(datas);
        todayFragment.setContext(context);
        todayFragment.setTitle(context.getString(R.string.today_tab));
        return todayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        initFAB(view);
        rv = (RecyclerView)view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(this);
        rv.setAdapter(adapter);
        mTodayFragmentViewModel = ViewModelProviders.of(this).get(TodayFragmentViewModel.class);
        mTodayFragmentViewModel.getAllReminds().observe(this, new Observer<List<RemindDTO>>() {
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

    private void initFAB(View view) {
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
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
        mTodayFragmentViewModel.delete(datas.get(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_TODAY);
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
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if(fromEditDialog){
            mTodayFragmentViewModel.update(remindItem);
        }else{
            mTodayFragmentViewModel.insert(remindItem);
        }
    }
}
