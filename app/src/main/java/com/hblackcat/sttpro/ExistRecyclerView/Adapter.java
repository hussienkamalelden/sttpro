package com.hblackcat.sttpro.ExistRecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hblackcat.sttpro.BuildConfig;
import com.hblackcat.sttpro.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private int counter = 0;
    private int lastPosition= -1;
    private Context context;
    private List<ExistDataCatcher> catcher_arr;
    private String which_folder;
    private boolean is_there_content;
    private Typeface typeFace;
    private Animation zoom_in_and_out_fast;

    //get Context and array of data ..
    public Adapter(Context contextX, List<ExistDataCatcher> catcher, String which_folders){
        this.which_folder = which_folders;
        this.context = contextX;
        this.catcher_arr = catcher;
        zoom_in_and_out_fast = AnimationUtils.loadAnimation(this.context, R.anim.zoom_in_and_out_fast);
        typeFace = Typeface.createFromAsset(this.context.getAssets(), "hamah.ttf");
    }

    // create holder ..
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.exists_row,parent,false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    // counter of items ...
    @Override
    public int getItemCount() {
        //check if there data in arraylist ..
        if(catcher_arr != null)
        {
            counter = catcher_arr .size(); // set array size in counter ..
            return catcher_arr.size(); // get array size ..
        }else {
            return counter;
        }
    }

    // this method called every item added or viewed on screen ..
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // create new object to receive data from list of objects *** ATTENTION: don't worry onBind will not call untill add item ***
        final ExistDataCatcher obj = catcher_arr.get(position);
        holder.file_name.setText(obj.file_name); // Get file_name from arraylist ..
        holder.file_name.setTypeface(typeFace); // setTypeface ..

        //Start animation ..
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.file_name.getContext(),android.R.anim.fade_in);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        //Click to File Item to open it ..
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation ..
                try {
                    holder.show.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                //Open file  ..
                try {
                    //Select file path ..
                    ExistDataCatcher object = catcher_arr.get(position);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+object.file_name);
                    //Toast.makeText(context, ""+file, Toast.LENGTH_SHORT).show();

                    //open file ..
                    try {
                        Intent i = new Intent("com.hblackcat.sttpro.HTML_WebViewer.WebViewer");
                        i.putExtra("file", file);
                        i.putExtra("file_name", object.file_name);
                        context.startActivity(i);
                    }catch (Exception e){e.printStackTrace();}
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Click to Delete Item ..
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation ..
                try {
                    holder.delete_btn.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                //Delete item ..
                try {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setMessage("سيتم مسح الملف .. هل تريد الاستمرار؟")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete..
                                    counter--; // delete -1 ..
                                    ExistDataCatcher objects = catcher_arr.get(position);
                                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+objects.file_name);
                                    file.delete();
                                    catcher_arr.remove(position); // remove this object from list of objects (ExistDataCatcher) ..
                                    notifyItemRemoved(position); // remove item by position ..
                                    notifyItemRangeChanged(position, counter); // give RecyclerView refresh & new counter & positions ..
                                    setTextOfParentView(); //setText Of Parent View ..
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

        //Click to Re-Use button ..
        holder.re_use_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation ..
                try {
                    holder.re_use_btn.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                //Check if there are no content ..
                try
                {
                    Intent intento = ((Activity) context).getIntent();
                    is_there_content = intento.getExtras().getBoolean("boolean");
                }catch(Exception e){}

                //Ask before re-use file ..
                try {
                    //If the main page content is not empty ..
                    if(is_there_content == true) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setMessage("سيتم مسح محتوى الصفحة وإبداله بمحتوى الملف الحالي .. هل تريد الاستمرار؟")
                                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue ..
                                        //Get the text file
                                        try {
                                            ExistDataCatcher objectss = catcher_arr.get(position);
                                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + which_folder + "/" + objectss.file_name);
                                            //Read text from file
                                            StringBuilder text = new StringBuilder();
                                            BufferedReader br = new BufferedReader(new FileReader(file));
                                            String line;
                                            while ((line = br.readLine()) != null) {
                                                text.append(line);
                                            }
                                            br.close();
                                            //Send file text to STT Activity ..
                                            Intent data = new Intent();
                                            data.putExtra("re_use_text", text.toString());
                                            ((Activity) context).setResult(Activity.RESULT_OK, data);
                                            ((Activity) context).finish();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                }).show();
                    //If the main page content is empty ..
                    }else {
                        //Get the text file
                        try {
                            ExistDataCatcher objectss = catcher_arr.get(position);
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + which_folder + "/" + objectss.file_name);
                            //Read text from file
                            StringBuilder text = new StringBuilder();
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;
                            while ((line = br.readLine()) != null) {
                                text.append(line);
                            }
                            br.close();
                            //Send file text to STT Activity ..
                            Intent data = new Intent();
                            data.putExtra("re_use_text", text.toString());
                            ((Activity) context).setResult(Activity.RESULT_OK, data);
                            ((Activity) context).finish();
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });

        //Click to Share button ..
        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation ..
                try {
                    holder.share_btn.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                //share file ..
                try {
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    ExistDataCatcher ob = catcher_arr.get(position);
                    File fileWithinMyDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ which_folder+ "/"+ob.file_name);
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("text/txt");
                        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",fileWithinMyDir);
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
                        context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }

    //created to clear animation ..
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    // setText Of Parent View ..
    public void setTextOfParentView()
    {
        TextView counter_numb =((Activity)context).findViewById(R.id.counter_numb); // inisialize parent textview by context ..
        counter_numb.setText(counter + ""); // settext new Value ..
    }
}