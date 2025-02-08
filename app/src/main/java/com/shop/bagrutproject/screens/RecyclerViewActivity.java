package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.ItemsAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> cartItems = new ArrayList<>();
    private DatabaseReference databaseReference;
    private Cart cart;
    private Button cartButton;
    private TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recyclerViewItems);
        cartButton = findViewById(R.id.cartButton);
        totalPriceText = findViewById(R.id.cartItemsText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // מאתחל את העגלה
        cart = new Cart(UUID.randomUUID().toString(), new ArrayList<Item>());

        // מאתחל את itemsAdapter ומעביר את שני הפרמטרים
        itemsAdapter = new ItemsAdapter(cartItems, this);
        recyclerView.setAdapter(itemsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        // נוודא שלחיצה על כפתור "עגלת קניות" תציג את העגלה
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecyclerViewActivity.this, CartActivity.class);
            intent.putExtra("cart", cart); // שליחה של העגלה כ-Extra
            startActivity(intent);
        });

        // טוען את המוצרים מ-Firebase
        fetchItemsFromFirebase();
    }

    private void fetchItemsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    cartItems.add(item);
                }
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // טיפול בשגיאות
            }
        });
    }

    // הוספת מוצר לעגלה
    public void addItemToCart(Item item) {
        cart.getItems().add(item);
        updateTotalPrice();  // עדכון המחיר הכולל
        Toast.makeText(RecyclerViewActivity.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
    }

    // עדכון המחיר הכולל בעגלה
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Item item : cart.getItems()) {
            totalPrice += item.getPrice();
        }
        totalPriceText.setText("סך הכל: ₪" + totalPrice);
    }
}
