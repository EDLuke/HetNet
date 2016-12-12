package android_network.hetnet.network;

import android.app.Activity;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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

  public static String getNetworkSpeed(Network network) {

    String speed = "N/A";

    return speed;


  }



}







