package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class ForgotPassword extends AppCompatActivity implements Validator.ValidationListener {

    private String username;
    public Validator validator;

    @NotEmpty
    @Email
    private EditText editText_email;

    @NotEmpty
    private EditText editText_verification_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        validator = new Validator(this);
        validator.setValidationListener(this);

        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_verification_code = (EditText)findViewById(R.id.editText_verification_code);
    }

    public void btnSendMailOnClick(View view) {
        username = editText_email.getText().toString();
        if ((isValidEmail(username))) {
            sendForgotPassword();
        } else {
            Toasty.error(this, "Enter Proper Email", Toasty.LENGTH_LONG).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void sendForgotPassword() {
        new Thread() {
            @Override
            public void run() {
                if (MainServices.isConnectedToVanshavaliServer()) {
                    MainServices obj = new MainServices();
                    obj.params.put("user_email", username);
                    try {
                        String response = obj.post("Login/forgotPassword", obj.params);
                        Log.d("response :", response);
                        JSONObject jsonobj = new JSONObject(response);
                        jsonobj = jsonobj.getJSONObject("vanshavali_response");
                        if (jsonobj.getInt("code") == 200) {
                            showToasty("success", ForgotPassword.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                        } else {
                            showToasty("error", ForgotPassword.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                        }
                    } catch (IOException e) {
                        Log.e("Io Exception ", e.getMessage());
                        showToasty("success", ForgotPassword.this, e.getMessage(), Toasty.LENGTH_LONG);
                    } catch (JSONException e) {
                        Log.e("Io Exception ", e.getMessage());
                        showToasty("error", ForgotPassword.this, e.getMessage(), Toasty.LENGTH_LONG);
                    }
                } else {
                    showToasty("error", ForgotPassword.this, "Server Connection Error", Toasty.LENGTH_LONG);
                }
            }
        }.start();
    }

    public void btn_verify(View view) {
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


                } else {
                    //server connection error
                    showToasty("error",ForgotPassword.this,"Server Connection Error",Toasty.LENGTH_LONG);
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


    public void showToasty(String type, Context c, final String toast, int length) {
        if (type.equals("error"))
            runOnUiThread(() -> Toasty.error(c, toast, length).show());
        if (type.equals("success"))
            runOnUiThread(() -> Toasty.success(c, toast, length).show());
    }
}
