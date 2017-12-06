package com.example.rudnev.remindme.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rudnev.remindme.dto.RemindDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RemindDBAdapter {

    Context context;
    RemindDBHelper dbHelper;

    public RemindDBAdapter(Context context) {
        this.context = context;
        dbHelper = new RemindDBHelper(context);
    }

    public void addItem(String title, String note, String date) {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("title", title);
        cv.put("note", note);
        cv.put("date", date);
        long rowID = db.insert("remindtable", null, cv);
        dbHelper.close();
    }

    public List<RemindDTO> getAllItems() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<RemindDTO> datas = new ArrayList<>();
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
                datas.add(new RemindDTO(c.getString(titleColIndex), c.getString(noteColIndex), c.getString(dateColIndex)));
            } while (c.moveToNext());
        } else
            c.close();
        dbHelper.close();
        return datas;
    }

    public void updateItem() {
    }

    public void removeItem(String title) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("remindtable", "title = " + "\"" + title + "\"", null);
        dbHelper.close();
    }
}
