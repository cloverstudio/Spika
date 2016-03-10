package com.clover_studio.spikachatmodule.robospice;

import android.content.Context;

import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.utils.Const;

import org.springframework.http.HttpHeaders;

public class NetworkUtils {

    public static HttpHeaders getHeaders(Context context) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(Const.Params.TOKEN, SingletonLikeApp.getInstance().getSharedPreferences(context).getToken());
        return headers;

    }
}
