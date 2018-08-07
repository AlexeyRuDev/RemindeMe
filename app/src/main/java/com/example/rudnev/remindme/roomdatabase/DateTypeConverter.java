package com.example.rudnev.remindme.roomdatabase;

import android.arch.persistence.room.TypeConverter;

import java.io.Serializable;
import java.util.Date;

public class DateTypeConverter implements Serializable{

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }
}