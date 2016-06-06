package com.clover_studio.spikachatmodule.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.api.DownloadFileManager;
import com.clover_studio.spikachatmodule.models.FileModel;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.clover_studio.spikachatmodule.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class PreviewAudioDialog extends Dialog {

    FileModel fileModel;

    private File sound = null;

    private SeekBar seekBar;
    private Chronometer chronometer;
    private MediaObserver observer;
    private MediaPlayer mediaPlayer;
    private ImageButton playPause;

    public static PreviewAudioDialog startDialog(Context context, FileModel fileModel){
        PreviewAudioDialog dialog = new PreviewAudioDialog(context, fileModel);
        return dialog;
    }

    public PreviewAudioDialog(Context context, FileModel fileModel) {
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
        setContentView(R.layout.dialog_preview_audio);

        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        chronometer = (Chronometer) findViewById(R.id.chronoPlay);
        playPause = (ImageButton) findViewById(R.id.playPause);

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(0);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                play(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (observer != null)
                    observer.stop();
                observer = null;
                if (seekBar != null && seekBar != seekBar)
                    seekBar.setProgress(0);
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = null;
                if (chronometer != null) {
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
                if (playPause != null) {
                    playPause.setBackgroundResource(R.drawable.ic_play);
                }
                seekBar.setMax(100);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        ((TextView)findViewById(R.id.nameOfAudio)).setText(fileModel.file.name);

    }

    /**
     * play audio file
     * @param startOffset start offset
     */
    private void play(final int startOffset) {
        if(mediaPlayer != null){
            if (observer != null)
                observer.stop();
            observer = null;
            seekBar.setProgress(0);
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            playPause.setBackgroundResource(R.drawable.ic_play);

            return;
        }
        mediaPlayer = new MediaPlayer();
        try {

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    if (startOffset != 0) {
                        double offset = (((double) seekBar.getMax() * (double) startOffset) / (double) 100);
                        mediaPlayer.seekTo((int) offset);
                        chronometer.setBase((long) (SystemClock.elapsedRealtime() - offset));
                    }
                }
            });

            mediaPlayer.setDataSource(sound.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            observer = new MediaObserver();
            new Thread(observer).start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    observer.stop();
                    seekBar.setProgress(0);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    playPause.setBackgroundResource(R.drawable.ic_play);
                }
            });

            playPause.setBackgroundResource(R.drawable.ic_pause);
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer = null;
        }
    }

    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                seekBar.setProgress((int) elapsedMillis);
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void show() {
        super.show();

        File audioFile = new File(Tools.getAudioFolderPath() + "/" + fileModel.file.name + "_" + fileModel.file.id);

        if(audioFile.exists()){
            String filePath = audioFile.getAbsolutePath();
            sound = new File(filePath);
        }else{

            final DownloadFileDialog dialog = DownloadFileDialog.startDialog(getOwnerActivity());

            DownloadFileManager.downloadVideo(getOwnerActivity(), Tools.getFileUrlFromId(fileModel.file.id, getOwnerActivity()), audioFile, new DownloadFileManager.OnDownloadListener() {
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
                            sound = new File(path);
                        }
                    });
                }
            });

        }

    }

    @Override
    public void dismiss() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            play(0);
        }
        super.dismiss();
    }
}