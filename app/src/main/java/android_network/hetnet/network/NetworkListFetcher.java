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

import java.lang.reflect.Method;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import java.util.List;

import static android_network.hetnet.network.Constants.BROADCAST_ACTION;
import static android_network.hetnet.network.Constants.EXTENDED_DATA_STATUS;

public class NetworkListFetcher extends IntentService {

  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public NetworkListFetcher(String name) {
    super(name);
  }

  public NetworkListFetcher() {
    super("NetworkListFetcher");
  }

  WifiManager mainWifi;
  String mainText;
  TelephonyManager tm;

  @Override
  protected void onHandleIntent(Intent intent) {
    // Getting the WiFi Manager
    mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

    // Getting telephony manager for LTE
    tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


    // turning on LTE
    //Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

    //if (null != setMobileDataEnabledMethod)
    //{
      //setMobileDataEnabledMethod.invoke(tm, mobileDataEnabled);
    //}



    // If WiFi disabled then enable it
    if (!mainWifi.isWifiEnabled()) {
      System.out.println("Turning WiFi On");
      Toast.makeText(getApplicationContext(), "Turning WiFi On", Toast.LENGTH_LONG).show();
      mainWifi.setWifiEnabled(true);
    }

    // Initiate the network scan
    WifiReceiver receiverWifi = new WifiReceiver();
    registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    //mainWifi.startScan();

    // Sending the message back by broadcasting the Intent to receivers in this app.
    while (null == mainText) {
      ;
    }

    // trial
    List<CellInfo> cellInfoList= tm.getAllCellInfo();

    for (CellInfo cellInfo: cellInfoList)
    {
      if (cellInfo instanceof CellInfoLte)
      {
        mainText = cellInfo.toString();
      }
    }

    while (null == mainText) {
      ;
    }

    Intent localIntent = new Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS, mainText);
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
  }

  private class WifiReceiver extends BroadcastReceiver {
    // This method call when number of WiFi connections changed
    public void onReceive(Context c, Intent intent) {
      StringBuilder sb = new StringBuilder();
      List<ScanResult> wifiList = mainWifi.getScanResults();
      sb.append("\nNumber Of WiFi connections: ").append(wifiList.size()).append("\n\n");

      for (int i = 0; i < wifiList.size(); i++) {
        sb.append(Integer.valueOf(i + 1).toString()).append(". ");
        sb.append((wifiList.get(i)).toString());
        sb.append("\n\n");
      }

      System.out.println("String: " + String.valueOf(sb));
      mainText = String.valueOf(sb);
    }
  }
}
