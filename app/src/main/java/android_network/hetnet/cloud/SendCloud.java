package android_network.hetnet.cloud;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android_network.hetnet.data.Application;
import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.Network;
import android_network.hetnet.system.ApplicationList;
import android_network.hetnet.system.SystemList;

public class SendCloud extends IntentService {
  private String PreUrl= "http://35.162.120.177";
  private String Email="test@test.com";

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
    Date curr= Calendar.getInstance().getTime();
    try {
      LocationPoster(tempdata.getLongitude(), tempdata.getLatitude(), curr, Email);
      NetworkPoster(tempdata.getListOfNetworks(), curr, Email);
      SystemPoster(tempdata.getSystemList(), curr, Email);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void LocationPoster(double Longtitude, double Latitude, Date current, String Email){
    Map<String, Object> temp = new HashMap<>();
    temp.put("Time", current.toString());
    temp.put("Latitude", Latitude);
    temp.put("Longtitude", Longtitude);
    temp.put("Email", Email);
    JSONObject holder = new JSONObject(temp);
    try {
      CloudPoster(PreUrl+"/location", holder.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void NetworkPoster(List<Network> networks, Date current, String Email){
    Map<String, Object> temp = new HashMap<>();
    temp.put("Time", current.toString());
    temp.put("Email", Email);
    JSONArray passednetwork = new JSONArray();
    Set<String> networkClean = new HashSet<>();
    for(Network net : networks){
      if(!networkClean.contains(net.getNetworkSSID())){
        networkClean.add(net.getNetworkSSID());
        Map<String, Object> tempnet = new HashMap<>();
        tempnet.put("Bandwidth", net.getBandwidth());
        tempnet.put("Cost", net.getCost());
        tempnet.put("NetworkSSID", net.getNetworkSSID());
        tempnet.put("SecurityProtocol", net.getSecurityProtocol());
        tempnet.put("SignalFrequency", net.getSignalFrequency());
        tempnet.put("TimeToConnect", net.getTimeToConnect());
        tempnet.put("SignalStrength", net.getSignalStrength());
        passednetwork.put(new JSONObject(tempnet));
      }
    }
    temp.put("Networks", passednetwork);
    try {
      CloudPoster(PreUrl+"/network", temp.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void SystemPoster(SystemList systems, Date current, String Email){
    Map<String, Object> holder = new HashMap<>();
    holder.put("Time", current.toString());
    holder.put("Email", Email);
    JSONArray applications = new JSONArray();
    Map<Integer, ApplicationList> temp = systems.getApplicationList();
    for(ApplicationList app : temp.values()){
      Map<String, Object> sys = new HashMap<>();
      sys.put("ProcessName",app.getProcessName());
      sys.put("CpuUsage", app.getCpuUsage());
      sys.put("RxBytes",app.getRxBytes());
      sys.put("TxBytes",app.getTxBytes());
      sys.put("PrivateClean",app.getPrivateClean());
      sys.put("BatteryPercent",app.getBatteryPercent());
      sys.put("Uss",app.getUss());
      sys.put("Pss",app.getPss());
      applications.put(new JSONObject(sys));
    }
    holder.put("Applications", applications);
    try {
      CloudPoster(PreUrl+"/system", holder.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void CloudPoster(String url, String data) throws Exception {
    System.out.println("\nUrl: "+url+"\nData: "+data+"\n");
    /*
      HttpURLConnection httpcon;
      String result = null;
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
      */
  }
}


