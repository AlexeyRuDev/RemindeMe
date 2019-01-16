package com.example.rudnev.remindme.dto;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "notestable")
public class Notes implements Parcelable {

    public static final Parcelable.Creator<Notes> CREATOR = new Parcelable.Creator<Notes>() {
        // распаковываем объект из Parcel
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        public Notes[] newArray(int size) {
            return new Notes[size];
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

    public Notes(Parcel parcel){
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.note = parcel.readString();
    }

    public Notes(long id, @NonNull String title, @NonNull String note, @NonNull Date date) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public Notes(@NonNull String title, @NonNull String note, @NonNull Date date) {
        this.title = title;
        this.note = note;
    }

    public Notes() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
