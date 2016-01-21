package com.clover_studio.spikachatmodule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.clover_studio.spikachatmodule.models.Config;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class Preferences {

    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.Preferences.TOKEN, token);
        editor.apply();
    }

    public String getToken(){
        return sharedPreferences.getString(Const.Preferences.TOKEN, "");
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.Preferences.USER_ID, userId);
        editor.apply();
    }

    public String getUserId(){
        return sharedPreferences.getString(Const.Preferences.USER_ID, "");
    }

    public void setConfig(Config config) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.Preferences.SOCKET_URL, config.socketUrl);
        editor.putString(Const.Preferences.BASE_URL, config.apiBaseUrl);
        editor.apply();
    }

    public Config getConfig(){
        Config config = new Config();
        config.apiBaseUrl = sharedPreferences.getString(Const.Preferences.BASE_URL, "");
        config.socketUrl = sharedPreferences.getString(Const.Preferences.SOCKET_URL, "");
        config.showSidebar = false;
        config.showTitlebar = false;
        return config;
    }

}
