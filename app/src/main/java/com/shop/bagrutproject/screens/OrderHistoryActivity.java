package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orders;
    private DatabaseService databaseService;
    private User user;
    private static final String TAG = "OrderHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orders = new ArrayList<>();
        databaseService = DatabaseService.getInstance();
        user = SharedPreferencesUtil.getUser(this);

        if (user != null) {
            fetchOrders(user.getUid());
        }

        Button btnDeleteOldOrders = findViewById(R.id.btnDeleteOldOrders);
        btnDeleteOldOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOldOrders();
            }
        });

    }

    private void fetchOrders(String userId) {
        databaseService.getOrders(userId, new DatabaseService.DatabaseCallback<List<Order>>() {
            @Override
            public void onCompleted(List<Order> ordersList) {
                orders.clear();
                orders.addAll(ordersList);
                orderAdapter = new OrderAdapter(orders);
                recyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("OrderHistoryActivity", "Error fetching orders", e);
            }
        });
    }

    private void deleteOldOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000; // 30 ימים ב-milliseconds
        long currentTime = System.currentTimeMillis();

        ordersRef.orderByChild("timestamp").endAt(currentTime - thirtyDaysInMillis)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue(); // מחיקת ההזמנה הישנה
                        }
                        Toast.makeText(OrderHistoryActivity.this, "הזמנות ישנות נמחקו", Toast.LENGTH_SHORT).show();
                        // רענון ה-RecyclerView אחרי מחיקת ההזמנות
                        fetchOrders(user.getUid());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(OrderHistoryActivity.this, "שגיאה: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
