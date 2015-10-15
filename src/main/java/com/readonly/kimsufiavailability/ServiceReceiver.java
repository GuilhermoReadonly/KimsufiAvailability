package com.readonly.kimsufiavailability;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// Broadcast receiver for receiving status updates from the IntentService
public class ServiceReceiver extends BroadcastReceiver
{
    RequestService rs;
    // Prevents instantiation
    public ServiceReceiver(RequestService rs) {
        this.rs = rs;
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive

    public void onReceive(Context context, Intent intent) {

        rs.mustContinue = false;

    }
}