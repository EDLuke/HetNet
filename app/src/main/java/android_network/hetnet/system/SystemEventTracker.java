package android_network.hetnet.system;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;

/**
 * SystemEventTracker
 * Polling activity manager continuously to detect changes in foreground activity
 */

public class SystemEventTracker extends Service {
    private static final String LOG_TAG = "SystemEventTracker";

    private ScheduledThreadPoolExecutor m_scheduledThreadPoolExecutor;
    private ActivityManager             m_activityManager;
    private Handler                     m_handler;
    private String                      m_currentName;

    @Override
    public IBinder onBind(Intent intent){
        return null; //Not using this
    }

    @Override
    public void onCreate(){
        super.onCreate();

        m_currentName = "";
        m_handler = new Handler();
        m_activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        m_scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        m_scheduledThreadPoolExecutor.scheduleAtFixedRate(new ForegroundPoll(), 0, 2, TimeUnit.SECONDS);

    }

    private class ForegroundPoll implements Runnable{
      private final Integer PROCESS_STATE_TOP = 2;

      @TargetApi(Build.VERSION_CODES.LOLLIPOP)
      private String getForegroundLollipop(){
        String ret = "";
        UsageStatsManager usm = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);

        if (appList != null && appList.size() > 0) {
          SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
          for (UsageStats usageStats : appList) {
            mySortedMap.put(usageStats.getLastTimeUsed(),
                    usageStats);
          }
          if (mySortedMap != null && !mySortedMap.isEmpty()) {
            ret = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
          }
        }

        return ret;
      }

      private String getForeground() {
        String ret = "";
        ActivityManager am = (ActivityManager) getBaseContext().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
          if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                  processInfo.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
            Integer state = null;
            try{
              state = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState").getInt(processInfo);
            } catch (Exception e) {

            }
            if(state != null && state == PROCESS_STATE_TOP) {
              ret = processInfo.processName;
            }
          }
        }

        return ret;
      }

      @Override
      public void run(){
        String newName = "";

        //Source:
        //http://stackoverflow.com/questions/33581311/android-m-how-can-i-get-the-current-foreground-activity-package-namefrom-a-ser
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
          newName = getForegroundLollipop();

          //Special cases for certain phones
          //Source:
          //http://stackoverflow.com/questions/32135903/devices-without-apps-using-usage-data-or-android-settings-usage-access-setting
          if(newName.equals(""))
            newName = getForeground();
        }
        else
          newName = getForeground();

        //Fire the event and show in log
        if(!(newName.equals(m_currentName)) && !(newName.equals(""))) {
            EventBus.getDefault().post(new SystemTriggerEvent(SYSTEM_EVENT_TRACKER, "Foreground Activity Changed", Calendar.getInstance().getTime()));
            m_currentName = new String(newName);

            Log.v(LOG_TAG, "Foreground application has been switched to " + newName);
        }
      }
    }
}
