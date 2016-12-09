package android_network.hetnet;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.common.trigger_events.TriggerEvent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PolicyEngineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PolicyEngineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PolicyEngineFragment extends Fragment {
  TextView eventList;

  private static final String ARG_PARAM_EVENT_LOG = "event_log";

  // TODO: Rename and change types of parameters
  private String m_event_log;

  private OnFragmentInteractionListener mListener;

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param event_log Current Event Log
   * @return A new instance of fragment PolicyEngineFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static PolicyEngineFragment newInstance(String event_log) {
    PolicyEngineFragment fragment = new PolicyEngineFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM_EVENT_LOG, event_log);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      m_event_log = getArguments().getString(ARG_PARAM_EVENT_LOG);
    }

    //register to event bus
    EventBus.getDefault().register(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_policy_engine, container, false);
    eventList = (TextView) view.findViewById(R.id.event_list);
    eventList.setText(m_event_log);

    return view;
  }

  @Override
  public void onStop() {
    //Unregister from the event bus
    EventBus.getDefault().unregister(this);
    super.onStop();
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
  public void onMessageEvent(TriggerEvent event) {
    eventList.append(event.toString() + "\n");
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
