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

public class SongsByAlbumFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs_by_album, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setSongsByAlbum();
    }

    public void setSongsByAlbum()
    {
        RecyclerView myRv = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.songsByAlbumRecyclerView);

        SongRecyclerViewAdapter myAdapter = new SongRecyclerViewAdapter(((MainActivity)getContext()),AlbumsRecyclerViewAdapter.songs);

        myRv.setLayoutManager(new LinearLayoutManager(((MainActivity)getContext())));

        myRv.setAdapter(myAdapter);
    }
}
