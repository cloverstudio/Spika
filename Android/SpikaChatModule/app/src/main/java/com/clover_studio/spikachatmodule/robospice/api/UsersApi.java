package com.clover_studio.spikachatmodule.robospice.api;

import com.clover_studio.spikachatmodule.base.SpikaApp;
import com.clover_studio.spikachatmodule.models.GetUserModel;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceRequest;
import com.clover_studio.spikachatmodule.utils.Const;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class UsersApi {

    public static class GetUsersInRoom extends CustomSpiceRequest<GetUserModel> {

        String roomId;

        public GetUsersInRoom(String roomId) {
            super(GetUserModel.class);
            this.roomId = roomId;
        }

        @Override
        public GetUserModel loadDataFromNetwork() throws Exception {

            URI uri = new URI(SpikaApp.getConfig().apiBaseUrl + Const.Api.USER_LIST + "/" + roomId);

            HttpEntity<?> entity = new HttpEntity<>(null, getHeaders());

            ResponseEntity responseEntity = getRestTemplate().exchange(uri, HttpMethod.GET, entity, GetUserModel.class);
            GetUserModel response = (GetUserModel) responseEntity.getBody();

//            LogCS.responseE(uri, getRestTemplate(), HttpMethod.GET, entity);

            return response;
        }
    }

}
