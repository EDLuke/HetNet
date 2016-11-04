package android_network.hetnet.network;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.List;

import static android_network.hetnet.Constants.BROADCAST_ACTION;
import static android_network.hetnet.Constants.EXTENDED_DATA_STATUS;

public class NetworkListFetcher extends IntentService {
  public NetworkListFetcher(String name) {
    super(name);
  }

  public NetworkListFetcher() {
    super("NetworkListFetcher");
  }

  WifiManager wifiManager;
  StringBuilder mainText = new StringBuilder();
  TelephonyManager telephonyManager;

  @Override
  protected void onHandleIntent(Intent intent) {
    // Getting the WiFi Manager
    wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

    // Getting telephony manager for LTE
    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    // Trial for carrier cost
    // DON'T DELETE!!!
    String carrierName = telephonyManager.getNetworkOperatorName();

    String cost = "$0";

    if (carrierName.equals("Verizon")) {
      cost = "$1";
    }
    if (carrierName.equals("AT&T")){
      cost = "$2";
    }
    if (carrierName.equals("Sprint")){
      cost = "$3";
    }
    if (carrierName.equals("Tmobile")){
      cost = "$4";
    }

    System.out.println("Carrier Name: " + carrierName);
    System.out.println("Cost: " + cost);

    // END OF TRIAL

    // If WiFi disabled then enable it
    if (!wifiManager.isWifiEnabled()) {
      Toast.makeText(getApplicationContext(), "Turning WiFi On", Toast.LENGTH_LONG).show();
      wifiManager.setWifiEnabled(true);
    }

    // Initiate the network scan
    WifiReceiver receiverWifi = new WifiReceiver();
    registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    wifiManager.startScan();

    // Sending the message back by broadcasting the Intent to receivers in this app.
    while (String.valueOf(mainText).equals("")) {
      ;
    }

    // Get LTE info
    List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

    for (CellInfo cellInfo : cellInfoList) {
      if (cellInfo instanceof CellInfoLte) {
        mainText.append(cellInfo.toString());
      }
    }

    Intent localIntent = new Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS, String.valueOf(mainText));
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
  }

  private class WifiReceiver extends BroadcastReceiver {
    // This method is called when number of WiFi connections changed
    public void onReceive(Context c, Intent intent) {
      List<ScanResult> wifiList = wifiManager.getScanResults();
      mainText.append("\nNumber Of WiFi connections: ").append(wifiList.size()).append("\n\n");

      for (int i = 0; i < wifiList.size(); i++) {
        mainText.append(Integer.valueOf(i + 1).toString()).append(". ");
        mainText.append((wifiList.get(i)).toString());
        mainText.append("\n\n");
      }
    }
  }
}