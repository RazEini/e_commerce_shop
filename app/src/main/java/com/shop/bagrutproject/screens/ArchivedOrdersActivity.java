package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
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

public class ArchivedOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminOrderAdapter adminOrderAdapter;
    private List<Order> archivedOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_orders);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewArchivedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        archivedOrders = new ArrayList<>();
        adminOrderAdapter = new AdminOrderAdapter(archivedOrders, true); // נשלח דגל למצב ארכיון
        recyclerView.setAdapter(adminOrderAdapter);

        Button backButton = findViewById(R.id.backToAdminPageButton);
        backButton.setOnClickListener(v -> finish());

        loadArchivedOrders();
    }

    private void loadArchivedOrders() {
        DatabaseReference archivedRef = FirebaseDatabase.getInstance().getReference("archivedOrders");

        archivedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                archivedOrders.clear();
                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    Order order = orderSnap.getValue(Order.class);
                    if (order != null) {
                        archivedOrders.add(order);
                    }
                }
                adminOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ArchivedOrdersActivity.this, "שגיאה בטעינת הארכיון", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
