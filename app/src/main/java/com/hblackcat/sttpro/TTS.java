package com.hblackcat.sttpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;
import com.hblackcat.sttpro.DataBase.MySQLiteHelper;

import java.util.Locale;

public class TTS extends Activity {

    private TextView tts_text;
    private String last_font_code,last_lang_code;
    private TextToSpeech tts_engine;
    private Typeface typeFace;
    private MySQLiteHelper db;
    private float speed_rate = 1.0f; // Default value ..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        Intent intents = getIntent();
        tts_text = findViewById(R.id.tts_text);
        tts_text.setTypeface(typeFace);
        db =new MySQLiteHelper(TTS.this);

        //Get speed rate from DB .. if DB crashed it will be 1.0f always ..
        try
        {
            speed_rate = Float.parseFloat(db.getValue("tts_speed"));
        }catch(Exception e){e.printStackTrace();}

        //get content ..
        try
        {
            tts_text.setText(intents.getExtras().getString("content_tts"));
        }catch(Exception e){e.printStackTrace();}

        //get font code  ..
        try
        {
            //Set the same font from the last activity ..
            last_font_code = intents.getExtras().getString("font_code_tts");
            typeFace = Typeface.createFromAsset(getAssets(), last_font_code); // here for all view ..
            tts_text.setTypeface(typeFace);
        }catch(Exception e){e.printStackTrace();}

        //get font size ..
        try
        {
            float font_size = intents.getExtras().getFloat("font_size_tts");
            tts_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,font_size);
        }catch(Exception e){e.printStackTrace();}

        //get lang code And Start TTS ..
        try{
            last_lang_code = intents.getExtras().getString("lang_code_tts");
            initializeTTS(last_lang_code);
        }catch(Exception e){e.printStackTrace();}
    }

    //Initialize TTS ..
    private void initializeTTS(final String lang_code_tts)
    {
        try{
            tts_engine=new TextToSpeech(TTS.this, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    // TODO Auto-generated method stub
                    if(status == TextToSpeech.SUCCESS){
                        int result=tts_engine.setLanguage(new Locale(lang_code_tts));
                        if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED) {
                            stopTTS();
                        }else {
                            TTSConverter();
                        }
                    }
                    else {
                        Toast.makeText(TTS.this,  getString(R.string.initialization_failed), Toast.LENGTH_LONG).show();
                        stopTTS();
                    }
                }
            });
        }catch(Exception e){e.printStackTrace();}
    }

    //Text to speech Converter ..
    private void TTSConverter() {
        try {
            String text = tts_text.getText().toString();
            if (!(text == null || text.equals(""))) {
                tts_engine.setSpeechRate(speed_rate);
                tts_engine.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }else
                stopTTS();
        }catch (Exception e){e.printStackTrace();}
    }

    //Stop TTS ..
    private void stopTTS()
    {
        try{
            tts_engine.stop();
            tts_engine.shutdown();
        }catch(Exception e){e.printStackTrace();}
    }

    //onPause ..
    @Override
    protected void onPause() {

        super.onPause();

        //Stop TTS ..
        try{
            stopTTS();
        }catch(Exception e){e.printStackTrace();}
    }
}