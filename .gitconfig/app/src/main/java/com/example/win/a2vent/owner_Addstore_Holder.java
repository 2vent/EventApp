package com.example.win.a2vent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by win on 2017-07-03.
 */

public class owner_Addstore_Holder extends RecyclerView.ViewHolder {

    ImageView com_imageView;
    TextView com_name;
    TextView com_addr;
    TextView com_manager;
    TextView com_category;
    TextView com_number;


    public owner_Addstore_Holder(View itemView) {
        super(itemView);
        com_imageView = (ImageView) itemView.findViewById(R.id.com_cardview_image);
        com_addr = (TextView) itemView.findViewById(R.id.com_cardview_text1);
        com_name  = (TextView) itemView.findViewById(R.id.com_cardview_text2);
        com_manager = (TextView) itemView.findViewById(R.id.com_cardview_text3);
        com_category = (TextView) itemView.findViewById(R.id.com_cardview_text4);
        com_number = (TextView) itemView.findViewById(R.id.com_cardview_text5);

    }
}
