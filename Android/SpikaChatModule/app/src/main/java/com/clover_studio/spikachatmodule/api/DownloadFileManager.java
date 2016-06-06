package com.clover_studio.spikachatmodule.api;

import android.content.Context;
import android.os.AsyncTask;

import com.clover_studio.spikachatmodule.utils.Tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by ubuntu_ivo on 12.08.15..
 */
public class DownloadFileManager {

    /**
     * download video from url
     * @param ctx
     * @param urlMain url of video
     * @param file file to save video
     * @param pbListener progress listener
     */
    public static void downloadVideo(final Context ctx, final String urlMain, final File file, final OnDownloadListener pbListener) {

        AsyncTask<String, Void, String> execute = new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... params) {

                HttpURLConnection urlConnection = null;
                int contentLength = 0;

                try {

                    URL url = new URL(urlMain);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    List values = urlConnection.getHeaderFields().get("content-Length");
                    if (values != null && !values.isEmpty()) {

                        String sLength = (String) values.get(0);

                        if (sLength != null) {

                            try {
                                contentLength = Integer.parseInt(sLength);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    pbListener.onSetMax(contentLength);

                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                    OutputStream os = new FileOutputStream(file);

                    Tools.copyStream(is, os, contentLength, pbListener);

                    is.close();
                    os.close();

                    return file.getAbsolutePath();

                } catch (Exception e) {

                    e.printStackTrace();

                } finally {

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

                return null;
            }

            protected void onPostExecute(String path) {
                super.onPostExecute(path);

                if (pbListener != null) {

                    pbListener.onResponse(true, path);
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (pbListener != null) {

                    pbListener.onStart();
                }
            }
        }.execute();
    }

    /**
     * download video from url
     * @param ctx
     * @param urlMain url of video
     * @param file file to save video
     * @param pbListener progress listener
     */
    public static void downloadImage(final Context ctx, final String urlMain, final File file, final OnDownloadListener pbListener) {

        AsyncTask<String, Void, String> execute = new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... params) {

                HttpURLConnection urlConnection = null;
                int contentLength = 0;

                try {

                    URL url = new URL(urlMain);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    List values = urlConnection.getHeaderFields().get("content-Length");
                    if (values != null && !values.isEmpty()) {

                        String sLength = (String) values.get(0);

                        if (sLength != null) {

                            try {
                                contentLength = Integer.parseInt(sLength);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    pbListener.onSetMax(contentLength);

                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                    OutputStream os = new FileOutputStream(file);

                    Tools.copyStream(is, os, contentLength, pbListener);

                    is.close();
                    os.close();

                    return file.getAbsolutePath();

                } catch (Exception e) {

                    e.printStackTrace();

                } finally {

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

                return null;
            }

            protected void onPostExecute(String path) {
                super.onPostExecute(path);

                if (pbListener != null) {

                    pbListener.onResponse(true, path);
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (pbListener != null) {

                    pbListener.onStart();
                }
            }
        }.execute();
    }

    /**
     * download progress listener
     */
    public interface OnDownloadListener{
        /**
         * start downloading
         */
        public void onStart();

        /**
         * set size of file
         * @param max size of file
         */
        public void onSetMax(int max);

        /**
         * current progress of downloading
         * @param current
         */
        public void onProgress(int current);

        /**
         * download file finished
         */
        public void onFinishDownload();

        /**
         * communication with server finished
         * @param isSuccess is success
         * @param path path of saved file
         */
        public void onResponse(boolean isSuccess, String path);
    }

}
