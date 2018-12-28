package com.example.rudnev.remindme.activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rudnev.remindme.MainActivity;
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

    private static final int REQUEST_ARCHIVE = 3;
    private ArchiveListAdapter adapter;
    RecyclerView rv;
    FragmentsViewModel mViewModel;
    private List<RemindDTO> remindsData;
    private Toolbar toolbar;
    private SearchView searchView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppDefault);
        setContentView(R.layout.archive_activity);
        initToolbar();
        rv = (RecyclerView) findViewById(R.id.recyclerViewArchive);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ArchiveListAdapter( this);
        rv.setAdapter(adapter);
        mViewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);

        mViewModel.getRemindsForArchive().observe(this, new Observer<List<RemindDTO>>() {
            @Override
            public void onChanged(@Nullable final List<RemindDTO> reminds) {
                adapter.setData(reminds);
                remindsData = reminds;
                adapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
    }


    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.tbArchive);
        toolbar.setTitle(R.string.archive_tab);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()){
                    adapter.setData(remindsData);
                }else{
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query.isEmpty()){
                    adapter.setData(remindsData);
                }else{
                    adapter.getFilter().filter(query);
                }
                return false;
            }
        });
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
