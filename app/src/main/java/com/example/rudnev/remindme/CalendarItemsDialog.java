package com.example.rudnev.remindme;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rudnev.remindme.adapter.CalendarItemsListAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;
import com.example.rudnev.remindme.viewmodels.CalendarItemsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarItemsDialog extends DialogFragment implements RemindItemClickListener, CreateItemDialog.EditNameDialogListener {

    private static final int REQUEST_CALENDAR_DIALOG = 2;
    private List<RemindDTO> datas;
    private RecyclerView listViewItems;
    private Button addItem;
    private Date date;
    Calendar calendar;
    private CalendarItemsListAdapter adapter;
    private RemindDBAdapter dbAdapter;
    private Context context;
    private long mItemID;

    private CalendarItemsViewModel mCalendarItemsViewModel;

    public static CalendarItemsDialog getInstance(Context context, Date date) {
        Bundle args = new Bundle();
        CalendarItemsDialog calendarFragment = new CalendarItemsDialog();
        calendarFragment.setArguments(args);
        calendarFragment.setContext(context);
        calendarFragment.setDate(date);
        return calendarFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_items_dialog, container);
        addItem = view.findViewById(R.id.addCalItem);
        listViewItems = view.findViewById(R.id.recyclerViewCalItems);
        listViewItems.setLayoutManager(new LinearLayoutManager(context));
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        adapter = new CalendarItemsListAdapter(this);
        listViewItems.setAdapter(adapter);

        mCalendarItemsViewModel = ViewModelProviders.of(this).get(CalendarItemsViewModel.class);
        mCalendarItemsViewModel.getAllReminds().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                // Update the cached copy of the words in the adapter.
                adapter.setData(reminds);
                datas = reminds;
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateItemDialog createItemDialog = new CreateItemDialog();
                createItemDialog.setDateField(calendar);
                createItemDialog.setTargetFragment(CalendarItemsDialog.this, REQUEST_CALENDAR_DIALOG);
                createItemDialog.show(getFragmentManager(), "create_item");
            }
        });
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
        mCalendarItemsViewModel.delete(datas.get(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datas.get(position).getDate());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_CALENDAR_DIALOG);
        createItemDialog.setDateField(calendar);
        createItemDialog.setmUpdateRemindItem(datas.get(position));
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setData(List<RemindDTO> data) {
        this.datas = data;
    }


    @Override
    public void onFinishEditDialog(RemindDTO remindItem, boolean fromEditDialog) {
        if (fromEditDialog) {
            mCalendarItemsViewModel.update(remindItem);
        } else {
            mCalendarItemsViewModel.insert(remindItem);
        }
    }

    public interface CalendarItemsUpdateListener {
        void onCloseDialog();
    }
}
