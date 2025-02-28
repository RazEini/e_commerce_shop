package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.ItemsAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Item;
import com.google.firebase.database.DatabaseReference;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> cartItems = new ArrayList<>();

    private ArrayList<Item> allItems=new ArrayList<>();
    private DatabaseReference databaseReference;
    private Cart cart;
    private Button cartButton;
    private ImageButton btnBack;
    private TextView totalPriceText;
    DatabaseService databaseService;
    AuthenticationService authenticationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerViewItems);
        cartButton = findViewById(R.id.cartButton);
        btnBack = findViewById(R.id.btnBack2);
        totalPriceText = findViewById(R.id.cartItemsText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        itemsAdapter = new ItemsAdapter(allItems, this, this::addItemToCart);
        recyclerView.setAdapter(itemsAdapter);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, CartActivity.class);

            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, UserAfterLoginPage.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // טוען את המוצרים מ-Firebase
        fetchItemsFromFirebase();

    }

    private void fetchItemsFromFirebase() {

        databaseService.getCart(AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart cart) {
                ShopActivity.this.cart = cart;
                updateTotalPrice();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> object) {
                Log.d(TAG, "onCompleted: " + object);
                allItems.clear();
                allItems.addAll(object);
                itemsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(Exception e) {

            }

        });
    }


    // הוספת מוצר לעגלה
    public void addItemToCart(Item item) {
        this.cart.addItem(item);

        Toast.makeText(ShopActivity.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();


        databaseService.updateCart(this.cart, AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                updateTotalPrice();  // עדכון המחיר הכולל

            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }

    // עדכון המחיר הכולל בעגלה
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Item item : this.cart.getItems()) {
            totalPrice += item.getPrice();
        }
        totalPriceText.setText("סך הכל: ₪" + totalPrice);
    }
}
