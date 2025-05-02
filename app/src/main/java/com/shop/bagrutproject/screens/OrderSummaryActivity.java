package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CartAdapter;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.models.Order;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {
    private ListView orderListView;
    private TextView totalPriceText;
    private Button proceedToPaymentButton;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        orderListView = findViewById(R.id.orderListView);
        totalPriceText = findViewById(R.id.totalPriceText);
        proceedToPaymentButton = findViewById(R.id.proceedToPaymentButton);

        String orderId = getIntent().getStringExtra("orderId");

        if (orderId == null) {
            Toast.makeText(this, "שגיאה בהזמנה", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // שליפת ההזמנה מפיירבייס
        DatabaseService.getInstance().getOrder(orderId, new DatabaseService.DatabaseCallback<Order>() {
            @Override
            public void onCompleted(Order object) {
                order = object;

                // שלוף את כל המבצעים לפני יצירת האדפטר
                DatabaseService.getInstance().getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
                    @Override
                    public void onCompleted(List<Deal> deals) {
                        for (Item item : order.getItems()) {
                            double originalPrice = item.getPrice();
                            for (Deal deal : deals) {
                                if (deal.isValid() && deal.getItemType().equals(item.getType())) {
                                    double discount = deal.getDiscountPercentage();
                                    double discountedPrice = originalPrice * (1 - discount / 100);
                                    item.setPrice(discountedPrice);
                                    break;
                                }
                            }
                        }

                        CartAdapter adapter = new CartAdapter(OrderSummaryActivity.this, order.getItems(), null, false);
                        orderListView.setAdapter(adapter);
                        updateTotalPrice(); // עכשיו totalPrice מתבסס על מחירים מעודכנים
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(OrderSummaryActivity.this, "שגיאה בטעינת מבצעים", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(OrderSummaryActivity.this, "שגיאה בטעינת ההזמנה", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        proceedToPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSummaryActivity.this, PaymentActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0;

        for (Item item : order.getItems()) {
            totalPrice += item.getPrice(); // המחיר כבר כולל הנחה אם הייתה
        }

        totalPriceText.setText("סה\"כ לתשלום: ₪" + String.format("%.2f", totalPrice));
    }
}
