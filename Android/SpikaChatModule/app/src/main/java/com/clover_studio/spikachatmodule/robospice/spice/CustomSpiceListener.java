package com.clover_studio.spikachatmodule.robospice.spice;

import android.content.Context;
import android.text.TextUtils;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.base.BaseModel;
import com.clover_studio.spikachatmodule.utils.ErrorHandle;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class CustomSpiceListener<T> implements RequestListener<T> {

    private Context ctx;
    private boolean showErrorDialog = true;

    public CustomSpiceListener(Context context) {
        this.ctx = context;
        this.showErrorDialog = true;
    }

    public CustomSpiceListener(Context context, boolean showErrorDialog) {
        this.ctx = context;
        this.showErrorDialog = false;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {

        if (ctx != null) {

            if (ctx instanceof BaseActivity && !((BaseActivity) ctx).isFinishing()) {
                ((BaseActivity) ctx).handleProgress(false);
            }

            if(showErrorDialog){
                final NotifyDialog dialog = new NotifyDialog(ctx,
                        ctx.getString(R.string.e_network_error),
                        !TextUtils.isEmpty(spiceException.getMessage()) ? spiceException.getMessage() : ctx.getString(R.string.e_something_went_wrong),
                        NotifyDialog.Type.INFO);

                dialog.show();
            }

        }
    }

    @Override
    public void onRequestSuccess(T t) {

        if (ctx instanceof BaseActivity && !((BaseActivity) ctx).isFinishing()) {
            ((BaseActivity) ctx).handleProgress(false);
        }

        if(t instanceof BaseModel){
            if(((BaseModel)t).code != 1 && showErrorDialog){
                NotifyDialog dialog = NotifyDialog.startInfo(ctx, ctx.getString(R.string.error), ErrorHandle.getMessageForCode(((BaseModel)t).code, ctx.getResources()));
            }
        }
    }
}
