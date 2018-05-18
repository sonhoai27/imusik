package com.sonhoai.sonho.imusik.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sonhoai.sonho.imusik.Adapters.SongAdapter;
import com.sonhoai.sonho.imusik.Models.Song;

public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_NEXT = "com.sonhoai.sonnho.imusik.ACTION_NEXT";
    public static final String ACTION_STOP = "com.sonhoai.sonnho.imusik.ACTION_STOP";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(ACTION_NEXT)){
            SongAdapter.helper.onNext();
        } else if(action.equals(ACTION_STOP)){
            SongAdapter.helper.onStop();
            SongAdapter.helper.stopApp();
        }
    }
}
