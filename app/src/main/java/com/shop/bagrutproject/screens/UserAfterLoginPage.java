package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shop.bagrutproject.R;

public class UserAfterLoginPage extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_after_login_page);
        mAuth = FirebaseAuth.getInstance();

        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    public void goToShop(View view) {
        Intent intent = new Intent(UserAfterLoginPage.this, ShopActivity.class);
        startActivity(intent);
    }

    public void goToPurchaseHistory(View view) {
         Intent intent = new Intent(UserAfterLoginPage.this, OrderHistoryActivity.class);
         startActivity(intent);
    }

    public void goToPersonalArea(View view) {
        Intent intent = new Intent(UserAfterLoginPage.this, UpdateUserDetailsActivity.class);
        startActivity(intent);
    }

    public void logout() {
        // ניקוי SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // התנתקות מ-Firebase
        mAuth.signOut();
        Log.d("Logout", "User successfully logged out");

        // מעבר לדף ההתחברות
        Intent go = new Intent(UserAfterLoginPage.this, Login.class);
        go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(go);
        Log.d("Logout", "Navigated to Login activity");

        finishAffinity();
        Toast.makeText(UserAfterLoginPage.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        setTitle("תפריט חנות");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_shop) {
            startActivity(new Intent(this, ShopActivity.class));
            return true;
        } else if (id == R.id.action_orders) {
            startActivity(new Intent(this, OrderHistoryActivity.class));
            return true;
        } else if (id == R.id.action_update) {
            startActivity(new Intent(this, UpdateUserDetailsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
