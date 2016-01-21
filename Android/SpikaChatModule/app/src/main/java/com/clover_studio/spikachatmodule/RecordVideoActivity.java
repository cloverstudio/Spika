package com.clover_studio.spikachatmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.base.SpikaApp;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.dialogs.UploadFileDialog;
import com.clover_studio.spikachatmodule.models.UploadFileResult;
import com.clover_studio.spikachatmodule.robospice.api.UploadFileManagement;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.clover_studio.spikachatmodule.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.net.URI;


public class RecordVideoActivity extends BaseActivity {

    String filePath;
    private VideoView mVideoView;
    private int mIsPlaying = 0;
    private ProgressBar mPbForPlaying;
    private ImageView mPlayPause;
    private Handler mHandlerForProgressBar = new Handler();
    private Runnable mRunnForProgressBar;

    private long mDurationOfVideo = 0;

    private LinearLayout chooseLayout;
    private LinearLayout confirmLayout;

    public static void starVideoPreviewActivity(Context context){
        Intent intent = new Intent(context, RecordVideoActivity.class);
        ((Activity)context).startActivityForResult(intent, Const.RequestCode.VIDEO_CHOOSE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        setToolbar(R.id.tToolbar, R.layout.custom_camera_preview_toolbar);
        setMenuLikeBack();

        findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        findViewById(R.id.galleryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        findViewById(R.id.okButton).setOnClickListener(onOkClickedListener);
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mIsPlaying = 0;

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mPbForPlaying = (ProgressBar) findViewById(R.id.progressBar);
        mPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
        confirmLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        chooseLayout = (LinearLayout) findViewById(R.id.buttonsChooseLayout);

        mPlayPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsPlaying == 2) {
                    // pause
                    mPlayPause.setImageResource(R.drawable.ic_play);
                    onPlay(2);
                } else {
                    // play
                    mPlayPause.setImageResource(R.drawable.ic_pause);
                    onPlay(0);
                }
            }
        });

        mPlayPause.setClickable(false);
        mPlayPause.setEnabled(false);

    }

    /**
     * open gallery to choose video
     */
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Const.RequestCode.GALLERY);
    }

    /**
     * open camera to record video
     */
    private void openCamera() {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && !getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            NotifyDialog.startInfo(this, getString(R.string.camera_error_title), getString(R.string.camera_error_no_camera));
        } else {

            try {
                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Const.Video.MAX_RECORDING_VIDEO_TIME);
                File videoFolder = Environment.getExternalStorageDirectory();

                videoFolder.mkdirs(); // <----
                File video = new File(videoFolder, Const.FilesName.VIDEO_TEMP_FILE_NAME);
                Uri uriSavedVideo = Uri.fromFile(video);

                filePath = video.getPath();

                // ----> don't know why, but on older devices don't work
                // with EXTRA_OUTPUT
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedVideo);
                }

                startActivityForResult(cameraIntent, Const.RequestCode.CAMERA);
            } catch (Exception ex) {
                ex.printStackTrace();
                NotifyDialog.startInfo(this, getString(R.string.camera_error_title), getString(R.string.camera_error_camera_init));
            }
        }

    }

    private View.OnClickListener onOkClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final UploadFileDialog dialog = UploadFileDialog.startDialog(getActivity());

            UploadFileManagement tt = new UploadFileManagement();
            tt.new BackgroundUploader(SpikaApp.getConfig().apiBaseUrl + Const.Api.UPLOAD_FILE, new File(filePath), Const.ContentTypes.VIDEO_MP4, new UploadFileManagement.OnUploadResponse() {
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
    };

    private void onResponseFailed() {
        NotifyDialog.startInfo(getActivity(), getString(R.string.error), getString(R.string.file_not_found));
    }

    protected void onResponseFinish(String result){
        ObjectMapper mapper = new ObjectMapper();
        UploadFileResult data = null;
        try {
            data = mapper.readValue(result, UploadFileResult.class);
        } catch (IOException e) {
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
        try {

            if(requestCode == Const.RequestCode.CAMERA){
                if(data == null || data.getData() == null){
                    File file = new File(filePath);
                    if(file.exists()){
                        setConfirmLayout();
                        return;
                    }
                }
            }

            Uri selectedVideoUri = data.getData();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                filePath = getVideoPath(selectedVideoUri);
            } else {
                if(selectedVideoUri.getScheme().equals("content")
                        || selectedVideoUri.getScheme().equals("file")){
                    filePath = getVideoPath(selectedVideoUri);
                }else{
                    filePath = selectedVideoUri.toString();
                }
            }

            setConfirmLayout();

        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    /**
     * replace gallery/camera layout with OK/CANCEL layout
     */
    private void setConfirmLayout(){
        mPlayPause.setEnabled(true);
        mPlayPause.setClickable(true);

        chooseLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.VISIBLE);

        File forSize = new File(filePath);
        if(forSize.exists()){
            long size = forSize.length();
            Button ok = (Button) findViewById(R.id.okButton);
            ok.setText(ok.getText() + ", " + Tools.readableFileSize(size));
        }
    }

    private String getVideoPath(Uri uri) {

        if (uri.getScheme().equals("content")) {

            String[] proj = { MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME };
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);

            int column_index_path = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index_path);

        } else if (uri.getScheme().equals("file")) {
            return new File(URI.create(uri.toString())).getAbsolutePath();
        }

        return null;
    }

    //PLAYING VIDEO METHODS
    private void onPlay(int playPauseStop) {

        if (playPauseStop == 0) {

            startPlaying();

        } else if (playPauseStop == 1) {

            pausePlaying();

        } else {

            stopPlaying();

        }
    }

    private void startPlaying() {
        if (mIsPlaying == 0) {
            mVideoView.setVideoURI(Uri.parse(filePath));
            mVideoView.start();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mDurationOfVideo = mVideoView.getDuration();

                    mPbForPlaying.setMax((int) mDurationOfVideo);

                    mRunnForProgressBar = new Runnable() {

                        @Override
                        public void run() {
                            mPbForPlaying.setProgress(mVideoView.getCurrentPosition());
                            if (mDurationOfVideo - 99 > mVideoView.getCurrentPosition()) {
                                mHandlerForProgressBar.postDelayed(mRunnForProgressBar, 100);
                            } else {
                                mPbForPlaying.setProgress(mVideoView.getDuration());
                            }
                        }
                    };
                    mHandlerForProgressBar.post(mRunnForProgressBar);
                    mIsPlaying = 2;
                }
            });

            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayPause.setImageResource(R.drawable.ic_play);
                    stopPlaying();
                }
            });

        } else if (mIsPlaying == 1) {
            mVideoView.start();
            mHandlerForProgressBar.post(mRunnForProgressBar);
            mIsPlaying = 2;
        }

    }

    private void stopPlaying() {
        mVideoView.stopPlayback();
        mHandlerForProgressBar.removeCallbacks(mRunnForProgressBar);
        mPbForPlaying.setProgress(0);
        mIsPlaying = 0;
        mPlayPause.setImageResource(R.drawable.ic_play);
    }

    private void pausePlaying() {
        mVideoView.pause();
        mHandlerForProgressBar.removeCallbacks(mRunnForProgressBar);
        mIsPlaying = 1;
    }
}
