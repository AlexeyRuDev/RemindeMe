package com.example.rudnev.remindme.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RecyclerItemTouchHelper;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.ArchiveListAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.viewmodels.FragmentsViewModel;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ArchiveActivity extends AppCompatActivity implements RemindItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private ArchiveListAdapter adapter;
    RecyclerView rv;
    FragmentsViewModel mViewModel;
    private static final int REQUEST_ARCHIVE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_activity);
        rv = (RecyclerView) findViewById(R.id.recyclerViewArchive);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ArchiveListAdapter( this);
        rv.setAdapter(adapter);
        mViewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);

        mViewModel.getRemindsForArchive().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                adapter.setData(reminds);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
    }


    @Override
    public void remindListOpenClicked(View v, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(adapter.getItemById(position).getDate().toDate());
        Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
        intent.putExtra("mRemindItem", adapter.getItemById(position));
        intent.putExtra("mDateField", calendar);
        startActivityForResult(intent, REQUEST_ARCHIVE);

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
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ArchiveListAdapter.RemindViewHolder) {
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
            mViewModel.delete(adapter.getItemById(position));
        }
    }
}