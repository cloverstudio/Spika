package com.clover_studio.spikachatmodule.robospice.spice;

import android.app.Application;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.json.jackson.JacksonObjectPersisterFactory;

import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SpringService extends Jackson2SpringAndroidSpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {

        CacheManager cacheManager = new CacheManager();
        JacksonObjectPersisterFactory jacksonObjectPersisterFactory = new JacksonObjectPersisterFactory(application);

        cacheManager.addPersister(jacksonObjectPersisterFactory);

        return cacheManager;
    }

    @Override
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //find more complete examples in RoboSpice Motivation app
        //to enable Gzip compression and setting request timeouts.

        // web services support json responses
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        // web services support form responses
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        // web services support Jackson2 responses
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        // web service support for Byte Response
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
        listHttpMessageConverters.add(stringHttpMessageConverter);
        listHttpMessageConverters.add(formHttpMessageConverter);
        listHttpMessageConverters.add(jackson2HttpMessageConverter);
        listHttpMessageConverters.add(byteArrayHttpMessageConverter);

        restTemplate.setMessageConverters(listHttpMessageConverters);

        return restTemplate;
    }

    @Override
    public int getThreadCount() {
        return 3;
    }
}
