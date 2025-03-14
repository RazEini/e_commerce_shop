package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Cart;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;

public class UpdateUserDetailsActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName, editTextPhone;
    private Button buttonUpdate;


    private User user = null;
    private String uid = "";

    DatabaseService databaseService;

    private static final String TAG = "UpdateUserDetiailsActivity";
    private AuthenticationService authenticationService;
    private Cart cart = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();

        uid = authenticationService.getCurrentUserId();


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);


        buttonUpdate.setOnClickListener(v -> updateUserDetails());

        databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                user = object;
                if (user != null) {
                    editTextEmail.setText(user.getEmail());
                    editTextFirstName.setText(user.getfName());
                    editTextLastName.setText(user.getlName());
                    editTextPhone.setText(user.getPhone());
                    editTextPassword.setText(user.getPassword());

                    cart = user.getCart();
                }

            }

            @Override
            public void onFailed(Exception e) {

                Toast.makeText(UpdateUserDetailsActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserDetails() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fName = editTextFirstName.getText().toString().trim();
        String lName = editTextLastName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // בדיקות תקינות
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }


        // בדיקת תקינות מספר הטלפון (למשל, לפחות 10 תווים)
        if (phone.length() < 10) {
            Toast.makeText(this, "Phone number must be at least 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }


        user.setlName(lName);
        user.setfName(fName);
        user.setPhone(phone);


        databaseService.updateUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(UpdateUserDetailsActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateUserDetailsActivity.this, UserAfterLoginPage.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(UpdateUserDetailsActivity.this, "Failed to update User: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
