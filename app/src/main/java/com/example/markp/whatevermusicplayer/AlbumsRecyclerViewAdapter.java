package com.example.markp.whatevermusicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.markp.whatevermusicplayer.MainActivity.albumsFragment;

public class AlbumsRecyclerViewAdapter extends RecyclerView.Adapter<AlbumsRecyclerViewAdapter.MyViewHolder> {

    public static ArrayList<Song> songs;

    private Context mContext;
    private ArrayList<Album> albums;

    public AlbumsRecyclerViewAdapter(Context mContext, ArrayList<Album> albums) {
        this.mContext = mContext;
        this.albums = albums;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_album,viewGroup,false);

        return new MyViewHolder(view);
    }


    private SongsByAlbumFragment songsByAlbumFragment;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final int position = i;

        myViewHolder.albumTitleCardView.setText(albums.get(i).getName());
        myViewHolder.albumArtCardView.setSelected(true);

        //GLIDE

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri,albums.get(i).getAlbumID());

        Glide.with(albumsFragment).load(uri).into(myViewHolder.albumArtCardView);



        myViewHolder.albumArtCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSongsByAlbum(albums.get(position).getName());

                if (MainActivity.albumsPagerAdapter.getCount()<2)
                {
                    songsByAlbumFragment = new SongsByAlbumFragment();

                    MainActivity.albumsPagerAdapter.addFragment(songsByAlbumFragment,"SongsOfAlbum");

                    MainActivity.albumsPagerAdapter.notifyDataSetChanged();
                }
                else
                {
                    songsByAlbumFragment.setSongsByAlbum();
                }

                MainActivity.albumsContainer.setCurrentItem(1);
            }
        });

        myViewHolder.albumTitleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new fragment with view of albums and songs of selected artist

                getSongsByAlbum(albums.get(position).getName());

                if (MainActivity.albumsPagerAdapter.getCount()<2)
                {
                    songsByAlbumFragment = new SongsByAlbumFragment();

                    MainActivity.albumsPagerAdapter.addFragment(songsByAlbumFragment,"SongsOfAlbum");

                    MainActivity.albumsPagerAdapter.notifyDataSetChanged();
                }
                else
                {
                    songsByAlbumFragment.setSongsByAlbum();
                }

                MainActivity.albumsContainer.setCurrentItem(1);

            }
        });
    }

    private void getSongsByAlbum(String album)
    {
        songs = new ArrayList<>();

        for (int i=0;i<MainActivity.allSongs.size();i++)
        {
            if (MainActivity.allSongs.get(i).getAlbum().getName().equals(album))
            {
                songs.add(MainActivity.allSongs.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView albumArtCardView;
        TextView albumTitleCardView;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            albumArtCardView = itemView.findViewById(R.id.albumArtCardView);
            albumTitleCardView = itemView.findViewById(R.id.albumTitleCardView);
        }

    }

}
