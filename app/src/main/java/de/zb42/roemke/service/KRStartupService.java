package de.zb42.roemke.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by roemke on 11.03.15.
 */
public class KRStartupService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, KRService.class);
        context.startService(serviceIntent);
    }

}
