package com.example.todolist;

import androidx.cardview.widget.CardView;

import com.example.todolist.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
