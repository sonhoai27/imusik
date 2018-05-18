package com.sonhoai.sonho.imusik.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.Adapters.ForYouAdapter;
import com.sonhoai.sonho.imusik.Adapters.SongAdapter;
import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {
    private Handler handler;
    private ImageView btnClosePlayer,
            imgShowCurrentList,
            btnPlayerMore;
    private static TextView titleSinger, titleSong;
    private static ImageView imgCoverSong, imgPlayPause,imgNextSong;
    public static Context context;

    private List<Song> songList;
    private SongAdapter songAdapter;
    private SeekBar seekBarPlayer;
    private RecyclerView lvCurrentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();
        if(PlayerHelper.getInstance().getState() == State.PLAY){
            handleSeekBar();
        }
    }

    private void init(){
        lvCurrentSong = findViewById(R.id.listCurrentSongs);
        btnClosePlayer = findViewById(R.id.btnClosePlayer);
        imgCoverSong = findViewById(R.id.coverSong);
        imgPlayPause = findViewById(R.id.playPause);
        imgNextSong = findViewById(R.id.nextSong);
        btnPlayerMore = findViewById(R.id.btnPlayerMore);

        titleSong = findViewById(R.id.tileSong);
        titleSinger = findViewById(R.id.tileSinger);
        context = getApplicationContext();

        //seek bar
        seekBarPlayer = findViewById(R.id.seekBarPlayer);
        seekBarPlayer.setEnabled(false);

        if(PlayerHelper.getInstance().getState() == State.PLAY){
            seekBarPlayer.setMax(PlayerHelper.getInstance().getMediaPlayer().getDuration());
        }else {
            seekBarPlayer.setMax(0);
        }

        initFun();
    }

    private void initFun(){
        showPlayerMore();
        closePlayer();
        showCurrentSong();
        nextSong();
        playPause();
    }

    private void initListSong(){

        songList = new ArrayList<>();
        lvCurrentSong.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                PlayerActivity.this,
                LinearLayoutManager.VERTICAL,
                false
        );
        lvCurrentSong.setLayoutManager(manager);
        songAdapter = new SongAdapter(PlayerActivity.this, songList);
        lvCurrentSong.setAdapter(songAdapter);
        initListCurrentSong();
    }

    private void initListCurrentSong(){
        if(PlayerHelper.getInstance().getSizeList() > 0){
            for(int i = 0; i < PlayerHelper.getInstance().getSizeList(); i++){
                songList.add(PlayerHelper.getInstance().getSongList().get(i));
            }
            songAdapter.notifyDataSetChanged();
            Log.i("KAKAAA", songList.size()+"AAA");
        }
    }
    private void showPlayerMore(){
        btnPlayerMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Song song = PlayerHelper.getInstance().getCurrentSong();
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PlayerActivity.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_show_more_player, null);
                builder.setView(dialogView);

                //khai bao

                final TextView titleSong, titleSinger, txtRbText;
                ImageView cover;
                final RatingBar rbNum;
                LinearLayout rateThisSong;

                //anh xa
                rbNum = dialogView.findViewById(R.id.rbNum);
                txtRbText = dialogView.findViewById(R.id.txtRbText);
                titleSinger = dialogView.findViewById(R.id.dPlayerMoreSinger);
                titleSong = dialogView.findViewById(R.id.dPlayerMoreTitle);
                cover = dialogView.findViewById(R.id.dPlayerMoreCover);
//                rateThisSong = dialogView.findViewById(R.id.rateThisSong);

                final AlertDialog alertDialog = builder.create();

                //custom position
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window dialogWindow = alertDialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();

                lp.x = 0;
                lp.y = 16; // The new position of the Y coordinates
                dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                dialogWindow.setAttributes(lp);

                if(PlayerHelper.getInstance().getSizeList() != 0){
                    //set data
                    Picasso.with(getApplicationContext())
                            .load(Connect.URLASSET+song.getImageSong())
                            .resize(200, 200)
                            .centerCrop()
                            .into(cover);
                    titleSinger.setText(song.getNameSinger());
                    titleSong.setText(song.getNameSong());


                    getRating(new CallBack<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                int a = object.getInt("noLove");
                                int b = object.getInt("littleLove");
                                int c = object.getInt("love");
                                int d = object.getInt("lotsofLove");
                                int e = object.getInt("superLove");
                                double sum = (((a*5)+(b*4)+(c*3)+(d*2)+(e*1))/(a+b+c+d+e));
                                txtRbText.setText(sum+"");
                                rbNum.setRating((float) sum);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(String result) {

                        }
                    }, PlayerHelper.getInstance().getCurrentSong().getId());
                }

                //show
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.Slide_Up_Down;
                alertDialog.show();
            }
        });
    }

    private void closePlayer(){
        btnClosePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void showCurrentSong(){
        if(PlayerHelper.getInstance().getSizeList() > 0 && PlayerHelper.getInstance().getCurrentSong()!=null){
            changeStatePlayPause();
            titleSinger.setText(PlayerHelper.getInstance().getCurrentSong().getNameSinger());
            titleSong.setText(PlayerHelper.getInstance().getCurrentSong().getNameSong());
            Picasso.with(context)
                    .load(Connect.URLASSET + PlayerHelper.getInstance().getCurrentSong().getImageSong())
                    .resize(550, 550)
                    .centerCrop()
                    .into(imgCoverSong);
        }
    }
    private void nextSong(){
        imgNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerHelper.getInstance().getSizeList() > 0){
                    PlayerHelper.getInstance().onNext();
                    showCurrentSong();
                }
            }
        });
    }
    private static void changeStatePlayPause(){
        if(PlayerHelper.getInstance().getState() == State.PLAY){
            imgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
        }else if(PlayerHelper.getInstance().getState() == State.PAUSE){
            imgPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }
    private void playPause(){
        imgPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerHelper.getInstance().getState() == State.PLAY){
                    PlayerHelper.getInstance().onPause();
                    changeStatePlayPause();
                }else if(PlayerHelper.getInstance().getState() == State.PAUSE){
                    PlayerHelper.getInstance().onResume();
                    changeStatePlayPause();
                    handleSeekBar();
                }
            }
        });
    }

    private void updateSeekBar(){
        seekBarPlayer.setProgress(PlayerHelper.getInstance().getMediaPlayer().getCurrentPosition());
        if(PlayerHelper.getInstance().getState() == State.PLAY){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }
    private void handleSeekBar(){
        handler = new Handler();
        updateSeekBar();
    }

    private void rateSong(Song song){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_bottom);
    }

    private void getRating(final CallBack<String> callBack, int idSong){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                callBack.onSuccess(result);
            }

            @Override
            public void onFail(String result) {

            }
        }).execute("/Loves/?idSong="+idSong+"&idUser="+ SharedPreferencesHelper.getInstance(context).getIdUser()+"&token="+SharedPreferencesHelper.getInstance(context).getToken());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initListSong();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListSong();
    }
}