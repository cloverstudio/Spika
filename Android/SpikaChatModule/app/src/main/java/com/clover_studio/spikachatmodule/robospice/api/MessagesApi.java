package com.clover_studio.spikachatmodule.robospice.api;

import android.content.Context;

import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.models.GetMessagesModel;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceRequest;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class MessagesApi {

    public static class GetMessages extends CustomSpiceRequest<GetMessagesModel> {

        String roomId;
        String lastMessageId;
        Context context;

        public GetMessages(String roomId, String lastMessageId, Context context) {
            super(GetMessagesModel.class, context);
            this.roomId = roomId;
            this.lastMessageId = lastMessageId;
            this.context = context;
        }

        @Override
        public GetMessagesModel loadDataFromNetwork() throws Exception {

            String url = SingletonLikeApp.getInstance().getConfig(context).apiBaseUrl + Const.Api.MESSAGES + "/" + roomId;

            if(lastMessageId != null) {
                url = url + "/" + lastMessageId;
            }else{
                url = url + "/0";
            }

            URI uri = new URI(url);

//            LogCS.d("LOG", "req: " + url);

            HttpEntity<?> entity = new HttpEntity<>(null, getHeaders());

            ResponseEntity responseEntity = getRestTemplate().exchange(uri, HttpMethod.GET, entity, GetMessagesModel.class);
            GetMessagesModel response = (GetMessagesModel) responseEntity.getBody();

//            LogCS.responseE(uri, getRestTemplate(), HttpMethod.GET, entity);

            return response;
        }
    }

    public static class GetLatestMessages extends CustomSpiceRequest<GetMessagesModel> {

        String roomId;
        String lastMessageId;
        Context context;

        public GetLatestMessages(String roomId, String lastMessageId, Context context) {
            super(GetMessagesModel.class, context);
            this.roomId = roomId;
            this.lastMessageId = lastMessageId;
            this.context = context;
        }

        @Override
        public GetMessagesModel loadDataFromNetwork() throws Exception {

            String url = SingletonLikeApp.getInstance().getConfig(context).apiBaseUrl + Const.Api.LATEST + "/" + roomId;

            if(lastMessageId != null) {
                url = url + "/" + lastMessageId;
            }else{
                url = url + "/0";
            }

            URI uri = new URI(url);

//            LogCS.d("LOG", "req: " + url);

            HttpEntity<?> entity = new HttpEntity<>(null, getHeaders());

            ResponseEntity responseEntity = getRestTemplate().exchange(uri, HttpMethod.GET, entity, GetMessagesModel.class);
            GetMessagesModel response = (GetMessagesModel) responseEntity.getBody();

//            LogCS.responseE(uri, getRestTemplate(), HttpMethod.GET, entity);

            return response;
        }
    }

}
