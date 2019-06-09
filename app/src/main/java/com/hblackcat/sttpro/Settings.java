package com.hblackcat.sttpro;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Settings extends Activity {
    private TextView Title,LanTitle,LanTitle_2,FontTitle,FontTestAr,FontTestEn,ContinueText;
    private RelativeLayout Continue;
    private Spinner SpinnerLan,SpinnerLan_2,SpinnerFont;
    private String LangName,LangName_2,LangCode,LangCode_2,FontName,FontCode;
    private int LangPosition,LangPosition_2,FontPosition;
    private boolean LangLastPosition,LangLastPosition_2,FontLastPosition,first_time_ever,first_time_ad;
    private Animation zoom_in_and_out;
    private Context context = this;
    private SharedPreferences first_time_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Initialize ..
        zoom_in_and_out = AnimationUtils.loadAnimation(this, R.anim.zoom_in_and_out_fast);
        final Typeface typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        SpinnerLan = findViewById(R.id.SpinnerLan);
        SpinnerLan_2 = findViewById(R.id.SpinnerLan_2);
        SpinnerFont = findViewById(R.id.SpinnerFont);
        Title = findViewById(R.id.Title);
        LanTitle = findViewById(R.id.LanTitle);
        LanTitle_2 = findViewById(R.id.LanTitle_2);
        FontTitle = findViewById(R.id.FontTitle);
        FontTestAr = findViewById(R.id.FontTestAr);
        FontTestEn = findViewById(R.id.FontTestEn);
        ContinueText = findViewById(R.id.ContinueText);
        Continue = findViewById(R.id.Continue);
        Title.setTypeface(typeFace);
        LanTitle.setTypeface(typeFace);
        LanTitle_2.setTypeface(typeFace);
        FontTitle.setTypeface(typeFace);
        ContinueText.setTypeface(typeFace);

        //get from SharedPreferences ..
        try {
            first_time_pref = getSharedPreferences("intro", Activity.MODE_PRIVATE);
            first_time_ever = first_time_pref.getBoolean("first", false);
        }catch (Exception e){first_time_ever = true;}

        //First time intro ..
        try {
            //check if the edittext is empty or not ..
            if(first_time_ever == false) {
                //Save in SharedPreferences ..
                first_time_ad = true;
                first_time_ever = true;
                first_time_pref = getSharedPreferences("intro", Activity.MODE_PRIVATE);
                SharedPreferences.Editor first_time_editor = first_time_pref.edit();
                first_time_editor.putBoolean("first", first_time_ever);
                first_time_editor.commit();

                Intent i=new Intent("com.hblackcat.sttpro.FirstTimeDialog");
                startActivityForResult(i,123123);
            }else {
                //Check Permissions for 6.0 marshmallow ..
                checkPermissions();
            }
        }catch (Exception e){checkPermissions();}

        //Get saved data ..
        try
        {
            SharedPreferences saveData = getSharedPreferences("save_data", Activity.MODE_PRIVATE);
            LangPosition = (saveData.getInt("lang_position", 0 ));
            LangPosition_2 = (saveData.getInt("lang_position_2", 0 ));
            FontPosition = (saveData.getInt("font_position",0));
            LangLastPosition = (saveData.getBoolean("lang_last_position",false));
            LangLastPosition_2 = (saveData.getBoolean("lang_last_position_2",false));
            FontLastPosition = (saveData.getBoolean("font_last_position",false));
        }catch(Exception e){e.printStackTrace();}

        //Continue Button ..
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start Activity and send data ..
                try {
                    Intent i = new Intent("com.hblackcat.sttpro.STT");
                    i.putExtra("lang_name", LangName);
                    i.putExtra("lang_code", LangCode);
                    i.putExtra("lang_name_2", LangName_2);
                    i.putExtra("lang_code_2", LangCode_2);
                    i.putExtra("font_code", FontCode);
                    startActivity(i);
                } catch (Exception e) {e.printStackTrace();}
            }
        });

        //Spinner For Languages ..................................................
        // Spinner Drop down elements
        List<String> categoriesLan = new ArrayList<String>();
        categoriesLan.add(getResources().getString(R.string.afrikaans_south_africa_name));
        categoriesLan.add(getResources().getString(R.string.amharic_ethiopia_name));
        categoriesLan.add(getResources().getString(R.string.armenian_armenia_name));
        categoriesLan.add(getResources().getString(R.string.azerbaijani_azerbaijan_name));
        categoriesLan.add(getResources().getString(R.string.indonesian_indonesia_name));
        categoriesLan.add(getResources().getString(R.string.malay_malaysia_name));
        categoriesLan.add(getResources().getString(R.string.bengali_bangladesh_name));
        categoriesLan.add(getResources().getString(R.string.bengali_india_name));
        categoriesLan.add(getResources().getString(R.string.catalan_spain_name));
        categoriesLan.add(getResources().getString(R.string.czech_czech_republic_name));
        categoriesLan.add(getResources().getString(R.string.danish_denmark_name));
        categoriesLan.add(getResources().getString(R.string.german_germany_name));
        categoriesLan.add(getResources().getString(R.string.english_australia_name));
        categoriesLan.add(getResources().getString(R.string.english_canada_name));
        categoriesLan.add(getResources().getString(R.string.english_ghana_name));
        categoriesLan.add(getResources().getString(R.string.english_united_kingdom_name));
        categoriesLan.add(getResources().getString(R.string.english_india_name));
        categoriesLan.add(getResources().getString(R.string.english_ireland_name));
        categoriesLan.add(getResources().getString(R.string.english_kenya_name));
        categoriesLan.add(getResources().getString(R.string.english_new_zealand_name));
        categoriesLan.add(getResources().getString(R.string.english_nigeria_name));
        categoriesLan.add(getResources().getString(R.string.english_philippines_name));
        categoriesLan.add(getResources().getString(R.string.english_singapore_name));
        categoriesLan.add(getResources().getString(R.string.english_south_africa_name));
        categoriesLan.add(getResources().getString(R.string.english_tanzania_name));
        categoriesLan.add(getResources().getString(R.string.english_united_states_name));
        categoriesLan.add(getResources().getString(R.string.spanish_argentina_name));
        categoriesLan.add(getResources().getString(R.string.spanish_bolivia_name));
        categoriesLan.add(getResources().getString(R.string.spanish_chile_name));
        categoriesLan.add(getResources().getString(R.string.spanish_colombia_name));
        categoriesLan.add(getResources().getString(R.string.spanish_costa_rica_name));
        categoriesLan.add(getResources().getString(R.string.spanish_ecuador_name));
        categoriesLan.add(getResources().getString(R.string.spanish_el_salvador_name));
        categoriesLan.add(getResources().getString(R.string.spanish_spain_name));
        categoriesLan.add(getResources().getString(R.string.spanish_united_states_name));
        categoriesLan.add(getResources().getString(R.string.spanish_guatemala_name));
        categoriesLan.add(getResources().getString(R.string.spanish_honduras_name));
        categoriesLan.add(getResources().getString(R.string.spanish_mexico_name));
        categoriesLan.add(getResources().getString(R.string.spanish_nicaragua_name));
        categoriesLan.add(getResources().getString(R.string.spanish_panama_name));
        categoriesLan.add(getResources().getString(R.string.spanish_paraguay_name));
        categoriesLan.add(getResources().getString(R.string.spanish_peru_name));
        categoriesLan.add(getResources().getString(R.string.spanish_puerto_rico_name));
        categoriesLan.add(getResources().getString(R.string.spanish_dominican_republic_name));
        categoriesLan.add(getResources().getString(R.string.spanish_uruguay_name));
        categoriesLan.add(getResources().getString(R.string.spanish_venezuela_name));
        categoriesLan.add(getResources().getString(R.string.basque_spain_name));
        categoriesLan.add(getResources().getString(R.string.filipino_philippines_name));
        categoriesLan.add(getResources().getString(R.string.french_canada_name));
        categoriesLan.add(getResources().getString(R.string.french_france_name));
        categoriesLan.add(getResources().getString(R.string.galician_spain_name));
        categoriesLan.add(getResources().getString(R.string.georgian_georgia_name));
        categoriesLan.add(getResources().getString(R.string.gujarati_india_name));
        categoriesLan.add(getResources().getString(R.string.croatian_croatia_name));
        categoriesLan.add(getResources().getString(R.string.zulu_south_africa_name));
        categoriesLan.add(getResources().getString(R.string.icelandic_iceland_name));
        categoriesLan.add(getResources().getString(R.string.italian_italy_name));
        categoriesLan.add(getResources().getString(R.string.javanese_indonesia_name));
        categoriesLan.add(getResources().getString(R.string.kannada_india_name));
        categoriesLan.add(getResources().getString(R.string.khmer_cambodia_name));
        categoriesLan.add(getResources().getString(R.string.lao_laos_name));
        categoriesLan.add(getResources().getString(R.string.latvian_latvia_name));
        categoriesLan.add(getResources().getString(R.string.lithuanian_lithuania_name));
        categoriesLan.add(getResources().getString(R.string.hungarian_hungary_name));
        categoriesLan.add(getResources().getString(R.string.malayalam_india_name));
        categoriesLan.add(getResources().getString(R.string.marathi_india_name));
        categoriesLan.add(getResources().getString(R.string.dutch_netherlands_name));
        categoriesLan.add(getResources().getString(R.string.nepali_nepal_name));
        categoriesLan.add(getResources().getString(R.string.norwegian_bokmål_norway_name));
        categoriesLan.add(getResources().getString(R.string.polish_poland_name));
        categoriesLan.add(getResources().getString(R.string.portuguese_brazil_name));
        categoriesLan.add(getResources().getString(R.string.portuguese_portugal_name));
        categoriesLan.add(getResources().getString(R.string.romanian_romania_name));
        categoriesLan.add(getResources().getString(R.string.sinhala_sri_lanka_name));
        categoriesLan.add(getResources().getString(R.string.slovak_slovakia_name));
        categoriesLan.add(getResources().getString(R.string.slovenian_slovenia_name));
        categoriesLan.add(getResources().getString(R.string.sundanese_indonesia_name));
        categoriesLan.add(getResources().getString(R.string.swahili_tanzania_name));
        categoriesLan.add(getResources().getString(R.string.swahili_kenya_name));
        categoriesLan.add(getResources().getString(R.string.finnish_finland_name));
        categoriesLan.add(getResources().getString(R.string.swedish_sweden_name));
        categoriesLan.add(getResources().getString(R.string.tamil_india_name));
        categoriesLan.add(getResources().getString(R.string.tamil_singapore_name));
        categoriesLan.add(getResources().getString(R.string.tamil_sri_lanka_name));
        categoriesLan.add(getResources().getString(R.string.tamil_malaysia_name));
        categoriesLan.add(getResources().getString(R.string.telugu_india_name));
        categoriesLan.add(getResources().getString(R.string.vietnamese_vietnam_name));
        categoriesLan.add(getResources().getString(R.string.turkish_turkey_name));
        categoriesLan.add(getResources().getString(R.string.urdu_pakistan_name));
        categoriesLan.add(getResources().getString(R.string.urdu_india_name));
        categoriesLan.add(getResources().getString(R.string.greek_greece_name));
        categoriesLan.add(getResources().getString(R.string.bulgarian_bulgaria_name));
        categoriesLan.add(getResources().getString(R.string.russian_russia_name));
        categoriesLan.add(getResources().getString(R.string.serbian_serbia_name));
        categoriesLan.add(getResources().getString(R.string.ukrainian_ukraine_name));
        categoriesLan.add(getResources().getString(R.string.hebrew_palestine_name));
        categoriesLan.add(getResources().getString(R.string.arabic_jordan_name));
        categoriesLan.add(getResources().getString(R.string.arabic_united_arab_emirates_name));
        categoriesLan.add(getResources().getString(R.string.arabic_bahrain_name));
        categoriesLan.add(getResources().getString(R.string.arabic_algeria_name));
        categoriesLan.add(getResources().getString(R.string.arabic_saudi_arabia_name));
        categoriesLan.add(getResources().getString(R.string.arabic_iraq_name));
        categoriesLan.add(getResources().getString(R.string.arabic_kuwait_name));
        categoriesLan.add(getResources().getString(R.string.arabic_morocco_name));
        categoriesLan.add(getResources().getString(R.string.arabic_tunisia_name));
        categoriesLan.add(getResources().getString(R.string.arabic_oman_name));
        categoriesLan.add(getResources().getString(R.string.arabic_palestine_name));
        categoriesLan.add(getResources().getString(R.string.arabic_qatar_name));
        categoriesLan.add(getResources().getString(R.string.arabic_lebanon_name));
        categoriesLan.add(getResources().getString(R.string.arabic_egypt_name));
        categoriesLan.add(getResources().getString(R.string.persian_iran_name));
        categoriesLan.add(getResources().getString(R.string.hindi_india_name));
        categoriesLan.add(getResources().getString(R.string.thai_thailand_name));
        categoriesLan.add(getResources().getString(R.string.korean_south_korea_name));
        categoriesLan.add(getResources().getString(R.string.chinese_mandarin_traditional_taiwan_name));
        categoriesLan.add(getResources().getString(R.string.chinese_cantonese_traditional_hong_kong_name));
        categoriesLan.add(getResources().getString(R.string.japanese_japan_name));
        categoriesLan.add(getResources().getString(R.string.chinese_mandarin_simplified_hong_kong_name));
        categoriesLan.add(getResources().getString(R.string.chinese_mandarin_simplified_china_name));

        // Sort the values alphabetically ..
        Collections.sort(categoriesLan);

        ////////////////////////////////// choose primary language //////////////////////////////////
        // Creating adapter for spinner 1 ..
        ArrayAdapter<String> dataAdapterLang = new ArrayAdapter<String>(this, R.layout.selected_item, categoriesLan);
        // Drop down layout style - list view with radio button ..
        dataAdapterLang.setDropDownViewResource(R.layout.drop_down_items);
        // attaching data adapter to spinner 1 ..
        SpinnerLan.setAdapter(dataAdapterLang);
        SpinnerLan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                if(LangLastPosition==true)
                {
                    LangName = parent.getItemAtPosition(LangPosition).toString();
                    SpinnerLan.setSelection(LangPosition); // to show position in spinner ..
                    LangLastPosition = false;
                }else
                {
                    LangName = parent.getItemAtPosition(position).toString();

                    // To save position onPause ..
                    LangPosition = position;
                }


                //get lang code ..
                LangCode = new Functions().getLangCode(LangName,context);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ////////////////////////////////// choose secondary language //////////////////////////////////
        // Creating adapter for spinner 2 ..
        ArrayAdapter<String> dataAdapterLang_2 = new ArrayAdapter<String>(this, R.layout.selected_item, categoriesLan);
        // Drop down layout style - list view with radio button ..
        dataAdapterLang_2.setDropDownViewResource(R.layout.drop_down_items);
        // attaching data adapter to spinner 2 ..
        SpinnerLan_2.setAdapter(dataAdapterLang_2);
        SpinnerLan_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // On selecting a spinner item
                if(LangLastPosition_2==true)
                {
                    LangName_2 = parent.getItemAtPosition(LangPosition_2).toString();
                    SpinnerLan_2.setSelection(LangPosition_2); // to show position in spinner ..
                    LangLastPosition_2 = false;
                }else
                {
                    LangName_2 = parent.getItemAtPosition(position).toString();

                    // To save position onPause ..
                    LangPosition_2 = position;
                }


                //get lang code ..
                LangCode_2 = new Functions().getLangCode(LangName_2,context);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Spinner For Fonts ..................................................
        // Spinner Drop down elements
        List<String> categoriesFont = new ArrayList<String>();
        categoriesFont.add(getResources().getString(R.string.roboto_name));
        categoriesFont.add(getResources().getString(R.string.noto_sans_name));
        categoriesFont.add(getResources().getString(R.string.open_sans_name));
        categoriesFont.add(getResources().getString(R.string.playfair_display_name));
        categoriesFont.add(getResources().getString(R.string.montserrat_name));
        categoriesFont.add(getResources().getString(R.string.hamah_name));
        categoriesFont.add(getResources().getString(R.string.gess_name));
        categoriesFont.add(getResources().getString(R.string.hayah_name));
        categoriesFont.add(getResources().getString(R.string.hacen_name));
        categoriesFont.add(getResources().getString(R.string.alhor_name));
        categoriesFont.add(getResources().getString(R.string.tradi_name));
        categoriesFont.add(getResources().getString(R.string.arial_name));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterFont = new ArrayAdapter<String>(this, R.layout.selected_item, categoriesFont);
        // Drop down layout style - list view with radio button
        dataAdapterFont.setDropDownViewResource(R.layout.drop_down_items);
        // attaching data adapter to spinner
        SpinnerFont.setAdapter(dataAdapterFont);
        SpinnerFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(FontLastPosition==true)
                {
                    FontName = parent.getItemAtPosition(FontPosition).toString();
                    SpinnerFont.setSelection(FontPosition); // to show position in spinner ..
                    FontLastPosition = false;
                }else{
                    FontName = parent.getItemAtPosition(position).toString();

                    // To save position onPause ..
                    FontPosition = position;
                }

                //get Font code ..
                FontCode = new Functions().getFontCode(FontName,context);

                try {
                    Typeface typeFaces = Typeface.createFromAsset(getAssets(), FontCode);
                    FontTestAr.setTypeface(typeFaces);
                    FontTestEn.setTypeface(typeFaces);

                    //Animation ..
                    try {
                        FontTestAr.startAnimation(zoom_in_and_out);
                        FontTestEn.startAnimation(zoom_in_and_out);
                    } catch (Exception e) {e.printStackTrace();}
                } catch (Exception e) {e.printStackTrace();}

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    //Save function for chosen language and font positions ..
    public void saveLangAndFont()
    {
        //SharedPreferences for Save Text  ..
        try{
            SharedPreferences saveData = getSharedPreferences("save_data", Activity.MODE_PRIVATE);
            SharedPreferences.Editor optionsEditor = saveData.edit();
            optionsEditor.putInt("lang_position", LangPosition);
            optionsEditor.putInt("lang_position_2", LangPosition_2);
            optionsEditor.putInt("font_position", FontPosition);
            optionsEditor.putBoolean("lang_last_position", true); // true to get data when open app after pause only..
            optionsEditor.putBoolean("lang_last_position_2", true); // true to get data when open app after pause only..
            optionsEditor.putBoolean("font_last_position", true); // true to get data when open app after pause only..
            optionsEditor.commit();
        }catch(Exception e){e.printStackTrace();}
    }

    //Check Permissions for 6.0 marshmallow ..
    private void checkPermissions()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    //Back From permission Dialod for 6.0 marshmallow ..
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        //back from Permissions
        if(requestCode == 1) {
            //RECORD_AUDIO ..
            if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                Toast.makeText(Settings.this,getResources().getString(R.string.error_1), Toast.LENGTH_LONG).show();
            }
            //WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE ..
            if (!(grantResults[1] == PackageManager.PERMISSION_GRANTED) && !(grantResults[2] == PackageManager.PERMISSION_GRANTED))
            {
                Toast.makeText(Settings.this,getResources().getString(R.string.error_2), Toast.LENGTH_LONG).show();
            }
        }
    }

    //after back from activity FirstTimeDialog ..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if(requestCode == 123123)
                checkPermissions();
        }catch (Exception e){checkPermissions();}
    }


    //onPause ..
    @Override
    protected void onPause() {

        super.onPause();

        //Save function for chosen language and font position..
        try {
            saveLangAndFont();
        } catch (Exception e) {e.printStackTrace();}
    }
}