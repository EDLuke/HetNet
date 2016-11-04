package android_network.hetnet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import android_network.hetnet.location.LocationManagerActivity;
import android_network.hetnet.network.NetworkListFetcher;
import android_network.hetnet.network.NetworkManagerActivity;
import android_network.hetnet.system.SystemManagerActivity;

import static android_network.hetnet.Constants.BROADCAST_ACTION;

public class MainActivity extends Activity {

  private static final int REQUEST_READ_PHONE_STATE = 100;
  private static final int REQUEST_ACCESS_COARSE_LOCATION = 101;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

  }

  /**
   * Creates System Manager Activity
   */
  public void startSystemManager(View view) {
    Intent intent = new Intent(this, SystemManagerActivity.class);
    startActivity(intent);
  }

  /**
   * Creates Network Manager Activity
   */
  public void startNetworkManager(View view) {
    Intent intent = new Intent(this, NetworkManagerActivity.class);
    startActivity(intent);
  }

  /**
   * Creates Location Manager Activity
   */
  public void startLocationManager(View view) {
    Intent intent = new Intent(this, LocationManagerActivity.class);
    startActivity(intent);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case REQUEST_READ_PHONE_STATE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

          if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_READ_PHONE_STATE);
          }
        }
      }
    }
  }
}
