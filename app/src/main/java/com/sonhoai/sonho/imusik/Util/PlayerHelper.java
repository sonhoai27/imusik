package com.sonhoai.sonho.imusik.Util;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;

import com.sonhoai.sonho.imusik.Activities.PlayerActivity;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerHelper {
    private static PlayerHelper instanse = null;
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private String state;
    private Song currentSong;

    public static PlayerHelper getInstance() {
        if (instanse == null) {
            instanse = new PlayerHelper();
        }
        return instanse;
    }

    private PlayerHelper() {
        mediaPlayer = new MediaPlayer();
        songList = new ArrayList<>();
        state = State.STOP;
        currentSong = null;
    }

    public void play(final Song song){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        addSong(song);
        currentSong = song;
        mediaPlayer.reset();
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
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
                mediaPlayer.start();
                state = State.PLAY;
            }
        };
        task.execute(song.getUrlSong());
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
