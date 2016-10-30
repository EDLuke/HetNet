package android_network.hetnet.system.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import android_network.hetnet.R;

/**
 * RunningApplicationListAdapter
 * AU: Gabe
 */
public class RunningApplicationListAdapter extends BaseAdapter {

    /* Custom List Adapter for the Running Applications List, this adapter takes running processes
    and constructs a list view of all running process. Ultimately this adapter will be removed.  */

    private List<ActivityManager.RunningAppProcessInfo> mRunningProcesses;
    private Context mContext;

    public RunningApplicationListAdapter(Context context, List<ActivityManager.RunningAppProcessInfo> runningProcesses) {
        mRunningProcesses = runningProcesses;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mRunningProcesses != null)
            return mRunningProcesses.size();
        else
            return -1;
    }

    @Override
    public Object getItem(int i) {
        if (mRunningProcesses != null)
            return mRunningProcesses.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View rowView, ViewGroup viewGroup) {

        //Create empty row layout to populate with information
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = li.inflate(R.layout.system_manager_row, viewGroup, false);
        //Find references to textviews
        TextView pname = (TextView) rowView.findViewById(R.id.pname);
        TextView pid = (TextView) rowView.findViewById(R.id.pid);
        TextView uid = (TextView) rowView.findViewById(R.id.uid);
        //Get running activity i and use the info from that proceess
        ActivityManager.RunningAppProcessInfo process = mRunningProcesses.get(i);
        pname.setText(process.processName);
        pid.setText(Integer.toString(process.pid));
        uid.setText(Integer.toString(process.uid));
        //Formatting
        pname.setTextColor(Color.BLACK);
        pid.setTextColor(Color.BLACK);
        uid.setTextColor(Color.BLACK);

        return rowView;
    }
}
