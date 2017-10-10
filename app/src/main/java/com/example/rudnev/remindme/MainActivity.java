package com.example.rudnev.remindme;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;
import com.example.rudnev.remindme.sql.RemindDBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CreateItemDialog.EditNameDialogListener {

    private static final int LAYOUT = R.layout.activity_main;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    //private RemindDBHelper dbHelper;
    private RemindDBAdapter dbAdapter;
    private String textFromDialog;

    private TabFragmentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        //dbHelper = new RemindDBHelper(this);
        dbAdapter = new RemindDBAdapter(this);
        initNavigationView();
        initTabs();
        initFAB();
    }


    private void initFAB() {
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdapter.addItem("Test", "TestNote");
                new RemindMeTask().execute();
                //showEditDialog();

                /*List<RemindDTO>data = adapter.getDatas();
                data.add(new RemindDTO("New"));
                adapter.setDatas(data);
                adapter.updateRVAdapter();*/
            }
        });
    }

    public void setTextFromDialog(String textFromDialog) {
        this.textFromDialog = textFromDialog;
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CreateItemDialog createItemDialog = new CreateItemDialog();
        createItemDialog.show(fm, "create_item_dialog");
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open,
                R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                switch(item.getItemId()){
                    case R.id.actionNotifItem:
                        showNotificationTab();
                }
                return true;
            }
        });
    }



    private void initTabs() {
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        adapter = new TabFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //createMockData();
        new RemindMeTask().execute();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void createMockData() {
        List<RemindDTO>datas = new ArrayList<>();
        datas.add(new RemindDTO("Testd"));
        datas.add(new RemindDTO("Testa"));
        datas.add(new RemindDTO("Testb"));
        datas.add(new RemindDTO("Testc"));
        adapter.setDatas(datas);
    }

    private void showNotificationTab(){
        viewPager.setCurrentItem(Constants.TAB_TODO);
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }

    private class RemindMeTask extends AsyncTask<Void, Void, List<RemindDTO>>{

        @Override
        protected List<RemindDTO> doInBackground(Void... voids) {
            List<RemindDTO> datas = dbAdapter.getAllItems();
            //datas.add(new RemindDTO("Item1"));

            //FIX
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return datas;
        }

        @Override
        protected void onPostExecute(List<RemindDTO> remindDTO) {
            adapter.setDatas(remindDTO);
        }
    }

    /*private List<RemindDTO> getDataFromDB(){
        List<RemindDTO> datas = new ArrayList<>();
        //datas.add(new RemindDTO("Item1"));

        ContentValues cv = new ContentValues();
        String name = "TEST";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");

            do {
                datas.add(new RemindDTO(c.getString(nameColIndex)));
            } while (c.moveToNext());
        } else
            c.close();
        dbHelper.close();
        return datas;
    }*/

}
