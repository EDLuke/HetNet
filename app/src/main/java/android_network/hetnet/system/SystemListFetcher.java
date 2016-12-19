package android_network.hetnet.system;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.BatteryStats;
import android.os.Debug.MemoryInfo;
import android.util.Log;
import android.util.SparseArray;

import com.android.internal.os.BatteryStatsImpl;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android_network.hetnet.common.Constants.SYSTEM_LIST_FETCHER;

/**
 * SystemListFetcher
 * Fetch current SystemList and post it to policy engine
 */

public class SystemListFetcher extends IntentService {
  private static boolean inProcess = false;

  private static final String LOG_TAG = "SystemListFetcher";

  //Current snapshot of all the running application processes
  List<ActivityManager.RunningAppProcessInfo> m_runningAppProcessInfos;

  private SystemList m_systemList;
  private ActivityManager m_activityManager;
  private PackageManager m_packageManager;

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
    m_packageManager = getPackageManager();
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
    getBatteryStats();
    getMemoryStats();
    getTrafficStats();
    getWakeLocks();

    //Set the name last
    getLabelName();

    m_systemList.setApplicationList(m_applicationListMap);
    EventBus.getDefault().post(new SystemResponseEvent(SYSTEM_LIST_FETCHER, m_systemList, Calendar.getInstance().getTime()));
  }

  private void initializeApplicationList() {
    for (ActivityManager.RunningAppProcessInfo processInfo : m_runningAppProcessInfos) {
      ApplicationList initialInfo = new ApplicationList();
      initialInfo.setProcessName(processInfo.processName);

      m_applicationListMap.put(processInfo.uid, initialInfo);

      if (m_last_applicationListMap.get(processInfo.uid) == null)
        m_last_applicationListMap.put(processInfo.uid, initialInfo);
    }
  }

  private void getBatteryStats() {
    /*Uid*/
    HashMap<Integer, Double> powerMap_uid = new HashMap<>();

    /*For components*/
    HashMap<String, Double> powerMap_components = new HashMap<>();

    double  powerSum = 0.0;

    Process p;
    try {
      String[] cmd = {
        "sh",
        "-c",
        "dumpsys batterystats"};
      p = Runtime.getRuntime().exec(cmd);
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;

      boolean parsePower       = false;

      /*Read from Estimated power use:
      * to
      * All kernel wake locks
      * mAh values then percentage values */
      while ((line = reader.readLine()) != null) {
        Log.v(LOG_TAG, line);
        //Skip the next line (empty)
        reader.readLine();

        if (line.contains("Estimated power use") || line.startsWith("Capacity") /*&& parseLastCharged*/ /*&& !parsePower*/) {
          parsePower = true;
          continue;
        } else if ((line.contains("All kernel wake locks") || line.trim().split(":").length != 2) && parsePower)
          parsePower = false;

        if (!parsePower)
          continue;
        else {
          String lineOutput[] = line.trim().split(":");
          if (lineOutput.length == 2) {
            double value = Double.parseDouble(lineOutput[1].trim());

            if(!lineOutput[0].equals("Unaccounted")) //Don't sum the Unaccounted battery usage
              powerSum += value;

            if (lineOutput[0].contains("Uid")) { //UID
              int uid = -1;
              if (lineOutput[0].contains("u0a")) {
                uid = 10000 + Integer.parseInt(lineOutput[0].substring(7));
              } else {
                uid = Integer.parseInt(lineOutput[0].substring(4));
              }

              powerMap_uid.put(uid, value);
            } else { //Component
              String component = lineOutput[0];

              powerMap_components.put(component, value);
            }
          }
        }

      }

    } catch (IOException e) {
      Log.e(LOG_TAG, "Error reading process\n" + e.toString());
    }

    Iterator<Map.Entry<Integer, Double>> powerMap_uid_it = powerMap_uid.entrySet().iterator();

    while (powerMap_uid_it.hasNext()) {
      Map.Entry<Integer, Double> pair = powerMap_uid_it.next();

      ApplicationList list = m_applicationListMap.get(pair.getKey());

      if (list == null) {
        Log.v(LOG_TAG, "UID :" + pair.getKey() + " is not present");
        continue;
      }

      list.setBatteryMah(pair.getValue());
      list.setBatteryPercent(pair.getValue() / powerSum);
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

      if(m_applicationListMap.containsKey(processInfo.uid)) {
        m_applicationListMap.get(processInfo.uid).setPrivateClean(memoryStats[0]);
        m_applicationListMap.get(processInfo.uid).setPrivateDirty(memoryStats[1]);
        m_applicationListMap.get(processInfo.uid).setPss(memoryStats[2]);
        m_applicationListMap.get(processInfo.uid).setUss(memoryStats[3]);
      }
      else{
        Log.e(LOG_TAG, "Missing process " + processInfo.processName + "\t" + processInfo.uid);
      }
    }
  }

  /**
   * @param pid Process id
   * @return int[]
   * TotalPrivateClean
   * TotalPrivateDirty
   * TotalPss (Proportional Set Size)
   * TotalUss (Unique Set Size)
   */
  private int[] getMemoryStats(int pid) {
    int pids[] = new int[1];
    pids[0] = pid;
    MemoryInfo[] memoryInfo = m_activityManager.getProcessMemoryInfo(pids);

    int[] ret = new int[4];
    ret[0] = memoryInfo[0].getTotalPrivateClean();
    ret[1] = memoryInfo[0].getTotalPrivateDirty();
    ret[2] = memoryInfo[0].getTotalPss();
    ret[3] = ret[0] + ret[1];

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

  private void getLabelName(){
    Iterator<Map.Entry<Integer, ApplicationList>> m_applicationListMap_it = m_applicationListMap.entrySet().iterator();

    while (m_applicationListMap_it.hasNext()) {
      Map.Entry<Integer, ApplicationList> pair = m_applicationListMap_it.next();

      String app_name = pair.getValue().getProcessName();
      try {
        app_name = m_packageManager.getApplicationLabel(m_packageManager.getApplicationInfo(m_packageManager.getNameForUid(pair.getKey()), 0)).toString();
      } catch (PackageManager.NameNotFoundException e) {

      }

      pair.getValue().setProcessName(app_name);
    }
  }

  private void getWakeLocks(){
    Process p;
    HashMap<String, int[]> ret = new HashMap<>();
    try {
      String[] cmd = {
              "sh",
              "-c",
              "dumpsys power"};
      p = Runtime.getRuntime().exec(cmd);
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = reader.readLine();

      boolean parseLock = false;
      int wakelockCount = -1;
      int wakelockIndex = 0;

      while ((line = reader.readLine()) != null) {
        Log.v(LOG_TAG, line);
        if(!parseLock && line.contains("Wake Locks:")){
          parseLock = true;
          wakelockCount = Integer.parseInt(line.substring(line.indexOf("size=") + ("size=").length()));
          continue;
        }

        if(parseLock && wakelockCount > 0){
          try {
            int uid = Integer.parseInt(line.substring(line.indexOf("uid=") + ("uid=").length(), line.indexOf(",")));
            if(m_applicationListMap.containsKey(uid))
              m_applicationListMap.get(uid).addWakeLockCount();
            wakelockIndex++;

            if (wakelockIndex >= wakelockCount) {
              parseLock = false; //OK to return prematurely here until we need more data from 'dumpsys power'
              return;
            }
          }catch(StringIndexOutOfBoundsException e){
            Log.e(LOG_TAG, "Error parse wakelock at line: " + line);
          }
        }

      }

    } catch (IOException e) {
      Log.e(LOG_TAG, "Error reading process\n" + e.toString());
    }
  }

  private void getCpuUsage() {
    //Total
    m_systemList.setCpuUsage(cpuUsage());

    //Application-wise
    getCpuUsageApplication();
  }

  private void getCpuUsageApplication() {
    //Parse cpu usage from 'top -n 1'
    //Process name / [0]:UID [1]:Percent
    HashMap<String, int[]> cpuUsage_app = cpuUsageApplication();

    Iterator<Map.Entry<String, int[]>> cpuUsage_app_it = cpuUsage_app.entrySet().iterator();

    while(cpuUsage_app_it.hasNext()){
      Map.Entry<String, int[]> pair = cpuUsage_app_it.next();

      int uid     = (pair.getValue())[0];
      int percent = (pair.getValue())[1];
      String processName = pair.getKey().equals("") ? m_applicationListMap.get(uid).getProcessName() : pair.getKey();

      if (m_applicationListMap.containsKey(uid)) { //Update CPU usage for existing entry
        int currentPercent = m_applicationListMap.get(uid).getCpuUsage();
        m_applicationListMap.get(uid).setCpuUsage(currentPercent + percent);

        //Also set the name here
        String app_name = "";
        try {
          app_name = m_packageManager.getApplicationLabel(m_packageManager.getApplicationInfo(m_packageManager.getNameForUid(uid), 0)).toString();
        } catch (PackageManager.NameNotFoundException e) {
          app_name = processName;
        }

        m_applicationListMap.get(uid).setProcessName(app_name);
      } else { //Add new entry
        ApplicationList list = new ApplicationList();
        list.setCpuUsage(percent);

        //Also set the name here
        String app_name = "";
        try {
          app_name = m_packageManager.getApplicationLabel(m_packageManager.getApplicationInfo(m_packageManager.getNameForUid(uid), 0)).toString();
        } catch (PackageManager.NameNotFoundException e) {
          app_name = processName;
        }

        list.setProcessName(app_name);

        m_applicationListMap.put(uid, list);
      }
    }
  }

  private HashMap<String, int[]> cpuUsageApplication() {
    BatteryStatsImpl batteryStatsImpl = new BatteryStatsImpl();

    Process p;
    HashMap<String, int[]> ret = new HashMap<>();
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
        int pid = Integer.parseInt(lineOutput[0]);
        int uid = -1;
        switch(lineOutput[8]){
          case "system":
            uid = 1000;
            break;
          case "radio":
            uid = 1001;
            break;
          case "log":
            uid = 1007;
            break;
          case "wifi":
            uid = 1010;
            break;
          case "media":
            uid = 1013;
            break;
          case "sdcard_rw":
            uid = 1015;
            break;
          case "gps":
            uid = 1021;
            break;
          case "nfc":
            uid = 1027;
            break;
          case "root":
            uid = 0;
            break;
          case "shell":
            uid = 2000;
            break;
        }

        if(uid == -1){
          if(lineOutput[8].contains("u0_a")){
            uid = 10000 + Integer.parseInt(lineOutput[8].substring(4));
          }
          else{
            Log.v(LOG_TAG, "Unable to parse " + line);
          }
        }

        //uid / percent
        int[] value = new int[2];
        value[0] = uid;
        value[1] = percent;

        ret.put(processName, value);
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
