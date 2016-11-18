package android_network.hetnet.system;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android_network.hetnet.common.Constants.SYSTEM_EVENT_TRACKER;

/**
 * SystemEventTracker
 * Polling activity manager continuously to detect changes in foreground activity
 */

public class SystemEventTracker extends Service {
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
        @Override
        public void run(){
            ActivityManager.RunningTaskInfo foregroundTaskInfo = m_activityManager.getRunningTasks(1).get(0);
            final String newName = foregroundTaskInfo.topActivity.getClassName();

            if(!(newName.equals(m_currentName)))
                EventBus.getDefault().post(new SystemTriggerEvent(SYSTEM_EVENT_TRACKER, "Foreground Activity Changed", Calendar.getInstance().getTime()));
        }
    }
}
