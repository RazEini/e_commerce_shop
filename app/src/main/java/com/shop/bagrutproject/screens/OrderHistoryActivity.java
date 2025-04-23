package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orders;
    private DatabaseService databaseService;
    private ImageButton btnBack;
    private User user;
    private static final String TAG = "OrderHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnBack = findViewById(R.id.btnBack5);
        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orders = new ArrayList<>();
        databaseService = DatabaseService.getInstance();
        user = SharedPreferencesUtil.getUser(this);

        if (user != null) {
            fetchOrders(user.getUid());
        }

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, UserAfterLoginPage.class);
            startActivity(intent);
            finish();
        });
    }

    // בתוך fetchOrders
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
                            Toast.makeText(OrderHistoryActivity.this, "לא נמצאו הזמנות", Toast.LENGTH_SHORT).show();
                        }

                        orders.clear();
                        orders.addAll(combinedOrders);
                        orderAdapter = new OrderAdapter(orders);
                        recyclerView.setAdapter(orderAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(OrderHistoryActivity.this, "שגיאה בטעינת ארכיון ההזמנות", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error loading archived orders", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "שגיאה בטעינת ההזמנות", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading orders", error.toException());
            }
        });
    }
}
