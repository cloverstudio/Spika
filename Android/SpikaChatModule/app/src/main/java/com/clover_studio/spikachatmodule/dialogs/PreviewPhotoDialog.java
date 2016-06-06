package com.clover_studio.spikachatmodule.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.api.DownloadFileManager;
import com.clover_studio.spikachatmodule.models.Message;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.clover_studio.spikachatmodule.utils.UtilsImage;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PreviewPhotoDialog extends Dialog {

    String imageUrl;
    Message message;

    int maxToDownload;

    public static PreviewPhotoDialog startDialog(Context context, String imageUrl, Message message){
        PreviewPhotoDialog dialog = new PreviewPhotoDialog(context, imageUrl, message);
        return dialog;
    }

    public PreviewPhotoDialog(Context context, String imageUrl, Message message) {
        super(context, R.style.Theme_Dialog);

        setOwnerActivity((Activity) context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        this.imageUrl = imageUrl;
        this.message = message;

        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_preview_photo);

        final TextView tvPercentLoading = (TextView) findViewById(R.id.percentLoading);
        tvPercentLoading.setText("0%");

        File file = new File(Tools.getImageFolderPath() + "/" + message.created + message.file.file.name);

        if(file.exists() && file.length() > 50){
            showImage(file);
        }else{
            DownloadFileManager.downloadVideo(getContext(), imageUrl, file, new DownloadFileManager.OnDownloadListener() {
                @Override
                public void onStart() {
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPercentLoading.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onSetMax(int max) {
                    maxToDownload = max;
                }

                @Override
                public void onProgress(final int current) {
                    final double currentProgress = (double)current / (double)maxToDownload;
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String text = (int)(currentProgress * 100) + "%";
                            tvPercentLoading.setText(text);
                        }
                    });
                }

                @Override
                public void onFinishDownload() {
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String text = 100 + "%";
                            tvPercentLoading.setText(text);
                        }
                    });
                }

                @Override
                public void onResponse(boolean isSuccess, String path) {
                    if(isSuccess){
                        showImage(new File(path));
                    }else{
                        getOwnerActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                                findViewById(R.id.percentLoading).setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });
        }

        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void showImage(File file){

        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        if(bitmap == null){
            findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
            findViewById(R.id.percentLoading).setVisibility(View.GONE);

            return;
        }

        int maxHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        int maxWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        PhotoView photo = (PhotoView) findViewById(R.id.photoIv);
        PhotoViewAttacher photoAttach = new PhotoViewAttacher(photo);
        photoAttach.setMaximumScale(5.00f);

        if(bitmap.getWidth() > maxWidth){
            int width = maxWidth;
            int height = (int) ((double)width / ((double)bitmap.getWidth() / (double)bitmap.getHeight()));

            UtilsImage.setImageWithFileLoaderAndCallback(photo, -1, null, file, new UtilsImage.ImageCallback() {
                @Override
                public void onSuccess() {
                    findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                    findViewById(R.id.percentLoading).setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                    findViewById(R.id.percentLoading).setVisibility(View.GONE);
                }
            });

        }else if(bitmap.getHeight() > maxHeight){
            int height = maxHeight;
            int width = (int) ((double)height / ((double)bitmap.getHeight() / (double)bitmap.getWidth()));

            UtilsImage.setImageWithFileLoaderAndCallback(photo, -1, null, file, new UtilsImage.ImageCallback() {
                @Override
                public void onSuccess() {
                    findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                    findViewById(R.id.percentLoading).setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                    findViewById(R.id.percentLoading).setVisibility(View.GONE);
                }
            });

        }else{
            UtilsImage.setImageWithFileLoaderAndCallback(photo, -1, null, file, new UtilsImage.ImageCallback() {
                @Override
                public void onSuccess() {
                    findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                    findViewById(R.id.percentLoading).setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                    findViewById(R.id.percentLoading).setVisibility(View.GONE);
                }
            });
        }

    }

}