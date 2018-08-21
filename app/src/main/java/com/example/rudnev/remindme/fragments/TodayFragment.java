package com.example.rudnev.remindme.fragments;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CursorAnchorInfo;
import android.widget.TextView;

import com.example.rudnev.remindme.CreateItemActivity;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.RemindListAdapter;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public class TodayFragment extends AbstractTabFragment implements RemindItemClickListener,
        TabFragmentAdapter.TabSelectedListener {

    private static final int LAYOUT = R.layout.today_fragment;
    private static final int REQUEST_TODAY = 1;
    private static final String TAG = "TODAY_FRAGMENT";


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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RemindListAdapter(this);
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

    public void showEditDialog() {
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        startActivityForResult(intent, REQUEST_TODAY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0){
            RemindDTO mRemindItem = (RemindDTO) data.getSerializableExtra("mRemindItem");
            if(mRemindItem!=null){
                if(data.getBooleanExtra("updateItem", false)){
                    mViewModel.update(mRemindItem);
                }else{
                    mViewModel.insert(mRemindItem);
                }
            }
        }
    }

    @Override
    public void remindListRemoveClicked(View v, int position) {
        mViewModel.delete(adapter.getItemById(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        intent.putExtra("mRemindItem", adapter.getItemById(position));
        startActivityForResult(intent, REQUEST_TODAY);
    }


    @Override
    public void popupMenuItemClicked(final View view, final int position) {
        TextView mTitleTV = (TextView) view.findViewById(R.id.title);
        PopupMenu popup = new PopupMenu(view.getContext(), mTitleTV);
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
        popup.setGravity(Gravity.END | Gravity.TOP);
        popup.show();
    }

    @Override
    public void onFragmentBecomesCurrent(boolean current) {

    }


}
