package com.hblackcat.sttpro.ReservedRecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.hblackcat.sttpro.DataBase.MySQLiteHelper;
import com.hblackcat.sttpro.R;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    public int reserved_counter = 0;
    private int lastPosition= -1;
    private Context context;
    private Typeface typeFace;
    private MySQLiteHelper db;
    private Animation zoom_in_and_out_fast;

    //get Context ..
    public Adapter(Context contextX){
        this.context=contextX;
        zoom_in_and_out_fast = AnimationUtils.loadAnimation(this.context, R.anim.zoom_in_and_out_fast);
        typeFace = Typeface.createFromAsset(this.context.getAssets(), "hamah.ttf");
        db =new MySQLiteHelper(this.context);
    }

    // create holder ..
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserved_row,parent,false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    // counter of items ...
    @Override
    public int getItemCount() {
        //check if there data in arraylist ..
        if(DialogNewItem.reserved_catcher != null)
        {
            reserved_counter = DialogNewItem.reserved_catcher .size(); // set array size in counter ..
            return DialogNewItem.reserved_catcher .size(); // get array size ..
        }else {
            return reserved_counter;
        }
    }

    // this method called every item added or viewed on screen ..
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // create new object to receive data from list of objects *** ATTENTION: don't worry onBind will not call untill add item ***
        ReservedDataCatcher obj = DialogNewItem.reserved_catcher.get(position);
        holder.words_before.setText(obj.word_before); // Get before name from arraylist ..
        holder.words_after.setText(obj.word_after); // Get after name from arraylist ..
        holder.words_before.setTypeface(typeFace); // setTypeface ..
        holder.words_after.setTypeface(typeFace); // setTypeface ..
        holder.words_after_title.setTypeface(typeFace); // setTypeface ..
        holder.words_before_title.setTypeface(typeFace); // setTypeface ..

        //Start animation ..
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.words_before.getContext(), android.R.anim.fade_in);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

        //edit_btn to edit dialog ..
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Animation ..
                try {
                    holder.edit_btn.startAnimation(zoom_in_and_out_fast);
                } catch (Exception e) {e.printStackTrace();}

                //Delete item ..
                try {
                    ReservedDataCatcher objec = DialogNewItem.reserved_catcher.get(position);
                    Intent i = new Intent("com.hblackcat.sttpro.ReservedRecyclerView.DialogNewItem");
                    i.putExtra("edit","edit");
                    i.putExtra("position",position);
                    context.startActivity(i);
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
                    builder.setMessage("سيتم مسح العنصر .. هل تريد الاستمرار؟")
                            .setPositiveButton("استمرار", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete..
                                    try{
                                        //delete from DB (first before delete from reserved_catcher)..
                                        ReservedDataCatcher objs = DialogNewItem.reserved_catcher.get(position);
                                        db.deleteRow(objs.word_before);
                                    }catch (Exception e){e.printStackTrace();}

                                    reserved_counter--; // delete from counter -1 ..
                                    DialogNewItem.reserved_catcher.remove(position); // remove this object from list of objects (ReservedDataCatcher) ..
                                    notifyItemRemoved(position); // remove item by position ..
                                    notifyItemRangeChanged(position, reserved_counter); // give RecyclerView refresh & new counter & positions ..
                                    Toast.makeText(context, context.getResources().getString(R.string.item_deleted) + "", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("رجوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();
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
}