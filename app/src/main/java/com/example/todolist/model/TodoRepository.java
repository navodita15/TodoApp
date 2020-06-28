package com.example.todolist.model;

import android.app.Application;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.example.todolist.db.TodoDao;
import com.example.todolist.db.TodoEntity;
import com.example.todolist.db.TodoRoomDatabase;

import java.util.List;

public class TodoRepository {

    private TodoDao mTodoDao;
    private LiveData<List<TodoEntity>> mAllTodo;

    public TodoRepository(Application application) {
        TodoRoomDatabase database = TodoRoomDatabase.getDatabase(application);
        mTodoDao = database.mTodoDao();
        mAllTodo = mTodoDao.getTodo();
    }

    public LiveData<List<TodoEntity>> getAllTodo() {
        return mAllTodo;
    }

    public void insert(TodoEntity todoEntity) {
        TodoRoomDatabase.dbExecutorService.execute(() -> mTodoDao.insert(todoEntity));
    }

    @WorkerThread
    public LiveData<List<TodoEntity>> getSearchTodo(String search) {
        return mTodoDao.getSearchTodo(search);
    }

    public void update(String oldTitle, String newTitle, boolean checked) {
        TodoRoomDatabase.dbExecutorService.execute(() -> mTodoDao.update(oldTitle, newTitle, checked));
    }
}
