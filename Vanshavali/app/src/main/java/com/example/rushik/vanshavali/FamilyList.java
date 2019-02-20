package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;

public class FamilyList extends AppCompatActivity {


    final static ArrayList<HashMap<String, ?>> family_data = new ArrayList<HashMap<String, ?>>();

    /*static {
        HashMap<String, Object> row = new HashMap<String, Object>();
        row.put("Icon", R.drawable.family_tree);
        row.put("FamilyName", "Family 1");
        row.put("Members_Count", "5 Members");
        family_data.add(row);
        row = new HashMap<String, Object>();
        row.put("Icon", R.drawable.family_tree);
        row.put("FamilyName", "Family 2");
        row.put("Members_Count", "5 Members");
        family_data.add(row);
        row = new HashMap<String, Object>();
        row.put("Icon", R.drawable.family_tree);
        row.put("FamilyName", "Family 3");
        row.put("Members_Count", "5 Members");
        family_data.add(row);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);



        ActionBar menu = getSupportActionBar();

        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_launcher_foreground);
        menu.setDisplayUseLogoEnabled(true);

        getFamilyTreeList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.Logout:
                Logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showToasty(String type, Context c, final String toast, int length) {
        if (type.equals("error"))
            runOnUiThread(() -> Toasty.error(c, toast, length).show());
        if (type.equals("success"))
            runOnUiThread(() -> Toasty.success(c, toast, length).show());
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("Message", "Onresume");
    }


    /*
     * Function To get Faimily TreeList
     * */
    public void getFamilyTreeList() {


        ListView family_list = (ListView) findViewById(R.id.family_list);
        family_list.setAdapter(null);
        family_data.clear();
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

                Thread familyList = new Thread() {
                    @Override
                    public void run() {
                        if (MainServices.isConnectedToVanshavaliServer()) {
                            MainServices obj = new MainServices();
                            if (obj.isUserValid(user_name, user_token)) {
                                //user Logged In valid. fetch family List Records

                                obj.params.put("user_email", user_name);
                                obj.params.put("token", user_token);
                                try {
                                    String response = obj.post("FamilyTree/getFamilyTreeList", obj.params);
                                    Log.d("response :", response);
                                    JSONObject jsonobj = new JSONObject(response);
                                    jsonobj = jsonobj.getJSONObject("vanshavali_response");
                                    if (jsonobj.getInt("code") == 200) {
                                        JSONObject data = jsonobj.getJSONObject("data");
                                        if (data.getInt("no_of_rows") > 0) {
                                            JSONArray family_list = data.getJSONArray("family_list");
                                            for (int i = 0; i < family_list.length(); i++) {
                                                JSONObject temp = family_list.getJSONObject(i);

                                                HashMap<String, Object> row = new HashMap<String, Object>();
                                                row.put("Icon", R.drawable.family_tree);
                                                row.put("FamilyId", temp.getString("family_tree_id"));
                                                row.put("FamilyName", temp.getString("family_tree_name"));
                                                row.put("Members_Count", temp.getString("member_count") + " Memebers");
                                                family_data.add(row);
                                                //Log.d("Message", family_list.get(i).toString());
                                            }

                                        } else {
                                            showToasty("error", FamilyList.this, "No Data Found", Toasty.LENGTH_LONG);
                                        }

                                    } else {
                                        showToasty("error", FamilyList.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                                    }
                                } catch (IOException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("success", FamilyList.this, e.getMessage(), Toasty.LENGTH_LONG);
                                } catch (JSONException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("error", FamilyList.this, e.getMessage(), Toasty.LENGTH_LONG);
                                }


                            } else {
                                //User is Invalid . Destroy Preference
                                edit.clear();
                                edit.apply();
                                Intent i = new Intent(FamilyList.this, LoginActivity.class);
                                showToasty("error", FamilyList.this, "User Invalid . Login Again", Toasty.LENGTH_LONG);
                                startActivity(i);
                            }
                        } else {
                            Log.d("Message", "In Else Part");
                            edit.clear();
                            edit.apply();
                            Intent i = new Intent(FamilyList.this, LoginActivity.class);
                            showToasty("error", FamilyList.this, "Server Connection Error", Toasty.LENGTH_LONG);
                            startActivity(i);
                        }
                    }
                };

                familyList.start();
                try {
                    familyList.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        } else {
            //User is Invalid . Destroy Preference
            edit.clear();
            edit.apply();
            Intent i = new Intent(FamilyList.this, LoginActivity.class);
            showToasty("error", FamilyList.this, "User Invalid . Login Again", Toasty.LENGTH_LONG);
            startActivity(i);
        }


        SimpleAdapter adapter = new SimpleAdapter(this,
                family_data,
                R.layout.family_list_row,
                new String[]{"Icon", "FamilyName", "Members_Count"},
                new int[]{R.id.family_list_row, R.id.family_name, R.id.family_members_count});
        family_list.setAdapter(adapter);

        family_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String family_tree_id = family_data.get(position).get("FamilyId").toString();
                edit.putString("vanshavali_mobile_family_id",family_tree_id);
                edit.apply();
                Intent i = new Intent(FamilyList.this,FamilyTab.class);
                startActivity(i);
            }
        });

    }
    public void Logout(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
        Intent i = new Intent(FamilyList.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


}
