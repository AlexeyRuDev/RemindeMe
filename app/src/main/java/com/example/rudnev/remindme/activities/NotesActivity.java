package com.example.rudnev.remindme.activities;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.RecyclerItemTouchHelper;
import com.example.rudnev.remindme.RemindItemClickListener;
import com.example.rudnev.remindme.adapter.NotesListAdapter;
import com.example.rudnev.remindme.components.DaggerNoteActivityComponent;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.modules.NoteActivityModule;
import com.example.rudnev.remindme.modules.RemindItemClickListenerModule;
import com.example.rudnev.remindme.viewmodels.NoteViewModel;

import java.util.List;

import javax.inject.Inject;

public class NotesActivity extends AppCompatActivity implements RemindItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @Inject
    NotesListAdapter adapter;

    private static final int REQUEST_NOTE = 4;
    private static final int LAYOUT = R.layout.notes_activity;

    RecyclerView rv;
    private List<Notes> notes;
    NoteViewModel mViewModel;
    FloatingActionButton fab;
    private Toolbar toolbar;
    private SearchView searchView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppDefault);
        setContentView(LAYOUT);
        initToolbar();
        initRecyclerView();
        initViewModel();
        initFAB();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, getTheme()));
        }
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);
    }

    @Override
    public void remindListOpenClicked(View v, int position) {

        Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
        intent.putExtra("mNoteItem", adapter.getItemById(position));
        intent.putExtra("isNote", true);
        startActivityForResult(intent, REQUEST_NOTE);

    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.tbNotes);
        toolbar.setTitle(R.string.notes_tab);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initRecyclerView(){
        rv = (RecyclerView) findViewById(R.id.recyclerViewNote);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        DaggerNoteActivityComponent.builder()
                .remindItemClickListenerModule(new RemindItemClickListenerModule(this))
                .noteActivityModule(new NoteActivityModule(this))
                .build()
                .injectNotesActivity(this);
        rv.setAdapter(adapter);
    }

    private void initViewModel(){
        mViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mViewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable final List<Notes> reminds) {
                adapter.setData(reminds);
                notes = reminds;
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initFAB(){
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
                intent.putExtra("isNote", true);
                startActivityForResult(intent, REQUEST_NOTE);
            }
        });
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
                    adapter.setData(notes);
                }else{
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query.isEmpty()){
                    adapter.setData(notes);
                }else{
                    adapter.getFilter().filter(query);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0){
            Notes mNoteItem = (Notes) data.getParcelableExtra("mNoteItem");
            if(mNoteItem!=null){
                if(data.getBooleanExtra("updateItem", false)){
                    mViewModel.update(mNoteItem);
                }else{
                    mViewModel.insert(mNoteItem);
                }
                adapter.notifyDataSetChanged();
            }
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        mViewModel.delete(adapter.getItemById(position));
    }
}
