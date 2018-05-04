package com.sonhoai.sonho.imusik.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

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
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                Toast.makeText(getContext(), "Log Out Ok.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
