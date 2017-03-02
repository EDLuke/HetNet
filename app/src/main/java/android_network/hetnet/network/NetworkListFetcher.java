package android_network.hetnet.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android_network.hetnet.data.Network;

import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;

public class NetworkListFetcher extends IntentService {
  private final String LOG_TAG = "NETWORK_LIST_FETCHER";

  static WifiManager wifiManager;
  TelephonyManager telephonyManager;
  List<Network> networkList = new ArrayList<>();
  boolean wifiDataReceived = false;

  static long startT;
  static long endT;
  static long connectT;

  public NetworkListFetcher() {
    super("NetworkListFetcher");
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(Intent intent) {

    getWifiInfo();
    getLTEInfo();
    EventBus.getDefault().post(new NetworkResponseEvent(NETWORK_LIST_FETCHER, networkList, Calendar.getInstance().getTime()));
  }

  private void getLTEInfo() {
    // Getting telephony manager for LTE
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    String carrierName = telephonyManager.getNetworkOperatorName();

    List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

    // hypothetical value/connection type ie. 3G -> 1Gbps
    int speedMobile = telephonyManager.getNetworkType();

    int i = 1;
    for (CellInfo cellInfo : cellInfoList) {
      Network network = new Network();

      if (cellInfo instanceof CellInfoLte) {
        if (cellInfo.isRegistered()) {
          network.setNetworkSSID(carrierName);
        } else {
          network.setNetworkSSID("Other");
        }

        // signal strength
        network.setSignalStrength(((CellInfoLte) cellInfo).getCellSignalStrength().getLevel());

        // display max value
        network.setBandwidth(speedMobile);

        // TBD
        SecurityManager.checkNetworkConnectivity(network);

        // hardcoded lTE connection immediate
        network.setTimeToConnect(0);

        network.setCost(getCarrierCost(carrierName));

        // hardcoded LTE is not selected
        network.setCurrentNetwork(false);

        networkList.add(network);
      }
    }
  }

  @NonNull
  private Double getCarrierCost(String carrierName) {
    // skeleton code for cost calculation
    Double cost = 0.0;

    switch (carrierName) {
      case "Fi Network":
        cost = 0.5;
        break;
      case "Verizon":
        cost = 1.0;
        break;
      case "AT&T":
        cost = 2.0;
        break;
      case "Sprint":
        cost = 3.0;
        break;
      case "Tmobile":
        cost = 4.0;
        break;
    }
    return cost;
  }

  private void getWifiInfo() {
    wifiDataReceived = false;

    // Getting the WiFi Manager
    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


    startT = System.currentTimeMillis();
    // Initiate the network scan
    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    wifiManager.startScan();


    while (!wifiDataReceived) {
      ;
    }

    try {
      unregisterReceiver(wifiReceiver);
    } catch (IllegalArgumentException e) {
      Log.e(LOG_TAG, "Error unregistering receiver in getWifiInfo");
    }
  }

  private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
    // This method is called when number of WiFi connections changed
    public void onReceive(Context context, Intent intent) {

      List<ScanResult> wifiList = wifiManager.getScanResults();

      // gets maximum network speed
      double max_speed = wifiManager.getConnectionInfo().getLinkSpeed();


      int i = 0;
      for (ScanResult result : wifiList) {

        Network network = new Network();

        //hypothetical value
        network.setBandwidth(max_speed);



        //name
        network.setNetworkSSID(result.SSID);

        //WPA
        network.setSecurityProtocol(result.capabilities);

        //dB
        network.setSignalStrength(result.level);

        //Hz not useful
        network.setSignalFrequency(result.frequency);

        // TBD
        SecurityManager.checkNetworkConnectivity(network);


        // this class not used atm can be removed
        //NetworkAdditionalInfo.getTimeToConnect(network);


        network.setCost(0.0);

        // check if this is current connected network
        isCurrentNet(context, network);

        if (network.isCurrentNetwork()) {
          endT = System.currentTimeMillis();
          connectT = endT - startT;

          network.setTimeToConnect(connectT);
        }
        if (!network.isCurrentNetwork()) {
          network.setTimeToConnect(-1);
        }

        // check if network requires password
        password(network);

        // separate app for speed calculation so N/A right now
        network.setSpeed(NetworkAdditionalInfo.getNetworkSpeed(network));

        networkList.add(network);

        i++;
      }

      wifiDataReceived = true;
    }
  };

  public static void isCurrentNet(Context context, Network network) {
    // sets current network variable
    String current_SSID = "";

    // get current connected network
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (cm != null) {
      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

      // format: "current_SSID"
      current_SSID = activeNetwork.getExtraInfo();

      // check if network name matches current_SSID
      if (current_SSID.contains(network.getNetworkSSID())) {
        network.setCurrentNetwork(true);
        // if protocol is [WPA2-PSK..] and you know password
        network.setPossibleToConnect(true);
      } else {
        network.setCurrentNetwork(false);
      }
    }
  }

  public static void password(Network network) {
    //Checks if network is open (no password required)

    String protocol = network.getSecurityProtocol();

    // if password is needed protocol will be [WPA2-PSK....]
    if (protocol.contains("[WPA2]") || protocol.contains("[WEP]")) {
      network.setPossibleToConnect(true);

    }
  }

}