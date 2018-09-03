package com.example.rudnev.remindme.dto;


import android.app.PendingIntent;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "remindtable")
public class RemindDTO implements Parcelable {

    public static final Parcelable.Creator<RemindDTO> CREATOR = new Parcelable.Creator<RemindDTO>() {
        // распаковываем объект из Parcel
        public RemindDTO createFromParcel(Parcel in) {
            return new RemindDTO(in);
        }

        public RemindDTO[] newArray(int size) {
            return new RemindDTO[size];
        }
    };

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


    public RemindDTO(Parcel parcel){
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.note = parcel.readString();
        this.date = (LocalDateTime) parcel.readSerializable();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(note);
        dest.writeSerializable(date);
    }
}
