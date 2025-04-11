package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.DealsAdapter;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class DealsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DealsAdapter adapter;
    List<Deal> dealsList;  // עכשיו אנחנו משתמשים ב-Deal ולא ב-String
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerDeals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack8);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DealsActivity.this, UserAfterLoginPage.class);
            startActivity(intent);
            finish();
        });

        dealsList = new ArrayList<>();

        DatabaseService databaseService = DatabaseService.getInstance();
        databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> result) {
                if (result != null && !result.isEmpty()) {
                    dealsList.addAll(result);  // הוספת המבצעים לרשימה
                    adapter = new DealsAdapter(dealsList);  // יצירת האדפטר לאחר שהנתונים התקבלו
                    recyclerView.setAdapter(adapter);  // הוספת האדפטר ל-RecyclerView
                    adapter.notifyDataSetChanged();  // עדכון ה-RecyclerView
                } else {
                    Toast.makeText(DealsActivity.this, "לא קיימים מבצעים", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(DealsActivity.this, "שגיאה בשליפת המבצעים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
