package VanshavaliServices;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;


public class ConnectToServer extends AsyncTask<String,Void,Boolean>  {

    protected Boolean doInBackground(String... param){
        try {
            URL myUrl = new URL(param[0]);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(Integer.valueOf(param[1]));
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exception
            e.printStackTrace();
            Log.d("Error ConnectedtoServer",e.getMessage());
            return false;
        }
    }
   /* protected void onPostExecute(Boolean response) {
        response = false;
        Log.d("Error","Error in connection");
    }*/


}
