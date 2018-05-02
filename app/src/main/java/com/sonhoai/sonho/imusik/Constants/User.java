package com.sonhoai.sonho.imusik.Constants;

import android.content.Context;

import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;


public class User {
    private static User user = null;
    private User(){}
    private Context context;
    public static User getInstance(Context context){
        context = context;
        if(user == null){
            user = new User();
        }
        return user;
    }
    public String idUser = SharedPreferencesHelper.getInstance(context).getSharePre("USERINFO", Context.MODE_PRIVATE).getString("idUser", "");
    public String token = SharedPreferencesHelper.getInstance(context).getSharePre("USERINFO", Context.MODE_PRIVATE).getString("tokenUser", "");
}
