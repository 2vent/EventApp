package com.example.win.a2vent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.win.a2vent.databinding.UserEventMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.win.a2vent.user_Event_Main.mRecyclerView;

/**
 * Created by EUNJAESHIN on 2017-07-10.
 * 사용자 이벤트 받아오는 어댑터
 *
 * 이미지 로드 라이브러리 Picasso
 * http://dwfox.tistory.com/33
 * AQuery (테스트만)
 * http://egloos.zum.com/mightyfine/v/307168
 */

public class user_Event_Adapter extends RecyclerView.Adapter<user_Event_Holder> {

    private Context context;
    private ArrayList<user_Event_Item> mItems = new ArrayList<user_Event_Item>();
    private int lastPosition = -1;

    public user_Event_Adapter(ArrayList items, Context mContext) {
        mItems = items;
        context = mContext;
    }

    @Override
    public user_Event_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_event_cardview, parent, false);
        user_Event_Holder holder = new user_Event_Holder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(user_Event_Holder holder, int position) {
//        피카소 라이브러리 적용
        Picasso.with(context).load(GlobalData.getURL()+ mItems.get(position).event_URI)
                .placeholder(R.drawable.event_default).into(holder.imageView);

        holder.textView1.setText(mItems.get(position).event_name);
        holder.textView2.setText(mItems.get(position).event_price);
        holder.textView3.setText(mItems.get(position).event_dis_price);
        holder.textView4.setText(mItems.get(position).event_startday);
        holder.textView5.setText(mItems.get(position).event_endday);

//        아이템 클릭리스너 (RecyclerItemClickListener)
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(context, position + "번 째 아이템", Toast.LENGTH_SHORT).show();
                    }
                })
        );

        setAnimation(holder.imageView, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    //    이미지 애니메이션 기본
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}


