package com.shop.bagrutproject.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.User;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.services.DatabaseService;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private static final String ADMIN_Email = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin2609";

    EditText etEmail, etPassword;
    Button btnLog;
    String email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        checkIfUserIsAlreadyLoggedIn(); // בדיקה האם משתמש או מנהל כבר מחוברים

        initViews();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLog = findViewById(R.id.btnSubmit);
        btnLog.setOnClickListener(this);
    }

    public void btnBack2(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        email = etEmail.getText().toString();
        pass = etPassword.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("נא להזין כתובת אימייל");
            etEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            etPassword.setError("נא להזין סיסמא");
            etPassword.requestFocus();
            return;
        }

        loginUser(email, pass);
    }


    private void checkIfUserIsAlreadyLoggedIn() {
        boolean isAdmin = SharedPreferencesUtil.isAdmin(this);
        if (isAdmin) {
            Log.d(TAG, "Admin is already logged in, redirecting...");
            Intent adminIntent = new Intent(this, AdminPage.class);
            startActivity(adminIntent);
            finish();
            return;
        }

        new Handler().postDelayed(() -> {
            if (authenticationService.isUserSignedIn()) {
                Log.d(TAG, "User is already logged in, redirecting...");
                SharedPreferencesUtil.setIsAdmin(this, false);
                Intent go = new Intent(this, UserAfterLoginPage.class);
                startActivity(go);
                finish();
            }
        }, 1000);
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        authenticationService.signOut();

        Intent go = new Intent(this, Login.class);
        startActivity(go);
        finish();
    }

    private void loginUser(String email, String password) {
        if (email.equals(ADMIN_Email) && password.equals(ADMIN_PASSWORD)) {
            SharedPreferencesUtil.setIsAdmin(this, true);

            // דיליי של 2 שניות לפני המעבר לעמוד המנהל
            new Handler().postDelayed(() -> {
                Intent adminIntent = new Intent(this, AdminPage.class);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(adminIntent);
                finish();
            }, 2000);

            return;
        }

        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String uid) {
                databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User user) {
                        SharedPreferencesUtil.saveUser(Login.this, user);
                        SharedPreferencesUtil.setIsAdmin(Login.this, false);
                        Intent mainIntent = new Intent(Login.this, UserAfterLoginPage.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish(); // סוגרים את הפעילות הנוכחית
                    }

                    @Override
                    public void onFailed(Exception e) {
                        etPassword.setError("Invalid email or password");
                        etPassword.requestFocus();
                        authenticationService.signOut();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        setTitle("תפריט חנות");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_homepage) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_register) {
            startActivity(new Intent(this, Register.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, Odot.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
