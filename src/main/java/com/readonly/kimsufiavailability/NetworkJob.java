package com.readonly.kimsufiavailability;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Telephony;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrateur on 24/09/2015.
 */
class NetworkJob extends AsyncTask<String, Void, String> {

    private KimsufiAvailableChecker kimsufiAvailableChecker;

    public NetworkJob(KimsufiAvailableChecker kimsufiAvailableChecker) {
        this.kimsufiAvailableChecker = kimsufiAvailableChecker;
    }

    @Override
    protected String doInBackground(String[] params) {
        String res = "";
        try {
            res = loadFromNetwork(params[0]);
        } catch (IOException e) {
            kimsufiAvailableChecker.addLog(e.getMessage());
        }
        return res;
    }

    //Write to the default sms app
    private void WriteSms(String message, String phoneNumber) {

        //Put content values
        ContentValues values = new ContentValues();
        values.put(Telephony.Sms.ADDRESS, phoneNumber);
        values.put(Telephony.Sms.DATE, System.currentTimeMillis());
        values.put(Telephony.Sms.BODY, message);

        //Insert the message
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            kimsufiAvailableChecker.getContentResolver().insert(Telephony.Sms.Sent.CONTENT_URI, values);
        }
        else {
            kimsufiAvailableChecker.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
        }

        //Change my sms app to the last default sms
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, Telephony.Sms.getDefaultSmsPackage(kimsufiAvailableChecker));
        kimsufiAvailableChecker.startActivity(intent);
    }

    @Override
    protected void onPostExecute(String message) {

        String msg = "";
        String availability = "";
        String zone = "";

        try {
            JSONObject jObject = new JSONObject(message);
            availability = jObject.getJSONObject("answer").getJSONArray("availability").getJSONObject(174).getJSONArray("metaZones").getJSONObject(1).getString("availability").toString();
            zone = jObject.getJSONObject("answer").getJSONArray("availability").getJSONObject(174).getJSONArray("metaZones").getJSONObject(1).getString("zone").toString();
            msg = jObject.getJSONObject("answer").getJSONArray("availability").getJSONObject(174).getJSONArray("metaZones").toString();

            //TODO : implement if/else condition for sending sms
            //Write to the default sms app

            //Get the package name and check if my app is not the default sms app
            final String myPackageName = "com.readonly.kimsufiavailability;";
            if (!Telephony.Sms.getDefaultSmsPackage(kimsufiAvailableChecker).equals(myPackageName)) {

                //Change the default sms app to my app
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                kimsufiAvailableChecker.startActivityForResult(intent, 1);
            }

            WriteSms("Hello you !", "066606980");

        } catch (JSONException e) {
            msg = "Error while parsing JSON";
        }

        kimsufiAvailableChecker.addLog(msg);
    }


    /**
     * Initiates the fetch operation.
     */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str = "";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream, 500);
        } catch (Exception ex) {
            kimsufiAvailableChecker.addLog(ex.getMessage());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     *
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }


    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        String ret = "";
        String line = null;
        while ((line = reader.readLine()) != null) {
            ret += line;
        }


        return ret;
    }
}
