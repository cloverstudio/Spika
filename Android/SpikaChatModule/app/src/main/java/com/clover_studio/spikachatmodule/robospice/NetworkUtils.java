package com.clover_studio.spikachatmodule.robospice;

import com.clover_studio.spikachatmodule.base.SpikaApp;
import com.clover_studio.spikachatmodule.utils.Const;

import org.springframework.http.HttpHeaders;

public class NetworkUtils {

    public static HttpHeaders getHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(Const.Params.TOKEN, SpikaApp.getSharedPreferences().getToken());
        return headers;

    }
}
