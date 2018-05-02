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
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.CircleTransformHelper;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.squareup.picasso.Picasso;

public class PlayerActivity extends AppCompatActivity {
    private Handler handler;
    private ImageView btnClosePlayer,
            imgShowCurrentList,
            btnPlayerMore;
    private static TextView titleSinger, titleSong;
    private static ImageView imgCoverSong, imgPlayPause,imgNextSong;
    public static Context context;

    private SeekBar seekBarPlayer;

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
        btnClosePlayer = findViewById(R.id.btnClosePlayer);
        imgCoverSong = findViewById(R.id.coverSong);
        imgShowCurrentList = findViewById(R.id.currentList);
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

                TextView titleSong, titleSinger;
                ImageView cover;
                LinearLayout rateThisSong;

                //anh xa

                titleSinger = dialogView.findViewById(R.id.dPlayerMoreSinger);
                titleSong = dialogView.findViewById(R.id.dPlayerMoreTitle);
                cover = dialogView.findViewById(R.id.dPlayerMoreCover);
                rateThisSong = dialogView.findViewById(R.id.rateThisSong);

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


                    //rate this song
                    rateThisSong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rateSong(song);
                            alertDialog.dismiss();
                        }
                    });
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
        if(PlayerHelper.getInstance().getSizeList() > 0){
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
        overridePendingTransition(0, 0);
    }
}