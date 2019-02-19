package com.example.rushik.vanshavali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemberListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemberListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    final static ArrayList<HashMap<String, ?>> member_data = new ArrayList<HashMap<String, ?>>();

    public MemberListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberListFragment newInstance(String param1, String param2) {
        MemberListFragment fragment = new MemberListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMemberList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }



    /*
     * Function To get  MemberList
     * */
    public void getMemberList() {
        ListView member_list = (ListView)getView().findViewById(R.id.member_list);
        member_list.setAdapter(null);
        member_data.clear();
        //check if shared preference Key exists
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
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
                                    String response = obj.post("Members/getMemberList", obj.params);
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
                                                if(temp.getString("member_gender").equals("1"))
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
                                            showToasty("error", getActivity(), "No Data Found", Toasty.LENGTH_LONG);
                                        }

                                    } else {
                                        showToasty("error", getActivity(), jsonobj.getString("message"), Toasty.LENGTH_LONG);
                                    }
                                } catch (IOException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("success", getActivity(), e.getMessage(), Toasty.LENGTH_LONG);
                                } catch (JSONException e) {
                                    Log.e("Io Exception ", e.getMessage());
                                    showToasty("error", getActivity(), e.getMessage(), Toasty.LENGTH_LONG);
                                }


                            } else {
                                //User is Invalid . Destroy Preference
                                edit.clear();
                                edit.apply();
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                showToasty("error", getActivity(), "User Invalid . Login Again", Toasty.LENGTH_LONG);
                                startActivity(i);
                            }
                        } else {
                            Log.d("Message", "In Else Part");
                            edit.clear();
                            edit.apply();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            showToasty("error", getActivity(), "Server Connection Error", Toasty.LENGTH_LONG);
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
            Intent i = new Intent(getActivity(), LoginActivity.class);
            showToasty("error", getActivity(), "User Invalid . Login Again", Toasty.LENGTH_LONG);
            startActivity(i);
        }


        SimpleAdapter adapter = new SimpleAdapter(getContext(),
                member_data,
                R.layout.member_list_row,
                new String[]{"Icon", "MemberId","MemberName", "MemberGender"},
                new int[]{R.id.family_members_count, R.id.member_name});
        member_list.setAdapter(adapter);

        member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),FamilyTab.class);
                startActivity(i);
            }
        });

    }
    public void Logout(){
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("vanshavali-pref", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.clear();
        edit.apply();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void showToasty(String type, Context c, final String toast, int length) {
        if (type.equals("error"))
            getActivity().runOnUiThread(() -> Toasty.error(c, toast, length).show());
        if (type.equals("success"))
            getActivity().runOnUiThread(() -> Toasty.success(c, toast, length).show());
    }
}
