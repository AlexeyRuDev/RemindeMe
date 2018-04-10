package com.example.rudnev.remindme;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    MainActivity mainActivity;

    @Rule
    public final ActivityTestRule<MainActivity> main = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mainActivity = main.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }

    @Test
    public void onCreate() throws Exception {
        Intent intent = new Intent(mainActivity.getApplication(), MainActivity.class);
        mainActivity.startActivity(intent);
    }

    @Test
    public void updateTabFragmentList() throws Exception {

    }

    @Test
    public void onFinishEditDialog() throws Exception {

    }

}