package android_network.hetnet.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by lanking on 10/11/2016.
 */

public class LocationFetcher extends AsyncTask<Void, Void, Void> {
    private Context ContextAsync;
    String LatLgn = "";
    String Disabled = "False";
    protected LocationManager locationManager;
    public AsyncResponse sender = null;

    public interface AsyncResponse{
        void processFinish(String output);
    }

    public LocationFetcher(Context context, AsyncResponse sender) {
        this.ContextAsync = context;
        this.sender = sender;
    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLgn = location.getLongitude() + "," + location.getLatitude();
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
    protected void onPreExecute() {
        super.onPreExecute();
        locationManager = (LocationManager) ContextAsync.getSystemService(ContextAsync.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ContextAsync, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContextAsync,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Disabled = "True";
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

    }
    @Override
    protected void onCancelled(){
        System.out.println("Cancelled by user!");
        if (ActivityCompat.checkSelfPermission(ContextAsync, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ContextAsync,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Disabled = "True";
            return;}
        locationManager.removeUpdates(listener);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(LatLgn.equals("")){};
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
        sender.processFinish(LatLgn);
    }
}
