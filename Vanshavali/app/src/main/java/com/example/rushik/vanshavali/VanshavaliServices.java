package com.example.rushik.vanshavali;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.net.URL;
import java.net.URLConnection;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VanshavaliServices {
    public static String host = "http://192.168.1.79:90/vanshavali/mobile/";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public Map<String, String> params = new HashMap<String, String>();
    public String url = "";

    OkHttpClient client = new OkHttpClient();


    String post(String request_url, Map<String, String> map) throws IOException {
        this.url = host + request_url;
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //ystem.out.println(entry.getKey() + " = " + entry.getKey());
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody body = formBuilder.build();

        Request request = new Request.Builder()
                .url(this.url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static boolean isConnectedToVanshavaliServer(){
        VanshavaliServices obj = new VanshavaliServices();
        if(obj.isConnectedToServer(host,5000)){
            return true;
        }
        return false;
    }

    private boolean isConnectedToServer(String url, int timeout) {
        try {
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
    public boolean checkUserExists(String Username){
        VanshavaliServices obj = new VanshavaliServices();
        obj.params.put("table","user_master");
        obj.params.put("field","user_email");
        obj.params.put("user_email",Username);
        try {
            String response = obj.post("Login/checkexists/user_id", obj.params);
            Log.d("response",response);
        }catch (IOException e){
            Log.d("response",e.getMessage());
        }

        return false;
    }
}

