package com.clover_studio.spikachatmodule.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.utils.ApplicationStateManager;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.Preferences;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class SpikaApp extends Application {

    private static Context mAppContext;
    private static Preferences mAppPreferences;
    private static Config mConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        setAppContext(getApplicationContext());

        new ApplicationStateManager(this);
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        SpikaApp.mAppContext = mAppContext;
    }

    public static Preferences getSharedPreferences() {

        if (mAppPreferences == null) {
            mAppPreferences = new Preferences(getAppContext());
        }

        return mAppPreferences;
    }

    public static void setConfig(Config config){
        mConfig = config;
    }

    public static Config getConfig(){
        if(mConfig == null){
            mConfig = getSharedPreferences().getConfig();
            if(TextUtils.isEmpty(mConfig.apiBaseUrl)){
                mConfig.apiBaseUrl = Const.Api.BASE_URL;
            }
            if(TextUtils.isEmpty(mConfig.socketUrl)){
                mConfig.socketUrl = Const.Socket.SOCKET_URL;
            }
        }

        return mConfig;
    }

}
