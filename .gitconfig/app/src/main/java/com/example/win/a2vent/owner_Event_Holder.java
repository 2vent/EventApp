package com.example.win.a2vent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by win on 2017-07-05.
 */

public class owner_Event_Holder extends RecyclerView.ViewHolder{
    TextView event_name;
    TextView event_day;
    TextView event_time;
    TextView event_price;
    TextView event_dis_price;
    TextView event_addr;

    ImageView event_main;







    public owner_Event_Holder(View itemView) {
        super(itemView);
        event_name=(TextView) itemView.findViewById(R.id.event_name);
        event_day=(TextView) itemView.findViewById(R.id.event_day);
        event_time=(TextView) itemView.findViewById(R.id.time);
        event_price=(TextView) itemView.findViewById(R.id.event_price);
        event_dis_price=(TextView) itemView.findViewById(R.id.event_dis_price);
        event_addr=(TextView) itemView.findViewById(R.id.event_addr);
        event_main=(ImageView) itemView.findViewById(R.id.event_main);
    }
}
