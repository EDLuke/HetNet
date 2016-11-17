package android_network.hetnet.location;

/**
 * Created by lanking on 21/10/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android_network.hetnet.R;

public class LocationManagerActivity extends Activity {
  protected LocationManager locationManager;
  protected SensorManager sManager;
  String address = "Please Waiting";
  String latLong = "";
  TextView txtLat;
  TextView txtGyro;
  TextView txtAddress;



  SensorEventListener mySensor = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
      if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
        return;
      }
      //else it will output the Roll, Pitch and Yawn values
      txtGyro.setText("Speed X (Roll) :" + Float.toString(event.values[2]) + "\n" +
        "Speed Y (Pitch) :" + Float.toString(event.values[1]) + "\n" +
        "Speed Z (Yaw) :" + Float.toString(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location_manager);

    txtLat = (TextView) findViewById(R.id.location_text);
    txtGyro = (TextView) findViewById(R.id.gyroscope);
    sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    sManager.registerListener(mySensor, sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);

    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        new LocationFetcher(LocationManagerActivity.this, new LocationFetcher.AsyncResponse() {
          @Override
          public void processFinish(String output) {
            txtLat = (TextView) findViewById(R.id.location_text);
            txtLat.setText("Lat,Long:" + output);
            latLong = output;
          }
        }).execute();
      }},3000);

    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        new LocationParser(new LocationParser.AsyncResponse() {
          @Override
          public void processFinish(String output) {
            address = output;
            txtAddress = (TextView) findViewById(R.id.parsed_address);
            txtAddress.setText("Current Address: " + address);
          }
        }).execute(latLong);
      }
    }, 5000);

  }
}

