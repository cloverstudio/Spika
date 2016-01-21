package com.clover_studio.spikachatmodule.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.utils.Tools;

public class DownloadFileDialog extends Dialog {

    private TextView progressTv;
    private TextView title;
    private ProgressBar progressPb;
    private ProgressBar loading;

    private int max;
    private String maxString;

    public static DownloadFileDialog startDialog(Context context){
        DownloadFileDialog dialog = new DownloadFileDialog(context);
        return dialog;
    }

    public DownloadFileDialog(Context context) {
        super(context, R.style.Theme_Dialog);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setOwnerActivity((Activity) context);

        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_file);

        title = (TextView) findViewById(R.id.title);
        progressTv = (TextView) findViewById(R.id.progress);
        progressPb = (ProgressBar) findViewById(R.id.progressBarHorizontal);
        loading = (ProgressBar) findViewById(R.id.progressBarLoading);

    }

    public void setMax(int max){
        progressPb.setMax(max);
        this.max = max;
        this.maxString = Tools.readableFileSize(max);
        progressTv.setText("0/" + maxString);
    }

    public void setCurrent(int current){
        progressPb.setProgress(current);
        progressTv.setText(Tools.readableFileSize(current) + "/" + maxString);
    }

    public void fileDownloaded(){
        progressPb.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        progressTv.setText(getOwnerActivity().getString(R.string.finishing));
    }

}