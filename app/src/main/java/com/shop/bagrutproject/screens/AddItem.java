package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Item;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.ImageUtil;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    /// tag for logging
    private static final String TAG = "AddItemActivity";

    private EditText ItemNameEditText, ItemPriceEditText;
    private Button addItemButton;
    private ImageButton selectImageButton, captureImageButton;
    private ImageView ItemImageView;
    private DatabaseService databaseService;

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentView(R.layout.activity_add_Item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// request permission for the camera and storage
        ImageUtil.requestPermission(this);

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();

        /// get the views
        ItemNameEditText = findViewById(R.id.Item_name);
        ItemPriceEditText = findViewById(R.id.Item_price);
        addItemButton = findViewById(R.id.add_Item_button);
        selectImageButton = findViewById(R.id.select_image_button);
        captureImageButton = findViewById(R.id.capture_image_button);
        ItemImageView = findViewById(R.id.Item_image);

        /// set the on click listeners
        addItemButton.setOnClickListener(this);
        selectImageButton.setOnClickListener(this);
        captureImageButton.setOnClickListener(this);

        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ItemImageView.setImageURI(selectedImage);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ItemImageView.setImageBitmap(bitmap);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addItemButton.getId()) {
            Log.d(TAG, "Add food button clicked");
            addFoodToDatabase();
            return;
        }
        if (v.getId() == selectImageButton.getId()) {
            // select image from gallery
            Log.d(TAG, "Select image button clicked");
            selectImageFromGallery();
            return;
        }
        if (v.getId() == captureImageButton.getId()) {
            // capture image from camera
            Log.d(TAG, "Capture image button clicked");
            captureImageFromCamera();
            return;
        }
    }

    /// add the food to the database
    /// @see Food
    private void addFoodToDatabase() {
        /// get the values from the input fields
        String name = ItemNameEditText.getText().toString();
        String priceText = ItemPriceEditText.getText().toString();
        String imageBase64 = ImageUtil.convertTo64Base(ItemImageView);

        /// validate the input
        /// stop if the input is not valid
        if (!isValid(name, priceText, imageBase64)) return;

        /// convert the price to double
        double price = Double.parseDouble(priceText);

        /// generate a new id for the item
        String id = databaseService.generateitemId();

        Log.d(TAG, "Adding item to database");
        Log.d(TAG, "ID: " + id);
        Log.d(TAG, "Name: " + name);
        Log.d(TAG, "Price: " + price);
        Log.d(TAG, "Image: " + imageBase64);

        /// create a new food object
        Item item = new Item(id, name, price, imageBase64);

        /// save the item to the database and get the result in the callback
        databaseService.createNewItem(item, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "item added successfully");
                Toast.makeText(activityAddItem.this, "item added successfully", Toast.LENGTH_SHORT).show();
                /// clear the input fields after adding the item for the next item
                Log.d(TAG, "Clearing input fields");
                ItemNameEditText.setText("");
                ItemPriceEditText.setText("");
                ItemImageView.setImageBitmap(null);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to add item", e);
                Toast.makeText(AddItemActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /// select image from gallery
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }


    /// validate the input
    private boolean isValid(String name, String priceText, String imageBase64) {
        if (name.isEmpty()) {
            Log.e(TAG, "Name is empty");
            ItemNameEditText.setError("Name is required");
            ItemNameEditText.requestFocus();
            return false;
        }

        if (priceText.isEmpty()) {
            Log.e(TAG, "Price is empty");
            ItemPriceEditText.setError("Price is required");
            ItemPriceEditText.requestFocus();
            return false;
        }

        if (imageBase64 == null) {
            Log.e(TAG, "Image is required");
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
