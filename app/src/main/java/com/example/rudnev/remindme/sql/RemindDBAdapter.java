package com.example.rudnev.remindme.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rudnev.remindme.dto.RemindDTO;

import java.util.ArrayList;
import java.util.List;


public class RemindDBAdapter {

    Context context;
    RemindDBHelper dbHelper;

    public RemindDBAdapter(Context context) {
        this.context = context;
        dbHelper = new RemindDBHelper(context);
    }

    public void addItem(String title, String note){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("name", title);
        long rowID = db.insert("mytable", null, cv);
        dbHelper.close();
    }

    public List<RemindDTO> getAllItems(){
        List<RemindDTO> datas = new ArrayList<>();
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
    }

    public void updateItem(){}

    public void removeItem(String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("mytable", "name = " + "\""+name+"\"", null);
        dbHelper.close();
    }
}
