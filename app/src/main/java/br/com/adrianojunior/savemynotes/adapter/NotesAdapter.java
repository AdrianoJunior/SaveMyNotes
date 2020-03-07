package br.com.adrianojunior.savemynotes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import br.com.adrianojunior.savemynotes.R;
import br.com.adrianojunior.savemynotes.activity.NoteActivity;
import br.com.adrianojunior.savemynotes.domain.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private final List<Note> notes_list;
    private Context context;

    public NotesAdapter(List<Note> notes_list, Context context) {
        this.notes_list = notes_list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_view, parent, false);
        context = parent.getContext();

        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        Note note = notes_list.get(position);

        String title = note.getTitle();
        holder.setNoteTitleView(title);

        String desc = note.getDesc();
        holder.setNoteDescView(desc);

        long milliseconds = note.getDate().getTime();
        String data = android.text.format.DateFormat
                .format("dd/MM/yyyy HH:mm", new Date(milliseconds)).toString();
        holder.setNoteDateView(data);

        final String note_id = note.NoteId;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteIntent = new Intent(context, NoteActivity.class);
                noteIntent.putExtra("note_id", note_id);
                context.startActivity(noteIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView noteTitleView;
        private TextView noteDateView;
        private TextView noteDescView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setNoteTitleView(String title) {
            noteTitleView = mView.findViewById(R.id.note_title_txt);
            noteTitleView.setText(title);
        }

        public void setNoteDescView(String desc) {
            noteTitleView = mView.findViewById(R.id.note_desc_txt);
            noteTitleView.setText(desc);
        }

        public void setNoteDateView(String date) {
            noteTitleView = mView.findViewById(R.id.note_modified_txt);
            noteTitleView.setText(date);
        }
    }
}
