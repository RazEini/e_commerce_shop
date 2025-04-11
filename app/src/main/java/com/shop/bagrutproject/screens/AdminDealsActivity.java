package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.DealsAdapter;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AdminDealsActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editDiscount, editValidUntil;
    private Button btnAddDeal, btnViewDeals;
    private RecyclerView recyclerViewDeals;
    private DealsAdapter dealsAdapter;
    private DatabaseService dealsDatabase;
    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deals);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // אתחול השדות
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDiscount = findViewById(R.id.editDiscount);
        editValidUntil = findViewById(R.id.editValidUntil);
        btnAddDeal = findViewById(R.id.btnAddDeal);
        btnViewDeals = findViewById(R.id.btnViewDeals);
        recyclerViewDeals = findViewById(R.id.recyclerViewDeals);

        dealsDatabase = DatabaseService.getInstance();

        // אתחול RecyclerView
        recyclerViewDeals.setLayoutManager(new LinearLayoutManager(this));
        dealsAdapter = new DealsAdapter(new ArrayList<>());
        recyclerViewDeals.setAdapter(dealsAdapter);
        btnBack = findViewById(R.id.btnBack9);
        Button btnHideDeals = findViewById(R.id.btnHideDeals);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDealsActivity.this, AdminPage.class);
            startActivity(intent);
            finish();
        });

        btnHideDeals.setOnClickListener(v -> {
            recyclerViewDeals.setVisibility(View.GONE); // הסתרת הרשימה
            btnHideDeals.setVisibility(View.GONE); // הסתרת הכפתור עצמו
        });

        // הוספת מבצע
        btnAddDeal.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String discountText = editDiscount.getText().toString().trim();
            String validUntil = editValidUntil.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty() && !discountText.isEmpty() && !validUntil.isEmpty()) {
                try {
                    discountText = discountText.replace("%", "");
                    double discountPercentage = Double.parseDouble(discountText);

                    Deal deal = new Deal();
                    deal.setTitle(title);
                    deal.setDescription(description);
                    deal.setDiscountPercentage(discountPercentage);
                    deal.setValidUntil(validUntil);

                    dealsDatabase.addDeal(deal, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void result) {
                            Toast.makeText(AdminDealsActivity.this, "המבצע נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(AdminDealsActivity.this, "שגיאה בהוספת המבצע", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NumberFormatException e) {
                    Toast.makeText(AdminDealsActivity.this, "אנא הזן אחוז הנחה תקין", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdminDealsActivity.this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewDeals.setOnClickListener(v -> {
            dealsDatabase.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
                @Override
                public void onCompleted(List<Deal> result) {
                    if (result != null && !result.isEmpty()) {
                        dealsAdapter = new DealsAdapter(result);
                        recyclerViewDeals.setAdapter(dealsAdapter);
                        recyclerViewDeals.setVisibility(View.VISIBLE); // מציג את הרשימה
                        btnHideDeals.setVisibility(View.VISIBLE); // מציג את כפתור ההסתרה
                    } else {
                        Toast.makeText(AdminDealsActivity.this, "לא קיימים מבצעים כרגע", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AdminDealsActivity.this, "שגיאה בשליפת המבצעים", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void clearFields() {
        editTitle.setText("");
        editDescription.setText("");
        editDiscount.setText("");
        editValidUntil.setText("");
    }
}

