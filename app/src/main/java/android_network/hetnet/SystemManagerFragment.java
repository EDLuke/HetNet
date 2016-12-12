package android_network.hetnet;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.PolicyEngineData;
import android_network.hetnet.system.ApplicationList;
import android_network.hetnet.system.SystemList;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;

import static android_network.hetnet.common.Constants.POLICY_ENGINE;

public class SystemManagerFragment extends Fragment {
  static final String SYSTEM_FRAGMENT_LOG = "system_log";

  private OnFragmentInteractionListener mListener;

  ExpandableListView m_systemLogs;
  SystemExpandableListAdapter m_systemLogsAdapter;

  public SystemManagerFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment SystemManagerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static SystemManagerFragment newInstance() {
    SystemManagerFragment fragment = new SystemManagerFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //register to event bus
    EventBus.getDefault().register(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_system_manager, container, false);

    //Hoop up with UI
    m_systemLogs = (ExpandableListView) (view.findViewById(R.id.system_logs));

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
        + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  //Source:
  //http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UITriggerEvent event) {
    if (event.getEventOriginator().equals(POLICY_ENGINE)) {
      DataStoreObject data = ((PolicyEngineData) (event.getEvent())).getDataStoreObject();

      SystemList currentList = data.getSystemList();
      HashMap<Integer, ApplicationList> currentAppList = currentList.getApplicationList();

      List<String> listDataHeader = new ArrayList<>();
      HashMap<String, List<String>> listDataChild = new HashMap<>();

      Iterator<Map.Entry<Integer, ApplicationList>> currentAppList_it = currentAppList.entrySet().iterator();

      while (currentAppList_it.hasNext()) {
        Map.Entry<Integer, ApplicationList> pair = currentAppList_it.next();
        ApplicationList applicationList = pair.getValue();

        listDataHeader.add(applicationList.getProcessName());

        List<String> applicationData = new ArrayList<String>();
        applicationData.add("CPU Usage: " + applicationList.getCpuUsage());
        applicationData.add("Received bytes: " + applicationList.getRxBytes());
        applicationData.add("Trasmitted bytes: " + applicationList.getTxBytes());
        applicationData.add("Received packets: " + applicationList.getRxPackets());
        applicationData.add("Transmitted packets: " + applicationList.getTxPackets());

        listDataChild.put(applicationList.getProcessName(), applicationData);

        currentAppList_it.remove();
      }

      m_systemLogsAdapter = new SystemExpandableListAdapter(getContext(), listDataHeader, listDataChild);
      m_systemLogs.setAdapter(m_systemLogsAdapter);
    }
  }

  public class SystemExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public SystemExpandableListAdapter(Context context, List<String> listDataHeader,
                                       HashMap<String, List<String>> listChildData) {
      this._context = context;
      this._listDataHeader = listDataHeader;
      this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
      return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
      return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

      final String childText = (String) getChild(groupPosition, childPosition);

      if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) this._context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.fragment_system_list_item, null);
      }

      TextView txtListChild = (TextView) convertView
        .findViewById(R.id.system_logs_item);

      txtListChild.setText(childText);
      return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
      return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
      return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
      return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
      return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
      String headerTitle = (String) getGroup(groupPosition);
      if (convertView == null) {
        LayoutInflater infalInflater = (LayoutInflater) this._context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.fragment_system_list_header, null);
      }

      TextView lblListHeader = (TextView) convertView
        .findViewById(R.id.system_logs_header);
      lblListHeader.setTypeface(null, Typeface.BOLD);
      lblListHeader.setText(headerTitle);

      return convertView;
    }

    @Override
    public boolean hasStableIds() {
      return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
      return true;
    }
  }

}
