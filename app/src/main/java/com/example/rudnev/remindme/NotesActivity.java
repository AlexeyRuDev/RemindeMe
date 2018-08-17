package com.example.rudnev.remindme;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.rudnev.remindme.adapter.NotesListAdapter;
import com.example.rudnev.remindme.dto.Notes;
import com.example.rudnev.remindme.viewmodels.NoteViewModel;

import java.util.Calendar;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements RemindItemClickListener{

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
    }
    @Override
    public void remindListRemoveClicked(View v, int position) {
        mViewModel.delete(adapter.getItemById(position));
    }

    @Override
    public void remindListUpdateClicked(View v, int position) {

        Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
        intent.putExtra("mNoteItem", adapter.getItemById(position));
        intent.putExtra("isNote", true);
        startActivityForResult(intent, REQUEST_NOTE);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode < 0){
            Notes mNoteItem = (Notes) data.getSerializableExtra("mNoteItem");
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
}
