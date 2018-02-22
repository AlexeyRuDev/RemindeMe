package com.example.rudnev.remindme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarItemsDialog extends DialogFragment implements RemindItemClickListener, CreateItemDialog.EditNameDialogListener{

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

    public static CalendarItemsDialog getInstance(Context context, Date date){
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
        dbAdapter = new RemindDBAdapter(context);
        datas = new ArrayList<>();
        datas = dbAdapter.getAllItems(1, date);
        adapter = new CalendarItemsListAdapter(datas, this);
        listViewItems.setAdapter(adapter);

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
        dbAdapter = new RemindDBAdapter(context);
        dbAdapter.removeItem(datas.get(position).getId());
        datas = dbAdapter.getAllItems(1, date);
        adapter.setData(datas);
        adapter.notifyDataSetChanged();
        ((CalendarItemsUpdateListener)getTargetFragment()).onCloseDialog();
        ((MainActivity)getActivity()).updateTabFragmentList();
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datas.get(position).getDate());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.setTargetFragment(this, REQUEST_CALENDAR_DIALOG);
        createItemDialog.setDateField(calendar);
        Bundle args = new Bundle();
        mItemID = datas.get(position).getId();
        args.putString("title", datas.get(position).getTitle());
        args.putString("note", datas.get(position).getNote());
        //args.putString("date", data.get(position).getDate());
        //args.putLong("itemID", datas.get(position).getId());
        createItemDialog.setArguments(args);
        createItemDialog.show(fm, "create_item_dialog");
        ((MainActivity)getActivity()).updateTabFragmentList();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra("title");
            String note = data.getStringExtra("note");
            Date date = new Date();
            date.setTime(data.getLongExtra("date", 0));
            onFinishEditDialog(mItemID, title, note, date, true);
            //int weight = data.getIntExtra(WeightDialogFragment.TAG_WEIGHT_SELECTED, -1);
        }
    }

    @Override
    public void onFinishEditDialog(long itemID, String inputText, String note, Date date, boolean fromEditDialog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("RU"));
        if(fromEditDialog){
            dbAdapter.updateItem(itemID, inputText, note, sdf.format(date));
        }else{
            dbAdapter.addItem(inputText, note, sdf.format(date));
        }
        datas = dbAdapter.getAllItems(1, date);
        adapter.setData(datas);
        CalendarItemsUpdateListener calendarItemsUpdateListener = (CalendarItemsUpdateListener) getTargetFragment();
        calendarItemsUpdateListener.onCloseDialog();
        ((MainActivity)getActivity()).updateTabFragmentList();
        adapter.notifyDataSetChanged();
    }

    public interface CalendarItemsUpdateListener {
        void onCloseDialog();
    }
}
