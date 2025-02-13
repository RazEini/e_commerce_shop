package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;

public class UserDetailActivity extends AppCompatActivity {

    TextView tvFName, tvLName, tvEmail, tvPhone;
    ImageButton btnBack;
    String uid, fName, lName, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        tvFName = findViewById(R.id.tvFName);
        tvLName = findViewById(R.id.tvLName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);

        btnBack = findViewById(R.id.btnBack);

        // Get the data passed from the adapter
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("USER_UID");
            fName = extras.getString("USER_FNAME");
            lName = extras.getString("USER_LNAME");
            email = extras.getString("USER_EMAIL");
            phone = extras.getString("USER_PHONE");

            // Set the data to the TextViews
            tvFName.setText("First Name: " + fName);
            tvLName.setText("Last Name: " + lName);
            tvEmail.setText("Email: " + email);
            tvPhone.setText("Phone: " + phone);
        }

        // Set the back button functionality
        btnBack.setOnClickListener(v -> {
            // Go back to the previous activity (the users list)
            onBackPressed();
        });
    }
}
