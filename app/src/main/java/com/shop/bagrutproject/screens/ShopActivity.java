package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.ItemsAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.models.Item;
import com.google.firebase.database.DatabaseReference;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> cartItems = new ArrayList<>();

    private ArrayList<Item> allItems = new ArrayList<>();
    private ArrayList<Item> filteredItems = new ArrayList<>(); // רשימה מסוננת של מוצרים
    private DatabaseReference databaseReference;
    private Cart cart;
    private ImageButton btnBack;
    private TextView totalPriceText;
    private TextView cartItemCount; // TextView עבור מספר המוצרים בעגלה
    DatabaseService databaseService;
    AuthenticationService authenticationService;
    private String selectedCategory; // משתנה לאחסון הקטגוריה שנבחרה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if (getSupportActionBar() != null) {

            // הגדרת כותרת מותאמת אישית
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.action_bar_shop);
            TextView titlebar = findViewById(R.id.action_bar_text);
            String greeting;

            // קבלת השעה הנוכחית
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            if (hour >= 5 && hour < 12) {
                greeting = "בוקר טוב";
            } else if (hour >= 12 && hour < 18) {
                greeting = "צהריים טובים";
            } else {
                greeting = "ערב טוב";
            }

            if (SharedPreferencesUtil.isAdmin(this)) {
                titlebar.setText(greeting + " " + "המנהל");
            } else {
                User user = SharedPreferencesUtil.getUser(this);
                String currentUserName = user.getfName();
                titlebar.setText(greeting + " " + currentUserName);
            }

            ImageView shopIcon = findViewById(R.id.shop_intro);

            shopIcon.setOnClickListener(v -> {
                // אנימציית קפיצה
                v.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(100)
                        .withEndAction(() -> v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100))
                        .start();

                // יצירת BottomSheet
                View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_shop, null);
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(sheetView);
                dialog.show();

                // לחיצה על כפתור
                Button learnMoreBtn = sheetView.findViewById(R.id.btn_learn_more);
                learnMoreBtn.setOnClickListener(btn -> {
                    Intent Intent = new Intent(this, Odot.class);
                    startActivity(Intent);
                    finish();
                });
            });
        }

        // קבלת שם הקטגוריה מ-Intent
        selectedCategory = getIntent().getStringExtra("category");

        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recyclerViewItems);
        ImageView cartIcon = findViewById(R.id.cartButton);
        cartItemCount = findViewById(R.id.cartItemCount); // מצאנו את ה-TextView של מספר המוצרים בעגלה
        if (SharedPreferencesUtil.isAdmin(ShopActivity.this)) {
            cartIcon.setVisibility(View.INVISIBLE);
        } else {
            cartIcon.setVisibility(View.VISIBLE);
        }
        btnBack = findViewById(R.id.btnBack2);
        totalPriceText = findViewById(R.id.cartItemsText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemsAdapter = new ItemsAdapter(filteredItems, this, this::addItemToCart);
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
            }
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ShopActivity.this, CategoriesActivity.class);
            startActivity(intent);
            finish();
        });

        fetchItemsFromFirebase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchItemsFromFirebase();  // טוען את המוצרים מחדש
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
                updateCartItemCount(); // עדכון מספר המוצרים בעגלה
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

        // טעינת המוצרים
        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> object) {
                Log.d(TAG, "onCompleted: " + object);
                allItems.clear();
                allItems.addAll(object);
                filterItemsByCategory();  // קריאה לסינון המוצרים אחרי טעינת המוצרים מחדש
                itemsAdapter.notifyDataSetChanged();

                // עדכון התצוגה על פי חיפוש
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

    private void filterItemsByCategory() {
        filteredItems.clear();  // מחיקת המוצרים הקודמים ברשימה
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            if (selectedCategory.equals("כל המוצרים")) {
                // אם בחרנו בקטגוריה "כל המוצרים", נציג את כל המוצרים
                filteredItems.addAll(allItems);
            } else {
                // אם נבחרה קטגוריה מסוימת, נבצע סינון
                for (Item item : allItems) {
                    if (item.getType().equalsIgnoreCase(selectedCategory)) {
                        filteredItems.add(item);
                    }
                }
            }
        } else {
            // אם לא נבחרה קטגוריה, נציג את כל המוצרים
            filteredItems.addAll(allItems);
        }
        itemsAdapter.notifyDataSetChanged();
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
                    updateCartItemCount(); // עדכון מספר המוצרים בעגלה אחרי הוספת מוצר
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
        }
    }

    private void updateTotalPrice() {
        if (SharedPreferencesUtil.isAdmin(ShopActivity.this)) {
            totalPriceText.setVisibility(View.GONE);
        } else {
            // השתמשנו ב-AtomicReference לאחסון המחיר הכולל בצורה בטוחה בתוך קריאה אסינכרונית
            final AtomicReference<Double> totalPriceRef = new AtomicReference<>(0.0);

            // קריאה למבצעי הנחה מהפיירבייס
            databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
                @Override
                public void onCompleted(List<Deal> deals) {
                    // חישוב המחיר הכולל
                    for (Item item : cart.getItems()) {
                        double itemPrice = item.getPrice();
                        double finalPrice = itemPrice;

                        // חיפוש אחר מבצע תקף לכל פריט
                        for (Deal deal : deals) {
                            if (deal.isValid() && deal.getItemType().equals(item.getType())) {
                                double discount = deal.getDiscountPercentage();
                                finalPrice = itemPrice * (1 - discount / 100);
                                break; // מצאנו הנחה עבור הפריט, נצא מהלולאה
                            }
                        }

                        // עדכון המחיר הכולל
                        totalPriceRef.set(totalPriceRef.get() + finalPrice);
                    }

                    // עדכון התצוגה של המחיר הכולל
                    totalPriceText.setText("סך הכל: ₪" + totalPriceRef.get());
                    totalPriceText.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailed(Exception e) {
                    // טיפול בשגיאה אם קרתה
                }
            });
        }
    }


    // עדכון מספר המוצרים בעגלה
    private void updateCartItemCount() {
        if (cart != null && cart.getItems() != null) {
            int itemCount = cart.getItems().size();
            cartItemCount.setText(String.valueOf(itemCount)); // הצגת מספר הפריטים בעיגול
            if (itemCount > 0) {
                cartItemCount.setVisibility(View.VISIBLE); // הצגת העיגול אם יש פריטים בעגלה
            } else {
                cartItemCount.setVisibility(View.INVISIBLE); // הסתרת העיגול אם אין פריטים
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (SharedPreferencesUtil.isAdmin(this)) {
            getMenuInflater().inflate(R.menu.menu_shopadmin, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_shop, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(SharedPreferencesUtil.isAdmin(this)){
            if (id == R.id.action_admin_page) {
                startActivity(new Intent(this, AdminPage.class));
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
                Toast.makeText(ShopActivity.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
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

                Intent go = new Intent(ShopActivity.this, Login.class);
                go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(go);
                finishAffinity();
                Toast.makeText(ShopActivity.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
