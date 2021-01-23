package com.dfsdk.ocr.scan.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;

public class DFMediaPlayer implements MediaPlayer.OnCompletionListener {
    public static final String TAG = "DFMediaPlayer";

    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private int mAudioId;

    public void setMediaSource(Context context, int resId) {
        if (mAudioId != resId) {
            mAudioId = resId;
            release();
            try {
                mHandler = new Handler();
                AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(resId);
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                prepareAndPlay();
                start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            start();
        }
    }

    public void prepareAndPlay() {
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        DFOCRScanUtils.logI(TAG, "stop");
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(null);
            if (mMediaPlayer.isPlaying()) {
                DFOCRScanUtils.logI(TAG, "stop===2");
                mMediaPlayer.stop();
            }
        }

        removeRepeatPlayMessage();
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        removeRepeatPlayMessage();
    }

    public void removeRepeatPlay() {
        if (mMediaPlayer != null){
            mMediaPlayer.setOnCompletionListener(null);
            removeRepeatPlayMessage();
        }
    }

    private void removeRepeatPlayMessage() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void start() {
        DFOCRScanUtils.logI(TAG, "start");
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(this);
        }
    }

    private void restartPrepareAndPlay() {
        DFOCRScanUtils.logI(TAG, "restartPrepareAndPlay");
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        DFOCRScanUtils.logI(TAG, "onCompletion");
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DFOCRScanUtils.logI(TAG, "onCompletion===postDelayed===");
                    start();
                }
            }, 1000);
        }
    }
}
