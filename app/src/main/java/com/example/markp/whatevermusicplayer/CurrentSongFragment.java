package com.example.markp.whatevermusicplayer;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CurrentSongFragment extends Fragment
{
    ImageView albumArt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_song, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        setAlbumArt();
    }

    public void setAlbumArt()
    {

        this.albumArt = ((MainActivity)getActivity()).findViewById(R.id.albumArt);

        albumArt.setImageDrawable(null);

        if (((MainActivity)getActivity()).albumArtUri!=null)
        {
            Glide.with(this).load(((MainActivity)getActivity()).albumArtUri).into(this.albumArt);
        }
        else
        {
            Glide.with(this).load(R.drawable.blank).into(this.albumArt);
        }

    }
}
