package com.example.rushik.vanshavali;

import android.content.Intent;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

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
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        //edit text
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_pass = (EditText) findViewById(R.id.editText_pass);


        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    /*
     * Goto Signin
     * */
    public void goToSignIn(View v) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
    }

    /*
     * Onsubmit Check Validation
     * */
    public void btn_registerOnClick(View view) {
        validator.validate();
        //check if user exists
        /*Thread one = new Thread() {
            public void run() {

                    if (MainServices.isConnectedToVanshavaliServer()) {
                        b = true;
                        Log.d("Register Activity :", "Connection Success");
                    } else {
                        b = false;
                        Log.e("Register Actitvity", "Connection Error");
                    }

            }
        };
        one.start();
        try {
            one.join();
        }catch (Exception e){
            Log.e("Register Actitvity", e.getMessage());
        }*/

    }


    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        if (MainServices.isConnectedToVanshavaliServer()) {
            EditText username = (EditText) findViewById(R.id.editText_username);
            EditText password = (EditText) findViewById(R.id.editText_pass);
            if (checkUserExists(username.getText().toString())) {
                Log.d("Message : ", "User Not Exits");
                //go for Registeration
                //Toasty.success(RegisterActivity.this,"User Not Exists").show();
                MainServices obj = new MainServices();
                obj.params.put("user_email", username.getText().toString());
                obj.params.put("user_pass", password.getText().toString());
                try {
                    String response = obj.post("login/registerUser", obj.params);
                } catch (IOException e) {
                    Log.e("Io Exception ", e.getMessage());
                }


            } else {
                //show  Error Message User Already Exists
                Log.d("Message : ", "User  Exits");
                Toasty.error(RegisterActivity.this, "User Already Exits", Toasty.LENGTH_LONG).show();
            }
            Log.d("Register Activity :", "Connection Success");
        } else {

            Log.e("Register Actitvity", "Connection Error");
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

    public boolean checkUserExists(String Username) {

        MainServices obj = new MainServices();
        //check if user Already Exits
        if (obj.checkUserExists(Username)) {
            return true;
        } else {

        }
        return false;
    }


}
