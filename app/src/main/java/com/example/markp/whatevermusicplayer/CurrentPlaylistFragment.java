package com.example.markp.whatevermusicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CurrentPlaylistFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_playlist, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setCurrentPlaylist();
    }

    @Override
    public void onResume() {
        super.onResume();

        //init CurrentPlaylist
        setCurrentPlaylist();

    }

    public void setCurrentPlaylist()
    {
        RecyclerView myRv = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.currentPlaylistRecyclerView);

        SongRecyclerViewAdapter myAdapter = new SongRecyclerViewAdapter(((MainActivity)getContext()),WhateverMediaPlayer.getInstance().playlist);

        myRv.setLayoutManager(new LinearLayoutManager(((MainActivity)getContext())));

        myRv.setAdapter(myAdapter);

    }

}
