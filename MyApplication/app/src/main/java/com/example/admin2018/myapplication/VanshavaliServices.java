package com.example.admin2018.myapplication;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VanshavaliServices {
    protected static String host = "http://192.168.1.103/";
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

}

