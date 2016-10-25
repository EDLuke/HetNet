package android_network.hetnet.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import android_network.hetnet.R;
import android_network.hetnet.system.adapter.RunningApplicationListAdapter;
import android_network.hetnet.system.monitor_threads.ActivityManagerThread;

public class SystemManager_Main extends Activity {
  /** Log Tag */
  private static final String TAG = "SystemManager";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_system_manager_main);

    MonitorManager_Main monitorManager_main = new MonitorManager_Main();

    ActivityManagerThread thread_am = new ActivityManagerThread(getApplicationContext(), (ListView)(findViewById(R.id.app_list)));

    monitorManager_main.insertNewThread(thread_am);

    monitorManager_main.startMonitor();
  }
}