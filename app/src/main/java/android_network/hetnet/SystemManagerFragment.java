package android_network.hetnet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.PolicyEngineData;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;

import static android_network.hetnet.common.Constants.POLICY_ENGINE;

public class SystemManagerFragment extends Fragment {
  static final String SYSTEM_FRAGMENT_LOG = "system_log";

  private OnFragmentInteractionListener mListener;

  TextView m_systemLogs;

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
    m_systemLogs = (TextView)(view.findViewById(R.id.system_logs));

    if(savedInstanceState != null){
      m_systemLogs.setText(savedInstanceState.getString(SYSTEM_FRAGMENT_LOG));
    }

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

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onMessageEvent(UITriggerEvent event) {
    if(event.getEventOriginator().equals(POLICY_ENGINE)) {
      DataStoreObject data = ((PolicyEngineData) (event.getEvent())).getDataStoreObject();

      String dataString = String.format("%s\t%s\t%s\n", data.getApplicationID(), data.getApplicationType(), event.getTimeOfEvent().toString());
      m_systemLogs.append(dataString);
    }
  }

}
