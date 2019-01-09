package com.example.markp.whatevermusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter<ArtistRecyclerViewAdapter.MyViewHolder>
{
    public static ArrayList<Song> songs;

    private Context mContext;
    private ArrayList<Artist> artists;

    public ArtistRecyclerViewAdapter(Context mContext, ArrayList<Artist> artists) {
        this.mContext = mContext;
        this.artists = artists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_artist,viewGroup,false);

        return new MyViewHolder(view);
    }

    private SongsByArtistFragment songsByArtistFragment;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final int position = i;

        myViewHolder.artistName.setText(artists.get(i).getName());

        myViewHolder.artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new fragment with view of albums and songs of selected artist

                getSongsByArtist(artists.get(position).getName());

                if (MainActivity.artistsPagerAdapter.getCount()<2)
                {
                    songsByArtistFragment = new SongsByArtistFragment();

                    MainActivity.artistsPagerAdapter.addFragment(songsByArtistFragment,"SongsOfArtist");

                    MainActivity.artistsPagerAdapter.notifyDataSetChanged();
                }
                else
                {
                    songsByArtistFragment.setSongsByArtist();
                }

                MainActivity.artistsContainer.setCurrentItem(1);

            }
        });
    }

    private void getSongsByArtist(String artist)
    {
        songs = new ArrayList<>();

        for (int i=0;i<MainActivity.allSongs.size();i++)
        {
            if (MainActivity.allSongs.get(i).getArtist().getName().equals(artist))
            {
                songs.add(MainActivity.allSongs.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView artistName;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            artistName = itemView.findViewById(R.id.artistName);
        }

    }
}
