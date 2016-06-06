package com.clover_studio.spikachatmodule.api.retrofit;


import com.clover_studio.spikachatmodule.models.GetMessagesModel;
import com.clover_studio.spikachatmodule.models.GetStickersData;
import com.clover_studio.spikachatmodule.models.GetUserModel;
import com.clover_studio.spikachatmodule.models.Login;
import com.clover_studio.spikachatmodule.models.User;
import com.clover_studio.spikachatmodule.utils.Const;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ubuntu_ivo on 10.02.16..
 */
public interface SpikaOSRetroApiInterface {

    @GET(Const.Api.USER_LIST)
    Call<GetUserModel> getUsersInRoom(@Path(Const.Params.ROOM_ID) String roomId, @Header(Const.Params.ACCESS_TOKEN) String token);

    @POST(Const.Api.USER_LOGIN)
    Call<Login> login(@Body User user);

    @GET(Const.Api.MESSAGES)
    Call<GetMessagesModel> getMessages(@Path(Const.Params.ROOM_ID) String roomId, @Path(Const.Params.LAST_MESSAGE_ID) String lastMessageId, @Header(Const.Params.ACCESS_TOKEN) String token);

    @GET(Const.Api.LATEST)
    Call<GetMessagesModel> getLatestMessages(@Path(Const.Params.ROOM_ID) String roomId, @Path(Const.Params.LAST_MESSAGE_ID) String lastMessageId, @Header(Const.Params.ACCESS_TOKEN) String token);

//    @GET(Const.ServerSpikaOS.GET_ALL_MESSAGE_FROM_ID)
//    Call<GetMessagesModel> getLastFromMessageId(@Path(Const.GetParams.ROOM_ID) String roomId, @Path(Const.GetParams.LAST_MESSAGE_ID) String lastMessageId, @Header(Const.HeadersParams.ACCESS_TOKEN) String token);

    @GET(Const.Api.STICKERS)
    Call<GetStickersData> getStickers(@Header(Const.Params.ACCESS_TOKEN) String token);

}
