package com.example.todolist.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.todolist.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddToDoActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_TITLE = "com.example.android.todolistsql.title.REPLY";
    public static final String EXTRA_REPLY_DESCRIPTION = "com.example.android.todolistsql.description.REPLY";
    public static final String EXTRA_REPLY_IMAGE = "com.example.android.todolistsql.image.REPLY";
    public static final int REQUEST_IMAGE_CODE = 1;

    private TextInputEditText mTitleEditText;
    private TextInputEditText mDescriptionEditText;
    private ImageView mTodoImageView;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_add_to_do);
        Toolbar toolbar = findViewById(R.id.add_todo_toolbar);
        mTodoImageView = findViewById(R.id.todo_image);
        mTitleEditText = findViewById(R.id.add_todo_title_text_view);
        mDescriptionEditText = findViewById(R.id.add_todo_description_text_view);
        setSupportActionBar(toolbar);
        final MaterialButton mAddButton = findViewById(R.id.done_button);
        final MaterialButton mCancelButton = findViewById(R.id.cancel_button);

        mTodoImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_CODE);
        });

        mCancelButton.setOnClickListener(view -> finish());

        mAddButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mTitleEditText.getText())
                    || TextUtils.isEmpty(mDescriptionEditText.getText())) {
                mTitleEditText.setError("Required");
                mDescriptionEditText.setError("Required");
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String mTitle = mTitleEditText.getText().toString();
                String mDescription = Objects.requireNonNull(mDescriptionEditText.getText()).toString();
                replyIntent.putExtra(EXTRA_REPLY_IMAGE, mImagePath);
                replyIntent.putExtra(EXTRA_REPLY_TITLE, mTitle);
                replyIntent.putExtra(EXTRA_REPLY_DESCRIPTION, mDescription);
                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                mImagePath = selectedImage.toString();
                Glide.with(this).load(selectedImage).into(mTodoImageView);
            }

        }
    }
}
