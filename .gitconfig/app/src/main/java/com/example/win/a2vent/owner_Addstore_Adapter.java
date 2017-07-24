package com.example.win.a2vent;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by win on 2017-07-03.
 */

public class owner_Addstore_Adapter extends RecyclerView.Adapter<owner_Addstore_Holder> {
    private ArrayList<owner_Addstore_Item> mItems = new ArrayList<owner_Addstore_Item>();


    public owner_Addstore_Adapter(ArrayList<owner_Addstore_Item> items) {
        mItems = items;

    }


    @Override
    public owner_Addstore_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_addstore_item, parent, false);
        owner_Addstore_Holder holder = new owner_Addstore_Holder(v);


        return holder;

    }

    @Override
    public void onBindViewHolder(owner_Addstore_Holder holder, int position) {
        Log.d(TAG, "onBindViewHolder: 안됨");
        Log.d(TAG, mItems.get(position).com_name.toString());
        holder.com_name.setText(mItems.get(position).com_name);
        holder.com_addr.setText(mItems.get(position).com_addr);
        holder.com_manager.setText(mItems.get(position).com_manager);
        holder.com_category.setText(mItems.get(position).com_category);


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
