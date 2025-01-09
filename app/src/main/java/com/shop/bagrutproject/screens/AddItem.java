package com.shop.bagrutproject.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;

public class AddItem extends AppCompatActivity {
    EditText etItemName1, etPrice1, etDescription;
    int price;
    String itemName, stPrice;
    Button btnGallery1,btnTakePic1, btnAddItem1;
    FirebaseDatabase database ;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        initViews();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Items").push();

        Spinner ItemTypeSpinner = findViewById(R.id.spType);
        Spinner ItemColorSpinner = findViewById(R.id.spColor);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.ItemTypeArray, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.ItemColorArray, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ItemTypeSpinner.setAdapter(adapter);

        ItemColorSpinner.setAdapter(adapter1);

        ItemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedItem = (String) parentView.getItemAtPosition(position);
                Toast.makeText(AddItem.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        ItemColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedItem = (String) parentView.getItemAtPosition(position);
                Toast.makeText(AddItem.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void initViews() {
        btnAddItem1=findViewById(R.id.btnAddItem);
        btnGallery1=findViewById(R.id.btnGallery);
        btnTakePic1=findViewById(R.id.btnTakePic);

        etItemName1=findViewById(R.id.etItemName);
        etPrice1=findViewById(R.id.etItemPrice);
        etDescription=findViewById(R.id.etItemInfo);

    }
}