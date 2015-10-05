package com.readonly.kimsufiavailability;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class KimsufiAvailableChecker extends Activity {

    private TextView tV = null;
    Intent mServiceIntent = null;
    IntentFilter mStatusIntentFilter = null;

    ResponseReceiver mResponseReceiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kimsufi_available_checker);

        tV = (TextView) findViewById(R.id.textViewOutput);

        mResponseReceiver = new ResponseReceiver(this);



        // The filter's action is BROADCAST_ACTION
        mStatusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);


        LocalBroadcastManager.getInstance(this).registerReceiver(mResponseReceiver, mStatusIntentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kimsufi_available_checker, menu);



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addLog(String stringToAdd){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        String tVText = tV.getText().toString();

        tVText += currentDateAndTime + " " + stringToAdd + System.getProperty("line.separator");
        tV.setText(tVText);
        Log.i(this.getClass().getName(), tVText);
    }


    public void emptyLog(){
        tV.setText("");
    }



    public void buttonOnClick(View v) {
        addLog("Starting service");


        mServiceIntent = new Intent(this, RequestService.class);
        mServiceIntent.setData(Uri.parse("startThread"));

        // Starts the IntentService
        this.startService(mServiceIntent);


    }

    public void buttonResetOnClick(View v) {

        mServiceIntent = new Intent(this, RequestService.class);
        mServiceIntent.setData(Uri.parse("stopThread"));
        this.stopService(mServiceIntent);

        emptyLog();

        addLog("Service has been stopped");
    }


}
