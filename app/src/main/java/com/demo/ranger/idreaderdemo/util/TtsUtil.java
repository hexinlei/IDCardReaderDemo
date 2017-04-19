package com.demo.ranger.idreaderdemo.util;

/**
 * Created by hexinlei on 2017/4/6.
 */
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TtsUtil {

    public static TextToSpeech mTts;

    public static void read(Context ctx, final String content) {

        if (null != mTts) {
            mTts.stop();
            try {
                mTts.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mTts = new TextToSpeech(ctx, new OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    if (null != mTts) {
                        mTts.setSpeechRate(1.0f);
                        mTts.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                        mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onDone(String utteranceId) {
                                mTts.stop();
                                mTts.shutdown();
                            }

                            @Override
                            public void onError(String utteranceId) {
                                mTts.stop();
                                mTts.shutdown();
                            }

                            @Override
                            public void onStart(String utteranceId) {
                            }
                        });
                    } else {
                        Log.e("RFLauncher", "Cann't create TextToSpeech object");
                    }
                }
            }
        });
    }
}
