package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);

        Button btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);
        Button btnAddItem = findViewById(R.id.btn_add_product);
        Button btnOrderHistoryAdmin = findViewById(R.id.btn_purchase_history);
        Button btnUsersList = findViewById(R.id.btn_users);
        Button btnDeals = findViewById(R.id.btn_add_deal);

        btnLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddItem(view);
            }
        });

        btnOrderHistoryAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAdminOrderHistory(view);
            }
        });

        btnUsersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUsersList(view);
            }
        });

        btnDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDeals(view);
            }
        });
    }

    public void goToAddItem(View view) {
        Intent intent = new Intent(AdminPage.this, AddItem.class);
        startActivity(intent);
    }

    public void goToAdminOrderHistory(View view) {
        Intent intent = new Intent(AdminPage.this, AdminOrderHistoryActivity.class);
        startActivity(intent);
    }

    public void goToUsersList(View view) {
        Intent intent = new Intent(AdminPage.this, UsersActivity.class);
        startActivity(intent);
    }

    public void goToDeals(View view) {
        Intent intent = new Intent(AdminPage.this, AdminDealsActivity.class);
        startActivity(intent);
    }

    public void logout() {
        SharedPreferencesUtil.signOutAdmin(AdminPage.this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        AuthenticationService.getInstance().signOut();

        Intent go = new Intent(AdminPage.this, Login.class);
        go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(go);
        finishAffinity();

        Toast.makeText(AdminPage.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        setTitle( "ברוך הבא 👋" + " המנהל");
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
        } else if (id == R.id.action_admindeals) {
            startActivity(new Intent(this, AdminDealsActivity.class));
            return true;
        } else if (id == R.id.action_catagories) {
            startActivity(new Intent(this, CategoriesActivity.class));
            return true;
        }else if (id == R.id.action_logout_admin) {
            SharedPreferencesUtil.signOutAdmin(AdminPage.this);

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            AuthenticationService.getInstance().signOut();

            Intent go = new Intent(AdminPage.this, Login.class);
            go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(go);
            finishAffinity();
            Toast.makeText(AdminPage.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
