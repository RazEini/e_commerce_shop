package com.shop.bagrutproject.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    public static void fetchAndSendDealNotification(Context context) {
        FirebaseDatabase.getInstance().getReference("deals")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Deal> deals = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Deal deal = data.getValue(Deal.class);
                            if (deal != null) {
                                deals.add(deal);
                            }
                        }

                        if (!deals.isEmpty()) {
                            Random random = new Random();
                            Deal randomDeal = deals.get(random.nextInt(deals.size()));
                            sendNotification(context, randomDeal.getTitle(), randomDeal.getDescription());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // לא הצלחנו לשלוף מבצע - אפשר להוסיף לוג אם רוצים
                    }
                });
    }

    private static void sendNotification(Context context, String title, String message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // יצירת מערך אימוג'ים אפשריים
        String[] titleEmojis = {"🎉", "🔥", "✨", "💥", "⚡"};
        String[] messageEmojis = {"🛍️", "🤑", "🎁", "💸", "🛒"};

        // בחירה רנדומלית של אימוג'י
        Random random = new Random();
        String randomTitleEmoji = titleEmojis[random.nextInt(titleEmojis.length)];
        String randomMessageEmoji = messageEmojis[random.nextInt(messageEmojis.length)];

        // הוספת האימוג'ים בצורה רנדומלית
        String emojiTitle = randomTitleEmoji + " " + title + " " + randomTitleEmoji;
        String emojiMessage = randomMessageEmoji + " " + message + " " + randomMessageEmoji;

        Intent intent = new Intent(context, Login.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_electric_plug)
                .setContentTitle(emojiTitle)
                .setContentText(emojiMessage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify(1, builder.build());
    }


}
