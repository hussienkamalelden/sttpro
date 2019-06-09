package com.hblackcat.sttpro.ExistRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hblackcat.sttpro.R;

public class ViewHolder  extends RecyclerView.ViewHolder {

    TextView file_name;
    ImageView delete_btn,share_btn,re_use_btn,show;

    ViewHolder(View itemView)
    {
        super(itemView);
        file_name = itemView.findViewById(R.id.file_name);
        show = itemView.findViewById(R.id.show);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        share_btn = itemView.findViewById(R.id.share_btn);
        re_use_btn = itemView.findViewById(R.id.re_use_btn);
    }
}
