package android_network.hetnet.system.system_old.monitor_threads;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.RandomAccessFile;

import android_network.hetnet.system.system_old.event.CPUUsageEvent;


/**
 * Created by gabe on 10/23/16.
 */

public class CPUUsageThread extends Thread {

  private final String m_threadName = "CPU_USAGE_THREAD";
  private Context mcontext;
//    TextView mUIElement;
//    String pid;
//    public CPUUsageThread(Context context, TextView ui_element_cpu) {
//        mUIElement = ui_element_cpu;
//    }

  public CPUUsageThread(Context context) {
    this.mcontext = context;
  }


  public void run() {
    //Post CPU usage percentage to event bus
    EventBus.getDefault().post(new CPUUsageEvent(m_threadName, "SUCCESS", cpuUsage()));
  }

  private float cpuUsage() {
    try {
//            Proc/stat CPU lines has this format
//            See https://supportcenter.checkpoint.com/supportcenter/portal?eventSubmit_doGoviewsolutiondetails=&solutionid=sk65143
//            The meanings of the columns are as follows, from left to right:
//            1st column : user = normal processes executing in user mode
//            2nd column : nice = niced processes executing in user mode
//            3rd column : system = processes executing in kernel mode
//            4th column : idle = twiddling thumbs
//            5th column : iowait = waiting for I/O to complete
//            6th column : irq = servicing interrupts
//            7th column : softirq = servicing softirqs
      RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
      String load = reader.readLine();
      // Split on one or more spaces
      String[] toks = load.split(" +");

      long idle1 = Long.parseLong(toks[4]);
      long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
        + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

      try {
        Thread.sleep(360);
      } catch (Exception e) {
      }

//            Go to the next line and parse data for a second CPU if there
      reader.seek(0);
      load = reader.readLine();
      reader.close();

      toks = load.split(" +");

      long idle2 = Long.parseLong(toks[4]);
      long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
        + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
//          CPU Usage Percentage is CPU usage cycles / Total CPU cycles (idles and processing)
      return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return 0;
  }


}


