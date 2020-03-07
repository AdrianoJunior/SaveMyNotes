package br.com.adrianojunior.savemynotes.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import br.com.adrianojunior.savemynotes.R;
import br.com.adrianojunior.savemynotes.domain.Note;
import livroandroid.lib.utils.AndroidUtils;

public class NoteActivity extends BaseActivity {

    private String noteTitle;
    private String noteDate;
    private String noteDesc;

    private TextView dateText;
    private TextView descText;

    private String noteKey;

    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private Long milliseconds;
    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = FirebaseAuth.getInstance().getCurrentUser();

        dateText = findViewById(R.id.note_page_date);
        descText = findViewById(R.id.note_page_desc);

        firestore = FirebaseFirestore.getInstance();

        noteKey = getIntent().getStringExtra("note_id");

        if (noteKey != null) {

            firestore.collection(user.getUid()).document(noteKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            note = task.getResult().toObject(Note.class);

                            noteTitle = note.getTitle();

                            milliseconds = note.getDate().getTime();

                            noteDate = android.text.format.DateFormat.format("dd/MM/yyyy HH:mm",
                                    new Date(milliseconds)).toString().trim();

                            noteDesc = note.getDesc();


                            setValues();

                        } else {

                            sendToMain();

                        }

                        if (document.getData().isEmpty()) {

                            sendToMain();

                        }
                    }
                }
            });
        }
    }


    private void setValues() {

        if (noteDate != null && noteDesc != null && noteTitle != null) {

            dateText.setText(noteDate);
            descText.setText(noteDesc);
            getSupportActionBar().setTitle(noteTitle);

        }
    }

    private void deleteNote() {

        firestore.collection(user.getUid()).document(noteKey).delete();
        sendToMain();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home:
                ActivityCompat.finishAfterTransition(getActivity());
                return true;
            case R.id.action_delete:
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    deleteNote();
                } else {
                    toast("Verifique a sua conexão com a internet.");
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_activity, menu);

        return true; //super.onCreateOptionsMenu(menu);
    }
}
