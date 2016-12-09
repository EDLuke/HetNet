package android_network.hetnet.cloud;


import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import org.json.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by lanking on 06/12/2016.
 */

public class sendCloud extends IntentService {

    public sendCloud(){super("sendCloud");}

    public sendCloud(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HttpURLConnection httpcon;
        Map<String,Object> params = new HashMap<>();
        params.put("applicationId", "test1234567");
        params.put("Location", "70.496,-40.2");
        params.put("networkSSID", "COLUMBIA U SECURE");
        params.put("bandwidth", 2.4);
        params.put("signalStrength", 32);
        params.put("signalFrequency", 100);
        params.put("timeToConnect", 10);
        params.put("cost", 20);
        JSONObject holder = new JSONObject(params);
        System.out.println(holder.toString());
        String url = "http://35.162.120.177/policy";
        String data = holder.toString();
        String result = null;
        try {
            //Connect
            httpcon = (HttpURLConnection) ((new URL (url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            System.out.println(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


