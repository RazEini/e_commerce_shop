package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;


public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText etFName, etLName, etPhone, etEmail, etPass;
    Button btnReg;
    String fName,lName, phone, email, pass;


    DatabaseService databaseService;

    private static final String TAG = "RegisterActivity";


    private AuthenticationService authenticationService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.action_bar_title);

            ImageView shopIcon = findViewById(R.id.shop_intro);

            shopIcon.setOnClickListener(v -> {
                v.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(100)
                        .withEndAction(() -> v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100))
                        .start();

                View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_shop, null);
                BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(sheetView);
                dialog.show();

                Button learnMoreBtn = sheetView.findViewById(R.id.btn_learn_more);
                learnMoreBtn.setOnClickListener(btn -> {
                    Intent Intent = new Intent(this, Odot.class);
                    startActivity(Intent);
                    finish();
                });
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();


        init_views();

    }

    private void init_views(){
        btnReg=findViewById(R.id.btnSubmit);
        etFName=findViewById(R.id.etFname);
        etLName=findViewById(R.id.etLname);
        etPhone=findViewById(R.id.etPhone);
        etEmail=findViewById(R.id.etEmail);
        etPass=findViewById(R.id.etPassword);
        btnReg.setOnClickListener(this);
    }

    public void btnBack(View view) {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        fName = etFName.getText().toString();
        lName = etLName.getText().toString();
        phone = etPhone.getText().toString();
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();

        // בדיקת תקינות הקלט
        Boolean isValid = true;

        // בדיקת שדות ריקים
        if (fName.isEmpty()) {
            etFName.setError("נא להזין שם פרטי");
            isValid = false;
        }

        if (lName.isEmpty()) {
            etLName.setError("נא להזין שם משפחה");
            isValid = false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("נא להזין מספר טלפון");
            isValid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError("נא להזין כתובת אימייל");
            isValid = false;
        }

        if (pass.isEmpty()) {
            etPass.setError("נא להזין סיסמא");
            isValid = false;
        }

        // אם כל השדות מלאים, נעבור לבדיקת תקינות
        if (isValid) {
            if (fName.length() < 2) {
                Toast.makeText(Register.this, "שם פרטי קצר מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (lName.length() < 2) {
                Toast.makeText(Register.this, "שם משפחה קצר מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (phone.length() < 9 || phone.length() > 10) {
                Toast.makeText(Register.this, "מספר הטלפון לא תקין", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (!email.contains("@")) {
                Toast.makeText(Register.this, "כתובת האימייל לא תקינה", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (pass.length() < 6) {
                Toast.makeText(Register.this, "הסיסמה קצרה מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (pass.length() > 20) {
                Toast.makeText(Register.this, "הסיסמה ארוכה מדי", Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }

        if (isValid) {
            registerUser(email, pass, fName, lName, phone);
        }
    }


    /// Register the user
    private void registerUser(String email, String password, String fName, String lName, String phone) {
        Log.d(TAG, "registerUser: Registering user...");

        /// call the sign up method of the authentication service
        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {

            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User registered successfully");
                /// create a new user object
                User user = new User();
                user.setUid(uid);
                user.setEmail(email);
                user.setPassword(password);
                user.setfName(fName);
                user.setlName(lName);
                user.setPhone(phone);

                databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {

                    @Override
                    public void onCompleted(Void object) {
                        Log.d(TAG, "onCompleted: User registered successfully");
                        /// save the user to shared preferences
                        SharedPreferencesUtil.saveUser(Register.this, user);



                        Log.d(TAG, "onCompleted: Redirecting to MainActivity");
                        Intent mainIntent = new Intent(Register.this, Login.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to register user", e);
                        /// show error message to user
                        Toast.makeText(Register.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                        /// sign out the user if failed to register
                        /// this is to prevent the user from being logged in again
                        authenticationService.signOut();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to sign up user", e);
                /// show error message to user
                Toast.makeText(Register.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        setTitle("תפריט חנות");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_homepage) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_login) {
            startActivity(new Intent(this, Login.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, Odot.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
