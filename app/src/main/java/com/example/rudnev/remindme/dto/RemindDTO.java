package com.example.rudnev.remindme.dto;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "remindtable")
public class RemindDTO implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "note")
    private String note;

    @NonNull
    @ColumnInfo(name = "date")
    private LocalDateTime date;

    public RemindDTO(long id, @NonNull String title, @NonNull String note, @NonNull LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public RemindDTO(@NonNull String title, @NonNull String note, @NonNull LocalDateTime date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public RemindDTO() {
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public void setNote(@NonNull String note) {
        this.note = note;
    }

    @NonNull
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDateTime date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
