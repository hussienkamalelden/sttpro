package com.hblackcat.sttpro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class EditContent extends Activity {

    private EditText edit_content_area;
    private String last_font_code;
    private ImageView done_icon;
    private String text_before_edit;
    private Typeface typeFace;
    private int start_text_from,start_from;
    private Animation zoom_in_and_out_fast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_content);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        zoom_in_and_out_fast = AnimationUtils.loadAnimation(this, R.anim.zoom_in_and_out_fast);
        Intent intents = getIntent();
        edit_content_area = findViewById(R.id.edit_content_area);
        done_icon = findViewById(R.id.done_icon);
        edit_content_area.setTypeface(typeFace);

        //get content ..
        try {
            edit_content_area.setText(intents.getExtras().getString("content"));
            text_before_edit = intents.getExtras().getString("content");
        } catch (Exception e) {e.printStackTrace();}

        //get font ..
        try {
            //Set the same font from the last activity ..
            last_font_code = intents.getExtras().getString("font_code");
            typeFace = Typeface.createFromAsset(getAssets(), last_font_code); // here for all view ..
            edit_content_area.setTypeface(typeFace);
        } catch (Exception e) {e.printStackTrace();}

        //get font size ..
        try {
            float font_size = intents.getExtras().getFloat("font_size");
            edit_content_area.setTextSize(TypedValue.COMPLEX_UNIT_PX, font_size);
        } catch (Exception e) {e.printStackTrace();}

        //get text current position ..
        try {
            start_text_from= intents.getExtras().getInt("start_from");
            edit_content_area.setSelection(start_text_from);
        } catch (Exception e) {e.printStackTrace();}

        //Confirm..
        done_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Animation ..
                try {
                    done_icon.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                //get current position in content to display it in same place ..
                try {
                    start_from = Math.max(edit_content_area.getSelectionStart(), 0);
                }catch (Exception e){e.printStackTrace();}

                //return data to method (onActivityResult)..
                try {
                    Intent data = new Intent();
                    data.putExtra("start_from", start_from);
                    data.putExtra("contents", edit_content_area.getText().toString());
                    setResult(RESULT_OK, data);

                    //hide keyboard before finish..
                    try{
                        final InputMethodManager inputMethodManager = (InputMethodManager) EditContent.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            if (EditContent.this.getCurrentFocus() != null) {
                                inputMethodManager.hideSoftInputFromWindow(EditContent.this.getCurrentFocus().getWindowToken(), 0);
                            }
                        }
                    }catch (Exception e){e.printStackTrace();}

                    finish();
                } catch (Exception e) {e.printStackTrace();}
            }
        });
    }

    //Check onBack Pressed ..
    @Override
    public void onBackPressed() {
        //check if user made any changes ..
        if (!edit_content_area.getText().toString().equals(text_before_edit)) {
            //open check dialog ..
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(EditContent.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(EditContent.this);
                }
                builder.setMessage("لم يتم التأكيد على التعديلات، هل تريد الرجوع؟")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //end activity ..
                                finish();
                            }
                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            } catch (Exception e) {e.printStackTrace();}
        } else {
            //end activity ..
            finish();
        }
    }
}