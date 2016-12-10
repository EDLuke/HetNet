package android_network.hetnet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import android_network.hetnet.common.trigger_events.UITriggerEvent;
import android_network.hetnet.data.DataStoreObject;
import android_network.hetnet.data.PolicyEngineData;
import android_network.hetnet.ui.TabFragment.OnFragmentInteractionListener;

import static android_network.hetnet.common.Constants.LOCATION_LIST_FETCHER;
import static android_network.hetnet.common.Constants.POLICY_ENGINE;

public class LocationManagerFragment extends Fragment {

  private OnFragmentInteractionListener mListener;

  TextView m_locationLogs;
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment LocationManagerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static LocationManagerFragment newInstance() {
    LocationManagerFragment fragment = new LocationManagerFragment();

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //register to event bus
    EventBus.getDefault().register(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_location_manager, container, false);

    //Hook up with UI
    m_locationLogs = (TextView)(view.findViewById(R.id.location_logs));

    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
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

      String dataString = String.format("%.2f\t%.2f\t%s\n", data.getLongitude(), data.getLatitude(), event.getTimeOfEvent().toString());
      m_locationLogs.append(dataString);
    }
  }
}
