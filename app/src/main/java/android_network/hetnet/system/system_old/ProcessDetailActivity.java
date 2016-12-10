package android_network.hetnet.system.system_old;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.RandomAccessFile;

import android_network.hetnet.R;


public class ProcessDetailActivity extends Activity {

    //    TextView mCPUUsage;
    RunningAppProcessInfo mProc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register to the eventbus

        setContentView(R.layout.activity_system_manager_process_detail);
        Bundle bundle = getIntent().getExtras();
        mProc = bundle.getParcelable("PROC");
        TextView pname = (TextView) findViewById(R.id.package_name_textview);

        TextView pid = (TextView) findViewById(R.id.pid_textview);
        TextView dPrivate = (TextView) findViewById(R.id.delvik_private_val);
        TextView dShared = (TextView) findViewById(R.id.delvik_shared_val);
        TextView dPss = (TextView) findViewById(R.id.delvik_pss_val);
        TextView nPrivate = (TextView) findViewById(R.id.native_private_val);
        TextView nShared = (TextView) findViewById(R.id.native_shared_val);
        TextView nPss = (TextView) findViewById(R.id.native_pss_val);
        TextView totalMem = (TextView) findViewById(R.id.total_mem_val);
        Context context = this.getApplicationContext();
        ActivityManager mgr = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);


        int[] pids = new int[1];
        pids[0] = mProc.pid;
        android.os.Debug.MemoryInfo[] MI = mgr.getProcessMemoryInfo(pids);

        pname.setText(mProc.processName);
        pid.setText("PID: " + Integer.toString(mProc.pid));
        dPrivate.setText(Integer.toString(MI[0].dalvikPrivateDirty) + " Kbs");
        dShared.setText(Integer.toString(MI[0].dalvikSharedDirty) + " Kbs");
        dPss.setText(Integer.toString(MI[0].dalvikPss) + " Kbs");
        nPrivate.setText(Integer.toString(MI[0].nativePrivateDirty) + " Kbs");
        nShared.setText(Integer.toString(MI[0].nativeSharedDirty) + " Kbs");
        nPss.setText(Integer.toString(MI[0].nativePss) + " Kbs");

        totalMem.setText(Integer.toString(MI[0].getTotalPrivateDirty()) + " Kbs");

        Log.e("memory", "     total private dirty memory (KB): " + MI[0].getTotalPrivateDirty());
        Log.e("memory", "     total shared (KB): " + MI[0].getTotalSharedDirty());
        Log.e("memory", "     total pss: " + MI[0].getTotalPss());


        Log.d("CPU Usage Percentage ", Float.toString(cpuUsage()));
    }


    public float cpuUsage() {
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


    @Override
    protected void onStop() {
        //Unregister from the event bus
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}