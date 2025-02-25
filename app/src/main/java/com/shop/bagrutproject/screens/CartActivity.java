package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CartAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";

    private ListView cartListView;
    private TextView totalPriceText;
    private Cart cart;
    private CartAdapter cartAdapter;
    private DatabaseService databaseService;
    User user=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.lvCart);
        totalPriceText = findViewById(R.id.cartItemsText);

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();

        user=Login.user;
        if (user == null) {
            return;

        }

        cartAdapter = new CartAdapter(this, new ArrayList<>());

        cartListView.setAdapter(cartAdapter);


        databaseService.getCart(user.getUid(), new DatabaseService.DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart object) {
                cart=object;
                Toast.makeText(getBaseContext(),object.getItems().size()+"",Toast.LENGTH_LONG).show();
                cartAdapter.setItems(cart.getItems());
                updateTotalPrice();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to read cart", e);
            }
        });
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
