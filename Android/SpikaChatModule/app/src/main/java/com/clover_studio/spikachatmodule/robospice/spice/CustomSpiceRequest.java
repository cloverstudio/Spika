package com.clover_studio.spikachatmodule.robospice.spice;

import android.content.Context;

import com.clover_studio.spikachatmodule.robospice.NetworkUtils;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpHeaders;

public class CustomSpiceRequest<T> extends SpringAndroidSpiceRequest<T> {

    Context context;

    public CustomSpiceRequest(Class<T> clazz, Context context) {
        super(clazz);
        this.context = context;
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        return null;
    }

    public HttpHeaders getHeaders() {
        return NetworkUtils.getHeaders(context);
    }

}
