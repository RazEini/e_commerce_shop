package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.AdminOrderAdapter;
import com.shop.bagrutproject.models.Order;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminOrderAdapter adminOrderAdapter; // השתמש ב-AdminOrderAdapter
    private List<Order> orders;
    private static final String TAG = "AdminOrderHistory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orders = new ArrayList<>();
        adminOrderAdapter = new AdminOrderAdapter(orders); // השתמש ב-AdminOrderAdapter
        recyclerView.setAdapter(adminOrderAdapter);

        Button backButton = findViewById(R.id.backToAdminPageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // חוזר לעמוד הקודם
            }
        });

        fetchAllOrders();
    }

    private void fetchAllOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        orders.add(order);
                    }
                }
                adminOrderAdapter.notifyDataSetChanged(); // עדכון לאדפטר החדש
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error fetching orders", error.toException());
                Toast.makeText(AdminOrderHistoryActivity.this, "שגיאה בטעינת ההזמנות", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
