package android_network.hetnet;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.common.trigger_events.TriggerEvent;
import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.PolicyEngineData;
import android_network.hetnet.data.PolicyVector;
import android_network.hetnet.policy_engine.PolicyEngine;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;
import android_network.hetnet.ui.TabFragment.TabFragment;

import static android_network.hetnet.common.Constants.LOCATION_EVENT_TRACKER;
import static android_network.hetnet.common.Constants.NETWORK_EVENT_TRACKER;
import static android_network.hetnet.common.Constants.POLICY_ENGINE;
import static android_network.hetnet.common.Constants.POLICY_VECTOR;
import static android_network.hetnet.common.Constants.STATE_VECTOR;
import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {
  private static final String LOG_TAG = "MainActivity";

  private static final int REQUEST_READ_PHONE_STATE = 100;
  private static final int REQUEST_ACCESS_COARSE_LOCATION = 101;
  private static final int REQUEST_ACCESS_NETWORK_STATE = 102;
  private static final int REQUEST_DUMP = 103;

  private String m_event_log;

  //UI elements
  private TextView m_eventList;
  private TextView m_policyRuleVector;
  private TextView m_currentStateVector;

  FragmentManager fragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    m_eventList = (TextView) findViewById(R.id.event_list);
    m_policyRuleVector = (TextView) findViewById(R.id.policy_rule_vector);
    m_currentStateVector = (TextView) findViewById(R.id.current_state_vector);

    fragmentManager = getSupportFragmentManager();
    FragmentTransaction firstTransaction = fragmentManager.beginTransaction();
    firstTransaction.replace(R.id.containerView, new TabFragment()).commit();

    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
    }

    permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.DUMP);

    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.DUMP}, REQUEST_DUMP);
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

      case REQUEST_DUMP: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          //Request granted
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
    //TODO:change this to log policy vector
    switch (event.getEventOriginator()) {
      case NETWORK_EVENT_TRACKER:
      case LOCATION_EVENT_TRACKER:
        m_eventList.setText(event.getEvent() + " event received from " + event.getEventOriginator() + " at " + event.getTimeOfEvent());
        break;
      case POLICY_ENGINE:
        PolicyVector ruleVector = ((PolicyEngineData) (event.getEvent())).getRuleVector();
        PolicyVector currentStateVector = ((PolicyEngineData) (event.getEvent())).getCurrentStateVector();
        String ruleVectorString = getVectorAsString(ruleVector, POLICY_VECTOR);
        String currentStateVectorString = getVectorAsString(currentStateVector, STATE_VECTOR);

        m_policyRuleVector.setText(ruleVectorString);
        m_currentStateVector.setText(currentStateVectorString);
        break;
      case SYSTEM_EVENT_TRACKER:
        NotificationCompat.Builder mBuilder =
          new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle("HetNet")
            .setContentText(event.getEvent().toString());

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
        break;
      default:
        Log.e(LOG_TAG, "Wrong event from: " + event.getEventOriginator());
    }
  }

  private String getVectorAsString(PolicyVector data, String format) {
    StringBuilder sb = new StringBuilder();
    if (POLICY_VECTOR.equals(format)) {
      sb.append("\nPolicy Rule Vector: <");
    } else {
      sb.append("\nCurrent State Vector: <");
    }

    sb.append("App ID: ").append(data.getApplicationID()).append(", ");
    sb.append("App Type: ").append(data.getApplicationType()).append(", ");
    sb.append("Latitude: ").append(data.getLatitude()).append(", ");
    sb.append("Longitude: ").append(data.getLongitude()).append(", ");
    sb.append("Network ID: ").append(data.getNetworkSSID()).append(", ");

    if (POLICY_VECTOR.equals(format)) {
      sb.append("Bandwidth Preference: ").append(data.getBandwidth()).append(", ");
      sb.append("Signal Strength Preference: ").append(data.getSignalStrength()).append(", ");
      sb.append("Connection Time Preference: ").append(data.getTimeToConnect()).append(", ");
      sb.append("Cost Preference: ").append(data.getCost());
    } else {
      sb.append("Bandwidth Rank: ").append(data.getBandwidth()).append(", ");
      sb.append("Signal Strength Rank: ").append(data.getSignalStrength()).append(", ");
      sb.append("Connection Time Rank: ").append(data.getTimeToConnect()).append(", ");
      sb.append("Cost Rank: ").append(data.getCost());
    }

    sb.append(">");

    return String.valueOf(sb);
  }

  @Override
  public void onFragmentInteraction(Uri uri) {
  }
}
