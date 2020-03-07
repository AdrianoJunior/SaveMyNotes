package br.com.adrianojunior.savemynotes.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.adrianojunior.savemynotes.R;
import br.com.adrianojunior.savemynotes.activity.NewNoteActivity;
import br.com.adrianojunior.savemynotes.adapter.NotesAdapter;
import br.com.adrianojunior.savemynotes.domain.Note;

public class NotesFrag extends BaseFragment {

    private List<Note> noteList;
    private RecyclerView notesRecyclerView;
    private FloatingActionButton addNoteFAB;

    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private NotesAdapter notesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();

        noteList = new ArrayList<>();

        notesRecyclerView = view.findViewById(R.id.notes_recycler_view);

        notesAdapter = new NotesAdapter(noteList, getContext());
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notesRecyclerView.setAdapter(notesAdapter);

        if (user != null) {

            firestore = FirebaseFirestore.getInstance();

            if (firestore != null) {

                Query query = firestore.collection(user.getUid()).orderBy("date", Query.Direction.DESCENDING);

                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (e != null) {
                                toast("ERRO: " + e.getMessage());
                            }

                            switch (doc.getType()) {

                                case ADDED:

                                    String culto_id = doc.getDocument().getId();

                                    Note note = doc.getDocument().toObject(Note.class).withId(culto_id);
                                    noteList.add(note);
                                    notesAdapter.notifyDataSetChanged();

                                    break;

                                case MODIFIED:

                                    note = doc.getDocument().toObject(Note.class);
                                    noteList.add(note);
                                    notesAdapter.notifyDataSetChanged();

                                    break;

                                case REMOVED:

                                    note = doc.getDocument().toObject(Note.class);
                                    noteList.add(note);
                                    notesAdapter.getItemCount();
                                    notesAdapter.notifyDataSetChanged();


                                    break;
                            }

                            if (documentSnapshots.getDocuments().isEmpty()) {

                                toast("Nenhuma nota encontrada para " + user.getDisplayName());

                            }
                        }
                    }
                });

            }


        }

        addNoteFAB = view.findViewById(R.id.add_note_fab);

        addNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getContext(), NewNoteActivity.class);
                startActivity(addIntent);
            }
        });


    }
}
