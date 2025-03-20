package com.shop.bagrutproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private DatabaseReference commentsRef;
    private Context context;
    private String itemId;

    public CommentAdapter(Context context, List<Comment> commentList, String itemId) {
        this.context = context;
        this.commentList = commentList;
        this.itemId = itemId;
        this.commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(itemId);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.commentText.setText(comment.getCommentText());
        holder.ratingBar.setRating(comment.getRating());
        holder.ratingBar.setIsIndicator(true);
        holder.userName.setText(comment.getUserName());

        if (comment.getCommentId() == null) {
            Log.e("CommentAdapter", "commentId is null at position " + position);
        } else {
            Log.d("CommentAdapter", "commentId: " + comment.getCommentId());
        }

        holder.itemView.setOnLongClickListener(v -> {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (comment.getUserId().equals(currentUserId)) {
                showDeleteConfirmationDialog(comment, position);
            } else {
                Toast.makeText(context, "לא ניתן למחוק תגובה של משתמש אחר", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }



    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        RatingBar ratingBar;
        TextView userName;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            userName = itemView.findViewById(R.id.userName);
        }
    }

    private void showDeleteConfirmationDialog(Comment comment, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("מחיקת תגובה");
        builder.setMessage("האם אתה בטוח שברצונך למחוק את התגובה?");
        builder.setPositiveButton("כן", (dialog, which) -> deleteComment(comment, position));
        builder.setNegativeButton("לא", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteComment(Comment comment, int position) {
        if (position < 0 || position >= commentList.size()) {
            Log.e("CommentAdapter", "Invalid index: " + position);
            return;
        }

        commentsRef.child(comment.getCommentId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("CommentAdapter", "Comment deleted successfully from Firebase.");

                    // מחיקה מהרשימה המקומית ועדכון ה-RecyclerView
                    commentList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, commentList.size());
                })
                .addOnFailureListener(e -> Log.e("CommentAdapter", "Failed to delete comment", e));
    }


}