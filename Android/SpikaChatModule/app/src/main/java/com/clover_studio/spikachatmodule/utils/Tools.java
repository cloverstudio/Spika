package com.clover_studio.spikachatmodule.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.api.DownloadFileManager;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class Tools {

    /**
     * checked if android version is above given version
     * @param version version to compare
     * @return true is above, false is equals or below
     */
    public static boolean isBuildOver(int version) {
        if (android.os.Build.VERSION.SDK_INT > version)
            return true;
        return false;
    }

    /**
     * generate random string with given length
     * @param length length for string to generate
     * @return generated string
     */
    public static String generateRandomString(int length) {
        String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * generate date for given format
     * @param format format for date
     * @param timestamp date in milliseconds to generate
     * @return string
     */
    public static String generateDate(String format, long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format(format, cal).toString();
        return date;
    }

    /**
     * get image path from uri
     * @param cntx
     * @param uri uri of file
     * @param isOverJellyBeam is android version over jelly beam version
     * @return string
     */
    public static String getImagePath(Context cntx, Uri uri, boolean isOverJellyBeam) {

        String extension = ".jpg";

        if (isOverJellyBeam) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = cntx.getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                // Bitmap image =
                // BitmapFactory.decodeFileDescriptor(fileDescriptor);
                copyStream(new FileInputStream(fileDescriptor), new FileOutputStream(new File(Tools.getTempFolderPath() + "/" + Const.FilesName.IMAGE_TEMP_FILE_NAME + extension)));
                parcelFileDescriptor.close();
                // saveBitmapToFile(image, cntx.getExternalCacheDir() + "/" +
                // "image_profile");
                return Tools.getTempFolderPath() + "/" + Const.FilesName.IMAGE_TEMP_FILE_NAME + extension;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        } else {

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = cntx.getContentResolver().query(uri, projection, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            String returnedString = cursor.getString(column_index);
            cursor.close();

            return returnedString;
        }
    }

    /**
     * copy stream without progress listener
     * @param is input stream
     * @param os output stream
     */
    public static void copyStream(InputStream is, OutputStream os) {
        copyStream(is, os, -1, null);
    }

    /**
     *
     * copy stream with progress listener
     *
     * @param is input stream
     * @param os output stream
     * @param length length of stream
     * @param listener progress listener
     */
    public static void copyStream(InputStream is, OutputStream os, long length, DownloadFileManager.OnDownloadListener listener) {
        final int buffer_size = 1024;
        int totalLen = 0;
        try {

            byte[] bytes = new byte[buffer_size];
            while (true) {
                // Read byte from input stream

                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    listener.onFinishDownload();
                    break;
                }

                // Write byte from output stream
                if (length != -1 && listener != null) {
                    totalLen = totalLen + count;
                    listener.onProgress(totalLen);
                }
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * save bitmap to given file
     * @param bitmap bitmap to save
     * @param path path of file
     * @return is success
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String path) {

        File file = new File(path);
        FileOutputStream fOut;

        try {

            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * return string of given size example 1MB, 1,4GB, 300KB
     * @param size size to convert
     * @return string
     */
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * return if mime type is image
     * @param mimeType
     * @return is image
     */
    public static boolean isMimeTypeImage(String mimeType) {
        if (mimeType.equals(Const.ContentTypes.IMAGE_GIF)) {
            return true;
        } else if (mimeType.equals(Const.ContentTypes.IMAGE_JPG)) {
            return true;
        } else if (mimeType.equals(Const.ContentTypes.IMAGE_PNG)) {
            return true;
        }
        return false;
    }

    /**
     * return if mime type is video
     * @param mimeType
     * @return is video
     */
    public static boolean isMimeTypeVideo(String mimeType) {
        if (mimeType.equals(Const.ContentTypes.VIDEO_MP4)) {
            return true;
        }
        return false;
    }

    /**
     * return if mime type is audio
     * @param mimeType
     * @return is audio
     */
    public static boolean isMimeTypeAudio(String mimeType) {
        if (mimeType.equals(Const.ContentTypes.AUDIO_MP3)) {
            return true;
        } else if (mimeType.equals(Const.ContentTypes.AUDIO_WAV)) {
            return true;
        }
        return false;
    }

    public static File getTempFile(Context context, String name) {
        return new File(context.getCacheDir(), TextUtils.isEmpty(name) ? Const.FilesName.TEMP_FILE_NAME : name);
    }

    /**
     * get mime type of given file
     * @param url file path or url
     * @return
     */
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * get url of file with given id
     * @param id id of file
     * @return full url to download
     */
    public static String getFileUrlFromId(String id, Context context) {
        return SingletonLikeApp.getInstance().getConfig(context).apiBaseUrl + Const.Api.DOWNLOAD_FILE + "/" + id;
    }

    /**
     * get root file of application
     * @return string
     */
    public static String getFilesFolderPath() {
        File folder = new File(Environment.getExternalStorageDirectory(), Const.CacheFolder.APP_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * get video folder path
     * @return string
     */
    public static String getVideoFolderPath() {
        File folder = new File(getFilesFolderPath(), Const.CacheFolder.VIDEO_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * get image folder path
     * @return string
     */
    public static String getImageFolderPath() {
        File folder = new File(getFilesFolderPath(), Const.CacheFolder.IMAGE_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * get downloaded folder path
     * @return string
     */
    public static String getDownloadFolderPath() {
        File folder = new File(getFilesFolderPath(), Const.CacheFolder.DOWNLOAD_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * get audio folder path
     * @return string
     */
    public static String getAudioFolderPath() {
        File folder = new File(getFilesFolderPath(), Const.CacheFolder.AUDIO_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     * get temp folder path
     * @return string
     */
    public static String getTempFolderPath() {
        File folder = new File(getFilesFolderPath(), Const.CacheFolder.TEMP_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    /**
     *
     * scale bitmap
     *
     * @param path file path
     * @param mContentResolver
     * @param maxSize max size for scale
     * @return bitmap
     */
    public static Bitmap scaleBitmap(String path, ContentResolver mContentResolver, int maxSize) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = maxSize;
            in = mContentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            LogCS.d("LOG", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = mContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                LogCS.d("LOG", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            LogCS.d("LOG", "bitmap size - width: " + b.getWidth() + ", height: " + b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("LOG", e.getMessage(), e);
            return null;
        }

    }

    /**
     * save string to file
     * @param dataInput string to save
     * @param path path of file
     * @return
     */
    public static boolean saveStringToFile(String dataInput, String path) {
        String filename = path;

        File file = new File(filename);
        FileOutputStream fos;

        byte[] data = dataInput.getBytes();
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static boolean checkGrantResults (int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * copy text to clipboard and show toast info
     * @param text string to copy
     * @return
     */
    public static void copyTextFromTextViewAndShowToast(String text, Context c){
        ClipData clipData = ClipData.newPlainText("copy", text);
        ClipboardManager clipboard = (ClipboardManager) c.getSystemService(c.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);

        Toast.makeText(c, R.string.message_copied_to_clipboard, Toast.LENGTH_LONG).show();
    }

    /**
     * get uri from imageView to share image
     * @param imageView imageView contains image to get uri
     * @return
     */
    public static Uri getLocalBitmapUri(ImageView imageView, Context context) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(getTempFile(context, "temp") + "/temp.png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    /**
     * share image via intent
     * @param context context
     * @param imageFile file to share
     * @return
     */
    public static void shareImage(final Context context, final File imageFile){

        new AsyncTask<Void, Void, File>(){

            @Override
            protected File doInBackground(Void... params) {

                Bitmap icon = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Tools.getTempFolderPath(), "share_temp.jpg");

                if(f.exists()){
                    f.delete();
                }

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return f;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                Uri uri = Uri.fromFile(file);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(share, context.getString(R.string.share____)));

                if(context instanceof BaseActivity){
                    ((BaseActivity)context).handleProgress(false);
                }

            }
        }.execute();

    }

    /**
     * share image via intent
     * @param context context
     * @param bitmap bitmap to share
     * @return
     */
    public static void shareImage(final Context context, final Bitmap bitmap){

        new AsyncTask<Void, Void, File>(){

            @Override
            protected File doInBackground(Void... params) {

                Bitmap icon = bitmap;
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Tools.getTempFolderPath(), "share_temp.jpg");

                if(f.exists()){
                    f.delete();
                }

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return f;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                Uri uri = Uri.fromFile(file);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(share, context.getString(R.string.share____)));

                if(context instanceof BaseActivity){
                    ((BaseActivity)context).handleProgress(false);
                }

            }
        }.execute();

    }

    /**
     * check if message has link
     * @param msg message
     * @return result not null if message has string
     */
    public static String checkForLink(String msg) {

        if (msg == null) {
            return null;
        } else if(msg.contains("@")){
            return null;
        } else {

            String result = null;
            Pattern pattern = Patterns.WEB_URL;
            Matcher matchURL = pattern.matcher(msg);

            if (matchURL.find()) {
                result = matchURL.group();
            }

            return result;
        }

    }

}
