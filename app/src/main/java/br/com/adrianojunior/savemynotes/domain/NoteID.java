package br.com.adrianojunior.savemynotes.domain;

import androidx.annotation.NonNull;

public class NoteID {

    public String NoteId;

    public <T extends NoteID> T withId(@NonNull final String id) {

        this.NoteId = id;
        return (T) this;
    }
}
