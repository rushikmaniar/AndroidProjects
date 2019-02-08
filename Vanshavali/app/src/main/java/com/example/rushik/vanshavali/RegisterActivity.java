package com.example.rushik.vanshavali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

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

        //edit text
        editText_username = (EditText)findViewById(R.id.editText_username);
        editText_pass = (EditText)findViewById(R.id.editText_pass);


        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    /*
    * Goto Signin
    * */
    public void goToSignIn(View v){
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }

    /*
    * Onsubmit Check Validation
    * */
    public void btn_registerOnClick(View view){
        validator.validate();
        //checkif user exists
    }


    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
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
