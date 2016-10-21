package android_network.hetnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android_network.hetnet.network.NetworkManager_Main;
import android_network.hetnet.location.LocationManager_Main;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  /**
   * Creates System Manager Activity
   */
  public void startSystemManager(View view) {
    Intent intent = new Intent(this, SystemManager_Main.class);
    startActivity(intent);
  }

  /**
   * Creates Network Manager Activity
   */
  public void startNetworkManager(View view) {
    Intent intent = new Intent(this, NetworkManager_Main.class);
    startActivity(intent);
  }
  /**
   * Creates Location Manager Activity
   */
  public void startLocationManager(View view) {
    Intent intent = new Intent(this, LocationManager_Main.class);
    startActivity(intent);
  }

}
