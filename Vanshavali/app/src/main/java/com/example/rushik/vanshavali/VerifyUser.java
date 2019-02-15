package com.example.rushik.vanshavali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import VanshavaliServices.MainServices;
import androidx.annotation.UiThread;
import es.dmoral.toasty.Toasty;

public class VerifyUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);
        String username = getIntent().getStringExtra("username");
        Toasty.success(this,"User Registered . Enter Verification Code").show();
        if(!username.equals("")){
            EditText editText_username = (EditText)findViewById(R.id.editText_username);
            editText_username.setText(username);
        }
    }


    public void btn_verifyOnClick(View view){
        //check if User Verified.and verify user
        EditText username = (EditText)findViewById(R.id.editText_username);
        EditText verification_code = (EditText)findViewById(R.id.editText_verification_code);
        Log.d("Message :","Username : "+username.getText());
        Log.d("Message :","verification : "+verification_code.getText());
        if(verification_code.getText().toString().equals("") || username.getText().toString().equals("")){
            //please enter verfication code
            Toasty.error(VerifyUser.this,"Please Enter username and Verification Code").show();
        }
        else{
            //make request for verified
            if(MainServices.isConnectedToVanshavaliServer()){
                MainServices obj = new MainServices();
                obj.params.put("user_email", username.getText().toString());
                obj.params.put("verification_code", verification_code.getText().toString());
                try {
                    String response = obj.post("login/verifyUser", obj.params);

                    JSONObject jsonobj = new JSONObject(response);
                    jsonobj = jsonobj.getJSONObject("vanshavali_response");
                    if (jsonobj.getInt("code") == 200) {
                        //user created Successfully ask to add verification to enter
                        Toasty.success(VerifyUser.this, "User Verified", Toasty.LENGTH_LONG).show();
                        Intent i = new Intent(getBaseContext(),LoginActivity.class);
                        startActivity(i);

                    } else {
                        Toasty.error(VerifyUser.this, jsonobj.getString("message"), Toasty.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Log.e("Io Exception ", e.getMessage());
                    Toasty.error(VerifyUser.this, e.getMessage(), Toasty.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Log.e("Json Exception ", e.getMessage());
                    Toasty.error(VerifyUser.this, e.getMessage(), Toasty.LENGTH_LONG).show();
                }
            }else{
                Toasty.error(VerifyUser.this,"Connection Error").show();
            }
        }


    }
    public void goToSignIn(View view){
        Intent i = new Intent(VerifyUser.this, LoginActivity.class);
        startActivity(i);
    }
}
