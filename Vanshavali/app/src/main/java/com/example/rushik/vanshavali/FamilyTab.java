package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import VanshavaliServices.MainServices;
import androidx.annotation.NonNull;
import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;
import es.dmoral.toasty.Toasty;

public class FamilyTab extends AppCompatActivity {
    private int nodeCount = 1;
    public SimpleAdapter adapter;
    public ListView member_list;
    final static ArrayList<HashMap<String, ?>> member_data = new ArrayList<HashMap<String, ?>>();
    public String family_id;

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
                        Toasty.success(FamilyTab.this, family_id).show();
                        break;
                    case "Treeview":
                        getTreeView();
                        Toasty.success(FamilyTab.this, family_id).show();
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

        //Set Tree VIew Invisible
        FrameLayout TreeView = (FrameLayout)findViewById(R.id.TreeView);
        TreeView.setVisibility(View.GONE);

        //List View Visisble
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
                                                row.put("MemberId", temp.getString("member_id"));
                                                row.put("MemberName", temp.getString("member_full_name"));
                                                row.put("MemberParentId",temp.getString("member_parent_id"));
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
                new String[]{"Icon", "MemberName","MemberId", "MemberGender","MemberParentId"},
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


    public void getTreeView() {
        int len = member_data.size();
        //Node[] node_list = new Node[len];
         HashMap<String, Node> nodes= new HashMap<String, Node>();
        //Integer[] temp_ids = new Integer[len];
        for(HashMap<String, ?> row:member_data){
            String member_id = row.get("MemberId").toString();
            String parent_id = row.get("MemberParentId").toString();
            String name = row.get("MemberName").toString();
            String gender = row.get("MemberGender").toString();
            String json_data = "{'member_id':'"+member_id+"' , 'member_parent_id':'"+parent_id+"' , 'member_name':'"+name+"' , 'member_gender':'"+gender+"' , 'node_id':'"+member_id+"'}";
            nodes.put("member_"+member_id,new Node(json_data));
        }
       /* for(int i=0;i<len-1;i++){
            HashMap<String, ?> row = member_data.get(i);
            String member_id = row.get("MemberId").toString();
            String parent_id = row.get("MemberParentId").toString();
            String name = row.get("MemberName").toString();
            String gender = row.get("MemberGender").toString();
            String json_data = "{'member_id':'"+member_id+"' , 'member_parent_id':'"+parent_id+"' , 'member_name':'"+name+"' , 'member_gender':'"+gender+"' , 'node_id':'"+member_id+"'}";
            nodes.put("member_"+member_id,new Node(json_data));
        }*/


        ListView member_list = (ListView)findViewById(R.id.member_list);
        member_list.setVisibility(View.GONE);

        FrameLayout TreeView = (FrameLayout)findViewById(R.id.TreeView);
        TreeView.setVisibility(View.VISIBLE);
        GraphView graphView = findViewById(R.id.graph);

        final Graph graph = new Graph();

        /*for(int i=0;i<len-1;i++){
            HashMap<String, ?> row = member_data.get(i);
            String member_id = row.get("MemberId").toString();
            String parent_id = row.get("MemberParentId").toString();
            String name = row.get("MemberName").toString();
            String gender = row.get("MemberGender").toString();
            Log.d("Message:","parent_id:"+parent_id + "  member_id:"+member_id);
            if(!parent_id.equals("0"))
                graph.addEdge(nodes.get("member_"+parent_id),nodes.get("member_"+member_id));
        }*/
        for(HashMap<String, ?> row:member_data){
            String member_id = row.get("MemberId").toString();
            String parent_id = row.get("MemberParentId").toString();
            String name = row.get("MemberName").toString();
            String gender = row.get("MemberGender").toString();
            //String json_data = "{'member_id':'"+member_id+"' , 'member_parent_id':'"+parent_id+"' , 'member_name':'"+name+"' , 'member_gender':'"+gender+"' , 'node_id':'"+member_id+"'}";
            if(!parent_id.equals("0"))
                graph.addEdge(nodes.get("member_"+parent_id),nodes.get("member_"+member_id));
        }



        // you can set the graph via the constructor or use the adapter.setGraph(Graph) method
        final BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>(this, R.layout.node, graph) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(View view) {
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                try{
                    JSONObject obj = new JSONObject(data.toString());
                    String member_name = obj.getString("member_name");
                    String gender = obj.getString("member_gender");
                    viewHolder.mTextView.setText(member_name);
                    if(gender.equals("1"))
                        viewHolder.icon.setImageResource(R.drawable.male);
                    else
                        viewHolder.icon.setImageResource(R.drawable.female);

                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("Message new ",data.toString());

            }
        };


        // set the algorithm here
        final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                /*.setSiblingSeparation(200)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)*/
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_LEFT_RIGHT)
                .build();
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));
        graphView.setAdapter(adapter);


    }

    public void btnzoomIn_onClick(View v) {
        GraphView graphView = findViewById(R.id.graph);
        graphView.zoomIn();
    }

    public void btnzoomOut_onClick(View v) {
        GraphView graphView = findViewById(R.id.graph);
        graphView.zoomOut();
    }


    private class ViewHolder {
        TextView mTextView;
        ImageView icon;

        ViewHolder(View view) {
            mTextView = view.findViewById(R.id.member_name);
            icon = view.findViewById(R.id.member_image);
            icon.setImageResource(R.drawable.female);
        }
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
