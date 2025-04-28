package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.shop.bagrutproject.adapters.OrderAdapter;
import com.shop.bagrutproject.models.Order;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class UserDetailActivity extends AppCompatActivity {

    TextView tvFName, tvLName, tvEmail, tvPhone;
    ImageButton btnBack;
    String uid, fName, lName, email, phone;
    private List<Order> orders;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseService databaseService;
    private static final String TAG = "UserDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button showRecyclerViewBtn = findViewById(R.id.showRecyclerViewBtn);
        Button closeRecyclerViewBtn = findViewById(R.id.closeRecyclerViewBtn);
        RecyclerView recyclerViewOrders = findViewById(R.id.recyclerViewOrders2);

        OrderAdapter orderAdapter = new OrderAdapter(orders);
        recyclerViewOrders.setAdapter(orderAdapter);

        showRecyclerViewBtn.setOnClickListener(v -> {
            recyclerViewOrders.setVisibility(View.VISIBLE);

            closeRecyclerViewBtn.setVisibility(View.VISIBLE);
        });

        closeRecyclerViewBtn.setOnClickListener(v -> {
            recyclerViewOrders.setVisibility(View.GONE);

            closeRecyclerViewBtn.setVisibility(View.GONE);
        });


        tvFName = findViewById(R.id.tvFName);
        tvLName = findViewById(R.id.tvLName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        orders = new ArrayList<>();
        databaseService = DatabaseService.getInstance();
        recyclerView = findViewById(R.id.recyclerViewOrders2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("USER_UID");
            fName = extras.getString("USER_FNAME");
            lName = extras.getString("USER_LNAME");
            email = extras.getString("USER_EMAIL");
            phone = extras.getString("USER_PHONE");
            fetchOrders(extras.getString("USER_UID"));

            // Set the data to the TextViews
            tvFName.setText("First Name: " + fName);
            tvLName.setText("Last Name: " + lName);
            tvEmail.setText("Email: " + email);
            tvPhone.setText("Phone: " + phone);
        }

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void fetchOrders(String userId) {
        List<Order> combinedOrders = new ArrayList<>();

        // שליפה ראשונה - מה-orders הרגיל
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    Order order = orderSnap.getValue(Order.class);
                    if (order != null && order.getUserId().equals(userId)) {
                        combinedOrders.add(order);
                    }
                }

                // שליפה שנייה - מה-archivedOrders
                DatabaseReference archivedRef = FirebaseDatabase.getInstance().getReference("archivedOrders");
                archivedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot archivedSnap : snapshot.getChildren()) {
                            Order order = archivedSnap.getValue(Order.class);
                            if (order != null && order.getUserId().equals(userId)) {
                                combinedOrders.add(order);
                            }
                        }

                        // עדכון הרשימה הסופית
                        if (combinedOrders.isEmpty()) {
                            Toast.makeText(UserDetailActivity.this, "לא נמצאו הזמנות", Toast.LENGTH_SHORT).show();
                        }

                        orders.clear();
                        orders.addAll(combinedOrders);
                        orderAdapter = new OrderAdapter(orders);
                        recyclerView.setAdapter(orderAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(UserDetailActivity.this, "שגיאה בטעינת ארכיון ההזמנות", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error loading archived orders", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UserDetailActivity.this, "שגיאה בטעינת ההזמנות", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading orders", error.toException());
            }
        });
    }
}
