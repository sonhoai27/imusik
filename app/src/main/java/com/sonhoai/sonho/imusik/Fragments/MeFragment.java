package com.sonhoai.sonho.imusik.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Post;
import com.sonhoai.sonho.imusik.Adapters.SongAdapter;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

import org.json.JSONObject;

public class MeFragment extends Fragment {
    private Button btnLogout, btnChangePass;
    public MeFragment() {

    }
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnLogout = view.findViewById(R.id.btnLogout);

        restartApp();
        changePass();
    }

    private void restartApp(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper.getInstance(
                        getContext()).setSharePre(
                        "USERINFO",
                        Context.MODE_PRIVATE,
                        "tokenUser",
                        ""
                );
                SharedPreferencesHelper.getInstance(
                        getContext()).setSharePre(
                        "USERINFO",
                        Context.MODE_PRIVATE,
                        "idUser",
                        ""
                );
                SongAdapter.helper.onStop();
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                Toast.makeText(getContext(), "Log Out Ok.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changePass(){
       btnChangePass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.myDialog));
               builder.setTitle("Change your password");
               LayoutInflater inflater = LayoutInflater.from(getContext());
               View view = inflater.inflate(R.layout.dialog_change_pass, null);
               final EditText edtPass = view.findViewById(R.id.edtChangePass);
               builder.setView(view);
               builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       doPostChangePass(edtPass.getText().toString());
                   }
               });
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                   }
               });
               builder.show();
           }
       });
    }

    private void doPostChangePass(String pass){
        JSONObject object = new JSONObject();
        try{
            object.put("pass",  pass);
            object.put("id",  SharedPreferencesHelper.getInstance(getContext()).getIdUser());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        new Post(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String result) {

            }
        }, object).execute("/Auth/Changepass/?token="+SharedPreferencesHelper.getInstance(getContext()).getToken());
    }
}
