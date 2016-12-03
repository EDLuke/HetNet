package android_network.hetnet.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import android_network.hetnet.data.Network;

import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;

public class NetworkListFetcher extends IntentService {
  WifiManager wifiManager;
  TelephonyManager telephonyManager;
  List<Network> networkList = new ArrayList<>();
  boolean wifiDataReceived = false;

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
    int i = 1;
    for (CellInfo cellInfo : cellInfoList) {
      Network network = new Network();
      if (cellInfo instanceof CellInfoLte) {
        if (cellInfo.isRegistered()) {
          network.setNetworkSSID(carrierName);
        } else {
          network.setNetworkSSID("Other");
        }
        network.setSignalStrength(((CellInfoLte) cellInfo).getCellSignalStrength().getLevel());
        NetworkBandwidthCalculator.getNetworkBandwidth(network);
        SecurityManager.checkNetworkConnectivity(network);
        NetworkBandwidthCalculator.getTimeToConnect(network);
        network.setCost(getCarrierCost(carrierName));
        network.setCurrentNetwork(false);
        networkList.add(network);
      }
    }
  }

  @NonNull
  private Double getCarrierCost(String carrierName) {
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
    // Getting the WiFi Manager
    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

    // Initiate the network scan
    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    wifiManager.startScan();

    while (!wifiDataReceived) {
      ;
    }

    unregisterReceiver(wifiReceiver);
  }

  private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
    // This method is called when number of WiFi connections changed
    public void onReceive(Context context, Intent intent) {
      List<ScanResult> wifiList = wifiManager.getScanResults();
      wifiDataReceived = true;
      int i = 0;
      for (ScanResult result : wifiList) {
        Network network = new Network();
        network.setNetworkSSID(result.SSID);
        network.setSecurityProtocol(result.capabilities);
        network.setSignalStrength(result.level);
        network.setSignalFrequency(result.frequency);
        NetworkBandwidthCalculator.getNetworkBandwidth(network);
        SecurityManager.checkNetworkConnectivity(network);
        NetworkBandwidthCalculator.getTimeToConnect(network);
        network.setCost(0.0);
        if (i == 0) network.setCurrentNetwork(true);
        else network.setCurrentNetwork(false);
        networkList.add(network);
        i++;
      }
    }
  };
}