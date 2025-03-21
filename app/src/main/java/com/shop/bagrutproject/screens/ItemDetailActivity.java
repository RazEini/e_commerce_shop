package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Comment;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.ImageUtil;

import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView itemName, itemPrice, itemInfo, itemCompany, itemColor, itemType;
    private ImageView itemImage;
    private String itemId;
    private Button btnGoBack, btnViewComments;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        databaseService = DatabaseService.getInstance();

        // קבלת ה-ID של המוצר שנבחר
        itemId = getIntent().getStringExtra("itemId");

        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        itemInfo = findViewById(R.id.itemInfo);
        itemCompany = findViewById(R.id.itemCompany);
        itemColor = findViewById(R.id.itemColor);
        itemType = findViewById(R.id.itemType);
        itemImage = findViewById(R.id.itemImage);
        RatingBar itemAverageRatingBar = findViewById(R.id.itemAverageRatingBar);

        btnGoBack = findViewById(R.id.btnGoToShop);
        btnViewComments = findViewById(R.id.btnViewComments);

        databaseService.getItem(itemId, new DatabaseService.DatabaseCallback<Item>() {
            @Override
            public void onCompleted(Item item) {
                if (item == null) return;
                itemName.setText(item.getName());
                itemPrice.setText("₪" + item.getPrice());
                itemInfo.setText(item.getAboutItem());
                itemCompany.setText(item.getCompany());
                itemColor.setText(item.getColor());
                itemType.setText(item.getType());

                if (item.getPic() != null && !item.getPic().isEmpty()) {
                    itemImage.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
                } else {
                    itemImage.setImageResource(R.drawable.ic_launcher_foreground);
                }

                updateAverageRating(itemAverageRatingBar);
            }

            @Override
            public void onFailed(Exception e) {
            }
        });

        btnGoBack.setOnClickListener(v -> {
            finish();
        });

        btnViewComments.setOnClickListener(v -> {
            Intent intent = new Intent(ItemDetailActivity.this, CommentActivity.class);
            intent.putExtra("itemId", itemId); // שליחת ה-ID של המוצר
            startActivity(intent);
        });
    }

    private void updateAverageRating(RatingBar itemAverageRatingBar) {
        databaseService.updateAverageRating(itemId, new DatabaseService.DatabaseCallback<Double>() {
            @Override
            public void onCompleted(Double averageRating) {
                itemAverageRatingBar.setRating(averageRating.floatValue());
                // הסר את העדכון של הצבע כדי שלא יתווסף גוון נוסף
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות
            }
        });
    }



}
