package com.sonhoai.sonho.imusik.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.API.Post;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginFragmentDialog extends DialogFragment {
    private static LoginFragmentDialog loginFragmentDialog = null;
    private Button btnLogin, btnRegister;
    private TextView btnShowRegister, btnShowLogin;
    private EditText edtEmail, edtPass;

    public LoginFragmentDialog() {
    }
    public static LoginFragmentDialog newInstance() {
        if(loginFragmentDialog==null){
            loginFragmentDialog = new LoginFragmentDialog();
        }
        return loginFragmentDialog;

    }
    @Override
    public void dismiss() {
    }

    @Override
    public boolean isCancelable() {
        return super.isCancelable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().getAttributes().windowAnimations =  R.style.Slide_Up_Down;
        View view = inflater.inflate(R.layout.fragment_login, container);
        init(view);
        return view;
    }


    private void init(View view){
        btnLogin = view.findViewById(R.id.btnLogin);
        edtEmail = view.findViewById(R.id.userEmail);
        edtPass = view.findViewById(R.id.userPass);
        btnShowRegister = view.findViewById(R.id.btnShowRegister);
        btnShowLogin = view.findViewById(R.id.btnShowLogin);
        btnRegister = view.findViewById(R.id.btnRegister);


        //init fun
        initFun();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void setStyle(int style, int theme) {
        super.setStyle(style, theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.7);
        Dialog d = getDialog();
        if (d!=null){
            d.getWindow().setLayout(width, height);
            d.getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private void initFun(){
        actionLogin();
        showRegister();
    }

    private void showRegister(){
        btnShowRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setVisibility(View.GONE);
                btnShowLogin.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
                btnShowRegister.setVisibility(View.GONE);
            }
        });
        btnShowLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setVisibility(View.GONE);
                btnShowRegister.setVisibility(View.VISIBLE);
                btnShowLogin.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        });
    }

    private void actionLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(edtEmail.getText().toString(),edtPass.getText().toString());
            }
        });
    }
    private void handleLogin(String email, String pass){
        JSONObject dataSend = new JSONObject();
        try {
            dataSend.put("email", email);
            dataSend.put("pass", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Post(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject object = new JSONObject(result);
                    if(object.getInt("status") == 200){
                        SharedPreferencesHelper.getInstance(
                                getContext()).setSharePre(
                                        "USERINFO",
                                Context.MODE_PRIVATE,
                                "tokenUser",
                                object.getString("message")
                        );
                        SharedPreferencesHelper.getInstance(
                                getContext()).setSharePre(
                                "USERINFO",
                                Context.MODE_PRIVATE,
                                "idUser",
                                object.getString("id")
                        );
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {

            }
        }, dataSend).execute("/Auth/Login");
    }
}
