package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todolist.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakenActivity extends AppCompatActivity {

     EditText editText_title, editText_notes;
     ImageView imageView;
     Notes notes;

     boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taken);

        imageView = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);

        notes = new Notes();
        try {
           notes = (Notes) getIntent().getSerializableExtra("old_note");
           editText_title.setText(notes.getTitle());
           editText_notes.setText(notes.getNotes());
           isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        imageView.setOnClickListener(view -> {
            String title = editText_title.getText().toString();
            String description = editText_notes.getText().toString();

            if(description.isEmpty()){
                Toast.makeText(NotesTakenActivity.this, "Please Add Some Notes", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
            Date date = new Date();


            if(!isOldNote){
                notes = new Notes();
            }

            notes.setTitle(title);
            notes.setNotes(description);
            notes.setDate(formatter.format(date));

            Intent intent = new Intent();
            intent.putExtra("note", notes);

            setResult(Activity.RESULT_OK, intent);
            finish();
        });

    }
}