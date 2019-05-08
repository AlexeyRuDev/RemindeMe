package com.example.rudnev.remindme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.rudnev.remindme.activities.ArchiveActivity;
import com.example.rudnev.remindme.activities.NotesActivity;
import com.example.rudnev.remindme.adapter.TabFragmentAdapter;
import com.example.rudnev.remindme.components.DaggerMainActivityComponent;
import com.example.rudnev.remindme.modules.ContextModule;
import com.example.rudnev.remindme.modules.MainActivityModule;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

    @Inject
    TabFragmentAdapter adapter;

    private static final int LAYOUT = R.layout.activity_main;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppDefault);
        setContentView(LAYOUT);
        initToolbar();
        JodaTimeAndroid.init(this);
        initNavigationView();
        initTabs();
        initFAB();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initFAB() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter!=null && viewPager!=null && viewPager.getCurrentItem()==0)
                    adapter.showEditDialog();
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
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
                    case R.id.actionArchiveItem:
                        Intent intent = new Intent(getApplicationContext(), ArchiveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.actionNotesItem:
                        intent = new Intent(getApplicationContext(), NotesActivity.class);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });
    }

    private void initTabs() {
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        DaggerMainActivityComponent.builder()
                .contextModule(new ContextModule(this))
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .injectMainActivity(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Log.i("PageSelected position ", " " + position);
                if(position == Constants.TAB_TODAY){
                    fab.show();
                }else{
                    fab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /*private void showCalendarTab(){
        viewPager.setCurrentItem(Constants.TAB_CALENDAR);
    }*/
}
