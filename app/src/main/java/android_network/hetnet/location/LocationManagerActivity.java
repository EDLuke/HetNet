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
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import android_network.hetnet.R;

public class LocationManagerActivity extends Activity {
  protected LocationManager locationManager;
  protected SensorManager sManager;
  TextView txtLat;
  TextView txtGyro;
  TextView txtAddress;

  LocationListener listener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
      txtLat = (TextView) findViewById(R.id.location_text);
      txtAddress = (TextView) findViewById(R.id.Parsed_Address);
      txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
      String latLong = location.getLatitude() + "," + location.getLongitude();
      new LocationParser(getApplicationContext(), txtAddress).execute(latLong);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
  };

  SensorEventListener mySensor = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {
      if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
        return;
      }
      //else it will output the Roll, Pitch and Yawn values
      txtGyro.setText("Orientation X (Roll) :" + Float.toString(event.values[2]) + "\n" +
        "Orientation Y (Pitch) :" + Float.toString(event.values[1]) + "\n" +
        "Orientation Z (Yaw) :" + Float.toString(event.values[0]));
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
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    sManager.registerListener(mySensor, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }

    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, listener);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, listener);
  }
}

