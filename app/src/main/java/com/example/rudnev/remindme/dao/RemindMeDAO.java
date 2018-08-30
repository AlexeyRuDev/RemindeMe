package com.example.rudnev.remindme.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.rudnev.remindme.dto.RemindDTO;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.List;

@Dao
public interface RemindMeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RemindDTO remindDTO);

    @Update
    void update(RemindDTO remindDTO);

    @Delete
    void delete(RemindDTO remindDTO);

    @Query("DELETE FROM remindtable")
    void deleteAll();

    @Query("SELECT * from remindtable ORDER BY title ASC")
    LiveData<List<RemindDTO>> getAllReminds();

    @Query("SELECT * from remindtable WHERE date(date) = date(:date)")
    LiveData<List<RemindDTO>> getRemindsForTodayFragment(LocalDateTime date);

    @Query("SELECT * from remindtable WHERE date(date) >= date(:date)")
    LiveData<List<RemindDTO>> getRemindsForCalendarFragment(LocalDateTime date);

    @Query("SELECT * from remindtable WHERE date(date) < date(:date)")
    LiveData<List<RemindDTO>> getRemindsForArchiveFragment(LocalDateTime date);
}
