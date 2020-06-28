package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.db.TodoEntity;
import com.example.todolist.model.TodoRepository;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private final TodoRepository mTodoRepository;
    private final LiveData<List<TodoEntity>> mAllTodoList;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        mTodoRepository = new TodoRepository(application);
        mAllTodoList = mTodoRepository.getAllTodo();

    }

    public LiveData<List<TodoEntity>> getAllTodoList() {
        return mAllTodoList;
    }

    public void insert(TodoEntity todoEntity) {
        mTodoRepository.insert(todoEntity);
    }

    public LiveData<List<TodoEntity>> getSearchTodoList(String search) {
        return mTodoRepository.getSearchTodo(search);
    }

    public void update(String oldTitle, String newTitle, boolean checked) {
        mTodoRepository.update(oldTitle, newTitle, checked);
    }
}
