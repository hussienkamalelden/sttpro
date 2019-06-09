package com.hblackcat.sttpro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.hblackcat.sttpro.DataBase.MySQLiteHelper;
import com.suke.widget.SwitchButton;

public class InnerSettings extends Activity {

    private SeekBar auto_stt_delay_seekbar,tts_speed_seekbar;
    private TextView seekbar_numb,title,auto_stt_delay_title,auto_stt_delay_desc
                        ,seekbar_tts_numb,tts_speed_title,tts_speed_desc,confirm
                        ,reserved_words_title,reserved_words_desc
                        ,reset_title,reset_desc
                        ,video_title,video_desc;
    private RelativeLayout confirm_box,reset_box,video_box;
    private Typeface typeFace;
    private SwitchButton reserved_word_switch;
    private MySQLiteHelper db;
    private int progressChangedValueSTT = 3 ;
    private float progressChangedValueTTS = 1.0f;
    private boolean reserved_word_activation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_settings);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        auto_stt_delay_seekbar = findViewById(R.id.auto_stt_delay_seekbar);
        tts_speed_seekbar = findViewById(R.id.tts_speed_seekbar);
        seekbar_numb = findViewById(R.id.seekbar_numb);
        reserved_words_title = findViewById(R.id.reserved_words_title);
        reserved_words_desc = findViewById(R.id.reserved_words_desc);
        reserved_word_switch = findViewById(R.id.reserved_word_switch);
        auto_stt_delay_title = findViewById(R.id.auto_stt_delay_title);
        auto_stt_delay_desc = findViewById(R.id.auto_stt_delay_desc);
        seekbar_tts_numb = findViewById(R.id.seekbar_tts_numb);
        tts_speed_title = findViewById(R.id.tts_speed_title);
        tts_speed_desc = findViewById(R.id.tts_speed_desc);
        confirm_box = findViewById(R.id.confirm_box);
        video_title = findViewById(R.id.video_title);
        video_desc = findViewById(R.id.video_desc);
        title = findViewById(R.id.title);
        reset_box = findViewById(R.id.reset_box);
        confirm = findViewById(R.id.confirm);
        video_box = findViewById(R.id.video_box);
        reset_title = findViewById(R.id.reset_title);
        reset_desc = findViewById(R.id.reset_desc);
        reserved_words_desc.setTypeface(typeFace);
        reserved_words_title.setTypeface(typeFace);
        video_desc.setTypeface(typeFace);
        video_title.setTypeface(typeFace);
        confirm.setTypeface(typeFace);
        tts_speed_desc.setTypeface(typeFace);
        tts_speed_title.setTypeface(typeFace);
        seekbar_tts_numb.setTypeface(typeFace);
        seekbar_numb.setTypeface(typeFace);
        title.setTypeface(typeFace);
        auto_stt_delay_title.setTypeface(typeFace);
        auto_stt_delay_desc.setTypeface(typeFace);
        reset_title.setTypeface(typeFace);
        reset_desc.setTypeface(typeFace);

        //get data from DB
        getDataFromDB();

        //Set default values for STT SeekBar ..
        auto_stt_delay_seekbar.setMax(9);
        auto_stt_delay_seekbar.setProgress(progressChangedValueSTT-1);
        seekbar_numb.setText(progressChangedValueSTT+"");
        // perform seek bar change listener event used for getting the progress value ..
        auto_stt_delay_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValueSTT = progress+1;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar_numb.setText(progressChangedValueSTT+"");
            }
        });


        //Set default values for TTS SeekBar ..
        tts_speed_seekbar.setMax(3);
        int tts_value = getIntValue(progressChangedValueTTS);
        tts_speed_seekbar.setProgress(tts_value);
        seekbar_tts_numb.setText(progressChangedValueTTS+"");
        // perform seek bar change listener event used for getting the progress value ..
        tts_speed_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                getFloatValue(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {
                seekbar_tts_numb.setText(progressChangedValueTTS+"");
            }
        });

        //switch ..
        reserved_word_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked == true){
                    reserved_word_activation = true;
                }else{
                    reserved_word_activation = false;
                }
            }
        });

        //video_box ..
        video_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //open youtube ..
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=8PRapRkspTw&feature=youtu.be")));
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //reset_box ..
        reset_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //show Dialog ..
                try {
                    //Alert Dialog ..
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(InnerSettings.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(InnerSettings.this);
                    }
                    builder.setMessage("سيتم مسح جميع إعدادات التطبيق والعودة للوضع الإفتراضي مع الحفاظ على الملفات المحفوظة .. هل تريد الاستمرار؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue ..
                                    try
                                    {
                                        // clearing app data
                                        String packageName = getApplicationContext().getPackageName();
                                        Runtime runtime = Runtime.getRuntime();
                                        runtime.exec("pm clear "+packageName);
                                    }catch(Exception e){e.printStackTrace();}

                                }
                            })
                            .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //confirm button ..
        confirm_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Save in DB and close ..
                try
                {
                    db =new MySQLiteHelper(InnerSettings.this);
                    db.replaceValue("auto_stt","auto_stt",progressChangedValueSTT+"");
                    db.replaceValue("tts_speed","tts_speed",progressChangedValueTTS+"");
                    db.replaceValue("reserved_words","reserved_words",reserved_word_activation+"");
                    finish();
                }catch(Exception e){e.printStackTrace();}
            }
        });
    }

    // Get float value ..
    private float getFloatValue(int progress)
    {
        switch(progress)
        {
            case 0 : progressChangedValueTTS = .5f; break;
            case 1 : progressChangedValueTTS = 1.0f; break;
            case 2 : progressChangedValueTTS = 1.5f; break;
            case 3 : progressChangedValueTTS = 2.0f; break;
        }
        return progressChangedValueTTS;
    }

    // Get int value ..
    private int getIntValue(float progress)
    {
        int y = 1;
        if(progress == .5f)
            y = 0;
        else if(progress == 1.0f)
            y = 1;
        else if(progress == 1.5f)
            y = 2;
        else if(progress == 2.0f)
            y = 3;
        return y;
    }

    //Get data from DB ..
    private void getDataFromDB()
    {
        try
        {
            db =new MySQLiteHelper(InnerSettings.this);
            progressChangedValueSTT = Integer.parseInt(db.getValue("auto_stt"));
            progressChangedValueTTS = Float.parseFloat(db.getValue("tts_speed"));
            reserved_word_activation = Boolean.parseBoolean(db.getValue("reserved_words"));
        }catch(Exception e){
            progressChangedValueSTT = 3 ;
            progressChangedValueTTS = 1.0f;
            reserved_word_activation = true;
        }

        //Initialize buttons ..
        try{
            if(reserved_word_activation == true)
                reserved_word_switch.setChecked(true);
            else
                reserved_word_switch.setChecked(false);
        }catch(Exception e){e.printStackTrace();}
    }
}