package com.readonly.kimsufiavailability;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class RequestService extends IntentService {

    public boolean mustContinue = true;

    private int index = 174;

    private Intent mServiceIntent = null;
    private IntentFilter mStatusIntentFilter = null;
    private ServiceReceiver mResponseReceiver = null;

    RequestService rs;

    Thread workerThread  =  new Thread(new Runnable() {
        public void run() {

            Log.i(this.getClass().getName(), "Start of thread");

            NetworkJob nj = new NetworkJob();
            while(mustContinue) {
                Log.i(this.getClass().getName(), "Starting a new attempt...");
                String result = "";
                try {
                    result = nj.loadFromNetwork(getString(R.string.kimsufiUrl));

                    result = extractInfo(result,index);

                } catch (IOException e) {
                    Log.e(this.getClass().getName(), e.getMessage());
                }

                // Puts the status into the Intent
                Intent localIntent = new Intent(Constants.BROADCAST_UI).putExtra(Constants.EXTENDED_DATA_STATUS, result);// Puts the status into the Intent
                // Broadcasts the Intent to receivers in this app.
                LocalBroadcastManager.getInstance(rs).sendBroadcast(localIntent);

                SystemClock.sleep(20000);
            }

            Log.i(this.getClass().getName(), "End of thread");
        }
    });


    public RequestService(String name) {
        super(name);

        init();
        rs = this;
    }
    public RequestService() {
        super("com.readonly.kimsufiavailability.RequestService");
        init();
    }

    private void init(){
        mResponseReceiver = new ServiceReceiver(this);
        mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mResponseReceiver, mStatusIntentFilter);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        String data = intent.getDataString();

        Server server = (Server) intent.getSerializableExtra("server");

        index = server.getIndexJson();

        if(data.equals("startThread")) {
            mustContinue = true;
            workerThread.start();
        }else if(data.equals("stopThread")) {
            mustContinue = false;
        }
    }

    protected String extractInfo(String message, int index) {

        String msg = "";
        String availability = "";
        //String zone = "";

        try {
            JSONObject jObject = new JSONObject(message);
            jObject = jObject.getJSONObject("answer").getJSONArray("availability").getJSONObject(index).getJSONArray("metaZones").getJSONObject(1);
            availability = jObject.getString("availability");
            //zone = jObject.getString("zone");

        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }

        return availability;

    }
}
