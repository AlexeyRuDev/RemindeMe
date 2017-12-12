package com.example.rudnev.remindme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.example.rudnev.remindme.sql.RemindDBAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements CreateItemDialog.EditNameDialogListener {

    private static final int LAYOUT = R.layout.activity_main;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    //private RemindDBHelper dbHelper;
    private RemindDBAdapter dbAdapter;

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
                //dbAdapter.addItem("Test", "TestNote");
                showEditDialog();

                /*List<RemindDTO>data = adapter.getDatas();
                data.add(new RemindDTO("New"));
                adapter.setDatas(data);
                adapter.updateRVAdapter();*/
            }
        });
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("PageSelected position ", " " + position);
                if(position == 0){
                    fab.show();
                }else{
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //createMockData();
        //new RemindMeTask().execute();
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void showNotificationTab(){
        viewPager.setCurrentItem(Constants.TAB_TODO);
    }

    @Override
    public void onFinishEditDialog(long itemID, String inputText, String note, Date date, boolean fromEditDialog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if(fromEditDialog){
            dbAdapter.updateItem(itemID, inputText, note, sdf.format(date));
        }else{
            dbAdapter.addItem(inputText, note, sdf.format(date));
        }
        adapter.setDatas(dbAdapter.getAllItems(1));
        adapter.notifyDataSetChanged();
        //new RemindMeTask().execute();
    }

    /*private class RemindMeTask extends AsyncTask<Void, Void, List<RemindDTO>>{

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
    }*/

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
