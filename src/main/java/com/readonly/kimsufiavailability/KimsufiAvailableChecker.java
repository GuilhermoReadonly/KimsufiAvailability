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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class KimsufiAvailableChecker extends Activity {

    private Button buttonStartStop = null;

    private RadioButton radioButtonKS6 = null;
    private RadioButton radioButtonKS5 = null;
    private RadioButton radioButtonKS4 = null;
    private RadioButton radioButtonKS3 = null;
    private RadioButton radioButtonKS2 = null;
    private RadioButton radioButtonKS2SSD = null;
    private RadioButton radioButtonKS1 = null;
    private RadioGroup radioGroup = null;

    Intent mServiceIntent = null;
    IntentFilter mStatusIntentFilter = null;

    ResponseReceiver mResponseReceiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kimsufi_available_checker);

        buttonStartStop = (Button) findViewById(R.id.buttonStartStop);
        radioButtonKS6 = (RadioButton) findViewById(R.id.radioButtonKS6);
        radioButtonKS5 = (RadioButton) findViewById(R.id.radioButtonKS5);
        radioButtonKS4 = (RadioButton) findViewById(R.id.radioButtonKS4);
        radioButtonKS3 = (RadioButton) findViewById(R.id.radioButtonKS3);
        radioButtonKS2 = (RadioButton) findViewById(R.id.radioButtonKS2);
        radioButtonKS2SSD = (RadioButton) findViewById(R.id.radioButtonKS2SSD);
        radioButtonKS1 = (RadioButton) findViewById(R.id.radioButtonKS1);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonKS1.setChecked(true);

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

    public void logI(String stringToAdd){
        Log.i(this.getClass().getName(), stringToAdd);
    }

    public void logE(String stringToAdd){
        Log.e(this.getClass().getName(), stringToAdd);
    }


    private void enableRadioButtons(boolean enable){
        /*
        radioButtonKS6.setEnabled(enable);
        radioButtonKS5.setEnabled(enable);
        radioButtonKS4.setEnabled(enable);
        radioButtonKS3.setEnabled(enable);
        radioButtonKS2.setEnabled(enable);
        radioButtonKS2SSD.setEnabled(enable);
        radioButtonKS1.setEnabled(enable);
        */
        for(int i=0;i<radioGroup.getChildCount();i++){
            radioGroup.getChildAt(i).setEnabled(enable);
        }
    }


    public void buttonStartStopOnClick(View v) {
        if(buttonStartStop.getText().equals(Constants.UI_BUTTON_START)) {
            logI("Starting service...");
            mServiceIntent = new Intent(this, RequestService.class);
            mServiceIntent.setData(Uri.parse("startThread"));

            Server server = getServerFromRadioButtons();
            mServiceIntent.putExtra("server",server);
            this.startService(mServiceIntent);

            enableRadioButtons(false);
            buttonStartStop.setText(Constants.UI_BUTTON_STOP);
            logI("Starting service...");
        }
        else if(buttonStartStop.getText().equals(Constants.UI_BUTTON_STOP)) {
            logI("Stoping service...");
            mServiceIntent = new Intent(this, RequestService.class);
            mServiceIntent.setData(Uri.parse("stopThread"));
            this.stopService(mServiceIntent);

            enableRadioButtons(true);
            buttonStartStop.setText(Constants.UI_BUTTON_START);
            logI("Service has been stopped");
        }
        else{
            logE("Something weird just happened... :(");
        }
    }

    private Server getServerFromRadioButtons() {
        Server ret = Constants.SERVER_KS1 ;
        int id = 0;
        for(int i=0;i<radioGroup.getChildCount();i++){
            if(((RadioButton)radioGroup.getChildAt(i)).isChecked()){
                id = radioGroup.getChildAt(i).getId();
                break;
            }

        }
        if(id == radioButtonKS1.getId()) {
            ret = Constants.SERVER_KS1;
        }
        if(id == radioButtonKS2.getId()) {
            ret = Constants.SERVER_KS2;
        }
        if(id == radioButtonKS2SSD.getId()) {
            ret = Constants.SERVER_KS2SSD;
        }
        if(id == radioButtonKS3.getId()) {
            ret = Constants.SERVER_KS3;
        }
        if(id == radioButtonKS4.getId()) {
            ret = Constants.SERVER_KS4;
        }
        if(id == radioButtonKS5.getId()) {
            ret = Constants.SERVER_KS5;
        }
        if(id == radioButtonKS6.getId()) {
            ret = Constants.SERVER_KS6;
        }

        return ret;
    }

}
