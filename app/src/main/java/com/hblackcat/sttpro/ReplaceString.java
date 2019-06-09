package com.hblackcat.sttpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReplaceString extends Activity {

    private EditText from_edit,to_edit;
    private Button confirmation;
    private TextView from_title,from_desc,to_title,to_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.replace_string);

        //Initialize ..
        final Typeface typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        from_edit = findViewById(R.id.from_edit);
        to_edit = findViewById(R.id.to_edit);
        confirmation = findViewById(R.id.confirmation);
        from_title = findViewById(R.id.from_title);
        from_desc = findViewById(R.id.from_desc);
        to_title = findViewById(R.id.to_title);
        to_desc = findViewById(R.id.to_desc);
        from_edit.setTypeface(typeFace);
        to_edit.setTypeface(typeFace);
        confirmation.setTypeface(typeFace);
        from_title.setTypeface(typeFace);
        from_desc.setTypeface(typeFace);
        to_title.setTypeface(typeFace);
        to_desc.setTypeface(typeFace);

        //Confirm..
        confirmation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //return data to method (onActivityResult)..
                try {
                    //check if empty ..
                    if (from_edit.getText().toString().matches("") || to_edit.getText().toString().matches(""))
                        Toast.makeText(ReplaceString.this, getString(R.string.empty), Toast.LENGTH_LONG).show();
                    else {
                        //Send replaced chars or words ..
                        Intent data = new Intent();
                        data.putExtra("replace_old",from_edit.getText().toString());
                        data.putExtra("replace_new",to_edit.getText().toString());
                        setResult(RESULT_OK,data);

                        //hide keyboard before finish..
                        try{
                            final InputMethodManager inputMethodManager = (InputMethodManager) ReplaceString.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                            if (inputMethodManager.isActive()) {
                                if (ReplaceString.this.getCurrentFocus() != null) {
                                    inputMethodManager.hideSoftInputFromWindow(ReplaceString.this.getCurrentFocus().getWindowToken(), 0);
                                }
                            }
                        }catch (Exception e){e.printStackTrace();}

                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //onPause ..
    @Override
    protected void onPause() {

        super.onPause();

        //hide keyboard on finish..
        try{
            final InputMethodManager inputMethodManager = (InputMethodManager) ReplaceString.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                if (ReplaceString.this.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(ReplaceString.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
}