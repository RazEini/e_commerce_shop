package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.ItemsAdapter;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecyclerViewActivity extends AppCompatActivity {

    /// tag for logging
    private static final String TAG = "ShopActivity";


    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> cartItems = new ArrayList<>();

    private ArrayList<Item> allItems=new ArrayList<>();
    private DatabaseReference databaseReference;
    private Cart cart;
    private Button cartButton;
    private TextView totalPriceText;
    DatabaseService databaseService;
    AuthenticationService authenticationService;
    User user=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);


        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
        user=Login.user;
        cart=Login.cart;




        recyclerView = findViewById(R.id.recyclerViewItems);
        cartButton = findViewById(R.id.cartButton);
        totalPriceText = findViewById(R.id.cartItemsText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // מאתחל את itemsAdapter ומעביר את שני הפרמטרים
        itemsAdapter = new ItemsAdapter(allItems, this);
        recyclerView.setAdapter(itemsAdapter);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecyclerViewActivity.this, CartActivity.class);

            startActivity(intent);
        });



        // נוודא שלחיצה על כפתור "עגלת קניות" תציג את העגלה


        // טוען את המוצרים מ-Firebase
        fetchItemsFromFirebase();
    }

    private void fetchItemsFromFirebase() {
        databaseService.getItems(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> object) {
                Log.d(TAG, "onCompleted: " + object);
                allItems.clear();
                allItems.addAll(object);
                /// notify the adapter that the data has changed
                /// this specifies that the data has changed
                /// and the adapter should update the view
                /// @see FoodSpinnerAdapter#notifyDataSetChanged()
                itemsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailed(Exception e) {

            }



    });
    }


    // הוספת מוצר לעגלה
    public void addItemToCart(Item item) {

        if(user==null){



            return;
        }

        if(Login.cart==null) {
            Cart newCart = new Cart();

            Login.cart=newCart;
        }
        Login.cart.addItem(item);
        updateTotalPrice();  // עדכון המחיר הכולל
        Toast.makeText(RecyclerViewActivity.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();


        databaseService.updateCart(Login.cart,user.getUid(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {


            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }

    // עדכון המחיר הכולל בעגלה
    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Item item : Login.cart.getItems()) {
            totalPrice += item.getPrice();
        }
        totalPriceText.setText("סך הכל: ₪" + totalPrice);
    }
}
