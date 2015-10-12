package com.readonly.kimsufiavailability;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// Broadcast receiver for receiving status updates from the IntentService
public class ResponseReceiver extends BroadcastReceiver
{
    KimsufiAvailableChecker kac;
    // Prevents instantiation
    public ResponseReceiver(KimsufiAvailableChecker kac) {
        this.kac = kac;
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive

    public void onReceive(Context context, Intent intent) {

        String response = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);

        kac.logI(response);

        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
    }
}