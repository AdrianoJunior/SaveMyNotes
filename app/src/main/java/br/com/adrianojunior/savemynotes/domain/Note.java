package br.com.adrianojunior.savemynotes.domain;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Note extends NoteID {

    String title;
    String desc;

    @ServerTimestamp
    Date date;

    public Note() {}

    public Note(String title, String desc, Date date) {
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
