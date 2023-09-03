package com.example.todolist.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.example.todolist.Models.Notes;

@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static RoomDatabase database;
    private static final String DATABASE_NAME = "NoteApp";

    public synchronized static RoomDatabase getInstance(Context context){

        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDatabase.class, "DATABASE_NAME")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return database;
    }

    public abstract MainDAO mainDAO();
}
