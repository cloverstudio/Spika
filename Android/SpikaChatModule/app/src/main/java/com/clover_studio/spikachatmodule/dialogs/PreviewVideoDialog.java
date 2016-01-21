package com.clover_studio.spikachatmodule.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.models.FileModel;
import com.clover_studio.spikachatmodule.robospice.api.DownloadFileManager;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.clover_studio.spikachatmodule.utils.Tools;

import java.io.File;

public class PreviewVideoDialog extends Dialog {

    FileModel fileModel;

    private String filePath = null;

    private VideoView mVideoView;

    private final int VIDEO_IS_PLAYING = 2;
    private final int VIDEO_IS_PAUSED = 1;
    private final int VIDEO_IS_STOPPED = 0;

    private int mIsPlaying = VIDEO_IS_STOPPED; // 0 - play is on stop, 1 - play
    // is on pause, 2
    // - playing

    private ProgressBar mPbForPlaying;
    private ImageView mPlayPause;

    private Handler mHandlerForProgressBar = new Handler();
    private Runnable mRunnForProgressBar;

    private long mDurationOfVideo = 0;

    public static PreviewVideoDialog startDialog(Context context, FileModel fileModel){
        PreviewVideoDialog dialog = new PreviewVideoDialog(context, fileModel);
        return dialog;
    }

    public PreviewVideoDialog(Context context, FileModel fileModel) {
        super(context, R.style.Theme_Dialog);

        setOwnerActivity((Activity) context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        this.fileModel = fileModel;

        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_preview_video);

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
        mPbForPlaying = (ProgressBar) findViewById(R.id.progressBar);

        mVideoView.setZOrderOnTop(true);

        mIsPlaying = VIDEO_IS_STOPPED;

        mPlayPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsPlaying == VIDEO_IS_PLAYING) {
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

        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();

        File videoFile = new File(Tools.getVideoFolderPath() + "/" + fileModel.file.name + "_" + fileModel.file.id);

        if(videoFile.exists()){
            filePath = videoFile.getAbsolutePath();
        }else{

            final DownloadFileDialog dialog = DownloadFileDialog.startDialog(getOwnerActivity());

            DownloadFileManager.downloadVideo(getOwnerActivity(), Tools.getFileUrlFromId(fileModel.file.id), videoFile, new DownloadFileManager.OnDownloadListener() {
                @Override
                public void onStart() {
                    LogCS.d("LOG", "START DOWNLOADING");
                }

                @Override
                public void onSetMax(final int max) {
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setMax(max);
                        }
                    });
                }

                @Override
                public void onProgress(final int current) {
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setCurrent(current);
                        }
                    });
                }

                @Override
                public void onFinishDownload() {
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.fileDownloaded();
                        }
                    });
                }

                @Override
                public void onResponse(boolean isSuccess, final String path) {
                    getOwnerActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            filePath = path;
                        }
                    });
                }
            });

        }

    }

    /**
     * play or stop video
     * @param playPauseStop 0 - play, 1 - pause, 2 - stop
     */
    private void onPlay(int playPauseStop) {
        if (playPauseStop == 0) {
            startPlaying();
        } else if (playPauseStop == 1) {
            pausePlaying();
        } else {
            stopPlaying();

        }
    }

    /**
     * play video and start runnable for seekbar
     */
    private void startPlaying() {
        try {
            if (mIsPlaying == VIDEO_IS_STOPPED) {
                mVideoView.requestFocus();

                mVideoView.setVideoURI(Uri.parse(filePath));
                mVideoView.setVideoPath(filePath);

                mVideoView.start();

                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mDurationOfVideo = mVideoView.getDuration();
                        mPbForPlaying.setMax((int) mDurationOfVideo);

                        mRunnForProgressBar = new Runnable() {

                            @Override
                            public void run() {
                                mPbForPlaying.setProgress((int) mVideoView.getCurrentPosition());
                                if (mDurationOfVideo - 99 > mVideoView.getCurrentPosition()) {
                                    mHandlerForProgressBar.postDelayed(mRunnForProgressBar, 100);
                                } else {
                                    mPbForPlaying.setProgress((int) mVideoView.getDuration());
                                    new Handler().postDelayed(new Runnable() {
                                        // *******wait for video to finish
                                        @Override
                                        public void run() {
                                            mPlayPause.setImageResource(R.drawable.ic_play);
                                            onPlay(2);
                                        }
                                    }, 120);
                                }
                            }
                        };
                        mHandlerForProgressBar.post(mRunnForProgressBar);
                        mIsPlaying = VIDEO_IS_PLAYING;
                    }
                });

                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mPlayPause.setImageResource(R.drawable.ic_play);
                        stopPlaying();
                    }
                });

            } else if (mIsPlaying == VIDEO_IS_PAUSED) {
                mVideoView.start();
                mHandlerForProgressBar.post(mRunnForProgressBar);
                mIsPlaying = VIDEO_IS_PLAYING;
            }
        } catch (Exception e) {
            NotifyDialog.startInfo(getOwnerActivity(), getOwnerActivity().getString(R.string.error), getOwnerActivity().getString(R.string.e_something_went_wrong));
        }
    }

    /**
     * stop playing and reset runnable for seekbar
     */
    private void stopPlaying() {
        mVideoView.stopPlayback();
        mHandlerForProgressBar.removeCallbacks(mRunnForProgressBar);
        mPbForPlaying.setProgress(0);
        mIsPlaying = VIDEO_IS_STOPPED;
    }

    /**
     * pause video
     */
    private void pausePlaying() {
        mVideoView.pause();
        mHandlerForProgressBar.removeCallbacks(mRunnForProgressBar);
        mIsPlaying = VIDEO_IS_PAUSED;
    }

}