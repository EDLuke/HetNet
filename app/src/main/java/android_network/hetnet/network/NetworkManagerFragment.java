package android_network.hetnet.network;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android_network.hetnet.R;
import android_network.hetnet.common.trigger_events.UITriggerEvent;

import static android_network.hetnet.common.Constants.NETWORK_LIST_FETCHER;
import static android_network.hetnet.common.Constants.POLICY_ENGINE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NetworkManagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NetworkManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NetworkManagerFragment extends Fragment {
  TextView networkList;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM_LIST = "network_list";

  // TODO: Rename and change types of parameters
  private String m_network_list;


  private OnFragmentInteractionListener mListener;

  public NetworkManagerFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param networkList Network List Object.
   * @return A new instance of fragment NetworkManagerFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static NetworkManagerFragment newInstance(NetworkList networkList) {
    NetworkManagerFragment fragment = new NetworkManagerFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM_LIST, networkList.getListOfNetworks());
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      m_network_list = getArguments().getString(ARG_PARAM_LIST);
    }

    //register to event bus
    EventBus.getDefault().register(this);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_network_manager, container, false);
    networkList = (TextView) view.findViewById(R.id.network_list);
    networkList.setText(m_network_list);

    return view;
  }

  @Override
  public void onStop() {
    //Unregister from the event bus
    EventBus.getDefault().unregister(this);
    super.onStop();
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
    if (event.getEventOriginator().equals(NETWORK_LIST_FETCHER)) {
      NetworkList list = (NetworkList)(event.getEventList());
      networkList.setText(list.getListOfNetworks());
    }
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
