package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private TextView averageRatingText;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        itemId = getIntent().getStringExtra("itemId");

        recyclerView = findViewById(R.id.recyclerViewComments);
        commentInput = findViewById(R.id.commentInput);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);
        averageRatingText = findViewById(R.id.averageRatingText); // טקסט הממוצע
        ratingBar = findViewById(R.id.ratingBar); // ה-RatingBar

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(commentAdapter);

        commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(itemId);

        // הצגת התגובות הקיימות
        loadComments();

        // כפתור לשליחת תגובה חדשה
        btnSubmitComment.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString().trim();
            float rating = ratingBar.getRating(); // קבלת הדירוג

            if (!commentText.isEmpty() && rating > 0) {
                submitComment(commentText, rating);
            } else {
                Toast.makeText(CommentActivity.this, "אנא הזן תגובה ודירוג", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnGoToItemDetails = findViewById(R.id.btnGoToItemDetail);

        btnGoToItemDetails.setOnClickListener(v -> {
            Intent intent = new Intent(CommentActivity.this, ItemDetailActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void loadComments() {
        commentsRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                commentList.clear();
                float totalRating = 0;
                int ratingCount = 0;

                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                        totalRating += comment.getRating();
                        ratingCount++;
                    }
                }

                // חישוב ממוצע הדירוגים
                if (ratingCount > 0) {
                    float averageRating = totalRating / ratingCount;
                    averageRatingText.setText("ממוצע דירוג: " + String.format("%.1f", averageRating)); // הצגת הממוצע
                } else {
                    averageRatingText.setText("אין דירוגים עדיין");
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                // טיפול בשגיאות
            }
        });
    }

    private void submitComment(String commentText, float rating) {
        String userId = "anonymous"; // כאן תוכל לשים את שם המשתמש הנוכחי

        Comment comment = new Comment(userId, commentText, rating);
        String commentId = commentsRef.push().getKey();
        if (commentId != null) {
            commentsRef.child(commentId).setValue(comment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CommentActivity.this, "התגובה נשלחה!", Toast.LENGTH_SHORT).show();
                        commentInput.setText(""); // ריקון השדה
                        ratingBar.setIsIndicator(true); // מונע שינוי הדירוג אחרי שליחה
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CommentActivity.this, "שגיאה בשליחת התגובה", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
