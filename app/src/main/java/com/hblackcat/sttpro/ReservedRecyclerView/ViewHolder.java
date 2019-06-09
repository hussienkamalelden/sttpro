package com.hblackcat.sttpro.ReservedRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hblackcat.sttpro.R;

public class ViewHolder  extends RecyclerView.ViewHolder {

    TextView words_before,words_after,words_before_title,words_after_title;
    RelativeLayout delete_btn,edit_btn;

    ViewHolder(View itemView)
    {
        super(itemView);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        edit_btn = itemView.findViewById(R.id.edit_btn);
        words_before = itemView.findViewById(R.id.words_before);
        words_after = itemView.findViewById(R.id.words_after);
        words_before_title = itemView.findViewById(R.id.words_before_title);
        words_after_title = itemView.findViewById(R.id.words_after_title);
    }
}
