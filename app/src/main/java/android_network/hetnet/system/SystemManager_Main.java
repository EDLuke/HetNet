package android_network.hetnet.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import android_network.hetnet.R;
import android_network.hetnet.system.monitor_threads.ActivityManagerThread;

public class SystemManager_Main extends Activity {
  /** Log Tag */
  private static final String TAG = "SystemManager";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_system_manager_main);

    MonitorManager_Main monitorManager_main = new MonitorManager_Main();

    //TODO: Migrate to Service
    //Intent m_service = new Intent(this, MonitorService.class);
    //this.startService(m_service);

    ActivityManagerThread thread_am = new ActivityManagerThread(getApplicationContext(), (ListView)(findViewById(R.id.listview_ps)));

    monitorManager_main.insertNewThread(thread_am);

    monitorManager_main.startMonitor();
  }
}