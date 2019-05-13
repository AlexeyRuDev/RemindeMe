package com.example.rudnev.remindme.fragments;

import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rudnev.remindme.NotificationReceiver;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RecyclerItemTouchHelper;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.activities.CreateItemActivity;
import com.example.rudnev.remindme.adapter.TodayListAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;

import java.util.List;


public class TodayFragment extends AbstractTabFragment implements RemindItemClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final int LAYOUT = R.layout.today_fragment;
    private static final int REQUEST_TODAY = 1;
    private static final String TAG = "TODAY_FRAGMENT";


    private TodayListAdapter adapter;
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
        rv.setLayoutManager(new LinearLayoutManager(getCurrentContext()));
        adapter = new TodayListAdapter(this);
        rv.setAdapter(adapter);

        mViewModel.getRemindsForToday().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                adapter.setData(reminds);

            }
        });
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
        return view;
    }


    public void setContext(Context context) {
        setCurrentContext(context);
    }

    public void showEditDialog() {
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        startActivityForResult(intent, REQUEST_TODAY);
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
            }
        }
    }

    @Override
    public void remindListOpenClicked(View v, int position) {
        Intent intent = new Intent(getActivity(), CreateItemActivity.class);
        intent.putExtra("mRemindItem", adapter.getItemById(position));
        startActivityForResult(intent, REQUEST_TODAY);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof TodayListAdapter.RemindViewHolder) {
            // get the removed item name to display it in snack bar
            //String name = cartList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            //final RemindDTO deletedItem = cartList.get(viewHolder.getAdapterPosition());
            //final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            //adapter.removeItem(viewHolder.getAdapterPosition());

            /*showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    //adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
            RemindDTO remindDTO = adapter.getItemById(position);
            int notificationID = remindDTO.getDate().getYear() + remindDTO.getDate().getMonthOfYear() + remindDTO.getDate().getDayOfMonth() +
                    remindDTO.getDate().getHourOfDay() + remindDTO.getDate().getMinuteOfHour() + remindDTO.getDate().getSecondOfMinute();
            Intent notificationIntent = new Intent(getCurrentContext(), NotificationReceiver.class);
            notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, notificationID);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getCurrentContext(), notificationID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            pendingIntent.cancel();
            mViewModel.delete(remindDTO);
        }
    }
}
