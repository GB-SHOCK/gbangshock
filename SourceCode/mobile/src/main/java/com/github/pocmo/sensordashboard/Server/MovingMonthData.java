package com.github.pocmo.sensordashboard.Server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bae Somi on 2016-11-27.
 */
public class MovingMonthData {
    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_DATE = "timestamp";
    private static final String TAG_COUNT = "step_count";

    JSONArray userInformation = null;
    ArrayList<HashMap<String, String>> movingMonth;

    public MovingMonthData() {
        movingMonth = new ArrayList<HashMap<String, String>>();
        downloadFromDB("test3");
    }

    public ArrayList<HashMap<String, String>> getDataList() {
        movingMonth.clear();
        getData();
        return movingMonth;
    }


    protected void getData() {
        try {
            // while (myJSON==null);

            JSONObject jsonObj = new JSONObject(myJSON);
            userInformation = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < userInformation.length(); i++) {
                JSONObject c = userInformation.getJSONObject(i);
                String date = c.getString(TAG_DATE);
                String stepcount = c.getString(TAG_COUNT);


                HashMap<String, String> info = new HashMap<String, String>();


                info.put(TAG_DATE, date);
                info.put(TAG_COUNT, stepcount);

                movingMonth.add(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void downloadFromDB(final String userID) {
        class downloadFromDBJSON extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                BufferedReader bufferedReader = null;
                try {
                    String link = "http://52.78.78.185/moving_month.php";

                    String data = URLEncoder.encode("USERID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
                    //String uri = params[0];

                    URL url = new URL(link);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    myJSON = sb.toString().trim();

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                //myJSON = result;
                //getData();
            }
        }
        downloadFromDBJSON task = new downloadFromDBJSON();
        try {
            task.execute(userID).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
