package br.com.adrianojunior.savemynotes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.adrianojunior.savemynotes.R;
import livroandroid.lib.utils.AndroidUtils;

public class NewNoteActivity extends BaseActivity {

    private TextInputEditText titleTxt;
    private TextInputEditText descTxt;
    private FloatingActionButton addNoteBtn;

    private String noteTitle;
    private String noteDesc;

    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nova Nota");


        titleTxt = findViewById(R.id.new_note_title);
        descTxt = findViewById(R.id.new_note_desc);
        addNoteBtn = findViewById(R.id.new_note_btn);
        progressBar = findViewById(R.id.new_note_progress);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            firestore = FirebaseFirestore.getInstance();

            addNoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    noteTitle = titleTxt.getText().toString().trim();

                    noteDesc = descTxt.getText().toString();

                    if (!noteTitle.isEmpty() && !noteDesc.isEmpty()
                            && noteTitle != null && noteDesc != null) {


                        progressBar.setIndeterminate(true);
                        progressBar.setVisibility(View.VISIBLE);

                        if (AndroidUtils.isNetworkAvailable(getContext())) {

                            Map<String, Object> note = new HashMap<>();
                            note.put("title", noteTitle);
                            note.put("desc", noteDesc);
                            note.put("date", FieldValue.serverTimestamp());

                            firestore.collection(user.getUid()).document().set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        toast("Nota salva com sucesso");
                                        sendToMain();

                                    } else {

                                        toast("Ocorreu um erro ao salvar a nota. Tente novamente!");

                                    }

                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            toast(R.string.msg_error_conexao_indisponivel);
                        }
                    }
                }
            });


        }
    }
}
