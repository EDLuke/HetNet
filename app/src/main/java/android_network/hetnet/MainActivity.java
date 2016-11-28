package android_network.hetnet;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.policy_engine.PolicyEngine;

import static android_network.hetnet.common.Constants.LOCATION_EVENT_TRACKER;
import static android_network.hetnet.common.Constants.NETWORK_EVENT_TRACKER;
import static android_network.hetnet.common.Constants.POLICY_ENGINE;
import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;
import static android_network.hetnet.common.Constants.SYSTEM_LIST_FETCHER;

public class MainActivity extends Activity {
  private static final int REQUEST_READ_PHONE_STATE = 100;
  private static final int REQUEST_ACCESS_COARSE_LOCATION = 101;
  private static final int REQUEST_ACCESS_NETWORK_STATE = 102;

  TextView eventList;
  TextView networkList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    eventList = (TextView) findViewById(R.id.event_list);
    networkList = (TextView) findViewById(R.id.network_list);

    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

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
  public void onMessageEvent(UITriggerEvent event) {
    if (event.getEventOriginator().equals(NETWORK_EVENT_TRACKER) || event.getEventOriginator().equals(LOCATION_EVENT_TRACKER)) {
      eventList.setText(event.getEventName() + " event received from " + event.getEventOriginator() + " at " + event.getTimeOfEvent());
      networkList.setText("");
    } else if (event.getEventOriginator().equals(POLICY_ENGINE)) {
      eventList.setText("Data received from " + event.getEventOriginator() + " at " + event.getTimeOfEvent());
      networkList.setText(event.getEventName());
    } else if (event.getEventOriginator().equals(SYSTEM_LIST_FETCHER) || event.getEventOriginator().equals(SYSTEM_EVENT_TRACKER)) {
      NotificationCompat.Builder mBuilder =
              new NotificationCompat.Builder(this)
                    /*.setSmallIcon(R.drawable.notification_icon)*/
                      .setContentTitle("My notification")
                      .setContentText("Hello World!");
      // Creates an explicit intent for an Activity in your app
      Intent resultIntent = new Intent(this, MainActivity.class);

      // The stack builder object will contain an artificial back stack for the
      // started Activity.
      // This ensures that navigating backward from the Activity leads out of
      // your application to the Home screen.
      TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
      // Adds the back stack for the Intent (but not the Intent itself)
      stackBuilder.addParentStack(MainActivity.class);
      // Adds the Intent that starts the Activity to the top of the stack
      stackBuilder.addNextIntent(resultIntent);
      PendingIntent resultPendingIntent =
              stackBuilder.getPendingIntent(
                      0,
                      PendingIntent.FLAG_UPDATE_CURRENT
              );
      mBuilder.setContentIntent(resultPendingIntent);
      NotificationManager mNotificationManager =
              (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      // mId allows you to update the notification later on.
      mNotificationManager.notify(0, mBuilder.build());
    }
  }
}
