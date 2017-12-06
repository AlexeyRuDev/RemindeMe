package com.example.rudnev.remindme.dto;


import java.util.Date;

public class RemindDTO {

    private String title;
    private String note;
    private String date;

    public RemindDTO(String title, String note, String date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
