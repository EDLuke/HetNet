package android_network.hetnet.system;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import android_network.hetnet.R;
import android_network.hetnet.system.monitor_threads.ActivityManagerThread;
import android_network.hetnet.system.monitor_threads.DevicePowerThread;

public class SystemManagerActivity extends Activity {
  /**
   * Log Tag
   */
  private static final String TAG = "SystemManager";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_system_manager);

    MonitorManager monitorManager = new MonitorManager();

    //TODO: Migrate to Service
    //Intent m_service = new Intent(this, MonitorService.class);
    //this.startService(m_service);

    ActivityManagerThread thread_am = new ActivityManagerThread(getApplicationContext(), (ListView) (findViewById(R.id.listview_ps)));
    DevicePowerThread powerThread = new DevicePowerThread(getApplicationContext(), (TextView) (findViewById(R.id.textview_devicepower)));
    monitorManager.insertNewThread(thread_am);
    monitorManager.insertNewThread(powerThread);

    monitorManager.startMonitor();
  }
}