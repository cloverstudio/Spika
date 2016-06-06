package com.clover_studio.spikachatmodule.utils;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * Created by ubuntu_ivo on 08.02.16..
 */
public class UtilsImage {

    public static void setImageWithLoader(final ImageView imageView, final int defaultResource, final ProgressBar pbLoading, String url){
        if(!TextUtils.isEmpty(url)){
            ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (pbLoading != null) pbLoading.setVisibility(View.GONE);
                    if (defaultResource != -1) imageView.setImageResource(defaultResource);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (pbLoading != null) pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if (pbLoading != null) pbLoading.setVisibility(View.GONE);
                    if (defaultResource != -1) imageView.setImageResource(defaultResource);
                }
            });
        }else{
            if(pbLoading != null) pbLoading.setVisibility(View.GONE);
            if(defaultResource != -1) imageView.setImageResource(defaultResource);
        }
    }

    public static void setImageWithFileLoaderAndCallback(final ImageView imageView, final int defaultResource, final ProgressBar pbLoading, final File file, final ImageCallback callback){
        if(file != null){

            ImageLoader.getInstance().displayImage("file://" + file.getAbsolutePath(), imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    callback.onError();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    callback.onSuccess();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    callback.onError();
                }
            });

        }else{
            if(pbLoading != null) pbLoading.setVisibility(View.GONE);
            if(defaultResource != -1) imageView.setImageResource(defaultResource);
        }
    }

    public static void setImageWithLoaderAndCallback(final ImageView imageView, final int defaultResource, final ProgressBar pbLoading, String url, final ImageCallback callback){
        if(!TextUtils.isEmpty(url)){

            ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    callback.onError();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    callback.onSuccess();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    callback.onError();
                }
            });

        }else{
            if(pbLoading != null) pbLoading.setVisibility(View.GONE);
            if(defaultResource != -1) imageView.setImageResource(defaultResource);
        }
    }

    public interface ImageCallback{
        void onSuccess();
        void onError();
    }

}
