package com.shop.bagrutproject.screens;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shop.bagrutproject.R; // ודא שההפניה נכונה לקובץ R
import com.shop.bagrutproject.adapters.ItemsAdapter; // יש לוודא שה- ItemsAdapter קיים
import com.shop.bagrutproject.models.Item; // ודא שה- Item קיים

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view); // הפניה לקובץ XML activity_recycler_view.xml

        recyclerView = findViewById(R.id.recyclerViewItems); // הפניה ל-RecycleView מתוך ה- XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // הגדרת LayoutManager

        // יצירת רשימה של אייטמים לצורך הצגה
        itemList = new ArrayList<>();
        itemList.add(new Item("1", "מכונת כביסה", "מוצרי חשמל", "לבן", "Samsung", "מכונת כביסה חכמה", 2500, "", 0, 0, 0));
        itemList.add(new Item("2", "טלוויזיה 55 אינץ'", "מוצרי חשמל", "שחור", "LG", "טלוויזיה 4K חכמה", 3200, "", 0, 0, 0));

        // יצירת האדפטר והגדרת הקשר עם ה- RecyclerView
        itemsAdapter = new ItemsAdapter(itemList);
        recyclerView.setAdapter(itemsAdapter);
    }
}

