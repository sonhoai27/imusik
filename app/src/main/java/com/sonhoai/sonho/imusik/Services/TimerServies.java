package com.sonhoai.sonho.imusik.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class TimerServies extends IntentService {

    public TimerServies(){
        super("Timer Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("timer", "Timer service has started");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        for(int i = 0; i < 5; i++){
            Log.i("Timer", "i = "+i);
            try{
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
    }
}
