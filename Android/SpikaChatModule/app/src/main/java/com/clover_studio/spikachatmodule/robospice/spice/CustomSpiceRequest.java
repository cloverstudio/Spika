package com.clover_studio.spikachatmodule.robospice.spice;

import com.clover_studio.spikachatmodule.robospice.NetworkUtils;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpHeaders;

public class CustomSpiceRequest<T> extends SpringAndroidSpiceRequest<T> {

    public CustomSpiceRequest(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        return null;
    }

    public HttpHeaders getHeaders() {
        return NetworkUtils.getHeaders();
    }

}
