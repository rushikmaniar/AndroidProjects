package com.example.admin2018.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    String url = "http://192.168.1.79:90/vanshavali/mobile/login/registerUser";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enableStrictMode();



        if((isConnectedToServer(url,2000))){
            //myfunc1(this.url,arr);
            HashMap arr = new HashMap();
            arr.put("user_name","abc");
            arr.put("user_pass","123");

            VanshavaliServices obj1 = new VanshavaliServices();
            obj1.params.put("user_email", "rushik");
            obj1.params.put("user_pass", "111111");

            try {
                String response = obj1.post("vanshavali/mobile/login/registerUser", obj1.params);
                TextView outputText = (TextView) findViewById(R.id.outputText);
                outputText.setText(response);

                JSONObject obj = new JSONObject(response);

                //TextView outputText = (TextView) findViewById(R.id.outputText);
                JSONObject j = obj.getJSONObject("vanshavali_response");
                outputText.setText(j.getString("message"));

            }catch (Exception e){
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this,"Error Connected",Toast.LENGTH_LONG).show();
        }

    }
    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
    public void myfunc() {
       /* try {

        }catch (Exception e){
            e.printStackTrace();
        }*/
        final TextView mTextView = (TextView) findViewById(R.id.outputText);

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.79:90/vanshavali/mobile/login/registerUser";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        mTextView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void myfunc1(String url,HashMap param) {
        final TextView mTextView = (TextView) findViewById(R.id.outputText);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        //mTextView.setText(response);

                        Gson gson = new Gson();
                        HashMap json = gson.fromJson(response,HashMap.class);
                        mTextView.setText(json.get("vanshavali_response").toString());


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_name", "Alif");
                params.put("user_pass", "123");

                return params;
            }
        };
        queue.add(postRequest);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exception
            e.printStackTrace();
            return false;
        }
    }


}
