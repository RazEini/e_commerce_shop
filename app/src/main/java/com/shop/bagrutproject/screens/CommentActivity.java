package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CommentAdapter;
import com.shop.bagrutproject.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private EditText commentInput;
    private Button btnSubmitComment;
    private DatabaseReference commentsRef;
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

        commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(itemId);

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
        commentsRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                commentList.clear();
                Log.d("CommentActivity", "Loading comments...");

                double sum = 0;
                int count = 0;
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        comment.setCommentId(snapshot.getKey());

                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if (comment.getUserId().equals(currentUserId)) {
                            comment.setUserName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        } else {
                            comment.setUserName("anonymous");
                        }

                        commentList.add(comment);
                        sum += comment.getRating();
                        count++;
                    }
                }

                double averageRating = count > 0 ? sum / count : 0;
                Log.d("CommentActivity", "Total comments loaded: " + commentList.size());

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Log.e("CommentActivity", "Error loading comments", databaseError.toException());
            }
        });
    }

    private void submitComment(String commentText, float rating) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String commentId = commentsRef.push().getKey();

        if (commentId != null) {
            Comment comment = new Comment(commentId, currentUserId, commentText, rating, currentUserName);

            commentsRef.child(commentId).setValue(comment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CommentActivity.this, "התגובה נשלחה!", Toast.LENGTH_SHORT).show();
                        commentInput.setText(""); // ריקון שדה הטקסט
                        ratingBar.setRating(0); // איפוס הדירוג

                        // טען מחדש את התגובות כדי שהמסך יתעדכן
                        loadComments();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CommentActivity.this, "שגיאה בשליחת התגובה", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}