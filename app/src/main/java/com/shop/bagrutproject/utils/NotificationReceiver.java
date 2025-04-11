package com.shop.bagrutproject.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shop.bagrutproject.utils.DealNotificationFetcher;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DealNotificationFetcher.fetchAndSendDealNotification(context);
    }
}
