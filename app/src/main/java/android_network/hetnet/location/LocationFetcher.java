package android_network.hetnet.location;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import static android_network.hetnet.common.Constants.LOCATION_LIST_FETCHER;

public class LocationFetcher extends IntentService {
  public LocationFetcher() {
    super("LocationFetcher");
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    Context context = getApplicationContext();
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
      || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      EventBus.getDefault().post(new LocationResponseEvent(LOCATION_LIST_FETCHER, location.getLatitude() + ", " + location.getLongitude(), Calendar.getInstance().getTime()));
    }
  }
}
