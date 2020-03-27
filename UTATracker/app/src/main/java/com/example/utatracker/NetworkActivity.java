package com.example.utatracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        String str = "http://api.rideuta.com/SIRI/SIRI.svc/VehicleMonitor/ByRoute?route=2&onwardcalls=false&usertoken=UUB2O040NV0";
        new DownloadXmlTask().execute(str);

    }
    // Implementation of AsyncTask used to download XML feed from UTA
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {

                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //setContentView(R.layout.activity_network);
            // Displays the HTML string in the UI via a WebView
            //WebView myWebView = (WebView) findViewById(R.id.webview);
            //myWebView.loadData(result, "text/html", null);
        }
    }
    // Uploads XML from stackoverflow.com, parses it, and combines it with
// HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        BufferedReader stream = null;

        List<UTATraxXMLParser.MonitoredVehicleByRoute> entries = null;

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + "MonitoredVehiclebyRoute" + "</h3>");

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        //conn.setInstanceFollowRedirects(true);

        //Creates connection to UTA API
        try{
            conn.connect();
        }
        catch (IOException e){
            return e.toString();
    }



        //Gets input stream to be read from.
        try {

            //stream = conn.getInputStream();

            stream = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = stream.readLine()) != null) {
                total.append(line);
            }


            InputStream inputStream = new ByteArrayInputStream(total.toString().getBytes("UTF-8"));
            BufferedInputStream streamReader = new BufferedInputStream(inputStream);
            entries = UTATraxXMLParser.parse(streamReader);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {

            //if (stream != null) {
             //   stream.close();

           // }
            stream.close();
            conn.disconnect();

        }
        return htmlString.toString();
    }
}
