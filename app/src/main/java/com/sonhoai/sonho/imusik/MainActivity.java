package com.sonhoai.sonho.imusik;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.Activities.PlayerActivity;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.Fragments.HomeFragment;
import com.sonhoai.sonho.imusik.Fragments.LibraryFragment;
import com.sonhoai.sonho.imusik.Fragments.LoginFragmentDialog;
import com.sonhoai.sonho.imusik.Fragments.MeFragment;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity{
    public static TextView txtNameCurrentSong;
    public static ImageView imgNextSong, imgPlayPause;
    public static Context context;

    private BottomNavigationView navigation;
    private LinearLayout linearLayoutBottom;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String token =  SharedPreferencesHelper.getInstance(getApplicationContext()).getSharePre("USERINFO", Context.MODE_PRIVATE).getString("tokenUser", "");
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(HomeFragment.newInstance());

                    return true;
                case R.id.navigation_hub:
                    if(token.isEmpty()){
                        Log.i("TOKEN", token);
                        showLoginView();
                        return false;
                    }else {
                        checkToken(token,new CallBack<Boolean>() {
                            @Override
                            public void onSuccess(Boolean result) {
                                if(result){
                                    setFragment(LibraryFragment.newInstance());
                                }else {
                                    showLoginView();
                                }
                            }

                            @Override
                            public void onFail(Boolean result) {

                            }
                        });
                        return true;
                    }
                case R.id.navigation_menu:
                    setFragment(MeFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setKeepScreenOn(true);
        setFragment(HomeFragment.newInstance());

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentlayout, fragment);
        transaction.commit();
    }

    private void init(){
        context = getApplicationContext();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        txtNameCurrentSong = findViewById(R.id.txtNameCurrentSong);
        imgNextSong = findViewById(R.id.imgNextSong);
        imgPlayPause = findViewById(R.id.imgPlayPause);
        linearLayoutBottom = findViewById(R.id.linearLayoutBottom);

        handleImgPlayPause();
        handleNextSong();
        handleShowPlayer();
    }

    private void handleShowPlayer() {
        linearLayoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up,R.anim.slide_bottom);
            }
        });
    }
    private void handleImgPlayPause(){
        imgPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerHelper.getInstance().getState() == State.PLAY){
                    PlayerHelper.getInstance().onPause();
                    imgPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }else if(PlayerHelper.getInstance().getState() == State.PAUSE){
                    PlayerHelper.getInstance().onResume();
                    imgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });
    }
    private void handleNextSong(){
        imgNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerHelper.getInstance().getSizeList() <= 1){
                    Toast.makeText(getApplicationContext(), "Danh sách chỉ chứa 1 bài.", Toast.LENGTH_SHORT).show();
                }else {
                    PlayerHelper.getInstance().onNext();
                }
            }
        });
    }

    private void checkToken(String token,final CallBack<Boolean> res){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TOENJSSS", result);
                try {
                    JSONObject  object = new JSONObject(result);
                    if(object.getString("message").equals("error")){
                        res.onSuccess(false);
                    }else if(object.getString("message").equals("ok")){
                        res.onSuccess(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {

            }
        }).execute("/Auth/CheckToken?token="+token);
    }

    private void showLoginView(){
        LoginFragmentDialog fragmentDialog = LoginFragmentDialog.newInstance();
        fragmentDialog.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppDialogFragmentTheme);
        FragmentManager fm = getSupportFragmentManager();
        fragmentDialog.show(fm, "Login");
    }
}
