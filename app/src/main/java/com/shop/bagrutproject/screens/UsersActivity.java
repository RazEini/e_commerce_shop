package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.UsersAdapter;
import com.shop.bagrutproject.models.User;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button backButton;
    private SearchView userSearchView;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // קישור רכיבים מה-XML
        userSearchView = findViewById(R.id.userSearchView);
        recyclerView = findViewById(R.id.UserRecyclerView);
        backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        userList = new ArrayList<>();
        usersAdapter = new UsersAdapter(userList, this);
        recyclerView.setAdapter(usersAdapter);

        // טעינת המשתמשים מהפיירבייס
        fetchUsers();

        // כפתור חזור
        backButton.setOnClickListener(v -> finish());

        // מאזין לחיפוש משתמשים
        userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.filter(newText);
                return false;
            }
        });
    }

    private void fetchUsers() {
        myRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                usersAdapter.filter(""); // טוען את כל המשתמשים ומציג אותם
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // טיפול בשגיאה
            }
        });
    }
}
