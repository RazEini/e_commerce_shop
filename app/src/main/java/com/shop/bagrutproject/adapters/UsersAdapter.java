package com.shop.bagrutproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.screens.UserDetailActivity;
import com.shop.bagrutproject.services.DatabaseService;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private List<User> usersList;
    private List<User> filteredList;
    private Context context;
    private static final String TAG = "UsersAdapter";

    public UsersAdapter(List<User> usersList, Context context) {
        this.usersList = usersList;
        this.filteredList = new ArrayList<>(usersList); // יצירת עותק לרשימה מסוננת
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = filteredList.get(position);
        holder.nameTextView.setText(user.getfName() + " " + user.getlName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("USER_UID", user.getUid());
            intent.putExtra("USER_FNAME", user.getfName());
            intent.putExtra("USER_LNAME", user.getlName());
            intent.putExtra("USER_EMAIL", user.getEmail());
            intent.putExtra("USER_PHONE", user.getPhone());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (user.getUid() != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteUserAndRefresh(user.getUid(), position);
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(context, "Cannot delete user: UID is null", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(usersList);
        } else {
            for (User user : usersList) {
                String fullName = user.getfName() + " " + user.getlName();
                if (fullName.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void deleteUserAndRefresh(String uid, int position) {
        if (uid == null || uid.isEmpty()) {
            Log.e(TAG, "Cannot delete user: uid is null or empty");
            Toast.makeText(context, "Cannot delete user: uid is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Trying to delete user with uid: " + uid);
        DatabaseService.getInstance().deleteUser(uid, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "User deleted successfully");
                usersList.remove(position);
                filter(""); // רענון הרשימה לאחר מחיקה
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to delete user: " + e.getMessage());
                Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvName);
        }
    }
}
