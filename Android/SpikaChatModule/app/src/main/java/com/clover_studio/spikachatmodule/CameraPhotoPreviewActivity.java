package com.clover_studio.spikachatmodule;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.clover_studio.spikachatmodule.api.UploadFileManagement;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.dialogs.UploadFileDialog;
import com.clover_studio.spikachatmodule.models.UploadFileResult;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraPhotoPreviewActivity extends BaseActivity {

    boolean mIsOverJellyBean;
    String mOriginalPath;
    String mScaledPath;
    Bitmap previewBitmap;
    ImageView previewImageView;

    /**
     * start preview for image from gallery
     * @param context
     */
    public static void starCameraFromGalleryPhotoPreviewActivity(Context context){
        Intent intent = new Intent(context, CameraPhotoPreviewActivity.class);
        intent.putExtra(Const.Extras.TYPE_OF_PHOTO_INTENT, Const.PhotoIntents.GALLERY);
        ((Activity)context).startActivityForResult(intent, Const.RequestCode.PHOTO_CHOOSE);
    }

    /**
     * start preview for image from camera
     * @param context
     */
    public static void starCameraPhotoPreviewActivity(Context context){
        Intent intent = new Intent(context, CameraPhotoPreviewActivity.class);
        intent.putExtra(Const.Extras.TYPE_OF_PHOTO_INTENT, Const.PhotoIntents.CAMERA);
        ((Activity)context).startActivityForResult(intent, Const.RequestCode.PHOTO_CHOOSE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo_preview);

        setToolbar(R.id.tToolbar, R.layout.custom_camera_preview_toolbar);
        setMenuLikeBack();

        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.okButton).setOnClickListener(onOkClicked);
        previewImageView = (ImageView) findViewById(R.id.ivCameraFullPhoto);

        ((ProgressBar)findViewById(R.id.progressBarLoading)).getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        mIsOverJellyBean = Tools.isBuildOver(18);

        if(getIntent().getExtras().getInt(Const.Extras.TYPE_OF_PHOTO_INTENT) == Const.PhotoIntents.GALLERY){
            if (mIsOverJellyBean) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, Const.RequestCode.GALLERY);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                this.startActivityForResult(intent, Const.RequestCode.GALLERY);
            }
        }else{
            try {
                startCamera();
            } catch (Exception ex) {
                ex.printStackTrace();

                NotifyDialog.startInfo(this, getString(R.string.camera_error_title), getString(R.string.camera_error_camera_init));
            }
        }

    }

    private View.OnClickListener onOkClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String sizeOfOriginalImage = "";
            String sizeOfScaledImage = "";

            File originalFile = new File(mOriginalPath);
            if(originalFile.exists()){
                sizeOfOriginalImage = Tools.readableFileSize(originalFile.length());
            }

            File scaledFile = new File(mScaledPath);
            if(scaledFile.exists()){
                sizeOfScaledImage = Tools.readableFileSize(scaledFile.length());
            }

            if(originalFile.length() < scaledFile.length()){
                uploadFile(mOriginalPath);
            }else{
                NotifyDialog dialog = NotifyDialog.startConfirm(getActivity(), getString(R.string.compress_image_title), getString(R.string.compress_image_text));

                dialog.setButtonsText(getString(R.string.NO_CAPITAL) + ", " + sizeOfOriginalImage, getString(R.string.YES_CAPITAL) + ", " + sizeOfScaledImage);

                dialog.setTwoButtonListener(new NotifyDialog.TwoButtonDialogListener() {
                    @Override
                    public void onOkClicked(NotifyDialog dialog) {
                        dialog.dismiss();
                        uploadFile(mScaledPath);
                    }

                    @Override
                    public void onCancelClicked(NotifyDialog dialog) {
                        dialog.dismiss();
                        uploadFile(mOriginalPath);
                    }
                });

                dialog.setCancelable(true);
            }

        }
    };

    private void uploadFile(String path){

        final UploadFileDialog dialog = UploadFileDialog.startDialog(getActivity());

        UploadFileManagement tt = new UploadFileManagement();
        tt.new BackgroundUploader(SingletonLikeApp.getInstance().getConfig(getActivity()).apiBaseUrl + Const.Api.UPLOAD_FILE, new File(path), Const.ContentTypes.IMAGE_JPG, new UploadFileManagement.OnUploadResponse() {
            @Override
            public void onStart() {
                LogCS.d("LOG", "START UPLOADING");
            }

            @Override
            public void onSetMax(final int max) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setMax(max);
                    }
                });
            }

            @Override
            public void onProgress(final int current) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setCurrent(current);
                    }
                });
            }

            @Override
            public void onFinishUpload() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.fileUploaded();
                    }
                });
            }

            @Override
            public void onResponse(final boolean isSuccess, final String result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if(!isSuccess){
                            onResponseFailed();
                        }else{
                            onResponseFinish(result);
                        }
                    }
                });
            }
        }).execute();
    }

    private void onResponseFailed() {
        NotifyDialog.startInfo(getActivity(), getString(R.string.error), getString(R.string.file_not_found));
    }

    protected void onResponseFinish(String result){
        Gson gson = new Gson();
        UploadFileResult data = null;
        try {
            data = gson.fromJson(result, UploadFileResult.class);

            new File(mScaledPath).delete();
            new File(mOriginalPath).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(data != null){
            sendMessage(data);
        }
        
    }

    private void sendMessage(UploadFileResult data) {
        Intent intentData = new Intent();
        intentData.putExtra(Const.Extras.UPLOAD_MODEL, data);
        setResult(RESULT_OK, intentData);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        if(requestCode == Const.RequestCode.GALLERY){
            if(resultCode == RESULT_OK){
                if (mIsOverJellyBean) {
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        String selected_image_path = Tools.getImagePath(this, uri, mIsOverJellyBean);
                        onPhotoTaken(selected_image_path);
                    } else {
                        NotifyDialog.startInfo(this, getString(R.string.gallery_error_title), getString(R.string.gallery_error_text));
                    }
                } else {
                    try {
                        Uri selected_image = data.getData();
                        String selected_image_path = Tools.getImagePath(this, selected_image, mIsOverJellyBean);
                        onPhotoTaken(selected_image_path);
                    } catch (Exception e) {
                        e.printStackTrace();

                        NotifyDialog.startInfo(this, getString(R.string.gallery_error_title), getString(R.string.gallery_error_text));
                    }
                }
            }
        }else if(requestCode == Const.RequestCode.CAMERA){
            if(resultCode == RESULT_OK){
                File file = new File(mOriginalPath);
                boolean exists = file.exists();
                if (exists) {
                    onPhotoTaken(mOriginalPath);
                } else {
                    NotifyDialog.startInfo(this, getString(R.string.camera_error_title), getString(R.string.camera_error_camera_image));
                }
            }
        }
    }

    private void startCamera() {

        // Check if camera exists
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && !getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {

            NotifyDialog.startInfo(this, getString(R.string.camera_error_title), getString(R.string.camera_error_no_camera));
        } else {

            try {
                long date = System.currentTimeMillis();
                String filename = Const.FilesName.CAMERA_TEMP_FILE_NAME;

                mOriginalPath = Tools.getTempFolderPath() + "/" + filename;

                File file = new File(mOriginalPath);
                Uri outputFileUri = Uri.fromFile(file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, Const.RequestCode.CAMERA);

            } catch (Exception ex) {
                ex.printStackTrace();

                NotifyDialog.startInfo(this, getString(R.string.camera_error_title), getString(R.string.camera_error_camera_init));
            }
        }
    }

    /**
     * scale and show image
     * @param path path oo image
     */
    protected void onPhotoTaken(String path) {

        String fileName = Uri.parse(path).getLastPathSegment();

        mOriginalPath = Tools.getTempFolderPath() + "/" + fileName;
        mScaledPath = Tools.getTempFolderPath()  + "/" + Const.FilesName.SCALED_PREFIX + fileName;

        if (!path.equals(mOriginalPath)) {
            try {
                Tools.copyStream(new FileInputStream(new File(path)), new FileOutputStream(new File(mOriginalPath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        new AsyncTask<String, Void, byte[]>() {

            @Override
            protected byte[] doInBackground(String... params) {
                try {

                    if (params == null) {
                        return null;
                    }

                    File f = new File(params[0]);
                    ExifInterface exif = new ExifInterface(f.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;

                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }

                    BitmapFactory.Options optionsMeta = new BitmapFactory.Options();
                    optionsMeta.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(f.getAbsolutePath(), optionsMeta);

                    int actualHeight = optionsMeta.outHeight;
                    int actualWidth = optionsMeta.outWidth;

                    // this options allow android to claim the bitmap memory
                    // if
                    // it runs low
                    // on memory
                    optionsMeta.inJustDecodeBounds = false;
                    optionsMeta.inPurgeable = true;
                    optionsMeta.inInputShareable = true;
                    optionsMeta.inTempStorage = new byte[16 * 1024];

                    // if (!isFromWall) {

                    float maxHeight = 1600.0f;
                    float maxWidth = 1600.0f;

                    optionsMeta.inSampleSize = Tools.calculateInSampleSize(optionsMeta, (int) maxWidth, (int) maxHeight);

                    // max Height and width values of the compressed image
                    // is
                    // taken as
                    // 816x612

                    float imgRatio = (float) actualWidth / (float) actualHeight;
                    float maxRatio = maxWidth / maxHeight;

                    if (actualHeight > maxHeight || actualWidth > maxWidth) {
                        if (imgRatio < maxRatio) {
                            imgRatio = maxHeight / actualHeight;
                            actualWidth = (int) (imgRatio * actualWidth);
                            actualHeight = (int) maxHeight;
                        } else if (imgRatio > maxRatio) {
                            imgRatio = maxWidth / actualWidth;
                            actualHeight = (int) (imgRatio * actualHeight);
                            actualWidth = (int) maxWidth;
                        } else {
                            actualHeight = (int) maxHeight;
                            actualWidth = (int) maxWidth;
                        }
                    }

                    Bitmap tempBitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, optionsMeta);
                    previewBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

                    float ratioX = actualWidth / (float) optionsMeta.outWidth;
                    float ratioY = actualHeight / (float) optionsMeta.outHeight;
                    float middleX = actualWidth / 2.0f;
                    float middleY = actualHeight / 2.0f;

                    Matrix mat = new Matrix();
                    mat.postRotate(angle);

                    Matrix scaleMatrix = new Matrix();
                    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                    Canvas canvas = new Canvas(previewBitmap);
                    canvas.setMatrix(scaleMatrix);
                    canvas.drawBitmap(tempBitmap, middleX - tempBitmap.getWidth() / 2, middleY - tempBitmap.getHeight() / 2, null);

                    previewBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, previewBitmap.getWidth(), previewBitmap.getHeight(), mat, true);

                    Tools.saveBitmapToFile(previewBitmap, mScaledPath);

                    return null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    previewBitmap = null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(byte[] result) {
                super.onPostExecute(result);

                if (null != previewBitmap) {
                    previewImageView.setImageBitmap(previewBitmap);
                } else {
                    NotifyDialog.startInfo(getActivity(), getString(R.string.gallery_error_title), getString(R.string.gallery_error_text));
                }

                findViewById(R.id.progressBarLoading).setVisibility(View.GONE);

            }
        }.execute(mOriginalPath);
    }

}
