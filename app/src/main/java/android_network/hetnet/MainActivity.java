package android_network.hetnet;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
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

import static android_network.hetnet.common.Constants.LOCATION_LIST_FETCHER;
import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;
import static android_network.hetnet.common.Constants.SYSTEM_LIST_FETCHER;

public class MainActivity extends Activity {
  private static final String LOG_TAG = "MainActivity";

  private static final int REQUEST_READ_PHONE_STATE = 100;
  private static final int REQUEST_ACCESS_COARSE_LOCATION = 101;
  private static final int REQUEST_ACCESS_NETWORK_STATE = 102;

  private String m_event_log;
  private SystemList m_system_list;
  /*TODO: Create NetworkList and LocationList*/
  private NetworkList m_network_list;
  /*private LocationList m_location_list;*/

  DrawerLayout        drawerLayout;
  NavigationView      navigationView;
  FragmentManager     fragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    navigationView = (NavigationView) findViewById(R.id.navigation_view);

    fragmentManager = getFragmentManager();
    FragmentTransaction firstTransaction = fragmentManager.beginTransaction();
    firstTransaction.replace(R.id.containerView, new PolicyEngineFragment()).commit();

    /* Setup onclick event for Navigation View Items */
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
      @Override
      public boolean onNavigationItemSelected(MenuItem menuItem){
        drawerLayout.closeDrawers();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (menuItem.getItemId()){
          case R.id.nav_item_policy:
            fragmentTransaction.replace(R.id.containerView, PolicyEngineFragment.newInstance(m_event_log)).commit();
            break;
          case R.id.nav_item_system:
            fragmentTransaction.replace(R.id.containerView, new SystemManagerFragment()).commit();
            break;
          case R.id.nav_item_network:
            fragmentTransaction.replace(R.id.containerView, NetworkManagerFragment.newInstance(m_network_list)).commit();
            break;
          case R.id.nav_item_location:
            fragmentTransaction.replace(R.id.containerView, new LocationManagerFragment()).commit();
            break;
          default:
            Log.e(LOG_TAG, "Wrong navigation menu item id: " + menuItem.getItemId());
        }
        return false;
      }
    });
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
        m_network_list = (NetworkList)(event.getEventList());
        break;
      case SYSTEM_LIST_FETCHER:
        m_system_list = (SystemList)(event.getEventList());
        break;
      case LOCATION_LIST_FETCHER:
        break;
      default:
        Log.e(LOG_TAG, "Wrong event from: " + event.getEventOriginator());
    }
  }
}
