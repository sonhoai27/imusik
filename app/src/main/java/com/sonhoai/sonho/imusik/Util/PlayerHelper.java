package com.sonhoai.sonho.imusik.Util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sonhoai.sonho.imusik.Activities.PlayerActivity;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.DetailPlayList;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerHelper extends Service{
    private static PlayerHelper instanse = null;
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private String state;
    private Song currentSong;

    private final IBinder mBinder = new ServiceBinder();

    public static PlayerHelper getInstance() {
        if (instanse == null) {
            instanse = new PlayerHelper();
        }
        return instanse;
    }

    public class ServiceBinder extends Binder {
        public PlayerHelper getService()
        {
            return PlayerHelper.this;
        }
    }

    private PlayerHelper() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();
        songList = new ArrayList<>();
        state = State.STOP;
        currentSong = null;
    }


    //services notitfication
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void play(final Song song){

        addSong(song);
        currentSong = song;
        mediaPlayer.reset();
        try{
            @SuppressLint("StaticFieldLeak") final AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... strings) {
                    try {
                        mediaPlayer.setDataSource(strings[0]);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "";
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try{
                        mediaPlayer.start();
                        state = State.PLAY;
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            };
            task.execute(song.getUrlSong());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onNext();
            }
        });

    }

    public void onPause() {
        if (state == State.PLAY) {
            mediaPlayer.pause();
            state = State.PAUSE;
            MainActivity.imgPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }

    public void onResume() {
        if (state == State.PAUSE) {
            mediaPlayer.start();
            state = State.PLAY;
            MainActivity.imgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
        }
    }

    public void onStop() {
        mediaPlayer.stop();
        state = State.STOP;
        currentSong = null;
        songList = null;
    }

    public void onNext(){
        int position = songList.indexOf(currentSong);
        if (position + 1 < songList.size()) {
            currentSong = songList.get(position + 1);
            play(currentSong);
        } else {
            currentSong = songList.get(0);
            play(currentSong);
        }
        MainActivity.txtNameCurrentSong.setText(currentSong.getNameSong());
    }

    public void removeSong(int id) {

    }

    public void addSong(Song song) {
        if (!songList.contains(song)) {
            songList.add(song);
        }
    }

    public void addPlayList(List<DetailPlayList>  songs){
        songList.clear();
        for(int i = 0; i < songs.size();i++){
            songList.add(new Song(
                    songs.get(i).getIdSong(),
                    songs.get(i).getIdKind(),
                    songs.get(i).getIdSinger(),
                    songs.get(i).getNameSong(),
                    songs.get(i).getNameSinger(),
                    songs.get(i).getImageSong(),
                    songs.get(i).getUrlSong(),
                    songs.get(i).getLuotNghe()
            ));
        }
        play(songList.get(0));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public static PlayerHelper getInstanse() {
        return instanse;
    }

    public static void setInstanse(PlayerHelper instanse) {
        PlayerHelper.instanse = instanse;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public int getSizeList() {
        return songList.size();
    }
}
