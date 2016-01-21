package com.clover_studio.spikachatmodule.robospice.api;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.clover_studio.spikachatmodule.models.UploadFileResult;
import com.clover_studio.spikachatmodule.utils.Const;

import org.springframework.http.converter.FormHttpMessageConverter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu_ivo on 12.08.15..
 */
public class UploadFileManagement {


    public class BackgroundUploader extends AsyncTask<Void, Integer, String>  {

        private String url;
        private String contentType;
        private File file;
        private OnUploadResponse listener;

        public BackgroundUploader(String url, File file, String contentType, OnUploadResponse listener) {
            this.url = url;
            this.file = file;
            this.contentType = contentType;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            listener.onStart();
        }

        @Override
        protected String doInBackground(Void... v) {

            String response = null;

            if(!file.exists()){
                return null;
            }

            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = file.getName();
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\""+ Const.Params.FILE +"\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: " + contentType + "\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = file.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                listener.onSetMax((int) fileLength);

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead;
                    // update progress bar
                    listener.onProgress(progress);
                }

                listener.onFinishUpload();

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                response = builder.toString();

            } catch (Exception e) {
                e.printStackTrace();
                listener.onResponse(false, null);
            } finally {
                if (connection != null) connection.disconnect();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String v) {
            if(v == null){
                listener.onResponse(false, null);
            }else{
                listener.onResponse(true, v);
            }
        }

    }

    public interface OnUploadResponse{
        /**
         * start uploading
         */
        public void onStart();

        /**
         * set size of file
         * @param max size of file
         */
        public void onSetMax(int max);

        /**
         * current progress of uploading
         * @param current
         */
        public void onProgress(int current);

        /**
         * download file finished
         */
        public void onFinishUpload();

        /**
         * communication with server finished
         * @param isSuccess is success
         * @param result path of saved file
         */
        public void onResponse(boolean isSuccess, String result);
    }

}
