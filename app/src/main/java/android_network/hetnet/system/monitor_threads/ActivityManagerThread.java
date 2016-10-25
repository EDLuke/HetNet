package android_network.hetnet.system.monitor_threads;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Iterator;
import java.util.List;

import android_network.hetnet.R;
import android_network.hetnet.system.adapter.RunningApplicationListAdapter;

/**
 * Activity Manager Thread
 */
public class ActivityManagerThread extends Thread{
    ListView m_ui_element;
    Context  m_context;

    List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

    public ActivityManagerThread(Context context, ListView ui_element){
      m_ui_element  = ui_element;
      m_context     = context;
    }

    public void run() {
      ActivityManager am = (ActivityManager)(m_context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE));

      m_runningAppProcessInfos = am.getRunningAppProcesses();

      final RunningApplicationListAdapter adapter = new RunningApplicationListAdapter(m_context, m_runningAppProcessInfos);

      m_ui_element.setAdapter(adapter);
    }
}

