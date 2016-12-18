package android_network.hetnet.cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by lanking on 17/12/2016.
 */

public class HttpService  {
    public HttpService(){}

    public String GET(String url, Map<Object, Object> param) throws Exception{
        HttpURLConnection httpcon;
        String result = null;
        String query = "";
        if (param != null){
           query = urlEncodeUTF8(param);
        }
        //Connect
        httpcon = (HttpURLConnection) ((new URL(url+"?"+query).openConnection()));
        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setRequestMethod("GET");
        httpcon.connect();

        //Read
        BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

        String line = null;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        result = sb.toString();
        return result;
    }

    public void POST(String url, String data) throws Exception {
        //System.out.println("\nUrl: "+url+"\nData: "+data+"\n");

      HttpURLConnection httpcon;
      String result = null;
      //Connect
      httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
      httpcon.setDoOutput(true);
      httpcon.setRequestProperty("Content-Type", "application/json");
      httpcon.setRequestProperty("Accept", "application/json");
      httpcon.setRequestMethod("POST");
      httpcon.connect();

      //Write
      OutputStream os = httpcon.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
      writer.write(data);
      writer.close();
      os.close();

      //Read
      BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

      String line = null;
      StringBuilder sb = new StringBuilder();

      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

      br.close();
      result = sb.toString();
      System.out.println(result);

    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }
}
