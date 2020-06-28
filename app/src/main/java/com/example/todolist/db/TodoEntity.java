package com.example.todolist.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class TodoEntity {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "checked")
    private boolean checked;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    public TodoEntity(@NonNull String title, @NonNull String description, boolean checked, @Nullable String imagePath) {
        this.title = title;
        this.description = description;
        this.checked = checked;
        this.imagePath = imagePath;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getImagePath() {
        return imagePath;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull

    @Override
    public String toString() {
        return "TodoEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", checked=" + checked +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
