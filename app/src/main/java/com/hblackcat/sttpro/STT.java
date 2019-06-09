package com.hblackcat.sttpro;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.hblackcat.sttpro.DataBase.MySQLiteHelper;
import com.suke.widget.SwitchButton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ren.qinc.edit.PerformEdit;

public class STT extends Activity {

    private TextToSpeech tts_engine;
    private EditText ContentText;
    private TextView words_counter,auto_stt_counter;
    private String lang_name,lang_name_2,lang_code,lang_code_2,font_code,current_lang_code;
    private SwitchButton CheckAutoStt;
    private Handler handler=new Handler();
    private ImageView SpeakButton,Copy,Zoom_In,Zoom_Out,Share,Delete,Listen,to_text_file,exist_files,tts,reserved_words,
                        keyboard,redo,undo,inner_settings,replace,up_down_action_bar,scroll_up,scroll_down;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private Animation zoom_in_and_out_fast;
    private boolean IsSttActive,lang_is_supported,if_replaced,is_selected,auto_stt,speak_button_check,autostt_speakbutton_relation;
    private boolean dialog_opened = false;
    private boolean secondary_language = false;
    private Button Language;
    private Context context = this;
    private NestedScrollView nested_scroll;
    private int intent_access_num_1 = 1230321;
    private int intent_access_num_2 = 2230322;
    private int intent_access_num_3 = 3230323;
    private int intent_access_num_4 = 4230324;
    private int startSelection,endSelection,start_from;
    private long currentLength;
    private int delay_millis = 3000; // default value ..
    private MySQLiteHelper db;
    private PerformEdit func;
    private RelativeLayout ActionBar,inner_container;
    private MyCountDownTimer my_countdown_timer;


    //check Google Installation in onStart because if user go to market or press home btn and made app onPause come back to onStart ..
    @Override
    public void onStart() {
        super.onStart();

        ComponentName serviceComponent = getServiceComponent();

        //if google not installed or device not support speech recognition ..
        if (serviceComponent == null) {
            //if dialog opened before .. because in onStart dialog will opening a lot of times if called onPause and come back ..
            if(dialog_opened == false) {
                //show dialog ..
                //Dialog ..
                try {
                    dialog_opened = true;
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(STT.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(STT.this);
                    }
                    builder.setCancelable(false); // prevent user to press back btn ..
                    builder.setTitle("تنبية !")
                            .setMessage(getString(R.string.error_no_default_recognizer_dialog))
                            .setPositiveButton("استمرار", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // if yes do Something ..
                                    //TODO: goToStore();
                                    try {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.googlequicksearchbox"));
                                        startActivity(browserIntent);
                                    } catch (Exception e) {
                                        // if google play market not installed open in browser ..
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox"));
                                        startActivity(browserIntent);
                                    }
                                }
                            })
                            .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // if no ..
                                    Toast.makeText(STT.this, getString(R.string.error_no_default_recognizer), Toast.LENGTH_LONG).show();
                                }
                            }).show();
                } catch (Exception e) {
                    Toast.makeText(STT.this, getString(R.string.error_no_default_recognizer), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //this SuppressLint ClickableViewAccessibility for ContentText setOnTouchListener .. NewApi for setShowSoftInputOnFocus ..
    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stt);

        //Initialize ..
        Intent intent = getIntent();
        zoom_in_and_out_fast = AnimationUtils.loadAnimation(this, R.anim.zoom_in_and_out_fast);
        final Typeface typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        ActionBar = findViewById(R.id.ActionBar);
        auto_stt_counter = findViewById(R.id.auto_stt_counter);
        Language = findViewById(R.id.Language);
        up_down_action_bar = findViewById(R.id.up_down_action_bar);
        CheckAutoStt = findViewById(R.id.CheckAutoStt);
        ContentText = findViewById(R.id.ContentText);
        inner_container = findViewById(R.id.inner_container);
        nested_scroll = findViewById(R.id.nested_scroll);
        SpeakButton = findViewById(R.id.SpeakButton);
        reserved_words = findViewById(R.id.reserved_words);
        Listen = findViewById(R.id.Listen);
        Copy = findViewById(R.id.Copy);
        replace = findViewById(R.id.replace);
        Zoom_In = findViewById(R.id.Zoom_In);
        Zoom_Out = findViewById(R.id.Zoom_Out);
        Share = findViewById(R.id.Share);
        Delete = findViewById(R.id.Delete);
        tts = findViewById(R.id.tts);
        redo = findViewById(R.id.redo);
        undo = findViewById(R.id.undo);
        scroll_up = findViewById(R.id.scroll_up);
        scroll_down = findViewById(R.id.scroll_down);
        inner_settings = findViewById(R.id.inner_settings);
        keyboard = findViewById(R.id.keyboard);
        to_text_file = findViewById(R.id.to_text_file);
        exist_files = findViewById(R.id.exist_files);
        words_counter = findViewById(R.id.words_counter);
        words_counter.setTypeface(typeFace);
        ContentText.setTypeface(typeFace);
        Language.setTypeface(typeFace);
        auto_stt_counter.setTypeface(typeFace);
        Listen.setVisibility(View.INVISIBLE); // hide Listen Image ..
        db =new MySQLiteHelper(this);

        //Initialize Func for redo and undo texts in EditText
        try {
            func = new PerformEdit(ContentText);
        }catch (Exception e){e.printStackTrace();}

        //hide edittext keyboard ..
        //keep it without if condition for api smaller than 21 .. it will work anyway .. and if not working (not big deal) ..
        try{
            ContentText.setShowSoftInputOnFocus(false);
        }catch (Exception e){e.printStackTrace();}

        // keep screen on ..
        try{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {e.printStackTrace();}

        //get Lang 1 ..
        try
        {
            lang_name = intent.getExtras().getString("lang_name");
            Language.setText(lang_name);
            lang_code = intent.getExtras().getString("lang_code");
            current_lang_code = lang_code; //set current lang code ..
            checkChoosenLangTTS(current_lang_code);  //Check if choosen lang is supported by TTS ..
        }catch(Exception e){ //Default Arabic (Egypt) if error ..
            lang_code=getResources().getString(R.string.arabic_egypt_code);
            Language.setText(getResources().getString(R.string.arabic_egypt_name));
        }

        //get Lang 2 ..
        try
        {
            lang_name_2 = intent.getExtras().getString("lang_name_2");
            lang_code_2 = intent.getExtras().getString("lang_code_2");
        }catch(Exception e){ //Default Arabic (Egypt) if error ..
            lang_code_2=getResources().getString(R.string.english_united_kingdom_code);
        }

        //get Font ..
        try
        {
            font_code = intent.getExtras().getString("font_code");
            Typeface fontFace = Typeface.createFromAsset(getAssets(), font_code); // here for ContentText ..
            ContentText.setTypeface(fontFace);
        }catch(Exception e){e.printStackTrace();}

        //Get Saved Text ..
        try
        {
            SharedPreferences save = getSharedPreferences("save", Activity.MODE_PRIVATE);
            ContentText.setText(save.getString("content_text", ""));
            ContentText.setTextSize(TypedValue.COMPLEX_UNIT_PX,(save.getFloat("font_size_saved", ContentText.getTextSize()))); //get last font size OR the default ..
        }catch(Exception e){e.printStackTrace();}

        //Initialize STT ..
        try{
            initializeSTT(current_lang_code);
        }catch(Exception e){e.printStackTrace();}

        //create and open DB ..
        try {
            insertIntoDB();
        }catch (Exception e){e.printStackTrace();}

        //choose between two language ..
        Language.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Stop STT ..
                try {
                    stopSTT();
                    speak_button_check = false; // make speak_button_check false ..
                }catch (Exception e){e.printStackTrace();}

                // Start Stt Automatically with new lang code ..
                try
                {
                    if(secondary_language == false)
                    {
                        secondary_language = true;
                        Language.setText(lang_name_2); // Set name of the secondary language ..
                        initializeSTT(lang_code_2); //Initialize STT for secondary lang code ..
                        current_lang_code = lang_code_2; //set current lang code ..
                        checkChoosenLangTTS(current_lang_code);  //Check if choosen lang is supported by TTS ..
                    }else{
                        secondary_language = false;
                        Language.setText(lang_name); // Set name of the primary language ..
                        initializeSTT(lang_code); //Initialize STT for primary lang code ..
                        current_lang_code = lang_code; //set current lang code ..
                        checkChoosenLangTTS(current_lang_code);  //Check if choosen lang is supported by TTS ..
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Switch button Auto Stt..
        CheckAutoStt.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                autostt_speakbutton_relation = isChecked;
                if(isChecked == true){
                    //destroy STT ..
                    try{
                        mSpeechRecognizer.destroy();
                        IsSttActive=false; //Make IsSttActive false ..
                        Listen.setVisibility(View.INVISIBLE); // Hide Listen Image ..
                    }catch(Exception e){e.printStackTrace();}

                    //Initialize STT ..
                    try{
                        initializeSTT(current_lang_code);
                    }catch(Exception e){e.printStackTrace();}

                    // Get delay_millis from DB .. if DB crashed it will be 3000 always ..
                    try {
                        delay_millis = Integer.parseInt(db.getValue("auto_stt"));
                        delay_millis = delay_millis * 1000; // *1000 to convert seconds to millis ..
                    }catch(Exception e){e.printStackTrace();}

                    // Start Stt Automatically ..
                    try
                    {
                        handler.post(runnable); //Start Handler ..
                        auto_stt = true; // make auto_stt true ..
                    }catch(Exception e){e.printStackTrace();}

                }else {
                    //destroy STT ..
                    try{
                        mSpeechRecognizer.destroy();
                        IsSttActive=false; //Make IsSttActive false ..
                        Listen.setVisibility(View.INVISIBLE); // Hide Listen Image ..
                        // Cancel countdown ..
                        if(my_countdown_timer!=null)
                            my_countdown_timer.cancel();
                        auto_stt_counter.setVisibility(View.GONE);// Hide counter ..
                    }catch(Exception e){e.printStackTrace();}

                    //Initialize STT ..
                    try{
                        initializeSTT(current_lang_code);
                    }catch(Exception e){e.printStackTrace();}
                    // Stop Stt Automatically ..
                    try
                    {
                        handler.removeCallbacks(runnable); //Remove handler ..
                        auto_stt = false; // make auto_stt false ..
                    }catch(Exception e){e.printStackTrace();}
                }
            }
        });

        //Speak Button..
        SpeakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Animation ..
                try {
                    SpeakButton.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //autostt_speakbutton_relation must be false before start SpeakButton to avoid crashes ..
                if (speak_button_check == false && autostt_speakbutton_relation == false) {

                    speak_button_check = true;
                    //Stop STT ..
                    try {
                        stopSTT();
                    } catch (Exception e) {e.printStackTrace();}

                    //Initialize STT ..
                    try {
                        initializeSTT(current_lang_code);
                    } catch (Exception e) {e.printStackTrace();}

                    //Start Stt Recognizer ..
                    try {
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    } catch (Exception e) {e.printStackTrace();}
                }else {

                    //Stop STT ..
                    try {
                        stopSTT();
                        speak_button_check = false; // make speak_button_check false ..
                    } catch (Exception e) {e.printStackTrace();}

                }
            }
        });

        //up_down_action_bar ..
        up_down_action_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try {
                    up_down_action_bar.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                String size="";
                //get screen size for margin..
                try{
                    Configuration config = STT.this.getResources().getConfiguration();
                    if (config.smallestScreenWidthDp >= 720) {
                        // sw720dp code goes here
                        size = "very_big";
                    }
                    else if (config.smallestScreenWidthDp >= 600) {
                        // sw600dp code goes here
                        size = "big";
                    }
                    else {
                        // fall-back code goes here
                        size = "small";
                    }
                }catch (Exception e){e.printStackTrace();}

                //start margin and show/hide action bar ..
                try{
                    //if ActionBar hidden ..
                    if(ActionBar.getVisibility() == View.GONE){
                        up_down_action_bar.setImageResource(R.drawable.down_action_bar);
                        ActionBar.setVisibility(View.VISIBLE); // show ActionBar ..

                        //margin ..
                        try {
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inner_container.getLayoutParams();
                            Resources r = getResources();
                            float px = 0.0f;
                            if (size.equals("small"))
                                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,130,r.getDisplayMetrics());
                            else if (size.equals("big"))
                                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,160,r.getDisplayMetrics());
                            else if (size.equals("very_big"))
                                px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,190,r.getDisplayMetrics());
                            params.bottomMargin = (int) px;
                        } catch (Exception e) {e.printStackTrace();}
                    }else {
                        //if ActionBar not hidden ..
                        up_down_action_bar.setImageResource(R.drawable.up_action_bar);
                        ActionBar.setVisibility(View.GONE); // hide ActionBar ..

                        //margin ..
                        try {
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inner_container.getLayoutParams();
                            Resources r = getResources();
                            float pxs = 0.0f;
                            if (size.equals("small"))
                                pxs = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,r.getDisplayMetrics());
                            else if (size.equals("big"))
                                pxs = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,r.getDisplayMetrics());
                            else if (size.equals("very_big"))
                                pxs = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120,r.getDisplayMetrics());
                            params.bottomMargin = (int) pxs;
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //scroll_up ..
        scroll_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    scroll_up.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //scroll_up..
                try{
                    scrollToTop();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //scroll_down ..
        scroll_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    scroll_down.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //scroll_down..
                try{
                    scrollToBottom();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Zoom_In..
        Zoom_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    Zoom_In.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Zoom_In..
                try{
                    ContentText.setTextSize(TypedValue.COMPLEX_UNIT_PX,(ContentText.getTextSize()+10f));
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Zoom_Out..
        Zoom_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    Zoom_Out.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Zoom_Out..
                try{
                    ContentText.setTextSize(TypedValue.COMPLEX_UNIT_PX,(ContentText.getTextSize()-10f));
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Copy..
        Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    Copy.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                try{
                    //check if the edittext is empty or not ..
                    if(ContentText.getText().toString().matches("")) {
                        Toast.makeText(context, getString(R.string.edittext_empty), Toast.LENGTH_LONG).show();
                    }else {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple texts", ContentText.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(STT.this, getString(R.string.copied), Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Share..
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    Share.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                try{
                    //check if the edittext is empty or not ..
                    if(ContentText.getText().toString().matches("")) {
                        Toast.makeText(context, getString(R.string.edittext_empty), Toast.LENGTH_LONG).show();
                    }else {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, ContentText.getText().toString());
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share text to.."));
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Delete..
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    Delete.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Stop STT ..
                try {
                    stopSTT();
                    speak_button_check = false; // make speak_button_check false ..
                }catch (Exception e){e.printStackTrace();}

                //Alert Dialog ..
                try {
                    //check if the edittext is empty or not ..
                    if(ContentText.getText().toString().matches("")) {
                        Toast.makeText(context, getString(R.string.edittext_empty), Toast.LENGTH_LONG).show();
                    }else {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setMessage("سيتم حذف المحتوي .. هل تريد الاستمرار؟")
                                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue ..
                                        ContentText.setText("");
                                    }
                                })
                                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                    }
                }catch (Exception e){e.printStackTrace();}

                //Then Re-initialize STT ..
                try
                {
                    initializeSTT(current_lang_code);
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Exist files ..
        exist_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    exist_files.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Start Exist Files activity ..
                try{
                    Intent i=new Intent("com.hblackcat.sttpro.ExistFiles");
                    //check if empty ..
                    if(ContentText.getText().toString().matches(""))
                        i.putExtra("boolean", false);
                    else
                        i.putExtra("boolean", true);
                    startActivityForResult(i,intent_access_num_3);
                }catch(Exception e){e.printStackTrace();}

            }
        });

        //Save To text file ..
        to_text_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    to_text_file.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Create folder if not exist and create txt file ..
                try{
                    //check if the edittext is empty or not ..
                    if(ContentText.getText().toString().matches(""))
                        Toast.makeText(context, getString(R.string.edittext_empty), Toast.LENGTH_LONG).show();
                    else
                        createFolderAndFile();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //TTS ..
        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    tts.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Start TTS activity .. putExtra current font size and current content ..
                try{
                    if(lang_is_supported == true) {
                        //check if empty ..
                        if(ContentText.getText().toString().matches(""))
                            Toast.makeText(STT.this, getString(R.string.tts_text_hint), Toast.LENGTH_LONG).show();
                        else {
                            Intent i = new Intent("com.hblackcat.sttpro.TTS");
                            i.putExtra("font_size_tts", ContentText.getTextSize());
                            i.putExtra("font_code_tts", font_code);
                            i.putExtra("content_tts", ContentText.getText().toString());
                            i.putExtra("lang_code_tts", current_lang_code);
                            startActivity(i);
                        }
                    }else
                        Toast.makeText(STT.this, getString(R.string.not_supported), Toast.LENGTH_LONG).show();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Reserved words ..
        reserved_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    reserved_words.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Start ReservedWords activity ..
                try{
                    Intent i = new Intent("com.hblackcat.sttpro.ReservedWords");
                    startActivity(i);
                }catch(Exception e){e.printStackTrace();}

            }
        });

        //keyboard ..
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    keyboard.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //get current position in content to display it in same place ..
                try {
                    start_from = Math.max(ContentText.getSelectionStart(), 0);
                }catch (Exception e){e.printStackTrace();}

                //Start EditContent activity .. putExtra current font name, font size and current content .. and get content back in return ..
                try{
                    Intent i=new Intent("com.hblackcat.sttpro.EditContent");
                    i.putExtra("font_size", ContentText.getTextSize());
                    i.putExtra("content", ContentText.getText().toString());
                    i.putExtra("font_code", font_code);
                    i.putExtra("start_from", start_from);
                    startActivityForResult(i,intent_access_num_4);
                }catch(Exception e){e.printStackTrace();}

            }
        });

        //Inner Settings  ..
        inner_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    inner_settings.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Start InnerSettings activity ..
                try{
                    Intent i = new Intent("com.hblackcat.sttpro.InnerSettings");
                    startActivity(i);
                }catch(Exception e){e.printStackTrace();}

            }
        });

        //Redo  ..
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    redo.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Redo ..
                try{
                    func.redo();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //Undo  ..
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    undo.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Undo ..
                try{
                    func.undo();
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //replace  ..
        replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Animation ..
                try
                {
                    replace.startAnimation(zoom_in_and_out_fast);
                }catch(Exception e){e.printStackTrace();}

                //Start ReplaceString activity ..
                try{
                    //check if the edittext is empty or not ..
                    if(ContentText.getText().toString().matches(""))
                        Toast.makeText(context, getString(R.string.edittext_empty), Toast.LENGTH_LONG).show();
                    else {
                        Intent i = new Intent("com.hblackcat.sttpro.ReplaceString");
                        startActivityForResult(i, intent_access_num_1);
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });

        //ContentText EditText TextWatcher to get length of text ..
        ContentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //get the length of text .. here because if user delete word or add word by himself without stt ..
                try {
                    if(!s.toString().matches("")) {
                        String currentText = s.toString();
                        String[] splited = currentText.trim().split("\\s+");
                        currentLength = (splited.length);
                        words_counter.setText("" + (currentLength));
                    }else
                        words_counter.setText("0");
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //ContentText EditText OnTouchListener to check selected text..
        ContentText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (MotionEvent.ACTION_UP == event.getAction()) {
                    //make is_selected true to replace or add text in the same position the user select or press on it ..
                    is_selected = true;
                }
                return false;
            }
        });
    }

    //Initialize STT ..
    private void initializeSTT(String lang_codes)
    {
        // hide Listen Image ..
        Listen.setVisibility(View.INVISIBLE);

        try{
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang_codes);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
            SpeechRecognitionListener listener = new SpeechRecognitionListener();
            mSpeechRecognizer.setRecognitionListener(listener);
        }catch(Exception e){e.printStackTrace();}
    }

    //Runnable for automatically start STT ..
    private Runnable runnable=new Runnable(){
        @Override
        public void run() {
            try
            {
                if(IsSttActive==false)
                {
                    //Start Stt Recognizer ..
                    try {
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    } catch (Exception e) {e.printStackTrace();}
                }
            }catch(Exception e){e.printStackTrace();}

            handler.postDelayed(runnable, delay_millis);
        }
    };

    //CountDown Timer ..
    private class MyCountDownTimer extends CountDownTimer
    {

        public MyCountDownTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            auto_stt_counter.setVisibility(View.VISIBLE);
            auto_stt_counter.setText("" + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            auto_stt_counter.setVisibility(View.GONE);
        }

    }

    // SpeechRecognitionListener class ..
    protected class SpeechRecognitionListener implements RecognitionListener
    {
        @Override
        public void onBeginningOfSpeech(){}
        @Override
        public void onBufferReceived(byte[] buffer) {}
        @Override
        public void onEndOfSpeech(){
            try
            {
                IsSttActive=false;
                speak_button_check = false; // make speak_button_check false ..
                Listen.setVisibility(View.INVISIBLE); //Hide Listen Image if user stop speak ..
            }catch(Exception e){e.printStackTrace();}

            //check auto_stt ..
            try{
                if(auto_stt == true)
                {
                    handler.removeCallbacks(runnable); //Remove handler ..
                    handler.post(runnable); //Start Handler ..
                    my_countdown_timer = new MyCountDownTimer(delay_millis,1000);
                    my_countdown_timer.start();
                }
            }catch (Exception e){e.printStackTrace();}
        }
        @Override
        public void onError(int error) {

            //Smart way to check all error from Speech Recognition ..
            new Functions().getErrorText(context,error);

            try
            {
                IsSttActive=false;
                speak_button_check = false; // make speak_button_check false ..
                Listen.setVisibility(View.INVISIBLE); //Hide Listen Image if user didnot speak ..
            }catch(Exception e){e.printStackTrace();}
        }
        @Override
        public void onEvent(int eventType, Bundle params) {}
        @Override
        public void onPartialResults(Bundle partialResults) {}
        @Override
        public void onReadyForSpeech(Bundle params){
            try
            {
                IsSttActive=true;
                Listen.setVisibility(View.VISIBLE); //Show Listen Image if user Start speak ..
            }catch(Exception e){e.printStackTrace();}}
        @Override
        public void onResults(Bundle results)
        {
            try {
                //get selected text from edittext if user select it ..
                try {
                    startSelection = Math.max(ContentText.getSelectionStart(), 0);
                    endSelection = Math.max(ContentText.getSelectionEnd(), 0);
                }catch (Exception e){e.printStackTrace();}

                /////////////////////////////////////////// If User Used Reserved Words //////////////////////////////////////////
                String final_new_text = "";
                boolean reserved_words_activation = true;

                // Get reserved_words_activation from db.. if crashed it will be always true ..
                try {
                    db = new MySQLiteHelper(context);
                    reserved_words_activation = Boolean.parseBoolean(db.getValue("reserved_words"));
                }catch (Exception e){e.printStackTrace();}

                //get string from google recognition api ..
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                // check reserved_words_activation ..
                if(reserved_words_activation == true) {
                    //create array for new replaced texts to get last text and replace it and put it in new String ..
                    ArrayList<String> new_texts_arr = new ArrayList();
                    new_texts_arr.add(matches.get(0));

                    //Check if new text contain reserved words ..
                    String before[] = db.getRowsAllData("before");
                    int arr_counter = 0;
                    for (int i = 0; i < before.length; i++) {
                        if (new_texts_arr.get(arr_counter).contains(before[i])) {
                            String new_texts = new_texts_arr.get(arr_counter).replace(before[i] + "", db.getValue(before[i]) + "");
                            new_texts_arr.add(new_texts);
                            arr_counter++;
                            //make if_replaced true to check "the letter before the last letter" in next step ..
                            if_replaced = true;
                        }
                    }
                    //get last value in arrayList that contained all changes..
                    final_new_text = new_texts_arr.get(arr_counter);
                    try {
                        //check if user used reserved words to remove the space before the last letter ..
                        if (if_replaced == true) {
                            //make if_replaced false ..
                            if_replaced = false;
                            //Check if text equal or more than 3 letters ..
                            if (final_new_text.length() >= 3) {
                                //Check if the letter before the last letter is "empty space" remove it  (تفاح ة)..
                                if (final_new_text.charAt(final_new_text.length() - 2) == ' ') {
                                    char[] myNameChars = final_new_text.toCharArray();
                                    myNameChars[final_new_text.length() - 2] = '\0';
                                    final_new_text = String.valueOf(myNameChars);
                                }
                            }
                        }
                    } catch (Exception e) {e.printStackTrace();}
                }else {
                    //reserved_words_activation is false set string without edit ..
                    final_new_text = matches.get(0);
                }

                /////////////////////////////////////////// Set Colors For Text ////////////////////////////////////////////////////////
                String new_old_text = "";
                String old_text = "";
                // convert all ("\n") format to html format ("<br>") ..
                //must be here to convert every time get text from (edittext) and convert to html ..
                if(ContentText.getText().toString().contains("\n"))
                {
                    new_old_text = ContentText.getText().toString().replace("\n","<br>");
                    old_text = "<font color='#cfcdcd'>" + new_old_text + "</font>";
                }else {
                    // set color white all old text ..
                    old_text = "<font color='#cfcdcd'>" + ContentText.getText().toString() + "</font>";
                }

                // set color to all old text ..
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N)
                    ContentText.setText(Html.fromHtml(old_text));
                else
                    ContentText.setText(Html.fromHtml(old_text , Html.FROM_HTML_MODE_LEGACY));

                String new_text;
                //check if this is the first text in edittext or not .. to add extra space or not ..
                if(!ContentText.getText().toString().matches("")) {
                    //check if new text is just one letter like "ة" don't add space ..
                    if(final_new_text.length() == 1) {
                        //remove extra space ..
                        new_text = "<font color='#5be2fb'>" + final_new_text + "</font>"; // set color red to new text only ..
                    }else {
                        //put extra space ..
                        new_text = "<font color='#5be2fb'>&nbsp;" + final_new_text + "</font>"; // set color red to new text only ..
                    }
                }else {
                    //remove extra space ..
                    new_text = "<font color='#5be2fb'>" + final_new_text + "</font>"; // set color red to new text only ..
                }

                //////////////////////////// Check If User Select Text Or Not And Add Text In EditText ////////////////////////////
                //check if user select text and want to replace it ..
                if(is_selected == false) {
                    // set old text + new text with red color ..
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N)
                        ContentText.append(Html.fromHtml(new_text));
                    else
                        ContentText.append(Html.fromHtml(new_text, Html.FROM_HTML_MODE_LEGACY));
                    // Scroll Text To Bottom Very Smoothly ..
                    scrollToBottom();
                }else {
                    is_selected = false;
                    try{
                        // replace old text with new text with red color ..
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N)
                            ContentText.getText().replace(Math.min(startSelection, endSelection), Math.max(startSelection, endSelection), Html.fromHtml(new_text), 0, Html.fromHtml(new_text).length());
                        else
                            ContentText.getText().replace(Math.min(startSelection, endSelection), Math.max(startSelection, endSelection), Html.fromHtml(new_text, Html.FROM_HTML_MODE_LEGACY), 0,Html.fromHtml(new_text, Html.FROM_HTML_MODE_LEGACY).length());
                    }catch (Exception e){e.printStackTrace();}
                }
            }catch(Exception e){e.printStackTrace();}

            //SharedPreferences for Save Text  ..
            try{
                saveText();
            }catch(Exception e){e.printStackTrace();}
        }
        @Override
        public void onRmsChanged(float rmsdB){}
    }

    //Stop STT ..
    private void stopSTT()
    {
        //destroy STT ..
        try{
            mSpeechRecognizer.destroy();
        }catch(Exception e){e.printStackTrace();}

        //Stop Handler fot STT ..
        try{
            handler.removeCallbacks(runnable);
        }catch(Exception e){e.printStackTrace();}

        auto_stt = false; // make auto_stt false ..
        CheckAutoStt.setChecked(false); // Uncheck ..
        IsSttActive=false; //Make IsSttActive false ..
        Listen.setVisibility(View.INVISIBLE); // Hide Listen Image ..
    }

    //Check if choosen lang is supported by TTS ..
    private void checkChoosenLangTTS(final String lang_code_tts_check)
    {
        try{
            tts_engine=new TextToSpeech(STT.this, new TextToSpeech.OnInitListener() {

                @Override
                public void onInit(int status) {
                    // TODO Auto-generated method stub
                    if(status == TextToSpeech.SUCCESS){
                        int result=tts_engine.setLanguage(new Locale(lang_code_tts_check));
                        if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED) {
                            lang_is_supported = false;
                            tts.setImageResource(R.drawable.tts_icon_notsupported);
                        }else {
                            lang_is_supported = true;
                            tts.setImageResource(R.drawable.tts_icon);
                        }
                    }
                    //Stop TTS After end of check ..
                    try {
                        stopTTS();
                    } catch (Exception e) {e.printStackTrace();}
                }
            });
        }catch(Exception e){e.printStackTrace();}
    }

    //Stop TTS  for checkChoosenLangTTS ..
    private void stopTTS()
    {
        try{
            tts_engine.stop();
            tts_engine.shutdown();
        }catch(Exception e){e.printStackTrace();}
    }

    //Create folder if not exist and create txt file ..
    private void createFolderAndFile()
    {
        //Start SetFileName activity ..
        try{
            Intent i=new Intent("com.hblackcat.sttpro.SetFileName");
            startActivityForResult(i,intent_access_num_2);
        }catch(Exception e){e.printStackTrace();}
    }

    // Scroll Text To Top Very Smoothly ..
    private void scrollToTop()
    {
        try {
            nested_scroll.post(new Runnable() {
                public void run() {
                    ObjectAnimator.ofInt(nested_scroll, "scrollY", ContentText.getTop()).setDuration(1000).start();
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    // Scroll Text To Bottom Very Smoothly ..
    private void scrollToBottom()
    {
        try {
            nested_scroll.post(new Runnable() {
                public void run() {
                    ObjectAnimator.ofInt(nested_scroll, "scrollY", ContentText.getBottom()).setDuration(1000).start();
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    //Save Function ..
    public void saveText()
    {
        //SharedPreferences for Save Text  ..
        try{
            SharedPreferences save = getSharedPreferences("save", Activity.MODE_PRIVATE);
            SharedPreferences.Editor options_editor = save.edit();
            options_editor.putString("content_text", ContentText.getText().toString());
            options_editor.putFloat("font_size_saved", ContentText.getTextSize());
            options_editor.commit();
        }catch(Exception e){e.printStackTrace();}
    }

    // if RecognitionService not set return null or return values ..
    private ComponentName getServiceComponent() {
        String pkg = null;
        String cls = null;
        List<ResolveInfo> services = getPackageManager().queryIntentServices(new Intent(RecognitionService.SERVICE_INTERFACE), 0);
        if (services.isEmpty()) {
            return null;
        }
        ResolveInfo ri = services.iterator().next();
        pkg = ri.serviceInfo.packageName;
        cls = ri.serviceInfo.name;
        return new ComponentName(pkg, cls);
    }

    //Get data from activities in return ..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Get data from ReplaceString activity in return ..
        if (requestCode == intent_access_num_1) {
            if (resultCode == RESULT_OK) {
                try
                {
                    String old_replace = data.getStringExtra("replace_old");
                    String new_replace = data.getStringExtra("replace_new");
                    new_replace = "<font color='#5be2fb'>" + new_replace + "</font>"; // set color red to new text only ..
                    String olds = ContentText.getText().toString();
                    if(ContentText.getText().toString().contains(old_replace)) {
                        // set color to all old text ..
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                            ContentText.setText(Html.fromHtml(olds.replace(old_replace, new_replace)));
                        }else {
                            ContentText.setText(Html.fromHtml(olds.replace(old_replace, new_replace), Html.FROM_HTML_MODE_LEGACY));
                        }
                        Toast.makeText(context, getString(R.string.item_edited), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context, getString(R.string.item_not_edited), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        }

        //Get data from SetFileName activity in return ..
        if (requestCode == intent_access_num_2) {
            if (resultCode == RESULT_OK) {
                try {
                    // create folder if not exist ..
                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File (root.getAbsolutePath() + "/TextFiles");
                    if(!dir.exists()) {
                        dir.mkdirs();
                    }

                    // Create txt file with specific name ..
                    FileOutputStream fOut = new FileOutputStream(root.getAbsolutePath() + "/TextFiles/"+data.getStringExtra("file_name")+".txt");
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);

                    //Re-Build text again after removing all html elements ..
                    String final_finals = "";
                    try{

                        if(ContentText.getText().toString().contains("<br>")||ContentText.getText().toString().contains("&nbsp;"))
                            final_finals = ContentText.getText().toString().replace("<br>","\n").replace("&nbsp;"," ");
                        else
                            final_finals = ContentText.getText().toString();

                    }catch (Exception e){final_finals = ContentText.getText().toString();}

                    // Write the string to the file
                    osw.write(final_finals);

                    // ensure that everything is really written out and close ..
                    osw.flush();
                    osw.close();
                    Toast.makeText(context,getString(R.string.saved) , Toast.LENGTH_LONG).show();
                }catch (Exception e){e.printStackTrace();}
            }
        }

        //Get data from Adapter (ExistFiles) activity in return ..
        if (requestCode == intent_access_num_3) {
            if (resultCode == RESULT_OK) {
                try
                {
                    ContentText.setText(data.getStringExtra("re_use_text"));
                }catch (Exception e){e.printStackTrace();}
            }
        }

        //Get data from EditContent activity in return ..
        if (requestCode == intent_access_num_4) {
            if (resultCode == RESULT_OK) {

                //get content ..
                try
                {
                    String result = data.getStringExtra("contents");
                    String new_result="";

                    // convert all ("\n") format to html format ("<br>") ..
                    //Very important to convert here to remove extra spaces and filter the text before make selection and set new text..
                    if(result.contains("\n"))
                    {
                        new_result = result.replace("\n","<br>");
                    }else {
                        new_result = result;
                    }

                    // set html format after edit ..
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N)
                        ContentText.setText(Html.fromHtml(new_result));
                    else
                        ContentText.setText(Html.fromHtml(new_result , Html.FROM_HTML_MODE_LEGACY));
                }catch (Exception e){e.printStackTrace();}

                //get last position ..
                try
                {
                    ContentText.setSelection(data.getExtras().getInt("start_from"));
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }

    //insert into database just only one ..
    private void insertIntoDB()
    {
        try {
            db.createDataBase(); //create database IF NOT EXIST ..
            db.openDataBase(); //open database ..
            //if true insert fixed values ..
            if (db.getValue("first_time").equals("true")) {

                db.replaceValue("first_time","first_time","false"); // replace value to be not the first time ..

                String before_fixed[] = {"تحيه","فتح اقواس","غلق اقواس","تنصيص","فاصله","نقطتين","نقطه","تعجب","استفهام","همزه","تاء مربوطه","مسافه","سطر جديد","comma","colon","full stop","exclamation","question mark","space","new line","quote"};
                String after_fixed[] = {"السلام عليكم ورحمة الله وبركاته","(",")","\"","،",":",".","!","؟","أ","ة","&nbsp;","<br>",",",":",".","!","?","&nbsp;","<br>","\""};
                for(int i=0;i<before_fixed.length;i++)
                    db.insertIntoDBNames(before_fixed[i],after_fixed[i]);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //onPause ..
    @Override
    protected void onResume() {

        super.onResume();

        //Close dataBase and ReOpen it (very important in case user drop database in ReservedWords Activity) ..
        try {
            db.close();
            db.openDataBase();
        }catch (Exception e){e.printStackTrace();}

        //Re-initialize STT ..
        try
        {
            initializeSTT(current_lang_code);
        }catch (Exception e){e.printStackTrace();}

        //get count of words in text onResume ..
        try {
            if(!ContentText.getText().toString().matches("")) {
                String currentText = ContentText.getText().toString();
                String[] splited = currentText.trim().split("\\s+");
                currentLength = (splited.length);
                words_counter.setText("" + (currentLength));
            }else
                words_counter.setText("0");
        }catch (Exception e){e.printStackTrace();}

        //Check Internet Connection ..
        try{
            new Functions().checkInternetStatus(this);
        }catch (Exception e){e.printStackTrace();}

    }

    //onPause ..
    @Override
    protected void onPause() {

        super.onPause();

        //SharedPreferences for Save Text  ..
        try{
            saveText();
        }catch(Exception e){e.printStackTrace();}

        //Finish STT ..
        try {
            speak_button_check = false; // make speak_button_check false ..
            stopSTT();
        } catch (Exception e) {e.printStackTrace();}
    }

    //onDestroy ..
    @Override
    protected void onDestroy() {

        super.onDestroy();

        //Stop TTS for checkChoosenLangTTS ..
        try {
            stopTTS();
        } catch (Exception e) {e.printStackTrace();}

        //Close DB ..
        try {
            db.close();
        } catch (Exception e) {e.printStackTrace();}
    }

}