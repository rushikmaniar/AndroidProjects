package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import VanshavaliServices.MainServices;
import es.dmoral.toasty.Toasty;

public class FamilyTab extends AppCompatActivity {

    public SimpleAdapter adapter;
    public ListView member_list;
    final static ArrayList<HashMap<String, ?>> member_data = new ArrayList<HashMap<String, ?>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
       /* mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        try {
            tab.select();
            getMemberList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    //Family Tree List
                    case "FamilyTree":
                        //goto family List Activity
                        Intent i = new Intent(FamilyTab.this, FamilyList.class);
                        startActivity(i);
                        break;

                    //Show List Of Family Members
                    case "FamilyMember":
                        getMemberList();
                        Toasty.success(FamilyTab.this, "FamilyMember").show();
                        break;
                    case "Treeview":

                        Toasty.success(FamilyTab.this, "Treeview").show();
                        break;
                    case "Calender":
                        Toasty.success(FamilyTab.this, "Calender").show();
                        break;
                }
                // Toasty.success(FamilyTab.this,tab.getText()).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_family_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Function To get  MemberList
     * */
    public void getMemberList() {

        member_list = (ListView) findViewById(R.id.member_list);
        member_list.setVisibility(View.VISIBLE);
        member_list.setAdapter(null);
        member_data.clear();
        //check if shared preference Key exists
        SharedPreferences pref = FamilyTab.this.getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        if (pref.contains("vanshavali_mobile_user_email")) {
            //check user and token
            String user_name = pref.getString("vanshavali_mobile_user_email", "0");
            String user_token = pref.getString("vanshavali_mobile_user_token", "0");
            String family_id = pref.getString("vanshavali_mobile_family_id", "0");
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
                                                row.put("MemberId", temp.getString("member_id"));
                                                row.put("MemberName", temp.getString("member_full_name"));
                                                row.put("MemberGender", temp.getString("member_gender"));
                                                member_data.add(row);
                                                //Log.d("Message", member_list.get(i).toString());
                                            }

                                        } else {
                                            showToasty("error", FamilyTab.this, "No Data Found", Toasty.LENGTH_LONG);
                                        }

                                    } else {
                                        showToasty("error", FamilyTab.this, jsonobj.getString("message"), Toasty.LENGTH_LONG);
                                    }
                                } catch (IOException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("success", FamilyTab.this, e.getMessage(), Toasty.LENGTH_LONG);
                                } catch (JSONException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("error", FamilyTab.this, e.getMessage(), Toasty.LENGTH_LONG);
                                }


                            } else {
                                //User is Invalid . Destroy Preference
                                edit.clear();
                                edit.apply();
                                Intent i = new Intent(FamilyTab.this, LoginActivity.class);
                                showToasty("error", FamilyTab.this, "User Invalid . Login Again", Toasty.LENGTH_LONG);
                                startActivity(i);
                            }
                        } else {
                            Log.d("Message", "In Else Part");
                            edit.clear();
                            edit.apply();
                            Intent i = new Intent(FamilyTab.this, LoginActivity.class);
                            showToasty("error", FamilyTab.this, "Server Connection Error", Toasty.LENGTH_LONG);
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


            }
        } else {
            //User is Invalid . Destroy Preference
            edit.clear();
            edit.apply();
            Intent i = new Intent(FamilyTab.this, LoginActivity.class);
            showToasty("error", FamilyTab.this, "User Invalid . Login Again", Toasty.LENGTH_LONG);
            startActivity(i);
        }


        adapter = new SimpleAdapter(this,
                member_data,
                R.layout.member_list_row,
                new String[]{"Icon", "MemberName", "MemberId", "MemberGender"},
                new int[]{R.id.member_list_row, R.id.member_name});
        member_list.setAdapter(adapter);

        member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(FamilyTab.this, FamilyTab.class);
                startActivity(i);
            }
        });

    }



    public void Logout() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
        Intent i = new Intent(FamilyTab.this, LoginActivity.class);
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
