package com.readonly.kimsufiavailability;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;


public class KimsufiAvailableChecker extends Activity {

    private TextView tV = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kimsufi_available_checker);

        tV = (TextView) findViewById(R.id.textViewOutput);
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
    }


    public void emptyLog(){
        tV.setText("");
    }







    public void buttonOnClick(View v) {
        addLog("Starting test");

        SendfeedBackJob job = new SendfeedBackJob();
        job.execute("https://ws.ovh.com/dedicated/r2/ws.dispatcher/getAvailability2");
    }

    public void buttonResetOnClick(View v) {
        emptyLog();
    }






    private class SendfeedBackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            String res = "";
            try {
                res = loadFromNetwork(params[0]);
            } catch (IOException e) {
                addLog(e.getMessage());
            }
            return res;
        }

        @Override
        protected void onPostExecute(String message) {

            String msg = "";
            try {
                JSONObject jObject = new JSONObject(message);
                msg = jObject.getJSONObject("answer").getJSONArray("availability").getJSONObject(174).getJSONArray("metaZones").getJSONObject(1).toString();


            } catch (JSONException e) {
                msg = "Error while parsing JSON";
            }

            addLog(msg);
        }







        /** Initiates the fetch operation. */
        private String loadFromNetwork(String urlString) throws IOException {
            InputStream stream = null;
            String str ="";

            try {
                stream = downloadUrl(urlString);
                str = readIt(stream, 500);
            }catch(Exception ex) {
                addLog(ex.getMessage());
            }
            finally
            {
                if (stream != null) {
                    stream.close();
                }
            }
            return str;
        }

        /**
         * Given a string representation of a URL, sets up a connection and gets
         * an input stream.
         * @param urlString A string representation of a URL.
         * @return An InputStream retrieved from a successful HttpURLConnection.
         * @throws java.io.IOException
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
            while((line = reader.readLine()) != null) {
                ret += line;
            }


            return ret;
        }
    }

}
