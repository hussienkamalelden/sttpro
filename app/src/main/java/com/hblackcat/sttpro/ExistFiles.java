package com.hblackcat.sttpro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.hblackcat.sttpro.ExistRecyclerView.Adapter;
import com.hblackcat.sttpro.ExistRecyclerView.ExistDataCatcher;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExistFiles extends Activity {

    private Typeface typeFace;
    private List<ExistDataCatcher> catcher_arr;
    private Context context =this;
    private RecyclerView recycler_view;
    private Adapter custom_adapter;
    private TextView exist_files_title_text,counter_text,counter_numb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exist_files);

        //Initialize ..
        typeFace = Typeface.createFromAsset(getAssets(), "hamah.ttf");
        catcher_arr = new ArrayList<>(); // array of objects to received data ..
        exist_files_title_text = findViewById(R.id.exist_files_title_text);
        counter_text = findViewById(R.id.counter_text);
        counter_numb = findViewById(R.id.counter_numb);
        recycler_view = findViewById(R.id.recycler_view);
        exist_files_title_text.setTypeface(typeFace);
        counter_text.setTypeface(typeFace);
        counter_numb.setTypeface(typeFace);

        //set up Adapter with recyclerView ..
        custom_adapter =new Adapter(context,catcher_arr,"TextFiles"); // send context in parameter ..
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(custom_adapter);

        //get files from directory and update RecyclerView ..
        try{
            updateRecyclerView();
        }catch (Exception e){e.printStackTrace();}
    }

    //get files from directory and update RecyclerView ..
    public void updateRecyclerView() {
        try {
            File f = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/TextFiles");
            f.mkdirs();
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++)
                if (!catcher_arr.contains(files[i].getName()))
                    catcher_arr.add(new ExistDataCatcher(files[i].getName()));
            custom_adapter.notifyDataSetChanged(); //Refresh recyclerView to get all new info from arraylist (DataCatcher) ..

            //check if there files or not ..
            if (catcher_arr.size() == 0) {
                recycler_view.setVisibility(View.GONE);
            }else {
                counter_numb.setText(catcher_arr.size()+"");
            }
        }catch (Exception e){e.printStackTrace();}
    }

    //onDestroy ..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            // clear ArrayList in case it run before .. clear better and faster than removeAll() method ..
            catcher_arr.clear();
        }catch (Exception e){e.printStackTrace();}
    }
}