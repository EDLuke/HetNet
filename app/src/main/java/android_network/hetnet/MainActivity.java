package android_network.hetnet;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.common.trigger_events.TriggerEvent;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.location.LocationManagerFragment;
import android_network.hetnet.network.NetworkList;
import android_network.hetnet.network.NetworkManagerFragment;
import android_network.hetnet.policy_engine.PolicyEngine;
import android_network.hetnet.policy_engine.PolicyEngineFragment;
import android_network.hetnet.system.SystemList;
import android_network.hetnet.system.SystemManagerFragment;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;
import android_network.hetnet.ui.TabFragment.TabFragment;

import static android_network.hetnet.common.Constants.LOCATION_LIST_FETCHER;
import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;
import static android_network.hetnet.common.Constants.SYSTEM_LIST_FETCHER;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
  private static final String LOG_TAG = "MainActivity";

  private static final int REQUEST_READ_PHONE_STATE = 100;
  private static final int REQUEST_ACCESS_COARSE_LOCATION = 101;
  private static final int REQUEST_ACCESS_NETWORK_STATE = 102;

  private String m_event_log;

  FragmentManager     fragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fragmentManager = getSupportFragmentManager();
    FragmentTransaction firstTransaction = fragmentManager.beginTransaction();
    firstTransaction.replace(R.id.containerView, new TabFragment()).commit();

    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

    //register to event bus
    EventBus.getDefault().register(this);

    Intent policyEngineService = new Intent(this, PolicyEngine.class);
    this.startService(policyEngineService);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case REQUEST_READ_PHONE_STATE: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

          if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
          }
        }
      }
      case REQUEST_ACCESS_COARSE_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

          if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_ACCESS_NETWORK_STATE);
          }
        }
      }
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(TriggerEvent event) {
    m_event_log += (event.toString() + "\t");
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UITriggerEvent event) {
    switch (event.getEventOriginator()){
      case NETWORK_LIST_FETCHER:
        break;
      case SYSTEM_LIST_FETCHER:
        break;
      case LOCATION_LIST_FETCHER:
        break;
      default:
        Log.e(LOG_TAG, "Wrong event from: " + event.getEventOriginator());
    }
  }

  @Override
  public void onFragmentInteraction(Uri uri){
  }
}
