package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.adapters.ItemsAdapter;
import com.shop.bagrutproject.models.Item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private List<Item> itemList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // יצירת אובייקט Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        itemList = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(itemList);
        recyclerView.setAdapter(itemsAdapter);

        // טוען את הנתונים מ-Firebase
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear(); // נמחק את הרשימה הישנה
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class); // המרת הנתונים לאובייקט Item
                    if (item != null) {
                        itemList.add(item); // הוספת אייטם לרשימה
                    }
                }
                itemsAdapter.notifyDataSetChanged(); // עדכון ה-RecyclerView עם הנתונים החדשים
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // טיפול בשגיאה
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
}
