package com.example.rudnev.remindme.dto;


import java.util.Date;

public class RemindDTO {

    private long id;
    private String title;
    private String note;
    private Date date;

    public RemindDTO(long id, String title, String note, Date date) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
