package android_network.hetnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android_network.hetnet.location.LocationManagerActivity;
import android_network.hetnet.network.NetworkManagerActivity;
import android_network.hetnet.system.SystemManagerActivity;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
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

}
