package android_network.hetnet.cloud;

import android.app.IntentService;
import android.content.Intent;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import org.json.*;


import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.Network;

/**
 * Created by lanking on 06/12/2016.
 */

public class SendCloud extends IntentService {

  public SendCloud() {
    super("SendCloud");
  }

  public SendCloud(String name) {
    super(name);
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    ArrayList<DataStoreObject> dataStoreObjectList = (ArrayList) intent.getSerializableExtra("currentData");
    DataStoreObject tempdata = dataStoreObjectList.get(0);
    System.out.println(dataStoreObjectList);

    HttpURLConnection httpcon;
    Map<String, Object> params = new HashMap<>();
    params.put("ApplicationID", tempdata.getApplicationID());
    params.put("ApplicationType",tempdata.getApplicationType());
    params.put("Latitude",tempdata.getLatitude());
    params.put("Longtitude",tempdata.getLongitude());
    JSONObject holder = new JSONObject(params);
    JSONArray networks = new JSONArray();
    for(Network network : tempdata.getListOfNetworks()){
      Map<String, Object> temp = new HashMap<>();
      temp.put("Bandwidth",network.getBandwidth());
      temp.put("Cost",network.getCost());
      temp.put("NetworkSSID",network.getNetworkSSID());
      temp.put("SecurityProtocol",network.getSecurityProtocol());
      temp.put("SignalFrequency",network.getSignalFrequency());
      temp.put("TimeToConnect",network.getTimeToConnect());
      temp.put("SignalStrength",network.getSignalStrength());
      networks.put(temp);
    }
    try {
      holder.put("Networks",networks);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    System.out.println(holder.toString());
    /*
    String url = "http://35.162.120.177/policy";
    String data = holder.toString();
    String result = null;
    try {
      //Connect
      httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
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
      BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

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
    */
  }
}


