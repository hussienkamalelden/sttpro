package com.hblackcat.sttpro.ReservedRecyclerView;

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
import com.hblackcat.sttpro.DataBase.MySQLiteHelper;
import com.hblackcat.sttpro.R;
import java.util.ArrayList;
import java.util.List;

public class DialogNewItem extends Activity {

    private Button confirm;
    private EditText word_before_edit, word_after_edit;
    private TextView dialog_word_before_title, dialog_word_after_title,word_before_note,word_after_note;
    private Intent intent;
    public static List<ReservedDataCatcher> reserved_catcher = new ArrayList<>(); // array of objects to received data ..
    private int position;
    private Typeface typeFace;
    private MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_new_item);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        word_before_edit = findViewById(R.id.word_before_edit);
        word_after_edit = findViewById(R.id.word_after_edit);
        dialog_word_before_title = findViewById(R.id.dialog_word_before_title);
        dialog_word_after_title = findViewById(R.id.dialog_word_after_title);
        word_before_note = findViewById(R.id.word_before_note);
        word_after_note = findViewById(R.id.word_after_note);
        confirm = findViewById(R.id.confirm);
        word_before_edit.setTypeface(typeFace);
        word_after_edit.setTypeface(typeFace);
        dialog_word_before_title.setTypeface(typeFace);
        dialog_word_after_title.setTypeface(typeFace);
        word_before_note.setTypeface(typeFace);
        word_after_note.setTypeface(typeFace);
        confirm.setTypeface(typeFace);
        db =new MySQLiteHelper(this);
        intent = getIntent();

        //if user click on edit button .. put data in dialog ..
        // hasExtra to check if intent had extra values ..
        if (intent.hasExtra("edit")) {
            position = getIntent().getIntExtra("position", 0);
            word_before_edit.setText(DialogNewItem.reserved_catcher.get(position).word_before);
            word_after_edit.setText(DialogNewItem.reserved_catcher.get(position).word_after);
        }

        //Confirm Button ..
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(word_before_edit.getText().toString().matches("") || word_after_edit.getText().toString().matches("")) {
                        Toast.makeText(DialogNewItem.this, getResources().getString(R.string.empty) + "", Toast.LENGTH_LONG).show();
                    }else {
                        //if Done for Edit ..
                        if (intent.hasExtra("edit")) {
                            // replace in DB .. (first before replace in reserved_catcher)
                            ReservedDataCatcher objs = DialogNewItem.reserved_catcher.get(position);
                            boolean if_done = db.replaceValue(objs.word_before, word_before_edit.getText().toString(), word_after_edit.getText().toString());

                            //Check if the user change word_before to name existed in DB it will return false ..
                            //because the names in DB UNIQUE so it will through Exception then return false ..
                            // Or maybe the replaceValue() was crashed so it will return false also ..
                            if (if_done == false) {
                                Toast.makeText(DialogNewItem.this, getResources().getString(R.string.words_before_error) + "", Toast.LENGTH_LONG).show();
                            } else {
                                //replace in reserved_catcher
                                ReservedDataCatcher DataObje = new ReservedDataCatcher(word_before_edit.getText().toString(), word_after_edit.getText().toString());
                                DialogNewItem.reserved_catcher.set(position, DataObje);
                                Toast.makeText(DialogNewItem.this, getResources().getString(R.string.item_edited) + "", Toast.LENGTH_LONG).show();

                                //hide keyboard before finish..
                                try{
                                    final InputMethodManager inputMethodManager = (InputMethodManager) DialogNewItem.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    if (inputMethodManager.isActive()) {
                                        if (DialogNewItem.this.getCurrentFocus() != null) {
                                            inputMethodManager.hideSoftInputFromWindow(DialogNewItem.this.getCurrentFocus().getWindowToken(), 0);
                                        }
                                    }
                                }catch (Exception e){e.printStackTrace();}

                                finish();
                            }
                        } else {
                            //if Done for First Time ..
                            //check if the word_before inserted in DB ..
                            if (db.searchInDB(word_before_edit.getText().toString()) == true) {
                                Toast.makeText(DialogNewItem.this, getResources().getString(R.string.words_before_error) + "", Toast.LENGTH_LONG).show();
                            } else {
                                ReservedDataCatcher DataObj = new ReservedDataCatcher(word_before_edit.getText().toString(), word_after_edit.getText().toString());
                                DialogNewItem.reserved_catcher.add(DataObj);

                                // add in DB ..
                                db.insertIntoDBNames(word_before_edit.getText().toString(), word_after_edit.getText().toString());
                                Toast.makeText(DialogNewItem.this, getResources().getString(R.string.item_added) + "", Toast.LENGTH_LONG).show();

                                //hide keyboard before finish..
                                try{
                                    final InputMethodManager inputMethodManager = (InputMethodManager) DialogNewItem.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    if (inputMethodManager.isActive()) {
                                        if (DialogNewItem.this.getCurrentFocus() != null) {
                                            inputMethodManager.hideSoftInputFromWindow(DialogNewItem.this.getCurrentFocus().getWindowToken(), 0);
                                        }
                                    }
                                }catch (Exception e){e.printStackTrace();}

                                finish();
                            }
                        }
                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        });
    }

    //onPause ..
    @Override
    protected void onPause() {

        super.onPause();

        //hide keyboard on finish..
        try{
            final InputMethodManager inputMethodManager = (InputMethodManager) DialogNewItem.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                if (DialogNewItem.this.getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(DialogNewItem.this.getCurrentFocus().getWindowToken(), 0);
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }
}