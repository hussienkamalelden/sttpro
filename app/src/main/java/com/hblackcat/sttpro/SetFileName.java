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

import java.io.File;

public class SetFileName extends Activity {

    private EditText set_file_name;
    private TextView set_file_name_desc;
    private Button confirmations;
    private int counter=0;
    private File root;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.set_file_name);

        //Initialize ..
        final Typeface typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf"); // here for all buttons ..
        set_file_name = findViewById(R.id.set_file_name);
        confirmations = findViewById(R.id.confirmations);
        set_file_name_desc = findViewById(R.id.set_file_name_desc);
        set_file_name.setTypeface(typeFace);
        confirmations.setTypeface(typeFace);
        set_file_name_desc.setTypeface(typeFace);

        //Confirm..
        confirmations.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //return data to method (onActivityResult)..
                try
                {
                    //check if empty ..
                    if(set_file_name.getText().toString().matches(""))
                        Toast.makeText(SetFileName.this, getString(R.string.empty_file_name), Toast.LENGTH_LONG).show();
                    else {
                        //Check if file name exist ..
                        checkFileNameExist(set_file_name.getText().toString(),counter);
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });
    }

    //Check if file name exist ..
    private void checkFileNameExist(String filename,int count)
    {
        root = android.os.Environment.getExternalStorageDirectory();
        //In the first time check just name without any numbers ..
        if (counter == 0)
            file = new File (root.getAbsolutePath() + "/TextFiles/"+filename+".txt");
        //After that check name with numbers ..
        else
            file = new File (root.getAbsolutePath() + "/TextFiles/"+filename+"("+count+").txt");
        //If not exist return name to method (onActivityResult)..
        if(!file.exists()) {
            Intent data = new Intent();
            if (counter == 0)
                //Send name without numbers ..
                data.putExtra("file_name",filename);
            else
                //Send name with numbers ..
                data.putExtra("file_name",filename+"("+count+")");
            setResult(RESULT_OK,data);

            //hide keyboard before finish..
            try{
                final InputMethodManager inputMethodManager = (InputMethodManager) SetFileName.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (SetFileName.this.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(SetFileName.this.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }catch (Exception e){e.printStackTrace();}

            finish();
        //If exist set number beside the name..
        }else {
            counter++;
            checkFileNameExist(filename,counter);
        }
    }

    //onPause ..
    @Override
    protected void onPause() {

        super.onPause();

        //hide keyboard on finish..
        try{
            final InputMethodManager inputMethodManager = (InputMethodManager) SetFileName.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                if (SetFileName.this.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(SetFileName.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
