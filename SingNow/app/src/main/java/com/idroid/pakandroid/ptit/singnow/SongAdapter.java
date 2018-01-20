package com.idroid.pakandroid.ptit.singnow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by admin on 1/20/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    IGiaoTiep myIGiaoTiep;
    ArrayList<Song> myList;
    Context myContext;

    public SongAdapter(IGiaoTiep myIGiaoTiep, ArrayList<Song> myList, Context myContext) {
        this.myIGiaoTiep = myIGiaoTiep;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtName.setText(myList.get(position).getName());
        holder.txtSinger.setText(myList.get(position).getSinger());
        final String title = myList.get(position).getName() + "_" + myList.get(position).getSinger() + ".MP4";
        holder.btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(myContext, "Nghe thử", Toast.LENGTH_SHORT).show();
                myIGiaoTiep.downloadVideo(title);
            }
        });
        holder.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(myContext, "Ghi âm", Toast.LENGTH_SHORT).show();
                myIGiaoTiep.swapActivity(title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSinger;
        Button btnRecord, btnListen;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textViewTitle);
            txtSinger = itemView.findViewById(R.id.textViewSinger);
            btnRecord = itemView.findViewById(R.id.buttonRecord);
            btnListen = itemView.findViewById(R.id.buttonListen);
        }
    }
}
