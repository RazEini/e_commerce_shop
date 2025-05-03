package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.shop.bagrutproject.R;

public class ThankYouActivity extends AppCompatActivity {

    private Button btnBackToShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        btnBackToShop = findViewById(R.id.btnBackToShop);

        // לחיצה על כפתור חזרה לחנות
        btnBackToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // חזרה למסך החנות
                Intent intent = new Intent(ThankYouActivity.this, ShopActivity.class);
                startActivity(intent);
                finish(); // סוגר את העמוד הזה כך שלא יישאר בהיסטוריית הניווט
            }
        });
    }
}
