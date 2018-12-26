package com.example.rudnev.remindme.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.rudnev.remindme.activities.CreateItemActivity;
import com.example.rudnev.remindme.adapter.NotesListAdapter;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.viewmodels.NoteViewModel;

import java.util.List;

public class NotesActivity extends AppCompatActivity implements RemindItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final int LAYOUT = R.layout.notes_activity;
    private NotesListAdapter adapter;
    RecyclerView rv;
    NoteViewModel mViewModel;
    FloatingActionButton fab;
    private static final int REQUEST_NOTE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        rv = (RecyclerView) findViewById(R.id.recyclerViewNote);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new NotesListAdapter( this);
        rv.setAdapter(adapter);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        mViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mViewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable final List<Notes> reminds) {
                adapter.setData(reminds);
                adapter.notifyDataSetChanged();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
                intent.putExtra("isNote", true);
                startActivityForResult(intent, REQUEST_NOTE);
            }
        });

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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        mViewModel.delete(adapter.getItemById(position));
    }
}
