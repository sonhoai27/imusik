package com.sonhoai.sonho.imusik.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static SharedPreferencesHelper instanse = null;
    private Context context;
    private int mode;

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instanse == null) {
            instanse = new SharedPreferencesHelper(context);
        }
        return instanse;
    }
    private SharedPreferencesHelper(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharePre(String name, int mode){
        return context.getSharedPreferences(name, mode);
    }
    public void setSharePre(String nameSharePre, int mode,String name ,String content){
        SharedPreferences sharedPreferences= context.getSharedPreferences(nameSharePre, mode);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name,content);
        editor.commit();
    }

    private String idUser;
    private String token;

    public String getIdUser() {
        return idUser = getSharePre("USERINFO", Context.MODE_PRIVATE).getString("idUser", "");
    }

    public String getToken() {
        return token = getSharePre("USERINFO", Context.MODE_PRIVATE).getString("tokenUser", "");
    }
}
