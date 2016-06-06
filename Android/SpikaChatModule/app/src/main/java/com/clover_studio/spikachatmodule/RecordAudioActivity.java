package com.clover_studio.spikachatmodule;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.clover_studio.spikachatmodule.api.UploadFileManagement;
import com.clover_studio.spikachatmodule.base.BaseActivity;
import com.clover_studio.spikachatmodule.base.SingletonLikeApp;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;
import com.clover_studio.spikachatmodule.dialogs.UploadFileDialog;
import com.clover_studio.spikachatmodule.models.UploadFileResult;
import com.clover_studio.spikachatmodule.utils.AnimUtils;
import com.clover_studio.spikachatmodule.utils.Const;
import com.clover_studio.spikachatmodule.utils.ExtAudioRecorder;
import com.clover_studio.spikachatmodule.utils.LogCS;
import com.clover_studio.spikachatmodule.utils.Tools;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


public class RecordAudioActivity extends BaseActivity {

    private static int START_PLAYING = 0;
    private static int PAUSE_PLAYING = 1;
    private static int STOP_PLAYING = 2;

    private static int PLAYING = 2;
    private static int PAUSE = 1;
    private static int STOP = 0;

    private boolean mIsRecording;
    private String mFilePath = null;
    private String mFileName = null;
    private ExtAudioRecorder mExtAudioRecorder;

    private Chronometer mRecordTime;

    private int mIsPlaying = STOP; // 0 - play is on stop, 1 - play is on pause,
    // 2 - playing
    private MediaPlayer mPlayer = null;
    private ImageView mPlayPause;
    private CardView mRlSoundControler;

    private ImageView startRec;

    private AsyncTask<Void, Void, Void> recordingAsync;
    private CountDownTimer mRecordingTimer;

    private Chronometer firstChornometer;
    private TextView secondChronometer;
    private CountDownTimer soundLeft;
    private SeekBar seekBarSound;
    private Button okButton;

    private boolean animating = false;
    private View animateSolid;
    private View animateStroke;
    private View viewBorderForHolder;
    private TextView tvSave;

    public static void starRecordAudioActivity(Context context){
        Intent intent = new Intent(context, RecordAudioActivity.class);
        ((Activity)context).startActivityForResult(intent, Const.RequestCode.AUDIO_CHOOSE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        setToolbar(R.id.tToolbar, R.layout.custom_camera_preview_toolbar);
        setMenuLikeBack();
        setToolbarTitle(getString(R.string.audio));

        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(onOkClickedListener);
        okButton.setEnabled(false);
        okButton.setClickable(false);
        findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRecordingTimer = new CountDownTimer(Const.Video.MAX_RECORDING_AUDIO_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                onRecord(!mIsRecording);
                NotifyDialog.startInfo(getActivity(), getString(R.string.audio_time_error), getString(R.string.audio_time_text));
            }

        };

        mRecordTime = (Chronometer) findViewById(R.id.recordTime);
        mRlSoundControler = (CardView) findViewById(R.id.scCardView);
        mPlayPause = (ImageView) findViewById(R.id.ivPlayPauseSound);

        startRec = (ImageView) findViewById(R.id.startRec);
        viewBorderForHolder = findViewById(R.id.viewBorderHolderForAnimate);
        tvSave = (TextView) findViewById(R.id.tvSaveInRecord);
        viewBorderForHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsRecording) {
                    onRecord(!mIsRecording);
                } else {
                    hideAndRestartSoundController();
                    mIsRecording = true;
                    onRecord(mIsRecording);
                }
            }
        });

        firstChornometer = (Chronometer) findViewById(R.id.firstChrono);
        secondChronometer = (TextView) findViewById(R.id.secondChrono);
        seekBarSound = (SeekBar) findViewById(R.id.seekBarSound);

        mRlSoundControler.setVisibility(View.INVISIBLE);

        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startPlaying(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (observer != null)
                    observer.stop();
                observer = null;
                stopPlaying();
                if (firstChornometer != null) {
                    firstChornometer.stop();
                    firstChornometer.setBase(SystemClock.elapsedRealtime());
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        animateSolid = findViewById(R.id.viewSolidForAnimate);
        animateStroke = findViewById(R.id.viewStrokeForAnimate);

        float widthOfImage = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        float width = getResources().getDisplayMetrics().widthPixels;
        float height = getResources().getDisplayMetrics().heightPixels;

        if(width > height){
            scale = width / widthOfImage;
        }else{
            scale = height / widthOfImage;
        }
    }

    float scale;

    private void startAnimate(){
        animateSolid.setVisibility(View.VISIBLE);
        AnimUtils.fade(animateSolid, 0, 1, 0, null);
        animateStroke.setVisibility(View.VISIBLE);
        animating = true;
        AnimUtils.scale(animateSolid, 1, scale, 1000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animateViewBorder();
            }
        });
        AnimUtils.scale(startRec, 1, 0.85f, 350, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvSave.setVisibility(View.VISIBLE);
                AnimUtils.fade(tvSave, 0, 1, 350, null);
                AnimUtils.translateY(tvSave, 0, -20, 350, null);
                AnimUtils.translateY(startRec, 0, -20, 350, null);
            }
        });
        AnimUtils.scale(animateStroke, 1, 0.92f, 350, null);
        AnimUtils.scale(viewBorderForHolder, 1, 0.92f, 800, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AnimUtils.scale(viewBorderForHolder, 0.92f, 1, 350, null);
                AnimUtils.scale(animateStroke, 0.92f, 1, 350, null);

            }
        });

        setToolbarTitle(getString(R.string.recording_));
    }

    private void stopAnimating(){
        animating = false;
        animateStroke.setVisibility(View.GONE);
        AnimUtils.fadeThenGoneOrVisible(animateSolid, 1, 0, 300);
        AnimUtils.scale(startRec, 0.85f, 1, 200, null);
        AnimUtils.translateY(startRec, -20, 0, 200, null);
        AnimUtils.fade(tvSave, 1, 0, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                tvSave.setVisibility(View.INVISIBLE);
            }
        });
        AnimUtils.translateY(tvSave, -20, 0, 200, null);

        setToolbarTitle(getString(R.string.audio));
    }

    private void animateViewBorder(){
        AnimUtils.scale(animateStroke, 1, scale, 1000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(animating){
                    animateViewBorder();
                }
            }
        });
    }

    /**
     * start or stop recording
     * @param start true start recording, false stop recording
     */
    private void onRecord(boolean start) {
        if (start) {
            onPlay(STOP_PLAYING);
            mPlayPause.setImageResource(R.drawable.ic_play);

            startRecordingAsync();

            startAnimate();

        } else {

            if (!mIsRecording) {
                recordingAsync.cancel(true);
            }

            stopRecording();

            stopAnimating();

            try{
                long size = 0;
                File forSize = new File(mFilePath);
                if(forSize.exists()){
                    size = forSize.length();
                    String text = getString(R.string.OK) + ", " + Tools.readableFileSize(size);
                    okButton.setText(text);
                }
            }catch (Exception e){
                e.printStackTrace();
            };

        }
    }

    private void startRecordingAsync() {
        recordingAsync = new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
//                mRecordTime.setVisibility(View.VISIBLE);
                mRecordTime.setBase(SystemClock.elapsedRealtime());
                mRecordTime.start();
                mRecordingTimer.start();
            };

            protected Void doInBackground(Void... params) {
                startRecording();
                return null;
            };
        }.execute();
    }

    private void startRecording() {
        setRecordingFile();

        mExtAudioRecorder = ExtAudioRecorder.getInstanse(false);
        mExtAudioRecorder.setOutputFile(mFilePath);
        mExtAudioRecorder.prepare();
        mExtAudioRecorder.start();
    }

    // stop recodrding for extaudio class
    private void stopRecording() {
        mExtAudioRecorder.stop();
        mExtAudioRecorder.release();
        mRecordTime.stop();
//        mRecordTime.setVisibility(View.INVISIBLE);
        showSoundController();
        mIsRecording = false;
        mRecordingTimer.cancel();
    }

    private void hideAndRestartSoundController() {

        mRlSoundControler.setVisibility(View.INVISIBLE);

        okButton.setEnabled(false);
        okButton.setClickable(false);

//        mRecordTime.setVisibility(View.INVISIBLE);

        mPlayPause.setImageResource(R.drawable.ic_play);
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void showSoundController() {

        okButton.setEnabled(true);
        okButton.setClickable(true);

        mIsPlaying = STOP;

        mPlayPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIsPlaying == PLAYING) {
                    // pause
                    mPlayPause.setImageResource(R.drawable.ic_play);
                    onPlay(PAUSE_PLAYING);
                } else {
                    // play
                    mPlayPause.setImageResource(R.drawable.ic_pause);
                    onPlay(START_PLAYING);
                }
            }
        });

        mRlSoundControler.setVisibility(View.VISIBLE);

    }

    private void onPlay(int playPauseStop) { // 0 is to start playing, 1 is to
        // pause playing and 2 is for
        // stop playing

        if (playPauseStop == START_PLAYING) {
            startPlaying(0);
        } else if (playPauseStop == PAUSE_PLAYING) {
            // pausePlaying();
            stopPlaying();
        } else {
            stopPlaying();
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
                long elapsedMillis = SystemClock.elapsedRealtime() - firstChornometer.getBase();
                seekBarSound.setProgress((int) elapsedMillis);
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MediaObserver observer = null;

    private void startPlaying(final int offset) {
        if (mIsPlaying == STOP) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(mFilePath);
                mPlayer.prepare();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBarSound.setMax(mp.getDuration());
                        if (offset != 0) {
                            mPlayer.seekTo((int) offset);
                            firstChornometer.setBase((long) (SystemClock.elapsedRealtime() - offset));
                        }
                        startChronoSecond(mp.getDuration(), offset);
                    }
                });
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopChronoAndSeek();
                        stopPlaying();
                        mPlayPause.setImageResource(R.drawable.ic_pause);
                    }
                });
                mPlayer.start();
                startChronoAndSeek();
                mIsPlaying = PLAYING;

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mIsPlaying == PAUSE) {
            mPlayer.start();
            // CONTINUE SEEKBAR AND CHRONO
            mIsPlaying = PLAYING;
        }

    }

    private void startChronoSecond(int duration, int offset) {
        soundLeft = new CountDownTimer(duration - offset, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                secondChronometer.setText(String.format("-00:%02d", va));
            }

            @Override
            public void onFinish() {
                secondChronometer.setText("00:00");
            }
        };
        soundLeft.start();
    }

    protected void stopChronoAndSeek() {
        if (observer != null)
            observer.stop();
        seekBarSound.setProgress(0);
        firstChornometer.stop();
        firstChornometer.setBase(SystemClock.elapsedRealtime());
        if (soundLeft != null)
            soundLeft.cancel();
        secondChronometer.setText("00:00");
    }

    private void startChronoAndSeek() {
        firstChornometer.setBase(SystemClock.elapsedRealtime());
        firstChornometer.start();

        observer = new MediaObserver();
        new Thread(observer).start();
    }

    private void stopPlaying() {

        if (mPlayer != null) {
            mPlayer.release();
        }

        stopChronoAndSeek();
        mPlayer = null;
        mIsPlaying = STOP;
    }

    private void pausePlaying() {
        mPlayer.pause();
        mIsPlaying = PAUSE;
    }

    private void setRecordingFile() {
        mFilePath = new File(Tools.getAudioFolderPath(), Const.FilesName.AUDIO_TEMP_FILE_NAME).getAbsolutePath();
        String[] items = mFilePath.split("/");
        mFileName = items[items.length - 1];
    }

    public void onPause() {
        super.onPause();
        if (mExtAudioRecorder != null) {
            stopRecording();
            mExtAudioRecorder.release();
            mExtAudioRecorder.stop();
            mExtAudioRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mIsPlaying = STOP;
            mPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    public void onFinish() {
        if (mExtAudioRecorder != null) {
            mExtAudioRecorder.release();
            mExtAudioRecorder.stop();
            mExtAudioRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mIsPlaying = STOP;
            mPlayPause.setImageResource(R.drawable.ic_play);
        }

        super.onDestroy();
    }

    @Override
    protected void onDestroy() {
        mRecordingTimer.cancel();
        mRecordingTimer = null;
        super.onDestroy();
    }

    private View.OnClickListener onOkClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final UploadFileDialog dialog = UploadFileDialog.startDialog(getActivity());

            UploadFileManagement tt = new UploadFileManagement();
            tt.new BackgroundUploader(SingletonLikeApp.getInstance().getConfig(getActivity()).apiBaseUrl + Const.Api.UPLOAD_FILE, new File(mFilePath), Const.ContentTypes.AUDIO_WAV, new UploadFileManagement.OnUploadResponse() {
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
        Gson gson = new Gson();
        UploadFileResult data = null;
        try {
            data = gson.fromJson(result, UploadFileResult.class);
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

}
