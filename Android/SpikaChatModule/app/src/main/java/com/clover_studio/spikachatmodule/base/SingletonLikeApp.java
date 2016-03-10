package com.clover_studio.spikachatmodule.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.utils.ApplicationStateManager;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.Preferences;

/**
 * Created by ubuntu_ivo on 16.02.16..
 */
public class SingletonLikeApp {

    private static SingletonLikeApp singleton;

    private Preferences mAppPreferences;
    private Config mConfig;

    public Preferences getSharedPreferences(Context context) {

        if (mAppPreferences == null) {
            mAppPreferences = new Preferences(context);
        }

        return mAppPreferences;
    }

    public static SingletonLikeApp getInstance() {

        if (singleton == null) {
            singleton = new SingletonLikeApp();
        }

        return singleton;
    }

    public void setApplicationState(Activity activity){
        new ApplicationStateManager(activity.getApplication());
    }

    public void setConfig(Config config){
        mConfig = config;
    }

    public Config getConfig(Context context){
        if(mConfig == null){
            mConfig = getSharedPreferences(context).getConfig();
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
