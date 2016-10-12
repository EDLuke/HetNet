package android_network.hetnet;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SystemManager_Main extends AppCompatActivity {
    /** Log Tag */
    private static final String TAG = "SystemManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_manager__main);


        startProcessProfiling();
    }

    /** From http://stackoverflow.com/questions/9756353/how-to-get-memory-usage-and-cpu-usage-by-application */

    /**
     *
     */
    private void startProcessProfiling(){
        Log.v(TAG, "Started process profiling");
        ProcessProfileThread processProfileThread = new ProcessProfileThread(this);
        processProfileThread.start();
    }

    /**
     * Currently we clear the table and re-populate it every sec
     * Need to find a more elegant way to 'update' the info
     *  */
    private class ProcessProfileThread extends Thread{
        ActivityManager m_am;
        TableLayout     m_process_tbl;
        Context         m_context;

        public ProcessProfileThread(Context context){
            m_am            = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
            m_process_tbl   = (TableLayout)findViewById(R.id.tbl_process_info);
            m_context       = context;
        }

        public void run() {
            while(true) {
                /** Clear the children except for the first row */
                m_process_tbl.post(new Runnable() {
                    public void run() {
                        m_process_tbl.removeViews(1, m_process_tbl.getChildCount() - 1);
                    }
                });

                List<ActivityManager.RunningAppProcessInfo> processes = m_am.getRunningAppProcesses();
                for(Iterator i = processes.iterator(); i.hasNext(); ) {
                    ActivityManager.RunningAppProcessInfo process = (ActivityManager.RunningAppProcessInfo)i.next();
                    TableRow tr = new TableRow(m_context);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    final TextView name = new TextView(m_context);
                    name.setText(process.processName);

                    final TextView pid = new TextView(m_context);
                    pid.setText(process.pid + "");

                    final TextView uid = new TextView(m_context);
                    uid.setText(process.uid + "");

                    final TextView pkgList = new TextView(m_context);
                    pkgList.setText(Arrays.toString(process.pkgList));

                    m_process_tbl.post(new Runnable() {
                       public void run() {
                           m_process_tbl.addView(name);
                           m_process_tbl.addView(pid);
                           m_process_tbl.addView(uid);
                           m_process_tbl.addView(pkgList);
                       }
                    });
                }

                /** Do this every sec*/
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    Log.e(TAG, "Sleep interrupted");
                }
            }
        }
    }
}
