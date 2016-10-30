package android_network.hetnet.location;

/**
 * Created by lanking on 27/10/2016.
 */

import java.util.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Location_parser {
    private final String USER_AGENT;
    private Map<String, String> geo_data;
    Location_parser() {
        USER_AGENT = "Mozilla/5.0";
        // Default location Dronero, Italy
        geo_data = new HashMap<String, String>();
        geo_data.put("street_number", "34");
        geo_data.put("route", "Via Pasubio");
        geo_data.put("postal_code", "12025");
        geo_data.put("admin_lvl3","Dronero");
        geo_data.put("admin_lvl2","CN");
        geo_data.put("Country","Italy");
    }
    public String parse(String s) throws Exception{
        String Location_API_URL = "http://maps.googleapis.com/maps/api/geocode/json";
        URL url = new URL(Location_API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        String response_data = response.toString();

        return "";
    }
    private void sendingGetRequest() throws Exception {

        String urlString = "http://localhost:8080/JAXRSJsonCRUDExample/rest/countries";

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // By default it is GET request
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("Sending get request : "+ url);
        System.out.println("Response code : "+ responseCode);

        // Reading response from input Stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        //printing result from response
        System.out.println(response.toString());

    }

    // HTTP Post request
    private void sendingPostRequest() throws Exception {

        String url = "http://localhost:8080/JAXRSJsonCRUDExample/rest/countries";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        String postJsonData = "{\"id\":5,\"countryName\":\"USA\",\"population\":8000}";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postJsonData);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + postJsonData);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        //printing result from response
        System.out.println(response.toString());
    }
}
