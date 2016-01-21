package com.clover_studio.spikachatmodule.robospice.api;

import com.clover_studio.spikachatmodule.base.SpikaApp;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceRequest;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class LoginApi {

    public static class Login extends CustomSpiceRequest<com.clover_studio.spikachatmodule.models.Login> {

        User user;

        public Login(User user) {
            super(com.clover_studio.spikachatmodule.models.Login.class);
            this.user = user;
        }

        @Override
        public com.clover_studio.spikachatmodule.models.Login loadDataFromNetwork() throws Exception {

            URI uri = new URI(SpikaApp.getConfig().apiBaseUrl + Const.Api.USER_LOGIN);

            HttpHeaders headers = getHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(user, headers);

            ResponseEntity responseEntity = getRestTemplate().exchange(uri, HttpMethod.POST, entity, com.clover_studio.spikachatmodule.models.Login.class);
            com.clover_studio.spikachatmodule.models.Login response = (com.clover_studio.spikachatmodule.models.Login) responseEntity.getBody();

//            LogCS.responseE(uri, getRestTemplate(), HttpMethod.POST, entity);

            return response;
        }
    }

}
