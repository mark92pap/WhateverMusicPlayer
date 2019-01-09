package com.example.markp.whatevermusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ArtistsFragment extends Fragment
{
    ArrayList<Artist> artistList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists_list, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setArtistsList();
    }

    private void setArtistsList()
    {
        RecyclerView myRv = (RecyclerView) ((MainActivity)getActivity()).findViewById(R.id.artistsListRecyclerView);

        getArtists();

        ArtistRecyclerViewAdapter myAdapter = new ArtistRecyclerViewAdapter(((MainActivity)getContext()),artistList);

        myRv.setLayoutManager(new LinearLayoutManager(((MainActivity)getContext())));

        myRv.setAdapter(myAdapter);
    }

    private void getArtists()
    {
        artistList = new ArrayList<>();

        ContentResolver contentResolver = ((MainActivity)getActivity()).getContentResolver();
        Uri artistsUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor artistsCursor = contentResolver.query(artistsUri,null,null,null,null);

        if (artistsCursor!=null && artistsCursor.moveToFirst())
        {
            int artistInt = artistsCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);

            do {

                String artistString = artistsCursor.getString(artistInt);

                Artist artist = new Artist(artistString);
                artistList.add(artist);
            }while (artistsCursor.moveToNext());
        }
    }
}
