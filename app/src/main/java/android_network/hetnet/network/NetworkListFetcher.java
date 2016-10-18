package android_network.hetnet.network;

import android.app.IntentService;
import android.content.Intent;

public class NetworkListFetcher extends IntentService {
  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public NetworkListFetcher(String name) {
    super(name);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    // Gets data from the incoming Intent
    String dataString = intent.getDataString();

    // TODO: Do work here, based on the contents of dataString
  }
}
