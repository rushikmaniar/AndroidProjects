package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONObject;

import java.util.List;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;

public class ResetPassword extends AppCompatActivity implements Validator.ValidationListener  {
    public Validator validator;
    @NotEmpty
    @Email
    EditText editText_username;

    @NotEmpty
    @Password
    EditText editText_pass;

    @NotEmpty
    @ConfirmPassword
    EditText editText_conf_pass;

    String random_code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        validator = new Validator(this);
        validator.setValidationListener(this);

        editText_username = (EditText)findViewById(R.id.editText_username);
        editText_pass = (EditText)findViewById(R.id.editText_pass);
        editText_conf_pass = (EditText)findViewById(R.id.editText_conf_pass);

        random_code = getIntent().getExtras().get("random_code").toString();

    }

    @Override
    public void onValidationSucceeded() {
        Thread connthread = new Thread() {
            @Override
            public void run() {

                if (MainServices.isConnectedToVanshavaliServer()) {
                    MainServices obj = new MainServices();
                    obj.params.put("user_email", editText_username.getText().toString());
                    obj.params.put("user_pass", editText_pass.getText().toString());
                    obj.params.put("random_code", random_code);
                    try {
                        String response = obj.post("login/resetPassword", obj.params);
                        JSONObject jsonobj = new JSONObject(response);
                        jsonobj = jsonobj.getJSONObject("vanshavali_response");
                        Log.d("response", response);
                        if (jsonobj.getInt("code") == 200) {
                            showToasty("success",ResetPassword.this,jsonobj.getString("message"),Toasty.LENGTH_LONG);
                            Intent i = new Intent(ResetPassword.this,LoginActivity.class);
                            startActivity(i);
                        } else {
                            showToasty("error",ResetPassword.this,jsonobj.getString("message"),Toasty.LENGTH_LONG);
                        }


                    } catch (Exception e) {
                        Log.d("response", e.getMessage());
                    }

                } else {
                    //server connection error
                    showToasty("error",ResetPassword.this,"Server Connection Error",Toasty.LENGTH_LONG);
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

    public void ResetBtnSubmit(View view){
        validator.validate();
    }
    public void showToasty(String type, Context c, final String toast, int length) {
        if (type.equals("error"))
            runOnUiThread(() -> Toasty.error(c, toast, length).show());
        if (type.equals("success"))
            runOnUiThread(() -> Toasty.success(c, toast, length).show());
    }


}
