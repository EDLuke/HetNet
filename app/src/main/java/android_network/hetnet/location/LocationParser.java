package android_network.hetnet.location;

/**
 * Created by lanking on 27/10/2016.
 */

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationParser extends AsyncTask<String, Void, String> {
  private final String USER_AGENT;
  public AsyncResponse sender = null;

  LocationParser(AsyncResponse sender) {
    this.sender = sender;
    USER_AGENT = "Mozilla/5.0";
  }

  public interface AsyncResponse {
    void processFinish(String output);
  }

  @Override
  protected String doInBackground(String... Strings) {
    String latLong = Strings[0];
    String data = "";

    try {
      data = gettingAddress(latLong);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return Location_extractor(data);
  }

  @Override
  protected void onPostExecute(String Address) {
    sender.processFinish(Address);
  }

  private String gettingAddress(String latLong) throws Exception {
    String urlString = "http://maps.googleapis.com/maps/api/geocode/json";
    String dataPart = "?" + "latlng=" + latLong;
    URL url = new URL(urlString + dataPart);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();

    // By default it is GET request
    con.setRequestMethod("GET");

    //add request header
    con.setRequestProperty("User-Agent", USER_AGENT);

    // Reading response from input Stream
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String output;
    StringBuilder response = new StringBuilder();

    while ((output = in.readLine()) != null) {
      response.append(output);
    }

    in.close();

    return response.toString();
  }

  private String Location_extractor(String data) {
    Pattern s1 = Pattern.compile("\"formatted_address\"");
    Matcher m = s1.matcher(data);

    if (m.find()) {
      data = data.substring(m.end());
    }

    Pattern s2 = Pattern.compile("\"[^\"]*\"");
    m = s2.matcher(data);

    if (m.find()) {
      return m.group(0);
    }

    return "Not found, please Check Internet Connection";
  }
}
