package com.example.utatracker;
import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import org.xmlpull.v1.XmlPullParserException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to make an AysncTask to retrieve an inputStream from UTA API
 */
public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_network);
        String str = "http://api.rideuta.com/SIRI/SIRI.svc/VehicleMonitor/ByRoute?route=703&onwardcalls=false&usertoken=UUB2O040NV0";
        new DownloadXmlTask().execute(str);

    }

    /**
     * Implementation of AsyncTask used to download XML feed from UTA.
     * Input String: URL
     * Output List: Returns a list of the requested API information
     */
    private class DownloadXmlTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... urls) {
            List<UTATraxXMLParser.MonitoredVehicleByRoute> toReturn = new ArrayList();
            try {
                toReturn = loadXmlFromNetwork(urls[0]);
                return toReturn;
            } catch (IOException e) {
                return toReturn;

            } catch (XmlPullParserException e) {
                return toReturn;
            }
        }

        @Override
        protected void onPostExecute(List result) {

        }

    }

    /**
     * Makes a URLConnection to the desired UTA API, retrieving the inputStream.
     * Uses a StringBuilder to store all the information from the inputStream.
     * Turns built string into a BufferedInputStream that can be read from
     * the UTATraxXMLParser class.
     * @param urlString
     * @return List
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
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

        //Creates connection to UTA API
        try{
            conn.connect();
        }
        catch (IOException e){
        }
        //Gets input stream to be read from.
        try {
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
            stream.close();
            conn.disconnect();
        }
        return entries;
    }
}
