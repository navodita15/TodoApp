package com.example.todolist.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.db.TodoEntity;
import com.example.todolist.model.ToDoAdapter;
import com.example.todolist.viewmodel.TodoViewModel;

import java.util.List;
import java.util.Objects;

import static androidx.appcompat.widget.SearchView.OnQueryTextListener;

public class MainActivity extends AppCompatActivity implements ToDoAdapter.ItemClickListener {
    private static final String TAG = "MainActivity";
    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;

    private RecyclerView mTodoRecyclerView;
    private TodoViewModel mTodoViewModel;
    private ToDoAdapter toDoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.todo_toolbar);
        SearchView searchView = findViewById(R.id.todo_search_edit_text_view);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        ImageView searchViewIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);
        mTodoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        setTodoRecyclerView();
        searchView.setOnQueryTextListener(mOnQueryTextListener);

    }

    OnQueryTextListener mOnQueryTextListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            if (s != null) {
                getItemsFromDb(s);
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (s != null) {
                getItemsFromDb(s);
            }
            return true;
        }
    };

    private void getItemsFromDb(String s) {
        String query = "%" + s + "%";
        mTodoViewModel.getSearchTodoList(query).observe(this, todoEntities -> {
            toDoAdapter.setTodo(todoEntities);
            mTodoRecyclerView.setAdapter(toDoAdapter);
        });
    }


    private void setTodoRecyclerView() {
        mTodoRecyclerView = findViewById(R.id.todo_recycler_view);
        toDoAdapter = new ToDoAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
//        layoutManager.setStackFromEnd(true);
        mTodoRecyclerView.setAdapter(toDoAdapter);
        mTodoRecyclerView.setLayoutManager(layoutManager);
        SeparatorDecoration decoration = new SeparatorDecoration(this, Color.GRAY, 1.5f);
        mTodoRecyclerView.addItemDecoration(decoration);
        mTodoViewModel.getAllTodoList().observe(this,
                (List<TodoEntity> todoEntities) -> {
                    for (TodoEntity todoEntity : todoEntities) {
                        Log.d(TAG, "setTodoRecyclerView: todos == " + todoEntity);
                    }
                    toDoAdapter.setTodo(todoEntities);
                });
        toDoAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);


        final MenuItem addTodo = menu.findItem(R.id.add_todo_button);

        addTodo.setActionView(R.layout.layout_add_todo);
        RelativeLayout addLayout = (RelativeLayout) addTodo.getActionView();
        Button addButton = addLayout.findViewById(R.id.toolbar_button);
        addButton.setOnClickListener(mOnClickListener);

        return super.onCreateOptionsMenu(menu);
    }

    View.OnClickListener mOnClickListener = view -> {
        Intent intent = new Intent(MainActivity.this, AddToDoActivity.class);
        startActivityForResult(intent, NEW_TODO_ACTIVITY_REQUEST_CODE);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                TodoEntity todoEntity = new TodoEntity(Objects.requireNonNull(data.getStringExtra(AddToDoActivity.EXTRA_REPLY_TITLE)),
                        Objects.requireNonNull(data.getStringExtra(AddToDoActivity.EXTRA_REPLY_DESCRIPTION)),
                        false,
                        data.getStringExtra(AddToDoActivity.EXTRA_REPLY_IMAGE));
                mTodoViewModel.insert(todoEntity);
            }

        }
    }

    @Override
    public void click(String oldTitle, String newTitle, boolean checked) {
        mTodoViewModel.update(oldTitle, newTitle, checked);
    }


}
