package com.example.rudnev.remindme.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RemindDBHelper extends SQLiteOpenHelper {

    public RemindDBHelper(Context context) {
        super(context, "myRemindDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table remindtable ("
                + "id integer primary key autoincrement,"
                + "title text, note text, date date"
                + ");");
        db.execSQL("create table remindnotestable ("
                + "id integer primary key autoincrement,"
                + "title text, note text, date date"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
