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

        // קוד מתאים לפי שיטת התשלום שנבחרה
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
        // שלב 1: אינטגרציה עם Google Pay (צריך להוסיף את ה-API של Google Pay כאן)
        Toast.makeText(this, "מעבר לתשלום דרך Google Pay...", Toast.LENGTH_SHORT).show();
        // אפשר להוסיף קוד אינטגרציה עם Google Pay כאן
    }

    private void handlePayPal() {
        // שלב 2: אינטגרציה עם PayPal (צריך להוסיף את ה-API של PayPal כאן)
        Toast.makeText(this, "מעבר לתשלום דרך PayPal...", Toast.LENGTH_SHORT).show();
        // אפשר להוסיף קוד אינטגרציה עם PayPal כאן
    }

    private void handleCreditCard() {
        // שלב 3: אינטגרציה עם כרטיסי אשראי (צריך להוסיף את ה-API של כרטיסי אשראי כאן)
        Toast.makeText(this, "מעבר לתשלום בכרטיס אשראי...", Toast.LENGTH_SHORT).show();
        // אפשר להוסיף קוד אינטגרציה עם מערכת תשלום בכרטיסי אשראי כאן
    }

    // פונקציה לחישוב המחיר המוזל של פריט לפי המבצע
    private double calculateDiscountedPrice(Item item, List<Deal> allDeals) {
        double discount = 0;
        // עובר על כל המבצעים ומחפש אם יש מבצע שמתאים לקטגוריה של המוצר
        for (Deal deal : allDeals) {
            if (deal.isValid() && item.getType().equals(deal.getItemType())) {
                discount = deal.getDiscountPercentage();
                break;  // מצאנו מבצע מתאים, אפשר להפסיק לחפש
            }
        }
        return item.getPrice() * (1 - discount / 100);  // מחשב את המחיר אחרי הנחה
    }

    // עדכון סיכום ההזמנה לאחר חישוב הנחות
    private void updateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        final double[] discountedTotal = {0};  // יצירת מערך כדי לשמור את הערך ולהיות פנוי לשימוש בפונקציה פנימית

        // שליפת כל המבצעים מתוך המאגר
        DatabaseService.getInstance().getAllDeals(new DatabaseService.DatabaseCallback<List<Deal>>() {
            @Override
            public void onCompleted(List<Deal> deals) {
                // עכשיו אנחנו מקבלים את כל המבצעים, אז אנחנו יכולים לחשב את המחיר המוזל לכל פריט
                for (Item item : order.getItems()) {
                    double discountedPrice = calculateDiscountedPrice(item, deals);
                    summary.append(item.getName())
                            .append(" - ₪")
                            .append(discountedPrice)
                            .append("\n");
                    discountedTotal[0] += discountedPrice;  // עדכון הערך במערך
                }

                summary.append("סה\"כ: ₪").append(discountedTotal[0]);  // הצגת המחיר הסופי אחרי הנחות
                orderSummaryText.setText(summary.toString());
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PaymentActivity.this, "שגיאה בטעינת המבצעים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
