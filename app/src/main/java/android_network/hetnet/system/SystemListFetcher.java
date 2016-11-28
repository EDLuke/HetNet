package android_network.hetnet.system;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android_network.hetnet.common.Constants.SYSTEM_LIST_FETCHER;

/**
 * SystemListFetcher
 * Fetch current SystemList and post it to policy engine
 */

public class SystemListFetcher extends IntentService{
    private static final String LOG_TAG = "SystemListFetcher";

    private SystemList m_systemList;
    private ActivityManager m_activityManager;

    public SystemListFetcher(){ super("SystemListFetcher"); }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent){
        m_activityManager = (ActivityManager)(getSystemService(ACTIVITY_SERVICE));
        m_systemList = new SystemList();

        getRunningProcess();
        getCpuUsage();
        getCpuUsageApplication();
        getDevicePower();
        EventBus.getDefault().post(new SystemResponseEvent(SYSTEM_LIST_FETCHER, m_systemList, Calendar.getInstance().getTime()));
    }

    private void getRunningProcess(){
        m_systemList.setRunningAppProcessInfos(m_activityManager.getRunningAppProcesses());
    }

    private void getCpuUsage(){
        m_systemList.setCpuUsage(cpuUsage());
    }

    //TODO:this is some late night hack, please change this
    private void getCpuUsageApplication(){
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = m_systemList.getRunningAppProcessInfos();
        HashMap<String, Integer> cpuUsage_app = new HashMap<>();
        HashMap<String, Integer> cpuUsage_app_temp = cpuUsageApplication();

        for(ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos){
            int percent = cpuUsage_app_temp.containsKey(processInfo.processName) ? cpuUsage_app_temp.get(processInfo.processName) : 0;
            cpuUsage_app.put(processInfo.processName, percent);
        }

        m_systemList.setCpuUsage_app(cpuUsage_app);
    }

    private HashMap<String, Integer> cpuUsageApplication(){
        Process p;
        HashMap<String, Integer> ret = new HashMap<>();
        try {
            String[] cmd = {
                    "sh",
                    "-c",
                    "top -n 1"};
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
          line = reader.readLine();
          line = reader.readLine();
          line = reader.readLine();
          line = reader.readLine();
          line = reader.readLine();
          line = reader.readLine();

          while ((line = reader.readLine()) != null && !line.equals("")){
            String lineOutput[] = line.trim().split("\\s+");

            if(lineOutput.length <= 9)
              continue;

            int percent = Integer.parseInt(lineOutput[2].substring(0, lineOutput[2].length() - 1));
            String processName = lineOutput[9];
            ret.put(processName, percent);
          }

        }catch(IOException e){
            Log.e(LOG_TAG, "Error reading process\n" + e.toString());
        }

        return ret;
    }

    private void getDevicePower(){
        //TODO: finish migrating this from DevicePowerThread
        m_systemList.setBatteryPct(0);
    }

    //AU: Gabe
    private float cpuUsage() {
        try {
            /* Proc/stat CPU lines has this format
             * See https://supportcenter.checkpoint.com/supportcenter/portal?eventSubmit_doGoviewsolutiondetails=&solutionid=sk65143
             * The meanings of the columns are as follows, from left to right:
             * 1st column : user = normal processes executing in user mode
             * 2nd column : nice = niced processes executing in user mode
             * 3rd column : system = processes executing in kernel mode
             * 4th column : idle = twiddling thumbs
             * 5th column : iowait = waiting for I/O to complete
             * 6th column : irq = servicing interrupts
             * 7th column : softirq = servicing softirqs
             */

            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            // Split on one or more spaces
            String[] toks = load.split(" +");

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);


            // Go to the next line and parse data for a second CPU if there
            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            // CPU Usage Percentage is CPU usage cycles / Total CPU cycles (idles and processing)
            return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}
