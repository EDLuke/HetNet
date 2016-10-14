package android_network.hetnet;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.data;
import static android_network.hetnet.R.id.pid;


//public class SystemManager_Main extends AppCompatActivity {
//    /** Log Tag */
//    private static final String TAG = "SystemManager";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_system_manager__main);
//
//
//        startProcessProfiling();
//    }
//
//
//
//    /** From http://stackoverflow.com/questions/9756353/how-to-get-memory-usage-and-cpu-usage-by-application */
//
//    /**
//     *
//     */
//    private void startProcessProfiling(){
//        Log.v(TAG, "Started process profiling");
//        ProcessProfileThread processProfileThread = new ProcessProfileThread(this);
//        processProfileThread.start();
//    }
//
//    /**
//     * Currently we clear the table and re-populate it every sec
//     * Need to find a more elegant way to 'update' the info
//     *  */
//    private class ProcessProfileThread extends Thread{
//        ActivityManager m_am;
//        TableLayout     m_process_tbl;
//        Context         m_context;
//
//        public ProcessProfileThread(Context context){
//
//            m_process_tbl   = (TableLayout)findViewById(R.id.tbl_process_info);
//            m_context       = context;
//        }
//
//        public void run() {
//            while(true) {
//                /** Clear the children except for the first row */
//                m_process_tbl.post(new Runnable() {
//                    public void run() {
//                        m_process_tbl.removeViews(1, m_process_tbl.getChildCount() - 1);
//                    }
//                });
//
//
//                for(Iterator i = processes.iterator(); i.hasNext(); ) {
//                    ActivityManager.RunningAppProcessInfo process = (ActivityManager.RunningAppProcessInfo)i.next();
//                    TableRow tr = new TableRow(m_context);

//
//
//                }
//
//                /** Do this every sec*/
//                try{
//                    Thread.sleep(1000);
//                }catch (InterruptedException e) {
//                    Log.e(TAG, "Sleep interrupted");
//                }
//            }
//        }
//    }
//}


public class SystemManager_Main extends Activity{
    //    /** Log Tag */
    private static final String TAG = "SystemManager";

    private List<ActivityManager.RunningAppProcessInfo> mRunningProcesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_manager__main);
        final ListView appList = (ListView) findViewById(R.id.app_list);
        ActivityManager m_am = (ActivityManager)getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        mRunningProcesses = m_am.getRunningAppProcesses();
        final RunningApplicationListAdapter adapter = new RunningApplicationListAdapter(getApplicationContext(), mRunningProcesses);
        appList.setAdapter(adapter);
        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityManager.RunningAppProcessInfo process = mRunningProcesses.get(i);
//                ActivityManager.killBackgroundProcesses(process.processName);
                Process.sendSignal(process.pid, Process.SIGNAL_KILL);
                Toast.makeText(getApplicationContext(), "Killing " + process.processName,
                        Toast.LENGTH_LONG).show();
                ActivityManager m_am = (ActivityManager)getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                mRunningProcesses = m_am.getRunningAppProcesses();
                

            }
        });


    }

    public class RunningApplicationListAdapter extends BaseAdapter{

        private List<ActivityManager.RunningAppProcessInfo> mRunningProcesses;
        private Context mContext;
        public RunningApplicationListAdapter(Context context, List<ActivityManager.RunningAppProcessInfo> runningProcesses) {
            mRunningProcesses = runningProcesses;
            mContext = context;


        }

        @Override
        public int getCount() {
            if (mRunningProcesses != null){
                return mRunningProcesses.size();
            }else{
                return -1;
            }

        }

        @Override
        public Object getItem(int i) {
            if(mRunningProcesses != null) {
                return mRunningProcesses.get(i);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View rowView, ViewGroup viewGroup) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = li.inflate(R.layout.system_manager_row, viewGroup, false);
            TextView pname = (TextView) rowView.findViewById(R.id.pname);
            TextView pid = (TextView) rowView.findViewById(R.id.pid);
            TextView uid = (TextView) rowView.findViewById(R.id.uid);
            TextView ppkgname = (TextView) rowView.findViewById(R.id.pkglist);
            ActivityManager.RunningAppProcessInfo process = mRunningProcesses.get(i);
            pname.setText(process.processName);
            pid.setText(Integer.toString(process.pid));
            uid.setText(Integer.toString(process.uid));
            ppkgname.setText(process.pkgList.toString());
            return rowView;
        }
    }


}