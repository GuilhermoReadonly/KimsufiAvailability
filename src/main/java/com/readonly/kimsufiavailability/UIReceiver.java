package com.readonly.kimsufiavailability;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

// Broadcast receiver for receiving status updates from the IntentService
public class UIReceiver extends BroadcastReceiver
{
    KimsufiAvailableChecker kac;
    // Prevents instantiation
    public UIReceiver(KimsufiAvailableChecker kac) {
        this.kac = kac;
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive

    public void onReceive(Context context, Intent intent) {

        String response = intent.getStringExtra(Constants.EXTENDED_DATA_STATUS);

        kac.logI(response);

        String status = response;

        if(! status.equals(Constants.STATUS_UNKNOWN)){
            Toast.makeText(context, R.string.serverAvailable, Toast.LENGTH_LONG).show();
            kac.ringtone.play();
        }

    }
}