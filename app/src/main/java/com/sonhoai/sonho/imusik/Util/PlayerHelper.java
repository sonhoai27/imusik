package com.sonhoai.sonho.imusik.Util;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.Constraints;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.sonhoai.sonho.imusik.Activities.PlayerActivity;
import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Constants.State;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.DetailPlayList;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Services.AudioPlayerBroadcastReceiver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlayerHelper extends Service {
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private String state;
    private Song currentSong;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private final IBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder {
        public PlayerHelper getService() {
            return PlayerHelper.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        songList = new ArrayList<>();

        if (mediaPlayer == null) {
            initPlayerHelper();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlayerHelper() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        state = State.STOP;
        currentSong = null;
    }


    //services notitfication
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void play(final Song song) {

        addSong(song);
        currentSong = song;
        mediaPlayer.reset();
        try {
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
                    try {
                        mediaPlayer.start();
                        state = State.PLAY;
                        setNotification();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            task.execute(song.getUrlSong());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onNext();
            }
        });

    }

    private void setNotification() {
        Intent showPlayer = new Intent(this, PlayerActivity.class);
        PendingIntent pendingStop = PendingIntent.getBroadcast(this, 100, new Intent(AudioPlayerBroadcastReceiver.ACTION_STOP), 0);
        PendingIntent pendingNext = PendingIntent.getBroadcast(this, 100, new Intent(AudioPlayerBroadcastReceiver.ACTION_NEXT), 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, showPlayer, 0);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(currentSong.getNameSong())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .addAction(android.R.drawable.ic_menu_view, "Next", pendingNext)
                .addAction(android.R.drawable.ic_menu_view, "Stop", pendingStop)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .setContentText(currentSong.getNameSinger());
        notificationManager.notify(1, notificationBuilder.build());
        startForeground(1, notificationBuilder.getNotification());
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

    public void onNext() {
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

    public void reupdateNameSong(){
        MainActivity.txtNameCurrentSong.setText(currentSong.getNameSong());
    }
    public void addSong(Song song) {
        try {
            if (!songList.contains(song)) {
                songList.add(song);
            }
        } catch (Exception e) {

        }
    }

    public void stopApp() {
        songList.clear();
        mediaPlayer.reset();
        currentSong = null;
        stopService(new Intent(this, PlayerHelper.class));
    }

    public void addPlayList(List<DetailPlayList> songs) {
        songList.clear();
        for (int i = 0; i < songs.size(); i++) {
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
