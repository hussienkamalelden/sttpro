package com.hblackcat.sttpro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Functions {

    private String LangCode;
    private String FontCode;
    private ConnectivityManager connMgr;
    private NetworkInfo wifi;
    private NetworkInfo mobile;

    //Smart way to check all error from Speech Recognition ..
    public void getErrorText(Context context , int errorCode) {
        switch (errorCode) {
            case 3:
                //message = "Audio recording error";
                break;
            case 5:
                //message = "Client side error";
                break;
            case 9:
                //message = "Insufficient permissions";
                break;
            case 2:
                //message = "Network error";
                break;
            case 1:
                //message = "Network timeout";
                break;
            case 7:
                //message = "No match";
                break;
            case 8:
                //message = "RecognitionService busy";
                break;
            case 4:
                Toast.makeText(context,context.getString(R.string.internet) + "" , Toast.LENGTH_LONG).show();
                //message = "error from server";
                break;
            case 6:
                //message = "No speech input";
                break;
            default:
                //message = "Didn't understand, please try again.";
                break;
        }
    }
    
    //get code of lang ..
    public String getLangCode(String LangName , Context context)
    {
        LangCode = context.getResources().getString(R.string.afrikaans_south_africa_code);
        try {
            if (LangName.equals(context.getResources().getString(R.string.afrikaans_south_africa_name)))
                LangCode = context.getResources().getString(R.string.afrikaans_south_africa_code);
            else if (LangName.equals(context.getResources().getString(R.string.amharic_ethiopia_name)))
                LangCode = context.getResources().getString(R.string.amharic_ethiopia_code);
            else if (LangName.equals(context.getResources().getString(R.string.armenian_armenia_name)))
                LangCode = context.getResources().getString(R.string.armenian_armenia_code);
            else if (LangName.equals(context.getResources().getString(R.string.azerbaijani_azerbaijan_name)))
                LangCode = context.getResources().getString(R.string.azerbaijani_azerbaijan_code);
            else if (LangName.equals(context.getResources().getString(R.string.indonesian_indonesia_name)))
                LangCode = context.getResources().getString(R.string.indonesian_indonesia_code);
            else if (LangName.equals(context.getResources().getString(R.string.malay_malaysia_name)))
                LangCode = context.getResources().getString(R.string.malay_malaysia_code);
            else if (LangName.equals(context.getResources().getString(R.string.bengali_bangladesh_name)))
                LangCode = context.getResources().getString(R.string.bengali_bangladesh_code);
            else if (LangName.equals(context.getResources().getString(R.string.bengali_india_name)))
                LangCode = context.getResources().getString(R.string.bengali_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.catalan_spain_name)))
                LangCode = context.getResources().getString(R.string.catalan_spain_code);
            else if (LangName.equals(context.getResources().getString(R.string.czech_czech_republic_name)))
                LangCode = context.getResources().getString(R.string.czech_czech_republic_code);
            else if (LangName.equals(context.getResources().getString(R.string.danish_denmark_name)))
                LangCode = context.getResources().getString(R.string.danish_denmark_code);
            else if (LangName.equals(context.getResources().getString(R.string.german_germany_name)))
                LangCode = context.getResources().getString(R.string.german_germany_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_australia_name)))
                LangCode = context.getResources().getString(R.string.english_australia_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_canada_name)))
                LangCode = context.getResources().getString(R.string.english_canada_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_ghana_name)))
                LangCode = context.getResources().getString(R.string.english_ghana_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_united_kingdom_name)))
                LangCode = context.getResources().getString(R.string.english_united_kingdom_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_india_name)))
                LangCode = context.getResources().getString(R.string.english_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_ireland_name)))
                LangCode = context.getResources().getString(R.string.english_ireland_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_kenya_name)))
                LangCode = context.getResources().getString(R.string.english_kenya_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_new_zealand_name)))
                LangCode = context.getResources().getString(R.string.english_new_zealand_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_nigeria_name)))
                LangCode = context.getResources().getString(R.string.english_nigeria_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_philippines_name)))
                LangCode = context.getResources().getString(R.string.english_philippines_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_singapore_name)))
                LangCode = context.getResources().getString(R.string.english_singapore_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_south_africa_name)))
                LangCode = context.getResources().getString(R.string.english_south_africa_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_tanzania_name)))
                LangCode = context.getResources().getString(R.string.english_tanzania_code);
            else if (LangName.equals(context.getResources().getString(R.string.english_united_states_name)))
                LangCode = context.getResources().getString(R.string.english_united_states_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_argentina_name)))
                LangCode = context.getResources().getString(R.string.spanish_argentina_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_bolivia_name)))
                LangCode = context.getResources().getString(R.string.spanish_bolivia_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_chile_name)))
                LangCode = context.getResources().getString(R.string.spanish_chile_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_colombia_name)))
                LangCode = context.getResources().getString(R.string.spanish_colombia_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_costa_rica_name)))
                LangCode = context.getResources().getString(R.string.spanish_costa_rica_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_ecuador_name)))
                LangCode = context.getResources().getString(R.string.spanish_ecuador_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_el_salvador_name)))
                LangCode = context.getResources().getString(R.string.spanish_el_salvador_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_spain_name)))
                LangCode = context.getResources().getString(R.string.spanish_spain_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_united_states_name)))
                LangCode = context.getResources().getString(R.string.spanish_united_states_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_guatemala_name)))
                LangCode = context.getResources().getString(R.string.spanish_guatemala_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_honduras_name)))
                LangCode = context.getResources().getString(R.string.spanish_honduras_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_mexico_name)))
                LangCode = context.getResources().getString(R.string.spanish_mexico_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_nicaragua_name)))
                LangCode = context.getResources().getString(R.string.spanish_nicaragua_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_panama_name)))
                LangCode = context.getResources().getString(R.string.spanish_panama_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_paraguay_name)))
                LangCode = context.getResources().getString(R.string.spanish_paraguay_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_peru_name)))
                LangCode = context.getResources().getString(R.string.spanish_peru_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_puerto_rico_name)))
                LangCode = context.getResources().getString(R.string.spanish_puerto_rico_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_dominican_republic_name)))
                LangCode = context.getResources().getString(R.string.spanish_dominican_republic_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_uruguay_name)))
                LangCode = context.getResources().getString(R.string.spanish_uruguay_code);
            else if (LangName.equals(context.getResources().getString(R.string.spanish_venezuela_name)))
                LangCode = context.getResources().getString(R.string.spanish_venezuela_code);
            else if (LangName.equals(context.getResources().getString(R.string.basque_spain_name)))
                LangCode = context.getResources().getString(R.string.basque_spain_code);
            else if (LangName.equals(context.getResources().getString(R.string.filipino_philippines_name)))
                LangCode = context.getResources().getString(R.string.filipino_philippines_code);
            else if (LangName.equals(context.getResources().getString(R.string.french_canada_name)))
                LangCode = context.getResources().getString(R.string.french_canada_code);
            else if (LangName.equals(context.getResources().getString(R.string.french_france_name)))
                LangCode = context.getResources().getString(R.string.french_france_code);
            else if (LangName.equals(context.getResources().getString(R.string.galician_spain_name)))
                LangCode = context.getResources().getString(R.string.galician_spain_code);
            else if (LangName.equals(context.getResources().getString(R.string.georgian_georgia_name)))
                LangCode = context.getResources().getString(R.string.georgian_georgia_code);
            else if (LangName.equals(context.getResources().getString(R.string.gujarati_india_name)))
                LangCode = context.getResources().getString(R.string.gujarati_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.croatian_croatia_name)))
                LangCode = context.getResources().getString(R.string.croatian_croatia_code);
            else if (LangName.equals(context.getResources().getString(R.string.zulu_south_africa_name)))
                LangCode = context.getResources().getString(R.string.zulu_south_africa_code);
            else if (LangName.equals(context.getResources().getString(R.string.icelandic_iceland_name)))
                LangCode = context.getResources().getString(R.string.icelandic_iceland_code);
            else if (LangName.equals(context.getResources().getString(R.string.italian_italy_name)))
                LangCode = context.getResources().getString(R.string.italian_italy_code);
            else if (LangName.equals(context.getResources().getString(R.string.javanese_indonesia_name)))
                LangCode = context.getResources().getString(R.string.javanese_indonesia_code);
            else if (LangName.equals(context.getResources().getString(R.string.kannada_india_name)))
                LangCode = context.getResources().getString(R.string.kannada_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.khmer_cambodia_name)))
                LangCode = context.getResources().getString(R.string.khmer_cambodia_code);
            else if (LangName.equals(context.getResources().getString(R.string.lao_laos_name)))
                LangCode = context.getResources().getString(R.string.lao_laos_code);
            else if (LangName.equals(context.getResources().getString(R.string.latvian_latvia_name)))
                LangCode = context.getResources().getString(R.string.latvian_latvia_code);
            else if (LangName.equals(context.getResources().getString(R.string.lithuanian_lithuania_name)))
                LangCode = context.getResources().getString(R.string.lithuanian_lithuania_code);
            else if (LangName.equals(context.getResources().getString(R.string.hungarian_hungary_name)))
                LangCode = context.getResources().getString(R.string.hungarian_hungary_code);
            else if (LangName.equals(context.getResources().getString(R.string.malayalam_india_name)))
                LangCode = context.getResources().getString(R.string.malayalam_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.marathi_india_name)))
                LangCode = context.getResources().getString(R.string.marathi_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.dutch_netherlands_name)))
                LangCode = context.getResources().getString(R.string.dutch_netherlands_code);
            else if (LangName.equals(context.getResources().getString(R.string.nepali_nepal_name)))
                LangCode = context.getResources().getString(R.string.nepali_nepal_code);
            else if (LangName.equals(context.getResources().getString(R.string.norwegian_bokmål_norway_name)))
                LangCode = context.getResources().getString(R.string.norwegian_bokmål_norway_code);
            else if (LangName.equals(context.getResources().getString(R.string.polish_poland_name)))
                LangCode = context.getResources().getString(R.string.polish_poland_code);
            else if (LangName.equals(context.getResources().getString(R.string.portuguese_brazil_name)))
                LangCode = context.getResources().getString(R.string.portuguese_brazil_code);
            else if (LangName.equals(context.getResources().getString(R.string.portuguese_portugal_name)))
                LangCode = context.getResources().getString(R.string.portuguese_portugal_code);
            else if (LangName.equals(context.getResources().getString(R.string.romanian_romania_name)))
                LangCode = context.getResources().getString(R.string.romanian_romania_code);
            else if (LangName.equals(context.getResources().getString(R.string.sinhala_sri_lanka_name)))
                LangCode = context.getResources().getString(R.string.sinhala_sri_lanka_code);
            else if (LangName.equals(context.getResources().getString(R.string.slovak_slovakia_name)))
                LangCode = context.getResources().getString(R.string.slovak_slovakia_code);
            else if (LangName.equals(context.getResources().getString(R.string.slovenian_slovenia_name)))
                LangCode = context.getResources().getString(R.string.slovenian_slovenia_code);
            else if (LangName.equals(context.getResources().getString(R.string.sundanese_indonesia_name)))
                LangCode = context.getResources().getString(R.string.sundanese_indonesia_code);
            else if (LangName.equals(context.getResources().getString(R.string.swahili_tanzania_name)))
                LangCode = context.getResources().getString(R.string.swahili_tanzania_code);
            else if (LangName.equals(context.getResources().getString(R.string.swahili_kenya_name)))
                LangCode = context.getResources().getString(R.string.swahili_kenya_code);
            else if (LangName.equals(context.getResources().getString(R.string.finnish_finland_name)))
                LangCode = context.getResources().getString(R.string.finnish_finland_code);
            else if (LangName.equals(context.getResources().getString(R.string.swedish_sweden_name)))
                LangCode = context.getResources().getString(R.string.swedish_sweden_code);
            else if (LangName.equals(context.getResources().getString(R.string.tamil_india_name)))
                LangCode = context.getResources().getString(R.string.tamil_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.tamil_singapore_name)))
                LangCode = context.getResources().getString(R.string.tamil_singapore_code);
            else if (LangName.equals(context.getResources().getString(R.string.tamil_sri_lanka_name)))
                LangCode = context.getResources().getString(R.string.tamil_sri_lanka_code);
            else if (LangName.equals(context.getResources().getString(R.string.tamil_malaysia_name)))
                LangCode = context.getResources().getString(R.string.tamil_malaysia_code);
            else if (LangName.equals(context.getResources().getString(R.string.telugu_india_name)))
                LangCode = context.getResources().getString(R.string.telugu_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.vietnamese_vietnam_name)))
                LangCode = context.getResources().getString(R.string.vietnamese_vietnam_code);
            else if (LangName.equals(context.getResources().getString(R.string.turkish_turkey_name)))
                LangCode = context.getResources().getString(R.string.turkish_turkey_code);
            else if (LangName.equals(context.getResources().getString(R.string.urdu_pakistan_name)))
                LangCode = context.getResources().getString(R.string.urdu_pakistan_code);
            else if (LangName.equals(context.getResources().getString(R.string.urdu_india_name)))
                LangCode = context.getResources().getString(R.string.urdu_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.greek_greece_name)))
                LangCode = context.getResources().getString(R.string.greek_greece_code);
            else if (LangName.equals(context.getResources().getString(R.string.bulgarian_bulgaria_name)))
                LangCode = context.getResources().getString(R.string.bulgarian_bulgaria_code);
            else if (LangName.equals(context.getResources().getString(R.string.russian_russia_name)))
                LangCode = context.getResources().getString(R.string.russian_russia_code);
            else if (LangName.equals(context.getResources().getString(R.string.serbian_serbia_name)))
                LangCode = context.getResources().getString(R.string.serbian_serbia_code);
            else if (LangName.equals(context.getResources().getString(R.string.ukrainian_ukraine_name)))
                LangCode = context.getResources().getString(R.string.ukrainian_ukraine_code);
            else if (LangName.equals(context.getResources().getString(R.string.hebrew_palestine_name)))
                LangCode = context.getResources().getString(R.string.hebrew_palestine_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_jordan_name)))
                LangCode = context.getResources().getString(R.string.arabic_jordan_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_united_arab_emirates_name)))
                LangCode = context.getResources().getString(R.string.arabic_united_arab_emirates_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_bahrain_name)))
                LangCode = context.getResources().getString(R.string.arabic_bahrain_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_algeria_name)))
                LangCode = context.getResources().getString(R.string.arabic_algeria_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_saudi_arabia_name)))
                LangCode = context.getResources().getString(R.string.arabic_saudi_arabia_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_iraq_name)))
                LangCode = context.getResources().getString(R.string.arabic_iraq_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_kuwait_name)))
                LangCode = context.getResources().getString(R.string.arabic_kuwait_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_morocco_name)))
                LangCode = context.getResources().getString(R.string.arabic_morocco_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_tunisia_name)))
                LangCode = context.getResources().getString(R.string.arabic_tunisia_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_oman_name)))
                LangCode = context.getResources().getString(R.string.arabic_oman_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_palestine_name)))
                LangCode = context.getResources().getString(R.string.arabic_palestine_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_qatar_name)))
                LangCode = context.getResources().getString(R.string.arabic_qatar_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_lebanon_name)))
                LangCode = context.getResources().getString(R.string.arabic_lebanon_code);
            else if (LangName.equals(context.getResources().getString(R.string.arabic_egypt_name)))
                LangCode = context.getResources().getString(R.string.arabic_egypt_code);
            else if (LangName.equals(context.getResources().getString(R.string.persian_iran_name)))
                LangCode = context.getResources().getString(R.string.persian_iran_code);
            else if (LangName.equals(context.getResources().getString(R.string.hindi_india_name)))
                LangCode = context.getResources().getString(R.string.hindi_india_code);
            else if (LangName.equals(context.getResources().getString(R.string.thai_thailand_name)))
                LangCode = context.getResources().getString(R.string.thai_thailand_code);
            else if (LangName.equals(context.getResources().getString(R.string.korean_south_korea_name)))
                LangCode = context.getResources().getString(R.string.korean_south_korea_code);
            else if (LangName.equals(context.getResources().getString(R.string.chinese_mandarin_traditional_taiwan_name)))
                LangCode = context.getResources().getString(R.string.chinese_mandarin_traditional_taiwan_code);
            else if (LangName.equals(context.getResources().getString(R.string.chinese_cantonese_traditional_hong_kong_name)))
                LangCode = context.getResources().getString(R.string.chinese_cantonese_traditional_hong_kong_code);
            else if (LangName.equals(context.getResources().getString(R.string.japanese_japan_name)))
                LangCode = context.getResources().getString(R.string.japanese_japan_code);
            else if (LangName.equals(context.getResources().getString(R.string.chinese_mandarin_simplified_hong_kong_name)))
                LangCode = context.getResources().getString(R.string.chinese_mandarin_simplified_hong_kong_code);
            else if (LangName.equals(context.getResources().getString(R.string.chinese_mandarin_simplified_china_name)))
                LangCode = context.getResources().getString(R.string.chinese_mandarin_simplified_china_code);
        }catch(Exception e){e.printStackTrace();}
        return LangCode;
    }

    //get code of Font ..
    public String getFontCode(String FontName , Context context)
    {
        FontCode = context.getResources().getString(R.string.roboto_code);
        try {
            if (FontName.equals(context.getResources().getString(R.string.roboto_name)))
                FontCode = context.getResources().getString(R.string.roboto_code);
            else if (FontName.equals(context.getResources().getString(R.string.noto_sans_name)))
                FontCode = context.getResources().getString(R.string.noto_sans_code);
            else if (FontName.equals(context.getResources().getString(R.string.open_sans_name)))
                FontCode = context.getResources().getString(R.string.open_sans_code);
            else if (FontName.equals(context.getResources().getString(R.string.playfair_display_name)))
                FontCode = context.getResources().getString(R.string.playfair_display_code);
            else if (FontName.equals(context.getResources().getString(R.string.montserrat_name)))
                FontCode = context.getResources().getString(R.string.montserrat_code);
            else if (FontName.equals(context.getResources().getString(R.string.hamah_name)))
                FontCode = context.getResources().getString(R.string.hamah_code);
            else if (FontName.equals(context.getResources().getString(R.string.gess_name)))
                FontCode = context.getResources().getString(R.string.gess_code);
            else if (FontName.equals(context.getResources().getString(R.string.hayah_name)))
                FontCode = context.getResources().getString(R.string.hayah_code);
            else if (FontName.equals(context.getResources().getString(R.string.hacen_name)))
                FontCode = context.getResources().getString(R.string.hacen_code);
            else if (FontName.equals(context.getResources().getString(R.string.alhor_name)))
                FontCode = context.getResources().getString(R.string.alhor_code);
            else if (FontName.equals(context.getResources().getString(R.string.tradi_name)))
                FontCode = context.getResources().getString(R.string.tradi_code);
            else if (FontName.equals(context.getResources().getString(R.string.arial_name)))
                FontCode = context.getResources().getString(R.string.arial_code);
        }catch(Exception e){e.printStackTrace();}
        return FontCode;
    }

    //Check internet ..
    public void checkInternetStatus(Context context) {
        connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (!(wifi.isConnectedOrConnecting () || mobile.isConnectedOrConnecting ()))
            Toast.makeText(context,context.getString(R.string.internet) + "" , Toast.LENGTH_LONG).show();
    }
}
