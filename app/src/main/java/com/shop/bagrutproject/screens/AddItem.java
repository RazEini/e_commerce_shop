package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;

public class AddItem extends AppCompatActivity {

    private EditText etItemName, etItemCompany, etItemInfo, etItemPrice;
    private Spinner spType, spColor;
    private Button btnGallery, btnTakePic, btnAddItem;
    private ImageView imageView;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etItemCompany = findViewById(R.id.etItemCompany);
        etItemInfo = findViewById(R.id.etItemInfo);
        etItemPrice = findViewById(R.id.etItemPrice);
        spType = findViewById(R.id.spType);
        spColor = findViewById(R.id.spColor);
        btnGallery = findViewById(R.id.btnGallery);
        btnTakePic = findViewById(R.id.btnTakePic);
        btnAddItem = findViewById(R.id.btnAddItem);
        imageView = findViewById(R.id.imageView);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = etItemName.getText().toString();
                String itemCompany = etItemCompany.getText().toString();
                String itemInfo = etItemInfo.getText().toString();
                String itemPrice = etItemPrice.getText().toString();
                String itemType = spType.getSelectedItem().toString();
                String itemColor = spColor.getSelectedItem().toString();

                if (itemName.isEmpty() || itemCompany.isEmpty() || itemInfo.isEmpty() ||
                        itemPrice.isEmpty() || itemType.isEmpty() || itemColor.isEmpty()) {
                    Toast.makeText(AddItem.this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddItem.this, "המוצר נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
        }
    }
}


