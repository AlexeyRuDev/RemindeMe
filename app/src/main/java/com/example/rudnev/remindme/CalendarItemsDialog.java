package com.example.rudnev.remindme;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rudnev.remindme.activities.CreateItemActivity;
import com.example.rudnev.remindme.adapter.CalendarItemsListAdapter;
import com.example.rudnev.remindme.components.DaggerMainActivityComponent;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.modules.RemindItemClickListenerModule;
import com.example.rudnev.remindme.viewmodels.FragmentsViewModel;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CalendarItemsDialog extends DialogFragment implements RemindItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {


    CalendarItemsListAdapter adapter;

    private static final String TAG = "CALENDAR_ITEMS_DIALOG";
    private static final int REQUEST_CALENDAR_DIALOG = 2;
    private List<RemindDTO> datas;
    private RecyclerView listViewItems;
    private Button addItem;
    private TextView titleCalItemDialog;
    private Date date;
    Calendar calendar;
    private Context context;
    private FragmentsViewModel mViewModel;


    public static CalendarItemsDialog getInstance(Context context, Date date, FragmentsViewModel mViewModel) {
        Bundle args = new Bundle();
        CalendarItemsDialog calendarFragment = new CalendarItemsDialog();
        calendarFragment.setArguments(args);
        calendarFragment.setContext(context);
        calendarFragment.setDate(date);
        calendarFragment.setViewModel(mViewModel);
        return calendarFragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_items_dialog, container);
        addItem = view.findViewById(R.id.addCalItem);
        listViewItems = view.findViewById(R.id.recyclerViewCalItems);
        listViewItems.setLayoutManager(new LinearLayoutManager(context));
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        titleCalItemDialog = (TextView)view.findViewById(R.id.titleCalItemDialog);
        titleCalItemDialog.setText(calendar.get(Calendar.DATE) + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + " " + calendar.get(Calendar.YEAR));
        adapter = new CalendarItemsListAdapter(this);

        listViewItems.setAdapter(adapter);

        mViewModel.getRemindsForConcreteDate(LocalDateTime.fromDateFields(calendar.getTime())).observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                datas = reminds;
                adapter.setData(reminds);

            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listViewItems);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemActivity();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

    }


    private void showAddItemActivity(){
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        intent.putExtra("mDateField", calendar);
        startActivityForResult(intent, REQUEST_CALENDAR_DIALOG);
    }


    public void setContext(Context context) {
        this.context = context;
    }


    @Override
    public void remindListOpenClicked(View v, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datas.get(position).getDate().toDate());
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        intent.putExtra("mRemindItem", datas.get(position));
        intent.putExtra("mDateField", calendar);
        startActivityForResult(intent, REQUEST_CALENDAR_DIALOG);
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

    public void setViewModel(FragmentsViewModel viewModel){
        this.mViewModel = viewModel;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0){
            RemindDTO mRemindItem = (RemindDTO) data.getParcelableExtra("mRemindItem");
            if(mRemindItem!=null){
                if(data.getBooleanExtra("updateItem", false)){
                    mViewModel.update(mRemindItem);
                }else{
                    mViewModel.insert(mRemindItem);
                }
                adapter.notifyDataSetChanged();
            }
        }else if(datas.size()==0){
            dismiss();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        RemindDTO remindDTO = adapter.getItemById(position);
        int notificationID = remindDTO.getDate().getYear() + remindDTO.getDate().getMonthOfYear() + remindDTO.getDate().getDayOfMonth() +
                remindDTO.getDate().getHourOfDay() + remindDTO.getDate().getMinuteOfHour() + remindDTO.getDate().getSecondOfMinute();
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, notificationID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        pendingIntent.cancel();
        mViewModel.delete(datas.get(position));
    }
}
