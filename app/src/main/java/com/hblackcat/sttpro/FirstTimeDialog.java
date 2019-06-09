package com.hblackcat.sttpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FirstTimeDialog extends Activity {

    private TextView welcome_text;
    private Button watch_now,watch_later;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.first_time_dialog);

        //Initialize ..
        final Typeface typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        welcome_text = findViewById(R.id.welcome_text);
        watch_now = findViewById(R.id.watch_now);
        watch_later = findViewById(R.id.watch_later);
        watch_later.setTypeface(typeFace);
        watch_now.setTypeface(typeFace);
        welcome_text.setTypeface(typeFace);

        //watch_now ..
        watch_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start Settings activity and open youtube..
                try {
                    startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=8PRapRkspTw&feature=youtu.be")),321123);
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //watch_later ..
        watch_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish ..
                try {
                    finish();
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    //after back from youtube ..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 321123) {
            finish();
        }
    }
}