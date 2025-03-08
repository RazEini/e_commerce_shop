package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
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

    private ArrayList<Item> allItems = new ArrayList<>();
    private DatabaseReference databaseReference;
    private Cart cart;
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
        ImageView cartIcon = findViewById(R.id.cartButton);
        btnBack = findViewById(R.id.btnBack2);
        totalPriceText = findViewById(R.id.cartItemsText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // יצירת ה-ItemsAdapter עם כל המוצרים
        itemsAdapter = new ItemsAdapter(allItems, this, this::addItemToCart);
        recyclerView.setAdapter(itemsAdapter);

        // חיבור ה-SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemsAdapter.filter(query);  // חיפוש לאחר לחיצה על "החיפוש"
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsAdapter.filter(newText);  // חיפוש בזמן כתיבה
                return false;
            }
        });

        // פעולת מעבר לעגלת קניות
        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, CartActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, UserAfterLoginPage.class);
            startActivity(intent);
            finish();
        });

        // טוען את המוצרים מ-Firebase כבר ב-onCreate
        fetchItemsFromFirebase();
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
                if (cart == null) {
                    cart = new Cart();
                }
                ShopActivity.this.cart = cart;
                updateTotalPrice();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load cart: ", e);
            }
        });

        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> object) {
                Log.d(TAG, "onCompleted: " + object);
                allItems.clear();
                allItems.addAll(object);
                itemsAdapter.notifyDataSetChanged();

                // ביצוע חיפוש אם יש טקסט ב-SearchView
                String query = ((SearchView) findViewById(R.id.searchView)).getQuery().toString();
                itemsAdapter.filter(query);  // סינון עם הטקסט הנוכחי, אם קיים
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load items: ", e);
            }
        });
    }


    private void fetchCartFromFirebase() {
        databaseService.getCart(AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart cart) {
                if (cart == null) {
                    cart = new Cart();
                }
                ShopActivity.this.cart = cart;
                updateTotalPrice();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load cart: ", e);
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
                Log.e(TAG, "Failed to update cart: ", e);
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


