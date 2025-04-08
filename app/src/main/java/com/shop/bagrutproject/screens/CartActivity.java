package com.shop.bagrutproject.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CartAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.models.Order;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";

    private ListView cartListView;
    private TextView totalPriceText;
    private Button checkoutButton;
    private ImageButton btnShop;
    private Cart cart;
    private CartAdapter cartAdapter;
    private DatabaseService databaseService;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.lvCart);
        totalPriceText = findViewById(R.id.cartItemsText);
        ImageView cartCheckoutIcon = findViewById(R.id.cartCheckoutButton);
        btnShop = findViewById(R.id.btnBackToShop);

        btnShop.setOnClickListener(v -> {
            finish();
        });

        databaseService = DatabaseService.getInstance();
        user = SharedPreferencesUtil.getUser(this);

        if (user == null) {
            return;
        }

        cartAdapter = new CartAdapter(this, new ArrayList<>(), new CartAdapter.OnCartClick() {
            @Override
            public void onItemCheckedChanged(int position, boolean isChecked) {
                if (isChecked) {
                    new AlertDialog.Builder(CartActivity.this)
                            .setMessage("האם אתה בטוח שברצונך למחוק את המוצר מהעגלה?")
                            .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cart.removeItem(position);
                                    databaseService.updateCart(cart, user.getUid(), new DatabaseService.DatabaseCallback<Void>() {
                                        @Override
                                        public void onCompleted(Void object) {
                                            cartAdapter.removeItem(position);
                                            updateTotalPrice();

                                            new AlertDialog.Builder(CartActivity.this)
                                                    .setMessage("המוצר נמחק מהעגלה בהצלחה!")
                                                    .setPositiveButton("אוקי", null)
                                                    .show();
                                        }

                                        @Override
                                        public void onFailed(Exception e) {
                                            Log.e(TAG, "Failed to update cart", e);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("לא", null)
                            .show();
                }
            }
        }, true);

        cartListView.setAdapter(cartAdapter);

        databaseService.getCart(user.getUid(), new DatabaseService.DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart object) {
                cart = object;
                cartAdapter.setItems(cart.getItems());
                updateTotalPrice();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to read cart", e);
            }
        });

        cartCheckoutIcon.setOnClickListener(v -> processOrder());
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Item item : cart.getItems()) {
            totalPrice += item.getPrice();
        }
        totalPriceText.setText("סך הכל: ₪" + totalPrice);
    }

    private void processOrder() {
        if (cart == null || cart.getItems().isEmpty()) {
            Toast.makeText(this, "העגלה ריקה!", Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = new Order(user.getUid(), cart.getItems());
        order.setTimestamp(System.currentTimeMillis());
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        ordersRef.child(order.getOrderId()).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CartActivity.this, "הזמנה נשמרה!", Toast.LENGTH_SHORT).show();

                    // מעבר לעמוד סיכום ההזמנה
                    Intent intent = new Intent(CartActivity.this, OrderSummaryActivity.class);
                    intent.putExtra("orderId", order.getOrderId());
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "שגיאה בשמירת ההזמנה", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        User user = SharedPreferencesUtil.getUser(this);
        String currentUserName = user.getfName() + " " + user.getlName();
        setTitle("שלום \uD83D\uDECD\uFE0F " + currentUserName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_user_page) {
            startActivity(new Intent(this, UserAfterLoginPage.class));
            return true;
        } else if (id == R.id.action_backtoshop) {
            startActivity(new Intent(this, ShopActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            AuthenticationService.getInstance().signOut();

            Intent go = new Intent(this, Login.class);
            go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(go);
            finishAffinity();
            Toast.makeText(CartActivity.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
