package com.sonhoai.sonho.imusik.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.API.Post;
import com.sonhoai.sonho.imusik.API.Put;
import com.sonhoai.sonho.imusik.Adapters.PlayListAdapter;
import com.sonhoai.sonho.imusik.Adapters.RatingAdapter;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Models.Rate;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RateFragment extends DialogFragment {
    private String idSong;
    private RecyclerView rvRate;
    private List<Rate> rates;
    private RatingBar rbNumUser, rbNum;
    private RatingAdapter ratingAdapter;
    private TextView txtRbText, txtrate5, txtrate4,txtrate3,txtrate2,txtrate1, btnUserRate;
    private static int STATUS_RATE_USER = 0;
    public RateFragment() {

    }
    public static RateFragment newInstance(String idSong) {
        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        args.putString("ID_SONG", idSong);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idSong = getArguments().getString("ID_SONG");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().getAttributes().windowAnimations =  R.style.Slide_Up_Down;
        View view = inflater.inflate(R.layout.fragment_rate, container);
        init(view);
        return view;
    }

    private void init(View view) {
        rvRate = view.findViewById(R.id.rvRatingSong);
        rbNumUser = view.findViewById(R.id.rbNumUser);
        rbNum = view.findViewById(R.id.rbNum);
        txtRbText = view.findViewById(R.id.txtRbText);
        txtrate5 = view.findViewById(R.id.txtrate5);
        txtrate4 = view.findViewById(R.id.txtrate4);
        txtrate3 = view.findViewById(R.id.txtrate3);
        txtrate2 = view.findViewById(R.id.txtrate2);
        txtrate1 = view.findViewById(R.id.txtrate1);
        btnUserRate = view.findViewById(R.id.btnUserRate);

        rates = new ArrayList<>();
        ratingAdapter = new RatingAdapter(rates,getContext());
        rvRate.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rvRate.setLayoutManager(manager);
        rvRate.setAdapter(ratingAdapter);

        //get rate
        getRate();
        handleUserRateSong();
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.9);
        Dialog d = getDialog();
        if (d!=null){
            d.getWindow().setLayout(width, height);
            d.getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private void getRate(){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray listRate = object.getJSONArray("loves");
                    JSONObject userRate = object.getJSONObject("user");
                    JSONObject sumRate = object.getJSONObject("sum");

                    rateUser(userRate);
                    updateList(listRate);
                    allRate(sumRate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String result) {

            }
        }).execute("/Loves/"+idSong+"?idUser="+ SharedPreferencesHelper.getInstance(getContext()).getIdUser()+"&token="+SharedPreferencesHelper.getInstance(getContext()).getToken());
    }

    private void updateList(final JSONArray object){
        for (int i = 0; i < object.length();i++){
            try {
                JSONObject obj = object.getJSONObject(i);
                rates.add(new Rate(
                   obj.getString("user"),
                   obj.getInt("noLove"),
                   obj.getInt("littleLove"),
                   obj.getInt("love"),
                   obj.getInt("lotsofLove"),
                   obj.getInt("superLove")
                ));

                ratingAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void rateUser(JSONObject object){
        try {
            int a= object.getInt("noLove");
            int b = object.getInt("littleLove");
            int c = object.getInt("love");
            int d = object.getInt("lotsofLove");
            int e = object.getInt("superLove");
            if(a != 0){
                rbNumUser.setRating(1);
                STATUS_RATE_USER = 1;
            }else if(b != 0){
                rbNumUser.setRating(2);
                STATUS_RATE_USER = 1;
            }else if(c != 0){
                rbNumUser.setRating(3);
                STATUS_RATE_USER = 1;
            }else if(d != 0){
                rbNumUser.setRating(4);
                STATUS_RATE_USER = 1;
            }else if(e != 0){
                rbNumUser.setRating(5);
                STATUS_RATE_USER = 1;
            }else {
                STATUS_RATE_USER = 0;
            }
            rbNumUser.setTag(object.getInt("idLove"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void allRate(JSONObject object) {
        try {
            int a= object.getInt("noLove");
            int b = object.getInt("littleLove");
            int c = object.getInt("love");
            int d = object.getInt("lotsofLove");
            int e = object.getInt("superLove");
            double sum = (((a*1)+(b*2)+(c*3)+(d*4)+(e*5))/(a+b+c+d+e));
            rbNum.setRating((int)sum);
            txtRbText.setText(sum+"");
            txtrate1.setText(a+"");
            txtrate2.setText(b+"");
            txtrate3.setText(c+"");
            txtrate4.setText(d+"");
            txtrate5.setText(e+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleUserRateSong(){
        btnUserRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rate = (int) rbNumUser.getRating();
                JSONObject userRate = new JSONObject();
                try {
                    userRate.put("idLove", rbNumUser.getTag());
                    userRate.put("idUser",SharedPreferencesHelper.getInstance(getContext()).getIdUser());
                    userRate.put("idSong",idSong);
                    userRate.put("noLove",rate==1? 1: 0);
                    userRate.put("littleLove",rate==2? 1: 0);
                    userRate.put("love1",rate==3? 1: 0);
                    userRate.put("lotsofLove",rate==4? 1: 0);
                    userRate.put("superLove",rate==5? 1: 0);
                    if(STATUS_RATE_USER == 0){
                        sendRatePost(userRate);
                    }else if(STATUS_RATE_USER == 1){
                        sendRatePut(userRate);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendRatePost(JSONObject object){
        new Post(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("status")==200){
                        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String result) {

            }
        }, object).execute("/Loves/?idUser="+SharedPreferencesHelper.getInstance(getContext()).getIdUser()+"&token="+SharedPreferencesHelper.getInstance(getContext()).getToken());
    }
    private void sendRatePut(JSONObject object){
        new Put(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject res = new JSONObject(result);
                    if(res.getInt("status")==200){
                        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String result) {

            }
        }, object).execute("/Loves/"+rbNumUser.getTag()+"/?idUser="+SharedPreferencesHelper.getInstance(getContext()).getIdUser()+"&token="+SharedPreferencesHelper.getInstance(getContext()).getToken());
    }
}
