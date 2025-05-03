package com.shop.bagrutproject.screens;

import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        // קבלת פרטי ההזמנה
        order = (Order) getIntent().getSerializableExtra("order");

        if (order == null) {
            Toast.makeText(this, "שגיאה בהזמנה", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateOrderSummary();

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

        Toast.makeText(this, "מעבד תשלום: " + paymentMethod, Toast.LENGTH_SHORT).show();

        // סימולציה של תשלום
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("pending");

        // חישוב המחיר הכולל עם המבצעים
        DatabaseService.getInstance().getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> deals) {
                double total = 0;
                for (Item item : order.getItems()) {
                    total += calculateDiscountedPrice(item, deals);
                }
                order.setTotalPrice(total);

                // שמירה לפיירבייס
                saveOrderToFirebase();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PaymentActivity.this, "שגיאה בטעינת המבצעים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveOrderToFirebase() {
        DatabaseService.getInstance().saveOrder(order, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void unused) {
                Toast.makeText(PaymentActivity.this, "ההזמנה נשמרה בהצלחה!", Toast.LENGTH_LONG).show();

                // מעבר לעמוד תודה
                Intent intent = new Intent(PaymentActivity.this, ThankYouActivity.class);
                startActivity(intent);
                finish(); // סוגר את העמוד הנוכחי (PaymentActivity)
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PaymentActivity.this, "שגיאה בשמירת ההזמנה", Toast.LENGTH_SHORT).show();
            }
        });
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
