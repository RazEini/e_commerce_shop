package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CartAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Item;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private TextView totalPriceText;
    private Cart cart;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        totalPriceText = findViewById(R.id.cartItemsText);

        // קבלת העגלה שנשלחה מ-RecyclerViewActivity
        cart = (Cart) getIntent().getSerializableExtra("cart");

        if (cart != null) {
            cartAdapter = new CartAdapter(this, cart.getItems());
            cartListView.setAdapter(cartAdapter);
            updateTotalPrice();
        }
    }

    // עדכון המחיר הכולל
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Item item : cart.getItems()) {
            totalPrice += item.getPrice();
        }
        totalPriceText.setText("סך הכל: ₪" + totalPrice);
    }
}
