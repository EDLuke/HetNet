package android_network.hetnet.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import static android_network.hetnet.common.Constants.LOCATION_EVENT_TRACKER;

public class LocationEventTracker extends Service {
  LocationManager locationManager;
  private Context context;
  LocationListener listener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
      System.out.println("Inside onLocationChanged");
      EventBus.getDefault().post(new LocationTriggerEvent(LOCATION_EVENT_TRACKER, "Location Changed", Calendar.getInstance().getTime()));
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

  @Override
  public void onCreate() {
    context = getApplicationContext();
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
      || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      System.out.println("Location Service Started");
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, listener);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, listener);
    }
    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
