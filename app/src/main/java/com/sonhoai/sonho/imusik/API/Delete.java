package com.sonhoai.sonho.imusik.API;

import android.os.AsyncTask;
import android.util.Log;

import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Interface.CallBack;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Delete extends AsyncTask<String, Void, String> {
    CallBack<String> callBack;

    public Delete(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = Connect.URL + strings[0];
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        int c;
        String result = "";

        try {
            url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();//truyen vao method
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setUseCaches(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();

            //khác -1 là vẫn còn
            while ((c = inputStream.read()) != -1) {
                result += (char) c;
            }
            if (result != null) {
                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "404";
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        Log.i("DELETE_STATUS", o);
        callBack.onSuccess(o);
    }
}
