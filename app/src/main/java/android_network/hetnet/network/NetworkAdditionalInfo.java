package android_network.hetnet.network;

import android.app.Activity;
import android.net.TrafficStats;

import com.google.android.gms.common.api.GoogleApiClient;

import android_network.hetnet.data.Network;

public class NetworkAdditionalInfo extends Activity {


  private static final String LOG_TAG_2 = "NETWORK_ADDITIONAL_INFO";
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;


  public static double getTimeToConnect(Network network) {
    return 1.0;

  }

  public static double getNetworkSpeed(Network network) {

    double rate;

    if (network.isPossibleToConnect()) {
      //ConnectionClassManager cq = ConnectionClassManager.getInstance();
      //rate = cq.getDownloadKBitsPerSecond();

      long mStartRX = TrafficStats.getTotalRxBytes();
      long mStartTX = TrafficStats.getTotalTxBytes();

      rate = mStartTX - mStartTX;


    } else {
      rate = 1;
    }

    return rate;


  }


}







