package com.example.rudnev.remindme.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.rudnev.remindme.dto.RemindDTO;

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

    @Query("SELECT * from remindtable WHERE date = :date")
    LiveData<List<RemindDTO>> getRemindsForTodayFragment(Date date);

    @Query("SELECT * from remindtable WHERE date >= :date")
    LiveData<List<RemindDTO>> getRemindsForCalendarFragment(Date date);

    @Query("SELECT * from remindtable WHERE date < :date")
    LiveData<List<RemindDTO>> getRemindsForArchiveFragment(Date date);
}
