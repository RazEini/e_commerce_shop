package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

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
                SharedPreferencesUtil.setIsAdmin(this, true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_homepage) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_register) {
            startActivity(new Intent(this, Register.class));
            return true;
        } else if (id == R.id.action_login) {
            startActivity(new Intent(this, Login.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, Odot.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}