package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.CategoryAdapter;
import com.shop.bagrutproject.models.Category;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        gridView = findViewById(R.id.gridView);
        categoryList = new ArrayList<>();

        // הוספת קטגוריה של כל המוצרים
        categoryList.add(new Category("כל המוצרים", R.drawable.all_items_catagory));

        categoryList.add(new Category("מקררים", R.drawable.refrigerator_catagory));
        categoryList.add(new Category("מכונות כביסה", R.drawable.washing_machine_catagory));
        categoryList.add(new Category("מדיחי כלים", R.drawable.dishwasher_catagory));
        categoryList.add(new Category("תנורים", R.drawable.oven_catagory));
        categoryList.add(new Category("מייבשים", R.drawable.dryer_catagory));
        categoryList.add(new Category("מזגנים", R.drawable.air_conditioner_catagory));
        categoryList.add(new Category("מאווררים", R.drawable.fan_catagory));
        categoryList.add(new Category("קומקומים", R.drawable.kettle_catagory));
        categoryList.add(new Category("טלוויזיות", R.drawable.tv_catagory));
        categoryList.add(new Category("קוטלי יתושים", R.drawable.bug_zapper_catagory));
        categoryList.add(new Category("מגהצים", R.drawable.iron_catagory));

        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Category clicked = categoryList.get(position);
            String selectedCategory = clicked.getName().trim();

            // אם הקטגוריה שנבחרה היא "כל המוצרים", נעביר למסך החנות ללא סינון
            if (selectedCategory.equals("כל המוצרים")) {
                Intent intent = new Intent(CategoriesActivity.this, ShopActivity.class);
                intent.putExtra("category", "");  // שלח קטיוגיה ריקה בשביל להציג את כל המוצרים
                startActivity(intent);
            } else {
                Intent intent = new Intent(CategoriesActivity.this, ShopActivity.class);
                intent.putExtra("category", selectedCategory);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catagorys, menu);
        User user = SharedPreferencesUtil.getUser(this);
        String currentUserName = user.getfName() + " " + user.getlName();
        setTitle("שלום \uD83D\uDECD\uFE0F " + currentUserName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_user_page) {
            startActivity(new Intent(this, UserAfterLoginPage.class));
            return true;
        }

        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            AuthenticationService.getInstance().signOut();

            Intent go = new Intent(this, Login.class);
            go.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(go);
            finishAffinity();
            Toast.makeText(CategoriesActivity.this, "התנתקת בהצלחה!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
