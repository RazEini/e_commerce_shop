package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
                logout(); // נקרא לפונקציה logout
            }
        });
    }
    public void goToShop(View view) {
        Intent intent = new Intent(UserAfterLoginPage.this, RecyclerViewActivity.class);
        startActivity(intent);
    }

    public void goToPurchaseHistory(View view) {
        // כאן תוכל לשים את הקוד שיבצע את המעבר לעמוד עגלת הקניות
        //Intent intent = new Intent(UserAfterLoginPage.this, PurchaseHistory.class);
        //startActivity(intent);
    }

    public void goToPersonalArea(View view) {
        Intent intent = new Intent(UserAfterLoginPage.this, UpdateUserDetailsActivity.class);
        startActivity(intent);
    }

    public void logout() {
        // מנקה את ה-SharedPreferences כאשר המשתמש מתנתק
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // מנקה את כל הנתונים
        editor.apply();

        // מבצע יציאה מהחשבון ב-Firebase
        mAuth.signOut();

        // מעביר את המשתמש למסך ההתחברות
        Intent go = new Intent(getApplicationContext(), Login.class);
        startActivity(go);
        finish(); // לסיים את ה-Activity הנוכחי כדי שלא יחזור למסך הקודם
    }

}