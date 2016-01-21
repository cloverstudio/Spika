package com.clover_studio.spikachatmodule.utils;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class LogCS {

    public static final String defaultLog = "LOG";

    public static void d(String tag, String message){
        Log.d(tag, message);
    }

    public static void d(String message){
        Log.d(defaultLog, message);
    }

    public static void e(String tag, String message){
        Log.e(tag, message);
    }

    public static void e(String message){
        Log.e(defaultLog, message);
    }

    public static void w(String tag, String message){
        Log.w(tag, message);
    }

    public static void w(String message){
        Log.w(defaultLog, message);
    }

    public static void v(String tag, String message){
        Log.v(tag, message);
    }

    public static void v(String message){
        Log.v(defaultLog, message);
    }

    public static void i(String tag, String message){
        Log.i(tag, message);
    }

    public static void i(String message){
        Log.i(defaultLog, message);
    }

    public static void response(URI uri, RestTemplate restTemplate, HttpMethod method, HttpEntity<MultiValueMap<String, String>> entity){
        ResponseEntity responseEntity = restTemplate.exchange(uri, method, entity, String.class);
        String response = (String) responseEntity.getBody();
        LogCS.custom(defaultLog, response);
    }

    public static void responseE(URI uri, RestTemplate restTemplate, HttpMethod method, HttpEntity<?> entity){
        ResponseEntity responseEntity = restTemplate.exchange(uri, method, entity, String.class);
        String response = (String) responseEntity.getBody();
        LogCS.custom(defaultLog, response);
    }

    public static void custom(String tag, String message) {

        int maxLogSize = 1000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {

            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;

            end = end > message.length() ? message.length() : end;

            LogCS.i(tag, message.substring(start, end));
        }
    }

}
