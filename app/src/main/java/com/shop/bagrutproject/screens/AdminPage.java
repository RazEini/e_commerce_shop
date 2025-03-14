package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.shop.bagrutproject.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

            Button btnAddProduct = findViewById(R.id.btn_add_product);
            Button btnPurchaseHistory = findViewById(R.id.btn_purchase_history);
            Button btnUsers = findViewById(R.id.btn_users);
            Button btnShop = findViewById(R.id.btn_shop);

            btnAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent(getApplicationContext(), AddItem.class);
                    startActivity(go);
                }
            });

            btnPurchaseHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent(getApplicationContext(), AdminOrderHistoryActivity.class);
                    startActivity(go);
                }
            });

            btnUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent(getApplicationContext(), UsersActivity.class);
                    startActivity(go);
                }
            });

            btnShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent(getApplicationContext(), ShopActivity.class);
                    startActivity(go);
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        setTitle("תפריט חנות");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_additem) {
            startActivity(new Intent(this, AddItem.class));
            return true;
        } else if (id == R.id.action_orderadmin) {
            startActivity(new Intent(this, AdminOrderHistoryActivity.class));
            return true;
        } else if (id == R.id.action_users) {
            startActivity(new Intent(this, UsersActivity.class));
            return true;
        }else if (id == R.id.action_shop) {
            startActivity(new Intent(this, ShopActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    }