package android_network.hetnet.system.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import android_network.hetnet.R;
import android_network.hetnet.system.ProcessDetailActivity;

/**
 * RunningApplicationListAdapter
 * AU: Gabe
 */
public class RunningApplicationListAdapter extends BaseAdapter {
  private List<ActivityManager.RunningAppProcessInfo> mRunningProcesses;
  private Context mContext;

  public RunningApplicationListAdapter(Context context, List<ActivityManager.RunningAppProcessInfo> runningProcesses) {
    mRunningProcesses = runningProcesses;
    mContext = context;
  }

  @Override
  public int getCount() {
    if (mRunningProcesses != null) {
      return mRunningProcesses.size();
    } else {
      return -1;
    }
  }

  @Override
  public Object getItem(int i) {
    if (mRunningProcesses != null) {
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

    final ActivityManager.RunningAppProcessInfo process = mRunningProcesses.get(i);
    pname.setText(process.processName);
    pid.setText(Integer.toString(process.pid));
    uid.setText(Integer.toString(process.uid));
    rowView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent(view.getContext(), ProcessDetailActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("PROC", process);
        i.putExtras(b);
        view.getContext().startActivity(i);
      }
    });
    pname.setTextColor(Color.BLACK);
    pid.setTextColor(Color.BLACK);
    uid.setTextColor(Color.BLACK);

    return rowView;
  }
}
