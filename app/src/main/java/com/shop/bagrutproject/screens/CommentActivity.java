package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CommentAdapter;
import com.shop.bagrutproject.models.Comment;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private EditText commentInput;
    private Button btnSubmitComment;
    private String itemId;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        itemId = getIntent().getStringExtra("itemId");

        recyclerView = findViewById(R.id.recyclerViewComments);
        commentInput = findViewById(R.id.commentInput);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);
        ratingBar = findViewById(R.id.ratingBar); // ה-RatingBar

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList, itemId);
        recyclerView.setAdapter(commentAdapter);


        loadComments();

        btnSubmitComment.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (!commentText.isEmpty()) {
                submitComment(commentText, rating);
            } else {
                Toast.makeText(CommentActivity.this, "אנא הזן תגובה ודירוג", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnGoToItemDetails = findViewById(R.id.btnGoToItemDetail);

        btnGoToItemDetails.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadComments() {
        DatabaseService.getInstance().getComments(itemId, new DatabaseService.DatabaseCallback<List<Comment>>() {
            @Override
            public void onCompleted(List<Comment> comments) {
                commentList.clear();
                commentList.addAll(comments);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void submitComment(String commentText, float rating) {
        User user = SharedPreferencesUtil.getUser(this);
        String currentUserId = user.getUid();
        String currentUserName = user.getfName() + " " + user.getlName();
        String commentId = DatabaseService.getInstance().generateNewCommentId(itemId);

        Comment comment = new Comment(commentId, currentUserId, commentText, rating, currentUserName);

        DatabaseService.getInstance().writeNewComment(itemId, comment, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(CommentActivity.this, "התגובה נשלחה!", Toast.LENGTH_SHORT).show();
                commentInput.setText(""); // ריקון שדה הטקסט
                ratingBar.setRating(0); // איפוס הדירוג

                // טען מחדש את התגובות כדי שהמסך יתעדכן
                loadComments();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CommentActivity.this, "שגיאה בשליחת התגובה", Toast.LENGTH_SHORT).show();

            }
        });


    }
}