package android_network.hetnet.system;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;
import android.os.Debug.MemoryInfo;

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

public class SystemListFetcher extends IntentService {

  private static final String LOG_TAG = "SystemListFetcher";

  //Current snapshot of all the running application processes
  List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

  private SystemList m_systemList;
  private ActivityManager m_activityManager;
  private PackageManager  m_packageManager;

  private static long lastTotalRxBytes;
  private static long lastTotalTxBytes;
  private static long lastTotalRxPackets;
  private static long lastTotalTxPackets;

  private long currentTotalRxBytes;
  private long currentTotalTxBytes;
  private long currentTotalRxPackets;
  private long currentTotalTxPackets;

  //UID / Application List
  private static HashMap<Integer, ApplicationList> m_last_applicationListMap;
  private HashMap<Integer, ApplicationList> m_applicationListMap;

  public SystemListFetcher() {
    super("SystemListFetcher");
  }

  @Override
  public void onCreate() {
    super.onCreate();

    m_activityManager = (ActivityManager) (getSystemService(ACTIVITY_SERVICE));
    m_packageManager  = getPackageManager();
    m_runningAppProcessInfos = m_activityManager.getRunningAppProcesses();

    if (m_last_applicationListMap == null)
      m_last_applicationListMap = new HashMap<>();

    m_applicationListMap = new HashMap<>();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //Initialize a new SystemList
    m_systemList = new SystemList();

    //First update the snapshot of all running application processes
    m_runningAppProcessInfos = m_activityManager.getRunningAppProcesses();

    initializeApplicationList();

    getCpuUsage();
    //getBatteryStats(); //TODO:Finish deciphering batterystats
    getMemoryStats();
    getTrafficStats();

    m_systemList.setApplicationList(m_applicationListMap);
    EventBus.getDefault().post(new SystemResponseEvent(SYSTEM_LIST_FETCHER, m_systemList, Calendar.getInstance().getTime()));
  }

  private void initializeApplicationList() {
    for (ActivityManager.RunningAppProcessInfo processInfo : m_runningAppProcessInfos) {
      m_applicationListMap.put(processInfo.uid, new ApplicationList());

      if (m_last_applicationListMap.get(processInfo.uid) == null)
        m_last_applicationListMap.put(processInfo.uid, new ApplicationList());
    }
  }

  private void getBatteryStats(){
    //TODO:Multi-thread battery info
    Process p;
    try {
      String[] cmd = {
              "sh",
              "-c",
              "dumpsys batterystats --checkin"};
      p = Runtime.getRuntime().exec(cmd);
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;

      while ((line = reader.readLine()) != null && !line.equals("")) {
        String lineOutput[] = line.trim().split(",");

        if (lineOutput[3].equals("br")){
          int debug = 1;
        }

      }

    } catch (IOException e) {
      Log.e(LOG_TAG, "Error reading process\n" + e.toString());
    }
  }

  //Source:
  //http://stackoverflow.com/questions/12765562/how-to-get-the-correct-number-of-bytes-sent-and-received-in-trafficstats
  private void getTrafficStats() {
    long rxBytes = TrafficStats.getTotalRxBytes();
    long txBytes = TrafficStats.getTotalTxBytes();
    long rxPackets = TrafficStats.getTotalRxPackets();
    long txPackets = TrafficStats.getTotalTxPackets();

    if (rxBytes == TrafficStats.UNSUPPORTED || txBytes == TrafficStats.UNSUPPORTED) {
      Log.e(LOG_TAG, "Your device does not support traffic stat monitoring");
      return;
    }

    //Total
    //Use lastTotals for now TODO: either store lastTotals and monitor or change to service
//    currentTotalRxBytes = rxBytes - lastTotalRxBytes;
//    currentTotalTxBytes = txBytes - lastTotalTxBytes;
//    currentTotalRxPackets = rxPackets - lastTotalRxPackets;
//    currentTotalTxPackets = txPackets - lastTotalTxPackets;

    currentTotalRxBytes = rxBytes;
    currentTotalTxBytes = txBytes;
    currentTotalRxPackets = rxPackets;
    currentTotalTxPackets = txPackets;

    lastTotalRxBytes = rxBytes;
    lastTotalTxBytes = txBytes;
    lastTotalRxPackets = rxPackets;
    lastTotalTxPackets = txPackets;

    //Application wise
    for (ActivityManager.RunningAppProcessInfo processInfo : m_runningAppProcessInfos) {
      getTrafficStats(processInfo.uid);
    }
  }


  private void getMemoryStats() {
    for (ActivityManager.RunningAppProcessInfo processInfo : m_runningAppProcessInfos) {
      int[] memoryStats = getMemoryStats(processInfo.pid);

      m_applicationListMap.get(processInfo.uid).setPrivateClean(memoryStats[0]);
      m_applicationListMap.get(processInfo.uid).setPrivateDirty(memoryStats[1]);
      m_applicationListMap.get(processInfo.uid).setPss(memoryStats[2]);
      m_applicationListMap.get(processInfo.uid).setUss(memoryStats[3]);
    }
  }

  /**
   * @param pid Process id
   * @return int[]
   *    TotalPrivateClean
   *    TotalPrivateDirty
   *    TotalPss (Proportional Set Size)
   *    TotalUss (Unique Set Size)
   */
  private int[] getMemoryStats(int pid){
    int pids[] = new int[1];
    pids[0] = pid;
    MemoryInfo[] memoryInfo = m_activityManager.getProcessMemoryInfo(pids);

    int[] ret = new int[4];
    ret[0] = memoryInfo[0].getTotalPrivateClean();
    ret[1] = memoryInfo[0].getTotalPrivateDirty();
    ret[2] = memoryInfo[0].getTotalPss();
    //Set USS to 0 for now
    ret[3] = 0; /*memoryInfo[0].getTotalUss();*/

    return ret;
  }

  private void getTrafficStats(int uid) {

    if (m_applicationListMap.containsKey(uid) && m_last_applicationListMap.containsKey(uid)) {

      //Already checked if supported in getTrafficStats(TRACK_STATE state)
      long rxBytes = TrafficStats.getUidRxBytes(uid);
      long txBytes = TrafficStats.getUidTxBytes(uid);
      long rxPackets = TrafficStats.getUidRxPackets(uid);
      long txPackets = TrafficStats.getUidTxPackets(uid);

      //Total
      //Use lastTotals for now TODO: either store lastTotals and monitor or change to service
      m_applicationListMap.get(uid).setRxBytes(rxBytes /*- m_last_applicationListMap.get(uid).getRxBytes()*/);
      m_applicationListMap.get(uid).setTxBytes(txBytes /*- m_last_applicationListMap.get(uid).getTxBytes()*/);
      m_applicationListMap.get(uid).setRxPackets(rxPackets /*- m_last_applicationListMap.get(uid).getRxPackets()*/);
      m_applicationListMap.get(uid).setTxPackets(txPackets /*- m_last_applicationListMap.get(uid).getTxPackets()*/);

      m_last_applicationListMap.get(uid).setRxBytes(rxBytes);
      m_last_applicationListMap.get(uid).setTxBytes(txBytes);
      m_last_applicationListMap.get(uid).setRxPackets(rxPackets);
      m_last_applicationListMap.get(uid).setRxPackets(txPackets);

    }
  }

  private void getCpuUsage() {
    //Total
    m_systemList.setCpuUsage(cpuUsage());

    //Application-wise
    getCpuUsageApplication();
  }

  //TODO:this is some late night hack, please change this
  private void getCpuUsageApplication() {
    //Parse cpu usage from 'top -n 1'
    HashMap<String, Integer> cpuUsage_app = cpuUsageApplication();

    for (ActivityManager.RunningAppProcessInfo processInfo : m_runningAppProcessInfos) {
      int percent = cpuUsage_app.containsKey(processInfo.processName) ? cpuUsage_app.get(processInfo.processName) : -1;

      if (m_applicationListMap.containsKey(processInfo.uid)) {
        /*Only retain the maximum cpu usage*/
        int currentPercent = m_applicationListMap.get(processInfo.uid).getCpuUsage();
        m_applicationListMap.get(processInfo.uid).setCpuUsage(currentPercent + percent);

        //Also set the name here
        String app_name = "";
        try{
          app_name = m_packageManager.getApplicationLabel(m_packageManager.getApplicationInfo(m_packageManager.getNameForUid(processInfo.uid), 0)).toString();
        }catch (PackageManager.NameNotFoundException e){
          app_name = processInfo.processName;
        }

        m_applicationListMap.get(processInfo.uid).setProcessName(app_name);
      } else {
        ApplicationList list = new ApplicationList();
        list.setCpuUsage(percent);
        list.setProcessName(processInfo.processName);

        m_applicationListMap.put(processInfo.uid, list);
      }
    }

  }

  private HashMap<String, Integer> cpuUsageApplication() {
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

      while ((line = reader.readLine()) != null && !line.equals("")) {
        String lineOutput[] = line.trim().split("\\s+");

        if (lineOutput.length <= 9)
          continue;

        int percent = Integer.parseInt(lineOutput[2].substring(0, lineOutput[2].length() - 1));
        String processName = lineOutput[9];
        ret.put(processName, percent);
      }

    } catch (IOException e) {
      Log.e(LOG_TAG, "Error reading process\n" + e.toString());
    }

    return ret;
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
