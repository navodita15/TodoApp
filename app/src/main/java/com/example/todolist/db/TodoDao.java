package com.example.todolist.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TodoEntity todoEntity);

    @Query("SELECT * FROM todo_table ORDER BY checked,id DESC")
    LiveData<List<TodoEntity>> getTodo();

    @Query("SELECT * FROM todo_table where title LIKE :search")
    LiveData<List<TodoEntity>> getSearchTodo(String search);

    @Query("UPDATE todo_table SET title = :newTitle , checked = :checked WHERE title =:oldTitle ")
    void update(String oldTitle, String newTitle, boolean checked);
}
