package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    DatabaseService databaseService;

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

        databaseService.getComments(itemId, new DatabaseService.DatabaseCallback<List<Comment>>() {
            @Override
            public void onCompleted(List<Comment> comments) {
                double sum = 0;
                int count = 0;
                for (Comment comment : comments) {
                    sum += comment.getRating();
                    count++;
                }
                double averageRating = count > 0 ? sum / count : 0;

                // הצגת ממוצע הדירוגים בכוכבים
                itemAverageRatingBar.setRating((float) averageRating);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        // חזרה לעמוד החנות
        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(ItemDetailActivity.this, ShopActivity.class);
            startActivity(intent);
            finish();
        });

        // מעבר לעמוד התגובות
        btnViewComments.setOnClickListener(v -> {
            Intent intent = new Intent(ItemDetailActivity.this, CommentActivity.class);
            intent.putExtra("itemId", itemId); // שליחת ה-ID של המוצר
            startActivity(intent);
        });

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
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
