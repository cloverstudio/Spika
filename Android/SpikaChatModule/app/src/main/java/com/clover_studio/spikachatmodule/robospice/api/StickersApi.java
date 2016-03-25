package com.clover_studio.spikachatmodule.robospice.api;

import android.content.Context;

import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.models.GetStickersData;
import com.clover_studio.spikachatmodule.models.GetUserModel;
import com.clover_studio.spikachatmodule.robospice.spice.CustomSpiceRequest;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Created by ubuntu_ivo on 23.03.16..
 */
public class StickersApi {

    public static class GetStickers extends CustomSpiceRequest<GetStickersData> {

        Context context;

        public GetStickers(Context context) {
            super(GetStickersData.class, context);
            this.context = context;
        }

        @Override
        public GetStickersData loadDataFromNetwork() throws Exception {

            URI uri = new URI(SingletonLikeApp.getInstance().getConfig(context).apiBaseUrl + Const.Api.STICKERS);

            HttpEntity<?> entity = new HttpEntity<>(null, getHeaders());

            ResponseEntity responseEntity = getRestTemplate().exchange(uri, HttpMethod.GET, entity, GetStickersData.class);
            GetStickersData response = (GetStickersData) responseEntity.getBody();

//            LogCS.responseE(uri, getRestTemplate(), HttpMethod.GET, entity);

            return response;
        }
    }

}
