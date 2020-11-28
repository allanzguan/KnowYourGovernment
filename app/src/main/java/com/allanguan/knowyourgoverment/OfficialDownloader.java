package com.allanguan.knowyourgoverment;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OfficialDownloader implements Runnable{

    private static final String TAG = "OfficialDownloader";
    private static final String KEY = "AIzaSyDOTGt7RU2iVGSp1k9Lm_CS0tyIxxQQqdI";
    private static final String URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private static final String KEY_URL = KEY +"&address=";

    private MainActivity mainActivity;
    private String searchTarget;
    private List<Official> oList = new ArrayList<>();
    private String city = "", state = "", zip = "";

    public OfficialDownloader(MainActivity mainActivity, String searchTarget){
        this.mainActivity = mainActivity;
        this.searchTarget = searchTarget;
    }

    @Override
    public void run() {
        Uri.Builder uriBuilder = Uri.parse(URL + KEY_URL + searchTarget).buildUpon();
        String urlToUse = uriBuilder.toString();

        StringBuilder sb = new StringBuilder();
        try{
            java.net.URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.doNoLocation(searchTarget);
                    }
                });
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

//            Log.d(TAG, "run: " + sb.toString());

        } catch (Exception e){
            Log.d(TAG, "run: ", e);
            return;
        }

        process(sb.toString());
    }

    private void process(String s){

        try{

            JSONObject jOfficial = new JSONObject(s);

            JSONObject normalInput = jOfficial.getJSONObject("normalizedInput");

            if(normalInput.has("city")){
                city = normalInput.getString("city");
            }

            if(normalInput.has("state")){
                state = normalInput.getString("state");
            }

            if(normalInput.has("zip")){
                zip = normalInput.getString("zip");
            }




            JSONArray offices = jOfficial.getJSONArray("offices");
            JSONArray officials = jOfficial.getJSONArray("officials");

            for (int i = 0; i < offices.length(); i++){
                String name, address = "", party = "", phone = "", website = "", email = "", photo = "", fb = "", tw = "", yt = "";


                String title = offices.getJSONObject(i).getString("name");
                JSONArray offIndex = offices.getJSONObject(i).getJSONArray("officialIndices");



                for(int j = 0; j < offIndex.length(); j++){
                    JSONObject oInfo = officials.getJSONObject(offIndex.getInt(j));

                    name = oInfo.getString("name");

                    if(oInfo.has("address")){
                        address = parseAddress(oInfo.getJSONArray("address"));
                    }

                    if(oInfo.has("party")){
                        party = oInfo.getString("party");
                    }

                    if(oInfo.has("phones")){
                        phone = (String) oInfo.getJSONArray("phones").get(0);
                    }

                    if(oInfo.has("urls")){
                        website = (String) oInfo.getJSONArray("urls").get(0);
                    }

                    if(oInfo.has("emails")){
                        email = (String) oInfo.getJSONArray("emails").get(0);
                    }

                    if(oInfo.has("photoUrl")){
                        photo = oInfo.getString("photoUrl");
                    }

                    JSONArray channels;
                    if(oInfo.has("channels")){
                        channels = oInfo.getJSONArray("channels");
                        for(int k = 0; k < channels.length(); k++){
                            JSONObject cObj = channels.getJSONObject(k);
                            if(cObj.getString("type").equals("Facebook")){
                                fb = cObj.getString("id");
                            }
                            if(cObj.getString("type").equals("Twitter")){
                                tw = cObj.getString("id");
                            }
                            if(cObj.getString("type").equals("YouTube")){
                                yt = cObj.getString("id");
                            }
                        }
                    }

                    Official newOfficial = new Official(name, title, party, address, phone, email, website, photo, fb, tw, yt);
                    oList.add(newOfficial);
                }
            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.addOfficialToList(oList, city, state, zip);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String parseAddress(JSONArray adrs){
        StringBuilder sb = new StringBuilder();

        try {
            JSONObject adrsObj = adrs.getJSONObject(0);
            if(adrsObj.has("line1")){
                sb.append(adrsObj.getString("line1"));
                sb.append("\n");
            }
            if(adrsObj.has("line2")){
                sb.append(adrsObj.getString("line2"));
                sb.append("\n");
            }
            if(adrsObj.has("line3")){
                sb.append(adrsObj.getString("line3"));
                sb.append("\n");
            }
            sb.append(adrsObj.getString("city"));
            sb.append(", ");
            sb.append(adrsObj.getString("state"));
            sb.append(" ");
            sb.append(adrsObj.getString("zip"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "parseAddress: " + sb.toString());
        return sb.toString();
    }


}
