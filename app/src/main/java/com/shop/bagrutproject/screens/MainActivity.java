package com.shop.bagrutproject.screens;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.shop.bagrutproject.R;
import com.shop.bagrutproject.services.AuthenticationService;
import com.shop.bagrutproject.utils.NotificationWorker;
import com.shop.bagrutproject.utils.SharedPreferencesUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "shop_notifications";
    Button btnReg, btnLog, btnOd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createNotificationChannel();
        requestNotificationPermission();
        scheduleNotificationWorker();

        initViews();
    }

    private void initViews() {
        btnReg = findViewById(R.id.btnRegister);
        btnLog = findViewById(R.id.btnLogin);
        btnOd = findViewById(R.id.btnOdot);

        btnReg.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Register.class)));
        btnLog.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Login.class)));
        btnOd.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Odot.class)));
    }


    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            boolean isFirstTime = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    .getBoolean("isFirstTime", true);

            if (isFirstTime) {
                getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isFirstTime", false)
                        .apply();

                if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "נדרשת הרשאה לשליחת התראות על מבצעים", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 102);
                }
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Shop Notifications";
            String description = "התראות על מבצעים ומוצרים";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void scheduleNotificationWorker() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 6, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }

    public static void sendRandomNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        String[] titles = {"🔥 מבצע לוהט!", "⚡ הנחה מטורפת!", "💡 חדש בחנות!", "🎉 אל תפספס!"};
        String[] messages = {
                "קבל 20% הנחה על מוצרי חשמל היום בלבד!",
                "המבצע נגמר בקרוב - אל תפספס!",
                "מוצרים חדשים במחירים מטורפים!",
                "קנה מוצר וקבל השני ב-50% הנחה!"
        };

        Random random = new Random();
        String randomTitle = titles[random.nextInt(titles.length)];
        String randomMessage = messages[random.nextInt(messages.length)];

        Intent intent = new Intent(context, Login.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_electric_plug)
                .setContentTitle(randomTitle)
                .setContentText(randomMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(random.nextInt(1000), builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setTitle("תפריט חנות");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_register) {
            startActivity(new Intent(this, Register.class));
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
