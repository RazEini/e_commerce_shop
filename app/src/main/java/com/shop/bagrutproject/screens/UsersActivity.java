package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.UsersAdapter;
import com.shop.bagrutproject.models.User;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<User> usersList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button backButton; // הוספת משתנה לכפתור

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.UserRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // חוזר לעמוד הקודם
            }
        });

        fetchUsers();
    }

    private void fetchUsers() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        user.setUid(snapshot.getKey());
                        usersList.add(user);
                    }
                }
                usersAdapter = new UsersAdapter(usersList, UsersActivity.this);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("UsersActivity", "Failed to read value.", error.toException());
                Toast.makeText(UsersActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
