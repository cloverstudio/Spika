package com.clover_studio.spikachatmodule.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.models.Config;
import com.clover_studio.spikachatmodule.utils.ApplicationStateManager;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.Preferences;
import com.clover_studio.spikachatmodule.utils.Tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @SuppressWarnings("deprecation")
    public boolean isAppInForeground(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName componentInfo;
        boolean isActive;

        if (Tools.isBuildOver(Build.VERSION_CODES.KITKAT)) {
            isActive = isActivePCG(am, context);
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            componentInfo = taskInfo.get(0).topActivity;
            isActive = componentInfo.getPackageName().equalsIgnoreCase(context.getPackageName());
        }

        boolean isScreenOn;
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Tools.isBuildOver(Build.VERSION_CODES.KITKAT)) {
            isScreenOn = pm.isInteractive();
        } else {
            isScreenOn = pm.isScreenOn();
        }

        return isActive && isScreenOn;
    }

    private boolean isActivePCG(ActivityManager am, Context c) {

        final Set<String> activePackages = new HashSet<>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }

        String[] activePCG;
        activePCG = activePackages.toArray(new String[activePackages.size()]);

        for (String activePackage : activePCG) {
            if (activePackage.equals(c.getPackageName())) {
                return true;
            }
        }

        return false;
    }

}
