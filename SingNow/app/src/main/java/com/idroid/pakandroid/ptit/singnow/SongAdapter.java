package com.idroid.pakandroid.ptit.singnow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 1/20/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    ArrayList<Song> myList;
    Context myContext;

    public SongAdapter(ArrayList<Song> myList, Context myContext) {
        this.myList = myList;
        this.myContext = myContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_row_song, parent, false);
        return (new ViewHolder(itemView));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtName.setText(myList.get(position).getName());
        holder.txtSinger.setText(myList.get(position).getSinger());
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSinger;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textViewTitle);
            txtSinger = itemView.findViewById(R.id.textViewSinger);
        }
    }
}
