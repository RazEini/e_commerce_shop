package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

public class ThankYouActivity extends AppCompatActivity {

    private Button btnBackToShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnBackToShop = findViewById(R.id.btnBackToShop);
        User user = SharedPreferencesUtil.getUser(this);

        if (user != null) {
            clearUserCart(user.getUid()); // ניקוי העגלה
        }

        btnBackToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThankYouActivity.this, ShopActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clearUserCart(String userId) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)
                .child("cart")
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // הצלחה - העגלה אופסה
                    } else {
                        // שגיאה - ניתן להציג טוסט או לוג
                    }
                });
    }


}
