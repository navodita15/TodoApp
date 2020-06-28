package com.example.todolist.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TodoEntity.class}, version = 1, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDao mTodoDao();

    private static volatile TodoRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService dbExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TodoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                            , TodoRoomDatabase.class,
                            "todo_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
