package android_network.hetnet.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;



/**
 * Created by gabe on 10/22/16.
 */

import android.app.ActivityManager.RunningAppProcessInfo;

import android_network.hetnet.R;

public class ProcessDetailActivity extends Activity {

//    TextView mCPUUsage;
    RunningAppProcessInfo mProc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ActivityManager mgr = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);


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

        Log.e("memory","     total private dirty memory (KB): " + MI[0].getTotalPrivateDirty());
        Log.e("memory","     total shared (KB): " + MI[0].getTotalSharedDirty());
        Log.e("memory","     total pss: " + MI[0].getTotalPss());

        }



    }