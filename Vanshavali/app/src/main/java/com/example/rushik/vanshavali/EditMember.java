package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;

public class EditMember extends AppCompatActivity implements Validator.ValidationListener {
    final static ArrayList<Member> member_data = new ArrayList<>();
    public String family_id;
    SearchableSpinner member_list_spinner;
    public Validator validator;

    private String user_name;
    private String user_token;
    public String member_id;
    public String member_name;
    public String member_gender;
    public String member_parent_id;

    @NotEmpty
    private EditText editText_member_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        member_id = getIntent().getExtras().getString("member_id");
        member_name = getIntent().getExtras().getString("member_name");
        member_gender = getIntent().getExtras().getString("member_gender");
        member_parent_id = getIntent().getExtras().getString("member_parent_id");

        //set values
        editText_member_name = (EditText)findViewById(R.id.editText_member_name);
        editText_member_name.setText(member_name);
        RadioButton fbtn = (RadioButton)findViewById(R.id.radioButton_female);
        if(member_gender.equals("2"))
            fbtn.setChecked(true);



        Toasty.success(EditMember.this,member_id,Toasty.LENGTH_LONG).show();

        validator = new Validator(this);
        validator.setValidationListener(this);

        //edit_text_member_name
        editText_member_name = (EditText) findViewById(R.id.editText_member_name);
        //spinner view
        member_list_spinner = (SearchableSpinner) findViewById(R.id.member_list_spinner);
        member_list_spinner.setTitle("Select Item");
        member_list_spinner.setPositiveButton("OK");
        member_data.clear();
        member_data.add(new Member("0","family Root","0"));
        //check if shared preference Key exists
        SharedPreferences pref = EditMember.this.getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        if (pref.contains("vanshavali_mobile_user_email")) {
            //check user and token
            user_name = pref.getString("vanshavali_mobile_user_email", "0");
            user_token = pref.getString("vanshavali_mobile_user_token", "0");
            family_id = pref.getString("vanshavali_mobile_family_id", "0");
            Log.d("user_email", user_name);
            Log.d("user_token", user_token);


            if (!(user_name.equals("0") || user_token.equals("0") || family_id.equals("0"))) {
                //check if user is valid . check user exists and token


                Thread memberList = new Thread() {
                    @Override
                    public void run() {
                        if (MainServices.isConnectedToVanshavaliServer()) {
                            MainServices obj = new MainServices();
                            if (obj.isUserValid(user_name, user_token)) {
                                //user Logged In valid. fetch family List Records

                                obj.params.put("user_email", user_name);
                                obj.params.put("token", user_token);
                                obj.params.put("family_id", family_id);

                                try {
                                    String response = obj.post("MembersManage/getFamilyMemberList", obj.params);
                                    Log.d("response :", response);
                                    JSONObject jsonobj = new JSONObject(response);
                                    jsonobj = jsonobj.getJSONObject("vanshavali_response");
                                    if (jsonobj.getInt("code") == 200) {
                                        JSONObject data = jsonobj.getJSONObject("data");
                                        if (data.getInt("no_of_rows") > 0) {
                                            JSONArray family_list = data.getJSONArray("member_list");
                                            for (int i = 0; i < family_list.length(); i++) {

                                                JSONObject temp = family_list.getJSONObject(i);

                                                HashMap<String, Object> row = new HashMap<String, Object>();
                                                if (temp.getString("member_gender").equals("1"))
                                                    row.put("Icon", R.drawable.male);
                                                else
                                                    row.put("Icon", R.drawable.female);

                                                String temp_member_id = temp.getString("member_id");
                                                String temp_member_name = temp.getString("member_full_name");

                                                member_data.add(new Member(temp_member_id,temp_member_name,temp_member_id));

                                            }

                                        } else {
                                            showToasty("error", EditMember.this, "No Data Found", Toasty.LENGTH_LONG);
                                        }

                                    } else {
                                        showToasty("error", EditMember.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                                    }
                                } catch (IOException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("success", EditMember.this, e.getMessage(), Toasty.LENGTH_LONG);
                                } catch (JSONException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("error", EditMember.this, e.getMessage(), Toasty.LENGTH_LONG);
                                }


                            } else {
                                //User is Invalid . Destroy Preference
                                edit.clear();
                                edit.apply();
                                Intent i = new Intent(EditMember.this, LoginActivity.class);
                                showToasty("error", EditMember.this, "User Invalid . Login Again", Toasty.LENGTH_LONG);
                                startActivity(i);
                            }
                        } else {
                            Log.d("Message", "In Else Part");
                            edit.clear();
                            edit.apply();
                            Intent i = new Intent(EditMember.this, LoginActivity.class);
                            showToasty("error", EditMember.this, "Server Connection Error", Toasty.LENGTH_LONG);
                            startActivity(i);
                        }
                    }
                };

                memberList.start();
                try {
                    memberList.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ArrayAdapter myadapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,member_data);
                myadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                member_list_spinner.setAdapter(myadapter);




            }
        } else {
            //User is Invalid . Destroy Preference
            edit.clear();
            edit.apply();
            Intent i = new Intent(EditMember.this, LoginActivity.class);
            showToasty("error", EditMember.this, "User Invalid . Login Again", Toasty.LENGTH_LONG);
            startActivity(i);
        }
        
    }
    public void editMemeberBtnSubmit(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {

        Member i = (Member) member_list_spinner.getSelectedItem();
        String member_parent_id = i.getMember_parent_id();
        //set shared prefrence

        Thread connthread = new Thread() {
            @Override
            public void run() {

                if (MainServices.isConnectedToVanshavaliServer()) {

                    editText_member_name = (EditText) findViewById(R.id.editText_member_name);
                    Integer member_gender = 1;
                    RadioButton btn_female = (RadioButton) findViewById(R.id.radioButton_female);


                    if(btn_female.isChecked()){
                        member_gender = 2;
                    }

                    MainServices obj = new MainServices();

                    obj.params.put("user_email", user_name);
                    obj.params.put("token", user_token);
                    obj.params.put("family_id", family_id);

                    obj.params.put("member_id", member_id);
                    obj.params.put("member_name",editText_member_name.getText().toString());
                    obj.params.put("member_gender",String.valueOf(member_gender));
                    obj.params.put("member_parent_id",member_parent_id);

                    try {
                        String response = obj.post("MembersManage/editFamilyMember", obj.params);
                        Log.d("response :", response);
                        JSONObject jsonobj = new JSONObject(response);
                        jsonobj = jsonobj.getJSONObject("vanshavali_response");
                        if (jsonobj.getInt("code") == 200) {
                            showToasty("success", EditMember.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                            Intent i = new Intent(EditMember.this,FamilyTab.class);
                            startActivity(i);
                            finish();
                        } else {
                            showToasty("error", EditMember.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                        }
                    } catch (IOException e) {
                        Log.e("Io Exception ", e.getMessage());
                        showToasty("success", EditMember.this, e.getMessage(), Toasty.LENGTH_LONG);
                    } catch (JSONException e) {
                        Log.e("Io Exception ", e.getMessage());
                        showToasty("error", EditMember.this, e.getMessage(), Toasty.LENGTH_LONG);
                    }


                } else {
                    //server connection error
                    showToasty("error",EditMember.this,"Server Connection Error",Toasty.LENGTH_LONG);
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

    public void Logout() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
        Intent i = new Intent(EditMember.this, LoginActivity.class);
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
