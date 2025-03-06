package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Order;
import com.shop.bagrutproject.services.DatabaseService;

public class PaymentActivity extends AppCompatActivity {

    private TextView orderSummaryText;
    private EditText addressEditText;
    private Button completePaymentButton;
    private RadioGroup paymentMethodGroup;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        orderSummaryText = findViewById(R.id.orderSummaryText);
        addressEditText = findViewById(R.id.addressEditText);
        completePaymentButton = findViewById(R.id.completePaymentButton);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

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
        }
    }

    private void handleGooglePay() {
        Toast.makeText(this, "מעבר לתשלום דרך Google Pay...", Toast.LENGTH_SHORT).show();
        // כאן יהיה קוד האינטגרציה עם Google Pay
    }

    private void handlePayPal() {
        Toast.makeText(this, "מעבר לתשלום דרך PayPal...", Toast.LENGTH_SHORT).show();
        // כאן יהיה קוד האינטגרציה עם PayPal
    }

    private void handleCreditCard() {
        Toast.makeText(this, "מעבר לתשלום בכרטיס אשראי...", Toast.LENGTH_SHORT).show();
        // כאן יהיה קוד לעמוד תשלום בכרטיס אשראי
    }

    private void updateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < order.getItems().size(); i++) {
            summary.append(order.getItems().get(i).getName())
                    .append(" - ₪")
                    .append(order.getItems().get(i).getPrice())
                    .append("\n");
        }
        orderSummaryText.setText(summary.toString());
    }
}
