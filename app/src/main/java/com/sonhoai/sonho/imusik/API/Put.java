package com.sonhoai.sonho.imusik.API;

import android.os.AsyncTask;
import android.util.Log;

import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Interface.CallBack;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Put extends AsyncTask<String, Void, String> {
    CallBack<String> callBack;
    JSONObject data;

    public Put(CallBack<String> callBack, JSONObject data) {
        this.callBack = callBack;
        this.data = data;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = Connect.URL + strings[0];
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        OutputStream outStream;
        int c;
        String result = "";
        try {
            url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();//truyen vao method
            httpURLConnection.setRequestMethod("PUT");

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setRequestProperty("Accept","application/json");

            outStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
            Log.i("AAA", data.toString());
            outStream.write(data.toString().getBytes(Charset.forName("UTF-8")));
            outStream.flush();
            outStream.close();

            inputStream = httpURLConnection.getInputStream();
            //khác -1 là vẫn còn
            while ((c=inputStream.read()) != -1){
                result+=(char)c;
            }
            return result;
        } catch (Exception e) {
            //that bai
            e.printStackTrace();
            return "400";
        }
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        Log.i("GET_STATUS", data);
        callBack.onSuccess(data);
    }
}
