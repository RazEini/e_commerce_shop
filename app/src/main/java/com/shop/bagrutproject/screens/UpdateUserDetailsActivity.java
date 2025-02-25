package com.shop.bagrutproject.screens;

import static com.shop.bagrutproject.screens.Login.cart;

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
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.DatabaseService;

public class UpdateUserDetailsActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName, editTextPhone;
    private Button buttonUpdate;
    private DatabaseReference usersRef;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_details);

        // חיבור לשדות במסך
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // קבלת UID של המשתמש המחובר
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        // מילוי הנתונים הקיימים מהפיירבייס
        DatabaseService.getInstance().getUser(uid, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if (user != null) {
                    editTextEmail.setText(user.getEmail());
                    editTextFirstName.setText(user.getFName());
                    editTextLastName.setText(user.getLName());
                    editTextPhone.setText(user.getPhone());
                    editTextPassword.setText(user.getPassword());
                }
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות
                Toast.makeText(UpdateUserDetailsActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // כפתור עדכון
        buttonUpdate.setOnClickListener(v -> updateUserDetails());
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

        // בדיקת תקינות המייל
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקת תקינות הסיסמא (למשל, לפחות 6 תווים)
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקת תקינות מספר הטלפון (למשל, לפחות 10 תווים)
        if (phone.length() < 10) {
            Toast.makeText(this, "Phone number must be at least 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת אובייקט משתמש מעודכן
        User updatedUser = new User(uid, email, password, fName, lName, phone);

        // עדכון הנתונים בפיירבייס
        usersRef.setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateUserDetailsActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateUserDetailsActivity.this, UserAfterLoginPage.class);
                    startActivity(intent);

                    // לאחר עדכון המידע, עדכן גם את העגלה
                    if (cart != null) { // הנח ש-cart הוא משתנה גלובלי שמכיל את העגלה
                        DatabaseService.getInstance().updateCart(cart, uid, new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void aVoid) {
                                Toast.makeText(UpdateUserDetailsActivity.this, "Cart updated successfully!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed(Exception e) {
                                Toast.makeText(UpdateUserDetailsActivity.this, "Failed to update cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateUserDetailsActivity.this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
