package com.clover_studio.spikachatmodule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.models.GetStickersData;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.models.Sticker;
import com.clover_studio.spikachatmodule.models.StickerCategory;
import com.clover_studio.spikachatmodule.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public void increaseClickSticker(Sticker sticker){
        String json = getStickersString();
        if(!TextUtils.isEmpty(json)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                StickerCategory responseModel = mapper.readValue(json, StickerCategory.class);
                if(responseModel.list.contains(sticker)){
                    int position = responseModel.list.indexOf(sticker);
                    responseModel.list.get(position).timesClicked ++;
                }else{
                    sticker.timesClicked = 1;
                    responseModel.list.add(sticker);
                }

                String jsonNew = mapper.writeValueAsString(responseModel);
                setRecentStickers(jsonNew);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            StickerCategory category = new StickerCategory();
            category.list = new ArrayList<>();
            sticker.timesClicked = 1;
            category.list.add(sticker);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonNew = mapper.writeValueAsString(category);
                setRecentStickers(jsonNew);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStickersString(){
        return sharedPreferences.getString(Const.Preferences.STICKERS_COUNT, "");
    }

    public StickerCategory getStickersLikeObject(){
        String json = getStickersString();
        Log.e("LOG", "STICKERS: " + json);
        if(TextUtils.isEmpty(json)){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            StickerCategory responseModel = mapper.readValue(json, StickerCategory.class);

            Collections.sort(responseModel.list, new Comparator<Sticker>() {
                @Override
                public int compare(Sticker lhs, Sticker rhs) {
                    return rhs.timesClicked - lhs.timesClicked;
                }
            });

            return responseModel;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setRecentStickers(String json){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Const.Preferences.STICKERS_COUNT, json);
        editor.apply();
    }

}
