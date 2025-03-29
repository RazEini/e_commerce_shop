package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

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
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

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

        itemsAdapter = new ItemsAdapter(allItems, this, this::addItemToCart);
        recyclerView.setAdapter(itemsAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                itemsAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsAdapter.filter(newText);
                return false;
            }
        });

        cartIcon.setOnClickListener(v -> {
            if (!SharedPreferencesUtil.isAdmin(ShopActivity.this)) {
                Intent intent = new Intent(ShopActivity.this, CartActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(ShopActivity.this, "אתה מנהל, אינך יכול לגשת לעגלה", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent;
            if (SharedPreferencesUtil.isAdmin(ShopActivity.this)) {
                intent = new Intent(ShopActivity.this, AdminPage.class);
            } else {
                intent = new Intent(ShopActivity.this, UserAfterLoginPage.class);
            }
            startActivity(intent);
            finish();
        });

        fetchItemsFromFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                new android.app.AlertDialog.Builder(ShopActivity.this)
                        .setMessage("נראה שקרתה תקלה בטעינת העגלה, נסה שוב")
                        .setPositiveButton("אוקי", null)
                        .show();
            }
        });

        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> object) {
                Log.d(TAG, "onCompleted: " + object);
                allItems.clear();
                allItems.addAll(object);
                itemsAdapter.notifyDataSetChanged();

                String query = ((SearchView) findViewById(R.id.searchView)).getQuery().toString();
                itemsAdapter.filter(query);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load items: ", e);
                new android.app.AlertDialog.Builder(ShopActivity.this)
                        .setMessage("נראה שקרתה תקלה בטעינת המוצרים, נסה שוב מאוחר יותר")
                        .setPositiveButton("אוקי", null)
                        .show();
            }
        });
    }

    public void addItemToCart(Item item) {
        if (!SharedPreferencesUtil.isAdmin(ShopActivity.this)) {
            this.cart.addItem(item);

            new android.app.AlertDialog.Builder(ShopActivity.this)
                    .setMessage("המוצר נוסף לעגלה בהצלחה!")
                    .setPositiveButton("אוקי", null)
                    .show();

            databaseService.updateCart(this.cart, AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    updateTotalPrice();
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Failed to update cart: ", e);
                    new android.app.AlertDialog.Builder(ShopActivity.this)
                            .setMessage("נראה שקרתה תקלה בהוספת המוצר לעגלה, נסה שוב")
                            .setPositiveButton("אוקי", null)
                            .show();
                }
            });
        } else {
            Toast.makeText(ShopActivity.this, "אתה מנהל, אינך יכול להוסיף מוצרים לעגלה", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalPrice() {
        if (SharedPreferencesUtil.isAdmin(ShopActivity.this)) {
            totalPriceText.setVisibility(View.GONE);
        } else {
            double totalPrice = 0;
            for (Item item : this.cart.getItems()) {
                totalPrice += item.getPrice();
            }
            totalPriceText.setText("סך הכל: ₪" + totalPrice);
            totalPriceText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(SharedPreferencesUtil.isAdmin(this)){
            getMenuInflater().inflate(R.menu.menu_shopadmin, menu);

        }
        else{
            getMenuInflater().inflate(R.menu.menu_shop, menu);

        }
        setTitle("תפריט חנות");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(SharedPreferencesUtil.isAdmin(this)){
            if (id == R.id.action_admin_page) {
                startActivity(new Intent(this, ShopActivity.class));
                return true;
            }

            if (id == R.id.action_logout_admin) {
                SharedPreferencesUtil.signOutAdmin(ShopActivity.this);

                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                AuthenticationService.getInstance().signOut();

                Intent go = new Intent(ShopActivity.this, Login.class);
                go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go);
                finishAffinity();
            }
        }

        else{
            if (id == R.id.action_user_page) {
                startActivity(new Intent(this, UserAfterLoginPage.class));
                return true;
            }

            if (id == R.id.action_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            }

            if (id == R.id.action_logout) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                AuthenticationService.getInstance().signOut();

                Intent go = new Intent(this, Login.class);
                go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go);
                finishAffinity();

            }
        }

        return super.onOptionsItemSelected(item);
    }
}
