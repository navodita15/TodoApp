package com.example.todolist.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.todolist.R;
import com.example.todolist.db.TodoEntity;
import com.google.android.material.textview.MaterialTextView;

import java.util.Collections;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {
    private List<TodoEntity> mTodo;
    private ItemClickListener mOnItemClickListener;
    private final Drawable mPlaceholder;
    private final Context mContext;

    public ToDoAdapter(Context context) {
        mPlaceholder = ContextCompat.getDrawable(context, R.drawable.todo1);
        mContext = context;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_todo, parent, false);
        return new ToDoViewHolder(view);
    }

    public void setTodo(List<TodoEntity> todo) {
        mTodo = todo;
        notifyDataSetChanged();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        if (mTodo != null) {
            TodoEntity todo = mTodo.get(position);
            Glide.with(mContext)
                    .load(todo.getImagePath() != null ? Uri.parse(todo.getImagePath()) : todo.getImagePath())
                    .placeholder(mPlaceholder)
                    .into(holder.mTodoImageView);
            holder.mItemHeadingTextView.setText(todo.getTitle());
            holder.mItemDescriptionTextView.setText(todo.getDescription());
            CheckBox checkBox = holder.mItemCheckBox;
            checkBox.setChecked(todo.isChecked());
            checkBox.setEnabled(!todo.isChecked());
            holder.mItemCheckBox.setOnClickListener(view -> {
                checkBox.setEnabled(false);

                if (mOnItemClickListener != null) {

                    Handler handler = new Handler();
                    int getPos = holder.getAdapterPosition();
                    if (getPos > -1) {
                        handler.post(() -> swapItem(getPos, getItemCount() - 1));
                    }
                    mOnItemClickListener.click(todo.getTitle(), "Done " + todo.getTitle(), true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTodo != null ? mTodo.size() : 0;
    }


    static class ToDoViewHolder extends RecyclerView.ViewHolder {

        private final MaterialTextView mItemHeadingTextView;
        private final CheckBox mItemCheckBox;
        private final MaterialTextView mItemDescriptionTextView;
        private final ImageView mTodoImageView;

        ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemHeadingTextView = itemView.findViewById(R.id.item_todo_heading_text_view);
            mItemCheckBox = itemView.findViewById(R.id.item_todo_check_box);
            mItemDescriptionTextView = itemView.findViewById(R.id.item_todo_description_textview);
            mTodoImageView = itemView.findViewById(R.id.item_todo_image);
        }

    }

    @SuppressWarnings("SameParameterValue")
    private void swapItem(int fromPosition, int toPosition) {
        Log.d("MainActivity", "swapItem: fromPosition == " + fromPosition);
        Log.d("MainActivity", "swapItem: toPosition == " + toPosition);
        Collections.swap(mTodo, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface ItemClickListener {
        void click(String oldTitle, String newTitle, boolean checked);
    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
