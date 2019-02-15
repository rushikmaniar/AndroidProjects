package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity  implements Validator.ValidationListener {

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

        editText_username = (EditText)findViewById(R.id.editText_username);
        editText_pass = (EditText)findViewById(R.id.editText_pass);

        //check if shared preference Key exists
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref",0);
        if(pref.contains("vanshavali_mobile_user_email")){
            Toasty.success(LoginActivity.this,pref.getString("vanshavali_mobile_user_email","default")).show();
        }

        validator = new Validator(this);
        validator.setValidationListener(this);


    }
    public void gotoSignUp(View v){
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }
    public void myToast(Context c,String message){
        Toast.makeText(c,message,Toast.LENGTH_LONG).show();
    }

    /*
    * @param View view
    * Function Called On Login Form submit
    *
    * */
    public void loginBtnSubmit(View view){
        //check For validation
        validator.validate();
    }


    @Override
    public void onValidationSucceeded() {
        //set shared prefrence
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref",0);

        editText_username = (EditText)findViewById(R.id.editText_username);
        editText_pass = (EditText)findViewById(R.id.editText_pass);

        SharedPreferences.Editor pref_edit = pref.edit();
        pref_edit.putString("vanshavali_mobile_user_email",editText_username.getText().toString());
        pref_edit.apply();

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
}
