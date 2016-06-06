package com.clover_studio.spikachatmodule.api.retrofit;

import android.content.Context;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.utils.ErrorHandle;
import com.clover_studio.spikachatmodule.utils.LogCS;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ubuntu_ivo on 10.02.16..
 */
public abstract class CustomResponse<T> implements CustomResponseListener<T>{

    public CustomResponse(Context context, boolean shouldTryAgain, boolean shouldShowErrorDialog){
        this.context = context;
        this.shouldTryAgain = shouldTryAgain;
        this.shouldShowErrorDialog = shouldShowErrorDialog;
    }

    private Context context;
    private boolean shouldTryAgain = false;
    private boolean shouldShowErrorDialog = true;

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        if (context != null && context instanceof BaseActivity && !((BaseActivity) context).isFinishing()) {
            ((BaseActivity) context).handleProgress(false);
        }

        if(t instanceof IOException){
            //no internet connection, or no host
        }
        if(context != null){
            if(shouldShowErrorDialog){
                final NotifyDialog dialog = new NotifyDialog(context,
                        context.getString(R.string.e_network_error),
                        context.getString(R.string.e_something_went_wrong),
                        NotifyDialog.Type.INFO);

                dialog.show();
            }
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (context != null && context instanceof BaseActivity && !((BaseActivity) context).isFinishing()) {
            ((BaseActivity) context).handleProgress(false);
        }

        if(response.body() instanceof BaseModel){
            if(((BaseModel)response.body()).code != 1){
                onCustomFailed(call, response);
            }else{
                onCustomSuccess(call, response);
            }
        }else if (response.body() instanceof String){
            LogCS.custom("LOG", "RESPONSE: " + response.body());
            onCustomFailed(call, response);
        }else{
            onCustomFailed(call, response);
        }
    }

    @Override
    public void onCustomFailed(final Call<T> call, final Response<T> response) {
        if(shouldShowErrorDialog){
            if(response.body() instanceof BaseModel){

                NotifyDialog.startInfo(context, context.getString(R.string.error), ErrorHandle.getMessageForCode(((BaseModel)response.body()).code, context.getResources()));

            }else{

                NotifyDialog.startInfo(context, context.getString(R.string.error), context.getString(R.string.e_something_went_wrong));

            }
        }
    }

    @Override
    public void onCustomSuccess(Call<T> call, Response<T> response) {

    }

}
