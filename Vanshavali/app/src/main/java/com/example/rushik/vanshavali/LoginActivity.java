package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    public Validator validator;

    @NotEmpty
    @Email
    private EditText editText_username;

    @NotEmpty
    @Password
    private EditText editText_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        //StrictMode.setThreadPolicy(policy);

        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_pass = (EditText) findViewById(R.id.editText_pass);

        //check if shared preference Key exists
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        if (pref.contains("vanshavali_mobile_user_email")) {
            //check user and token
            String user_name = pref.getString("vanshavali_mobile_user_email", "0");
            String user_token = pref.getString("vanshavali_mobile_user_token", "0");
            Log.d("user_email", user_name);
            Log.d("user_token", user_token);


            if (!(user_name.equals("0") || user_token.equals("0"))) {
                //check if user is valid . check user exists and token

                new Thread() {
                    @Override
                    public void run() {
                        if (MainServices.isConnectedToVanshavaliServer()) {
                            MainServices obj = new MainServices();
                            if (obj.isUserValid(user_name, user_token)) {
                                //user Logged In
                                //goto family Tree Activity
                                Intent i = new Intent(LoginActivity.this, FamilyList.class);
                                startActivity(i);
                            } else {
                                //User is Invalid . Destroy Preference
                                edit.clear();
                                edit.apply();
                                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                                showToasty("error",LoginActivity.this,"User Invalid . Login Again",Toasty.LENGTH_LONG);
                                startActivity(i);
                            }
                        } else {
                            Log.d("Message","In Else Part");
                            showToasty("error",LoginActivity.this, "Server Connection Error",Toasty.LENGTH_LONG);
                        }
                    }
                }.start();


            }
        } else {
            Log.d("Message", "No Preference Set");
        }

        validator = new Validator(this);
        validator.setValidationListener(this);


    }

    public void gotoSignUp(View v) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }


    /*
     * @param View view
     * Function Called On Login Form submit
     *
     * */
    public void loginBtnSubmit(View view) {
        //check For validation
        validator.validate();
    }


    @Override
    public void onValidationSucceeded() {
        //set shared prefrence
        Thread connthread = new Thread() {
            @Override
            public void run() {

                if (MainServices.isConnectedToVanshavaliServer()) {

                    editText_username = (EditText) findViewById(R.id.editText_username);
                    editText_pass = (EditText) findViewById(R.id.editText_pass);

                    //check user credentials
                    checkUserCredentials(editText_username.getText().toString(), editText_pass.getText().toString());
                } else {
                    //server connection error
                    showToasty("error",LoginActivity.this,"Server Connection Error",Toasty.LENGTH_LONG);
                }
            }
        };
        connthread.start();
        try{
            connthread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkUserCredentials(String user_email, String user_pass) {
        MainServices obj = new MainServices();
        obj.params.put("user_email", user_email);
        obj.params.put("user_pass", user_pass);
        try {
            String response = obj.post("login/checkUserCredentials", obj.params);
            JSONObject jsonobj = new JSONObject(response);
            jsonobj = jsonobj.getJSONObject("vanshavali_response");
            Log.d("response", response);
            if (jsonobj.getInt("code") == 200) {
                String token = jsonobj.getJSONObject("data").getString("token");

                //user is ok 200 set shared Preference
                SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
                SharedPreferences.Editor pref_edit = pref.edit();
                pref_edit.putString("vanshavali_mobile_user_email", user_email);
                pref_edit.putString("vanshavali_mobile_user_token", token);

                pref_edit.apply();

                showToasty("success",LoginActivity.this,"User Logged In ",Toasty.LENGTH_LONG);
                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(i);

            } else {
                showToasty("error",LoginActivity.this,jsonobj.getString("message"),Toasty.LENGTH_LONG);
            }


        } catch (Exception e) {
            Log.d("response", e.getMessage());
        }
    }

    public void showToasty(String type,Context c,final String toast,int length) {
        if(type.equals("error"))
            runOnUiThread(() -> Toasty.error(c, toast, length).show());
        if(type.equals("success"))
            runOnUiThread(() -> Toasty.success(c, toast, length).show());
    }

}
