package com.shop.bagrutproject.screens;

import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CartAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.models.Order;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.action_bar_shop);
            TextView titlebar = findViewById(R.id.action_bar_text);
            String greeting;

            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hour >= 5 && hour < 12) {
                greeting = "בוקר טוב";
            } else if (hour >= 12 && hour < 18) {
                greeting = "צהריים טובים";
            } else {
                greeting = "ערב טוב";
            }

            User user = SharedPreferencesUtil.getUser(this);
            String name = user.getfName();
            titlebar.setText(greeting + " " + name);

            ImageView shopIcon = findViewById(R.id.shop_intro);
            shopIcon.setOnClickListener(v -> {
                v.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(100)
                        .withEndAction(() -> v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100))
                        .start();

                View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_shop, null);
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(sheetView);
                dialog.show();

                Button learnMoreBtn = sheetView.findViewById(R.id.btn_learn_more);
                learnMoreBtn.setOnClickListener(btn -> {
                    Intent intent = new Intent(this, Odot.class);
                    startActivity(intent);
                    finish();
                });
            });
        }

        cartListView = findViewById(R.id.lvCart);
        totalPriceText = findViewById(R.id.cartItemsText);
        checkoutButton = findViewById(R.id.btnCheckout);
        btnShop = findViewById(R.id.btnBackToShop);

        btnShop.setOnClickListener(v -> finish());

        databaseService = DatabaseService.getInstance();
        user = SharedPreferencesUtil.getUser(this);

        if (user == null) return;

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
                if (cart != null && cart.getItems() != null) {
                    cartAdapter.setItems(cart.getItems());
                    updateTotalPrice();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to read cart", e);
            }
        });

        databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> deals) {
                cartAdapter.setDeals(deals);
            }

            @Override
            public void onFailed(Exception e) {}
        });

        checkoutButton.setOnClickListener(v -> processOrder());
    }

    private void updateTotalPrice() {
        if (SharedPreferencesUtil.isAdmin(CartActivity.this)) {
            totalPriceText.setVisibility(View.GONE);
        } else {
            final AtomicReference<Double> totalPriceRef = new AtomicReference<>(0.0);

            if (cart == null || cart.getItems() == null) return;

            databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
                @Override
                public void onCompleted(List<Deal> deals) {
                    for (Item item : cart.getItems()) {
                        double itemPrice = item.getPrice();
                        double finalPrice = itemPrice;

                        for (Deal deal : deals) {
                            if (deal.isValid() && deal.getItemType().equals(item.getType())) {
                                double discount = deal.getDiscountPercentage();
                                finalPrice = itemPrice * (1 - discount / 100);
                                break;
                            }
                        }

                        totalPriceRef.set(totalPriceRef.get() + finalPrice);
                    }

                    totalPriceText.setText("סך הכל: ₪" + totalPriceRef.get());
                    totalPriceText.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Failed to get deals for total price", e);
                }
            });
        }
    }

    private void processOrder() {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            Toast.makeText(this, "העגלה ריקה!", Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = new Order(user.getUid(), cart.getItems());
        order.setTimestamp(System.currentTimeMillis());

        databaseService.getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> deals) {
                double total = 0.0;
                for (Item item : cart.getItems()) {
                    double itemPrice = item.getPrice();
                    for (Deal deal : deals) {
                        if (deal.isValid() && deal.getItemType().equals(item.getType())) {
                            itemPrice = itemPrice * (1 - deal.getDiscountPercentage() / 100);
                            break;
                        }
                    }
                    total += itemPrice;
                }

                order.setTotalPrice(total);
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putExtra("order", order);
                startActivity(intent);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CartActivity.this, "שגיאה בקריאת מבצעים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
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
