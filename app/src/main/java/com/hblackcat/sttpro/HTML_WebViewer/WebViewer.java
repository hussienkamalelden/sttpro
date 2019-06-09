package com.hblackcat.sttpro.HTML_WebViewer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import com.hblackcat.sttpro.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class WebViewer extends Activity {

    private WebView web_view;
    private String file_name,new_file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_viewer);

        //get data from intent and call openFileHTML ..
        File f =(File) getIntent().getExtras().get("file");
        file_name = getIntent().getStringExtra("file_name");
        new_file_name = file_name.replace(".txt", "");
        openFileHTML(f);
    }

    //open file came from recyclerview ..
    private void openFileHTML(File file)
    {
        try
        {
            web_view = findViewById(R.id.web_view);
            //Set bg color ..
            web_view.setBackgroundColor(Color.parseColor("#4c4d4e"));
            //make view Wide and control it with two fingers ..
            web_view.getSettings().setLoadWithOverviewMode(true);
            web_view.getSettings().setUseWideViewPort(true);
            web_view.getSettings().setBuiltInZoomControls(true);
            web_view.getSettings().setDisplayZoomControls(false);
            web_view.loadDataWithBaseURL(null,htmlDesign(file), "text/html", "UTF-8",null);
        }catch (Exception e){}
    }

    //Get text from txt file ..
    private String getDataFromFile(File filee)
    {
        //Get the text file
        File dir = new File (""+filee);

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(dir));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append("<br>");
            }
            br.close();
        }
        catch (Exception e) {e.printStackTrace();}
        return text.toString();
    }

    //Create html design ..
    private String htmlDesign(File fileee)
    {
        String text_design ="<!doctype html>\n" +
                "<html >\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"UTF-8\">\n" +
                "\t\t<style>\n" +
                "\t\t\tbody {\n" +
                "\t\t\t\tmargin: 60px;\n" +
                "\t\t\t\tbackground-color: #1f1f1f;\n" +
                "\t\t\t\ttext-align: right;\n" +
                "\t\t\t}\n" +
                "\t\t\th2 {\n" +
                "\t\t\t\tword-wrap: break-word;\n" +
                "\t\t\t\tcolor: #cfcdcd;\n" +
                "\t\t\t\tfont-size: 45px;\n" +
                "\t\t\t}\n" +
                "\t\t\th1 {\n" +
                "\t\t\t\tword-wrap: break-word;\n" +
                "\t\t\t\tcolor: #0387a0;\n" +
                "\t\t\t\tfont-size: 55px;\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t}\n" +
                "\t\t</style>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<h1>"+new_file_name+"</h1>\n" +
                "\t\t<h2>"+getDataFromFile(fileee)+"</h2>\n" +
                "\t</body>\n" +
                "</html>";

        return text_design;
    }
}
