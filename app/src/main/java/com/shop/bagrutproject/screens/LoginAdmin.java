package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.shop.bagrutproject.R;

public class LoginAdmin extends AppCompatActivity {

    EditText edtAdminUsername, edtAdminPassword;
    Button btnAdminLogin;

    private static final String ADMIN_USERNAME = "admin";  // Admin username
    private static final String ADMIN_PASSWORD = "2609";  // Admin password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_admin);

        edtAdminUsername = findViewById(R.id.edtAdminUsername);
        edtAdminPassword = findViewById(R.id.edtAdminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        // Admin login button click listener
        btnAdminLogin.setOnClickListener(view -> {
            String username = edtAdminUsername.getText().toString();
            String password = edtAdminPassword.getText().toString();

            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                // Admin logged in successfully, show a Toast or go to the Admin Dashboard
                Intent intent = new Intent(LoginAdmin.this, AdminPage.class);
                startActivity(intent);
            } else {
                // Invalid login, show error message
                Toast.makeText(LoginAdmin.this, "Invalid Admin Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnBack4(View view) {
        Intent intent = new Intent(LoginAdmin.this, MainActivity.class);
        startActivity(intent);
    }
}