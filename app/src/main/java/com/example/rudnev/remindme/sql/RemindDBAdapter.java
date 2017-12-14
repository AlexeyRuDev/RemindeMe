package com.example.rudnev.remindme.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    private final int TODAY_OR_CALENDAR_ITEMS_FRAGMENT = 1;
    private final int CALENDAR_FRAGMENT = 2;
    private final int ARCHIVE_FRAGMENT = 3;

    private Context context;
    private RemindDBHelper dbHelper;
    private String selection = null;
    private String[] selectionArgs = null;

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

    public List<RemindDTO> getAllItems(int tabNumber, Date date) {
        SimpleDateFormat sdfCal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<RemindDTO> datas = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        switch(tabNumber){
            case TODAY_OR_CALENDAR_ITEMS_FRAGMENT:
                if(date != null) {
                    cal.setTime(date);
                }
                selection = "date(date) = ?";
                break;
            case CALENDAR_FRAGMENT:
                selection = "date(date) >= ?";
                break;
            case ARCHIVE_FRAGMENT:
                selection = "date(date) < ?";
                break;
        }
        String tadayFormstDate = sdfCal.format(cal.getTime());
        selectionArgs = new String[] { tadayFormstDate };
        //selectionArgs = new String[] { CalendarDay.today().toString() };
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("remindtable", null, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int titleColIndex = c.getColumnIndex("title");
            int noteColIndex = c.getColumnIndex("note");
            int dateColIndex = c.getColumnIndex("date");

            do {
                //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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

   /* public static String formatDateTime(Context context, String timeToFormat) {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                long when = date.getTime();
                int flags = 0;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

                finalDateTime = android.text.format.DateUtils.formatDateTime(context,
                        when + TimeZone.getDefault().getOffset(when), flags);
            }
        }
        return finalDateTime;
    }*/

}
