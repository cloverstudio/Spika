package com.clover_studio.spikachatmodule.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.clover_studio.spikachatmodule.R;


public class SimpleProgressDialog extends AlertDialog {

    public SimpleProgressDialog(Context context) {
        super(context, R.style.Theme_Dialog);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
    }

}
