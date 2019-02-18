package VanshavaliServices;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

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

public class MainServices {
    public static String host = "http://192.168.1.103/vanshavali/mobile/";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public Map<String, String> params = new HashMap<String, String>();
    public String url = "";

    OkHttpClient client = new OkHttpClient();


    public String post(String request_url, Map<String, String> map) throws IOException {
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

    public static boolean isConnectedToVanshavaliServer() {
        final MainServices obj = new MainServices();
        boolean temp = false;
        if (obj.isConnectedToServer(host, 5000)) {
            return true;
        }


        return false;
    }

    private boolean isConnectedToServer(final String url, final Integer timeout) {

        /*new Thread() {
            @Override
            public void run() {
                //your code here
                Thread mythread = Thread.currentThread();
                Log.d("message",mythread.getName());
                 temp[0] = myfunc(url,timeout);
                Log.d("message",temp[0].toString());

            }

            public boolean myfunc(String url, int timeout) {
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
        }.start();*/
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
        /*try {
            Boolean temp = new ConnectToServer().execute(url, timeout.toString()).get();
            Log.d("Error",temp.toString());
            return temp;
        }catch (Exception e){
            //e.printStackTrace();
            Log.d("Error",e.toString());
            return false;
        }*/

        //Log.d("message",mythread.getName());
    }

    public boolean checkUserExists(String Username) {
        Boolean is_user_exists = false;
        MainServices obj = new MainServices();
        obj.params.put("table", "user_master");
        obj.params.put("field", "user_email");
        obj.params.put("user_email", Username);
        try {
            String response = obj.post("Login/checkexists/user_id", obj.params);
            JSONObject jsonobj = new JSONObject(response);
            jsonobj = jsonobj.getJSONObject("vanshavali_response");
            is_user_exists = jsonobj.getBoolean("message");
            Log.d("response", response);
        } catch (Exception e) {
            Log.d("response", e.getMessage());
        }

        return is_user_exists;
    }

    public boolean isUserValid(String user_email,String token) {
        Boolean is_user_valid = false;
        MainServices obj = new MainServices();
        obj.params.put("user_email", user_email);
        obj.params.put("token", token);
        try {
            String response = obj.post("Login/isUserValid", obj.params);
            JSONObject jsonobj = new JSONObject(response);
            jsonobj = jsonobj.getJSONObject("vanshavali_response");
            if(jsonobj.getInt("code" ) == 200)
                is_user_valid = true;
            Log.d("response", response);
        } catch (Exception e) {
            Log.d("response", e.getMessage());
        }

        return is_user_valid;
    }

}

