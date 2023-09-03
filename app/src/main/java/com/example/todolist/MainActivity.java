package com.example.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.todolist.Adapters.NotesListAdapter;
import com.example.todolist.Database.RoomDatabase;
import com.example.todolist.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDatabase roomDatabase;
    FloatingActionButton fab_add;
    SearchView searchView_home;

    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);

        roomDatabase = RoomDatabase.getInstance(this);

        notes = roomDatabase.mainDAO().getAll();

        updateRecycler(notes);

        fab_add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NotesTakenActivity.class);
            startActivityForResult(intent, 101);

        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();

        for(Notes singleNote : notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }

        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                roomDatabase.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(roomDatabase.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == 102) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                roomDatabase.mainDAO().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(roomDatabase.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakenActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.pop_menu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    roomDatabase.mainDAO().pin(selectedNote.getID(), false);
                    Toast.makeText(MainActivity.this, "Unpinned!", Toast.LENGTH_SHORT).show();
                }
                else {
                    roomDatabase.mainDAO().pin(selectedNote.getID(), true);
                    Toast.makeText(MainActivity.this, "Pinned!", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(roomDatabase.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                roomDatabase.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }
}