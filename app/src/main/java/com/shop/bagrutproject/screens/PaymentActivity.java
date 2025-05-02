package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.PaymentAdapter;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.models.Order;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private TextView orderSummaryText;
    private EditText addressEditText;
    private Button completePaymentButton;
    private RadioGroup paymentMethodGroup;
    private Order order;

    private RecyclerView itemsRecyclerView;
    private TextView totalPriceText;
    private PaymentAdapter paymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        orderSummaryText = findViewById(R.id.orderSummaryText);
        addressEditText = findViewById(R.id.addressEditText);
        completePaymentButton = findViewById(R.id.completePaymentButton);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String orderId = getIntent().getStringExtra("orderId");

        if (orderId == null) {
            Toast.makeText(this, "שגיאה בהזמנה", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseService.getInstance().getOrder(orderId, new DatabaseService.DatabaseCallback<Order>() {
            @Override
            public void onCompleted(Order object) {
                order = object;
                updateOrderSummary();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PaymentActivity.this, "שגיאה בטעינת ההזמנה", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        completePaymentButton.setOnClickListener(v -> completePayment());
    }

    private void completePayment() {
        String address = addressEditText.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(this, "נא להזין כתובת", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "בחר שיטת תשלום", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedPaymentMethod = findViewById(selectedId);
        String paymentMethod = selectedPaymentMethod.getText().toString();

        switch (paymentMethod) {
            case "Google Pay":
                handleGooglePay();
                break;
            case "PayPal":
                handlePayPal();
                break;
            case "כרטיס אשראי":
                handleCreditCard();
                break;
            default:
                Toast.makeText(this, "שיטת תשלום לא נתמכת", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void handleGooglePay() {
        Toast.makeText(this, "מעבר לתשלום דרך Google Pay...", Toast.LENGTH_SHORT).show();
    }

    private void handlePayPal() {
        Toast.makeText(this, "מעבר לתשלום דרך PayPal...", Toast.LENGTH_SHORT).show();
    }

    private void handleCreditCard() {
        Toast.makeText(this, "מעבר לתשלום בכרטיס אשראי...", Toast.LENGTH_SHORT).show();
    }

    private double calculateDiscountedPrice(Item item, List<Deal> allDeals) {
        double discount = 0;
        for (Deal deal : allDeals) {
            if (deal.isValid() && item.getType().equals(deal.getItemType())) {
                discount = deal.getDiscountPercentage();
                break;
            }
        }
        return item.getPrice() * (1 - discount / 100);
    }

    private void updateOrderSummary() {
        DatabaseService.getInstance().getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> deals) {
                double total = 0;
                for (Item item : order.getItems()) {
                    total += calculateDiscountedPrice(item, deals);
                }

                totalPriceText.setText("סה\"כ: ₪" + String.format("%.2f", total));
                paymentAdapter = new PaymentAdapter(order.getItems(), deals);
                itemsRecyclerView.setAdapter(paymentAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PaymentActivity.this, "שגיאה בטעינת המבצעים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
