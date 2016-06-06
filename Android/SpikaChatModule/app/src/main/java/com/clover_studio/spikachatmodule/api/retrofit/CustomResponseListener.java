package com.clover_studio.spikachatmodule.api.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ubuntu_ivo on 10.02.16..
 */
public interface CustomResponseListener<T> extends Callback<T>{

    @Override
    void onFailure(Call<T> call, Throwable t);

    @Override
    void onResponse(Call<T> call, Response<T> response);

    void onCustomFailed(Call<T> call, Response<T> response);

    void onCustomSuccess(Call<T> call, Response<T> response);

}
