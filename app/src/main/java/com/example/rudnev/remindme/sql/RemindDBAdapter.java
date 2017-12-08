package com.example.rudnev.remindme.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rudnev.remindme.EventDecorator;
import com.example.rudnev.remindme.R;
import com.example.rudnev.remindme.dto.RemindDTO;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class RemindDBAdapter {

    Context context;
    RemindDBHelper dbHelper;

    public RemindDBAdapter(Context context) {
        this.context = context;
        dbHelper = new RemindDBHelper(context);
    }

    public long addItem(String title, String note, String date) {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("title", title);
        cv.put("note", note);
        cv.put("date", date);
        long rowID = db.insert("remindtable", null, cv);
        dbHelper.close();
        return rowID;
    }

    public List<RemindDTO> getAllItems() {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<RemindDTO> datas = new ArrayList<>();
        HashSet<CalendarDay> dates = new HashSet<>();
        Calendar cal = Calendar.getInstance();
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("remindtable", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int titleColIndex = c.getColumnIndex("title");
            int noteColIndex = c.getColumnIndex("note");
            int dateColIndex = c.getColumnIndex("date");

            do {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                try {
                    cal.setTime(sdf.parse(c.getString(dateColIndex)));
                    datas.add(new RemindDTO(c.getLong(idColIndex), c.getString(titleColIndex), c.getString(noteColIndex), cal.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (c.moveToNext());
        } else
            c.close();
        dbHelper.close();
        return datas;
    }

    public void updateItem(long itemID, String title, String note, String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("note", note);
        cv.put("date", date);
        db.update("remindtable", cv, "id = " + "\"" + itemID + "\"", null);
        dbHelper.close();
    }

    public void removeItem(String title) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("remindtable", "title = " + "\"" + title + "\"", null);
        dbHelper.close();
    }
}
