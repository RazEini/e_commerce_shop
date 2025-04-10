package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.DealsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DealsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DealsAdapter adapter;
    List<String> dealsList;
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
        dealsList.add("🔥 מבצע לוהט! קבל 20% הנחה על מוצרי חשמל היום בלבד!");
        dealsList.add("⚡ הנחה מטורפת! המבצע נגמר בקרוב - אל תפספס!");
        dealsList.add("💡 חדש בחנות! מוצרים חדשים במחירים מטורפים!");
        dealsList.add("🎉 קנה מוצר וקבל השני ב-50% הנחה!");

        adapter = new DealsAdapter(dealsList);
        recyclerView.setAdapter(adapter);
    }
}
