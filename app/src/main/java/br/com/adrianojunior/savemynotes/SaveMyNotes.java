package br.com.adrianojunior.savemynotes;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SaveMyNotes extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings
                .Builder()
                .setPersistenceEnabled(true)
                .build();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.setFirestoreSettings(settings);
    }
}
