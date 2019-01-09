package com.example.markp.whatevermusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {
    ArrayList<Album> albumsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAlbumsList();
    }

    private void setAlbumsList() {
        RecyclerView myRv = (RecyclerView) ((MainActivity) getActivity()).findViewById(R.id.albumsListRecyclerView);

        getAlbums();

        AlbumsRecyclerViewAdapter myAdapter = new AlbumsRecyclerViewAdapter(((MainActivity) getContext()), albumsList);

        myRv.setLayoutManager(new GridLayoutManager(((MainActivity) getContext()), 2));

        myRv.setAdapter(myAdapter);
    }

    private void getAlbums() {
        albumsList = new ArrayList<>();

        ContentResolver contentResolver = ((MainActivity) getActivity()).getContentResolver();
        Uri albumsUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums._ID

        };

        Cursor albumsCursor = contentResolver.query(albumsUri, projection, null, null, null);

        if (albumsCursor != null && albumsCursor.moveToFirst()) {
            int albumInt = albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artistInt = albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int id = albumsCursor.getColumnIndex(MediaStore.Audio.Albums._ID);


            do {

                String albumString = albumsCursor.getString(albumInt);
                String artistString = albumsCursor.getString(artistInt);
                long albumID = albumsCursor.getInt(id);

                Album album = new Album(albumString, new Artist(artistString), albumID);
                albumsList.add(album);
            } while (albumsCursor.moveToNext());
        }
    }
}
