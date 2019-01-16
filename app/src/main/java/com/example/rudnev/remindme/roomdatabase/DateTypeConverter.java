package com.example.rudnev.remindme.roomdatabase;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTypeConverter implements Serializable{

    @TypeConverter
    public static LocalDateTime toDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value == null ? null : LocalDateTime.fromDateFields(date);
    }

    @TypeConverter
    public static String toString(LocalDateTime value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return value == null ? null : sdf.format(value.toDate());
    }
}