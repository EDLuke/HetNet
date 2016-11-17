package android_network.hetnet.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import android_network.hetnet.R;

import static android_network.hetnet.Constants.BROADCAST_ACTION;
import static android_network.hetnet.Constants.EXTENDED_DATA_STATUS;

public class NetworkManagerActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network_manager);

    Intent mServiceIntent = new Intent(this, NetworkListFetcher.class);
    this.startService(mServiceIntent);

    IntentFilter mStatusIntentFilter = new IntentFilter(BROADCAST_ACTION);
    // Instantiates a new DownloadStateReceiver
    NetworkManagerActivity.DownloadStateReceiver mDownloadStateReceiver = new NetworkManagerActivity.DownloadStateReceiver();
    // Registers the DownloadStateReceiver and its intent filters
    LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, mStatusIntentFilter);
  }

  private class DownloadStateReceiver extends BroadcastReceiver {
    // Prevents instantiation
    private DownloadStateReceiver() {
    }

    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    public void onReceive(Context context, Intent intent) {
      String result = intent.getStringExtra(EXTENDED_DATA_STATUS);
      TextView networkList = (TextView) findViewById(R.id.networkList);

      System.out.println(result);
      if (networkList != null) {
        networkList.setText(result);
      }
    }
  }
}