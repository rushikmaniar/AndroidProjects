package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;

public class EditFamilyTree extends AppCompatActivity implements Validator.ValidationListener{
    public Validator validator;

    private String user_name;
    private String user_token;
    private String family_name;

    private SharedPreferences pref;
    @NotEmpty
    private EditText editText_family_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_family_tree);

        pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        user_name = pref.getString("vanshavali_mobile_user_email", "0");
        user_token = pref.getString("vanshavali_mobile_user_token", "0");


        //edit_text_member_name
        editText_family_name = (EditText) findViewById(R.id.editText_family_name);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    @Override
    public void onValidationSucceeded() {

        family_name = editText_family_name.getText().toString();


        //set shared prefrence

        Thread connthread = new Thread() {
            @Override
            public void run() {

                if (MainServices.isConnectedToVanshavaliServer()) {

                    editText_family_name = (EditText) findViewById(R.id.editText_member_name);


                    MainServices obj = new MainServices();

                    obj.params.put("user_email", user_name);
                    obj.params.put("token", user_token);
                    obj.params.put("family_tree_name",family_name);



                    try {
                        String response = obj.post("FamilyTree/createFamilyTree", obj.params);
                        JSONObject jsonobj = new JSONObject(response);
                        jsonobj = jsonobj.getJSONObject("vanshavali_response");
                        if (jsonobj.getInt("code") == 200) {
                            showToasty("success", EditFamilyTree.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                            Intent i = new Intent(EditFamilyTree.this,FamilyList.class);
                            startActivity(i);
                            finish();
                        } else {
                            showToasty("error", EditFamilyTree.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                        }
                    } catch (IOException e) {
                        Log.e("Io Exception ", e.getMessage());
                        showToasty("success", EditFamilyTree.this, e.getMessage(), Toasty.LENGTH_LONG);
                    } catch (JSONException e) {
                        Log.e("Io Exception ", e.getMessage());
                        showToasty("error", EditFamilyTree.this, e.getMessage(), Toasty.LENGTH_LONG);
                    }


                } else {
                    //server connection error
                    showToasty("error",EditFamilyTree.this,"Server Connection Error",Toasty.LENGTH_LONG);
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

    public void addFamilyTreeBtnSubmit(View view) {
        validator.validate();
    }

    public void Logout() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
        Intent i = new Intent(EditFamilyTree.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void showToasty(String type, Context c, final String toast, int length) {
        if (type.equals("error"))
            runOnUiThread(() -> Toasty.error(c, toast, length).show());
        if (type.equals("success"))
            runOnUiThread(() -> Toasty.success(c, toast, length).show());
    }
}
