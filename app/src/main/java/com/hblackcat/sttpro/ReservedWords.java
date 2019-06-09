package com.hblackcat.sttpro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hblackcat.sttpro.DataBase.MySQLiteHelper;
import com.hblackcat.sttpro.ReservedRecyclerView.Adapter;
import com.hblackcat.sttpro.ReservedRecyclerView.DialogNewItem;
import com.hblackcat.sttpro.ReservedRecyclerView.ReservedDataCatcher;

public class ReservedWords extends Activity {

    private TextView no_reserved_words,add_word_text,reserved_words_title_text;
    private Context context =this;
    private RelativeLayout add_word,reset;
    private RecyclerView recycler_view;
    private Adapter custom_adapter;
    private Typeface typeFace;
    private MySQLiteHelper db;
    private String auto_stt = "3";
    private String tts_speed ="1.0";
    private String reserved_words ="true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserved_words);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        recycler_view = findViewById(R.id.recycler_view);
        no_reserved_words = findViewById(R.id.no_reserved_words);
        add_word_text = findViewById(R.id.add_word_text);
        add_word = findViewById(R.id.add_word);
        reserved_words_title_text = findViewById(R.id.reserved_words_title_text);
        reset = findViewById(R.id.reset);
        no_reserved_words.setTypeface(typeFace);
        add_word_text.setTypeface(typeFace);
        reserved_words_title_text.setTypeface(typeFace);
        no_reserved_words.setVisibility(View.GONE);
        saveOtherValuesFromDB(); //save other values in DB before drop ..

        try {
            //set up customAdapter Inside recyclerView ..
            custom_adapter =new Adapter(context); // send context in parameter ..
            recycler_view.setLayoutManager(new LinearLayoutManager(context));
            recycler_view.setAdapter(custom_adapter);
        }catch (Exception e){e.printStackTrace();}

        //Button to Add Row ..
        add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //open dialog ..
                    Intent i = new Intent("com.hblackcat.sttpro.ReservedRecyclerView.DialogNewItem");
                    startActivity(i);
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Reset button ..
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Alert Dialog ..
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setMessage("سيتم مسح جميع النصوص المضافة والمعدلة فى الكلمات المحجوزة والعودة للوضع الافتراضي .. هل تريد الاستمرار؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue ..
                                    db =new MySQLiteHelper(ReservedWords.this);
                                    db.dropDataBase(); // drop DataBase ..
                                    db.createDataBase(); //create database IF NOT EXIST ..
                                    db.openDataBase(); //open database ..

                                    //insert fixed values ..
                                    db.replaceValue("first_time","first_time","false"); // replace value to be not the first time ..
                                    setValuesAgainInDB(); //set old values again in DB after Drop ..

                                    String before_fixed[] = {"تحيه","فتح اقواس","غلق اقواس","تنصيص","فاصله","نقطتين","نقطه","تعجب","استفهام","همزه","تاء مربوطه","مسافه","سطر جديد","comma","colon","full stop","exclamation","question mark","space","new line","quote"};
                                    String after_fixed[] = {"السلام عليكم ورحمة الله وبركاته","(",")","\"","،",":",".","!","؟","أ","ة","&nbsp;","<br>",",",":",".","!","?","&nbsp;","<br>","\""};
                                    for(int i=0;i<before_fixed.length;i++)
                                        db.insertIntoDBNames(before_fixed[i],after_fixed[i]);

                                    try {
                                        //Rebuild and Refresh ..
                                        rebuildAndRefresh();
                                    }catch (Exception e){e.printStackTrace();}
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
    }

    //Save other values in DB before drop ..
    private void saveOtherValuesFromDB()
    {
        db =new MySQLiteHelper(ReservedWords.this);
        auto_stt = db.getValue("auto_stt");
        tts_speed = db.getValue("tts_speed");
        reserved_words = db.getValue("reserved_words");
    }

    //Set old values again in DB after Drop ..
    private void setValuesAgainInDB()
    {
        db =new MySQLiteHelper(ReservedWords.this);
        db.replaceValue("auto_stt","auto_stt",auto_stt);
        db.replaceValue("tts_speed","tts_speed",tts_speed);
        db.replaceValue("reserved_words","reserved_words",reserved_words);
    }

    //onResume ..
    @Override
    public void onResume() {
        super.onResume();
        try {
            //Rebuild and Refresh ..
            rebuildAndRefresh();
        }catch (Exception e){e.printStackTrace();}
    }

    //Rebuild and Refresh ..
    private void rebuildAndRefresh()
    {
        try {
            try{
                // clear ArrayList in case it run before .. clear better and faster than removeAll() method ..
                DialogNewItem.reserved_catcher.clear();
            }catch (Exception e){e.printStackTrace();}

            // get words before and after from DB and put them into ReservedDataCatcher ..
            // first value in db is == (first_time) so we start from 1 not 0 and make length of array -1 ..
            try{
                db =new MySQLiteHelper(this.context);
                String before[] = db.getRowsAllData("before");
                String after[] = db.getRowsAllData("after");
                for (int i =0 ; i < db.getCountOfRows()-4 ; i++) {
                    ReservedDataCatcher data_obje = new ReservedDataCatcher(before[i+4], after[i+4]);
                    DialogNewItem.reserved_catcher.add(i, data_obje);
                }
            }catch (Exception e){e.printStackTrace();}

            // refresh customAdapter every time click back or done from dialog ..
            custom_adapter.notifyDataSetChanged(); //Refresh recyclerView ..\
            //check if there data in arraylist ..
            if (DialogNewItem.reserved_catcher != null) {
                if (DialogNewItem.reserved_catcher.size() != 0)
                    no_reserved_words.setVisibility(View.GONE);
                else
                    no_reserved_words.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //onDestroy ..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            // clear ArrayList in case it run before .. clear better and faster than removeAll() method ..
            DialogNewItem.reserved_catcher.clear();
        }catch (Exception e){e.printStackTrace();}
    }
}