package com.shop.bagrutproject.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shop.bagrutproject.R;
import com.shop.bagrutproject.models.Deal;
import com.shop.bagrutproject.screens.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DealNotificationFetcher {

    public static final String CHANNEL_ID = "shop_notifications";
    private static final int NOTIFICATION_ID = 1001;

    public static void fetchAndSendDealNotification(Context context) {
        if (context == null) return;

        FirebaseDatabase.getInstance().getReference("deals")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Deal> validDeals = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Deal deal = data.getValue(Deal.class);
                            if (deal != null && deal.isValid()) {  // בדיקה אם המבצע תקף
                                validDeals.add(deal);
                            }
                        }

                        if (!validDeals.isEmpty()) {
                            Deal randomDeal = validDeals.get(new Random().nextInt(validDeals.size()));
                            sendNotification(context, randomDeal.getTitle(), randomDeal.getDescription());
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // אפשר להוסיף לוג או טוסט במקרה של שגיאה
                    }
                });
    }

    private static void sendNotification(Context context, String title, String message) {
        if (context == null) return;

        // בדיקת הרשאות להתראות (מ-Android 13 ומעלה)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // אימוג'ים רנדומליים לכותרת ולתיאור
        String[] titleEmojis = {"🎉", "🔥", "✨", "💥", "⚡"};
        String[] messageEmojis = {"🛍️", "🤑", "🎁", "💸", "🛒"};

        Random random = new Random();
        String emojiTitle = titleEmojis[random.nextInt(titleEmojis.length)] + " " + title + " " + titleEmojis[random.nextInt(titleEmojis.length)];
        String emojiMessage = messageEmojis[random.nextInt(messageEmojis.length)] + " " + message + " " + messageEmojis[random.nextInt(messageEmojis.length)];

        // כאשר לוחצים על ההתראה – פותח את מסך הלוגין (אפשר לשנות למסך אחר)
        Intent intent = new Intent(context, Login.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_electric_plug)
                .setContentTitle(emojiTitle)
                .setContentText(emojiMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }
}
