package com.example.rana_jabareen.wearablehealthtracker;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by RANA_JABAREEN on 04/22/2016.
 */
public class LocationUploadTask extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    private ArrayList<LocationClass> locations = new ArrayList<LocationClass>();
    String UploadLocationUrl="";
    public LocationUploadTask(ArrayList<LocationClass> locationsSets)
    {
        locations=locationsSets;


    }
    @Override
    protected String doInBackground(String... URL) {
        UploadLocationUrl=URL[0];
        StringBuilder stringbuilder = new StringBuilder(); // to return the result
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(UploadLocationUrl);

        try {
            Gson gson = new GsonBuilder().create();  // parse object to json
            String postMessage = gson.toJson( locations);
            StringEntity param = new StringEntity(postMessage); // add parameter to post request
            httpPost.setEntity(param);
        } catch (UnsupportedEncodingException e) {
            Log.d("Json", e.getLocalizedMessage());

        }

        try {
            HttpResponse response = httpclient.execute(httpPost); // get the response
            StatusLine statusline = response.getStatusLine();
            int StatusCode = statusline.getStatusCode();
            if (StatusCode == 200) // there is result
            {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringbuilder.append(line);
                }
                inputStream.close();
            } else {
                Log.d("Json", "Failed to download file");

            }


        } catch (Exception E) {
            Log.d("Json", E.getLocalizedMessage());


        }

        return stringbuilder.toString();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
