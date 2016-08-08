package com.itworx.httpclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String url = "http://api.androidhive.info/contacts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testWS();
    }


    public void testWS()
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... params) {
                String responseString  =  WebServiceManager.GET(url);
                try {
                    JSONObject json = new JSONObject(responseString);
                    Log.d("","response :"+json);
                    JSONArray jsonArray = json.getJSONArray("contacts");
                    for(int i = 0 ; i <jsonArray.length();i++)
                    {
                        String name  = jsonArray.getJSONObject(i).getString("name");
                        Log.d("","karim:"+name);
                    }
                }
                catch(Exception ex){
                   Log.d("","exception:"+ex.getMessage());
                }
                return null;
            }


        }.execute();
    }
}
