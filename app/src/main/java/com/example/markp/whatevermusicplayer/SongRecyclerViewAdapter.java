package com.example.markp.whatevermusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.MyViewHolder>
{
    private Context mContext;
    private List<Song> songs;

    public SongRecyclerViewAdapter(Context mContext, List<Song> songs) {
        this.mContext = mContext;
        this.songs = songs;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_song,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final int position = i;
        myViewHolder.songTitle.setText(songs.get(i).getName());
        myViewHolder.songArtist.setText(songs.get(i).getArtist().getName());

        myViewHolder.songTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhateverMediaPlayer.getInstance().setPlaylist((ArrayList)songs);
                WhateverMediaPlayer.getInstance().playSong(position);
                WhateverMediaPlayer.getInstance().navigateToCurrentSongView();
            }
        });

        myViewHolder.songArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhateverMediaPlayer.getInstance().setPlaylist((ArrayList)songs);
                WhateverMediaPlayer.getInstance().playSong(position);
                WhateverMediaPlayer.getInstance().navigateToCurrentSongView();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView songTitle;
        TextView songArtist;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            songTitle = (TextView) itemView.findViewById(R.id.songTitle);
            songArtist = (TextView) itemView.findViewById(R.id.songArtist);
        }

    }

}
