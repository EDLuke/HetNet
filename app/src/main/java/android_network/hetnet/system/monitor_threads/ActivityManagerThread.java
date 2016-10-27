package android_network.hetnet.system.monitor_threads;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
    RunningApplicationListAdapter m_adapter;

    public ActivityManagerThread(Context context, ListView ui_element){
      m_ui_element  = ui_element;
      m_context     = context;

      m_adapter = new RunningApplicationListAdapter(m_context, m_runningAppProcessInfos);

      m_ui_element.setAdapter(m_adapter);
    }

    public void run() {

      m_ui_element.post(new Runnable(){
        @Override
        public void run(){
          ActivityManager am = (ActivityManager)(m_context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE));

          m_runningAppProcessInfos = am.getRunningAppProcesses();
          m_adapter = new RunningApplicationListAdapter(m_context, m_runningAppProcessInfos);

          //TODO: Use notifyDataSetChanged instead of recreating the adapter every time
          //TODO: Better TODO: get rid of this and use either AsyncTask or Service
          //m_adapter.notifyDataSetChanged();
          m_ui_element.setAdapter(m_adapter);

        }

      });

    }
}

