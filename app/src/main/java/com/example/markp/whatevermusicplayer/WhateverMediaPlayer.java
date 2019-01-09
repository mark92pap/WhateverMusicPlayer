package com.example.markp.whatevermusicplayer;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.markp.whatevermusicplayer.MainActivity.currentPlaylistContainer;
import static com.example.markp.whatevermusicplayer.MainActivity.currentSongFragment;

public class WhateverMediaPlayer
{
    //region Variables

    MainActivity instance;

    //CURRENT PLAYLIST
    ArrayList<Song> playlist;

    MediaPlayer mediaPlayer;
    int currentSong;
    int currentPosition = 0;
    int repeat;     //0 = no repeat     --        1 = repeat all          --          2 = repeat one
    int shuffle;    //0 = no shuffle    --        1 = shuffle

    //SINGLETON

    private static final WhateverMediaPlayer ourInstance = new WhateverMediaPlayer();

    public static WhateverMediaPlayer getInstance() {return ourInstance;}

    //endregion

    //region Constructors - Getters - Setters

    private WhateverMediaPlayer()
    {
    }

    public WhateverMediaPlayer(ArrayList<Song> playlist, MediaPlayer mediaPlayer, int currentSong, int repeat, int shuffle) {
        this.playlist = playlist;
        this.mediaPlayer = mediaPlayer;
        this.currentSong = currentSong;
        this.repeat = repeat;
        this.shuffle = shuffle;
    }

    public ArrayList<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Song> playlist) {
        this.playlist = playlist;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public int getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(int currentSong) {
        this.currentSong = currentSong;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        if (repeat==0 || repeat==3)
        {
            instance.repeatBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_repeat_blue));
            this.repeat=0;
        }
        else if (repeat==1)
        {
            instance.repeatBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_repeat_orange));
            this.repeat=1;
        }
        else if (repeat==2)
        {
            instance.repeatBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_repeat_one_orange));
            this.repeat=2;
        }
    }

    public int getShuffle() {
        return shuffle;
    }

    public void setShuffle(int shuffle) {
        if (shuffle==0 || shuffle==2)
        {
            //NO SHUFFLE
            this.shuffle=0;
            instance.shuffleBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_shuffle_blue));
        }
        else if (shuffle==1)
        {
            //SHUFFLE
            this.shuffle=1;
            instance.shuffleBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_shuffle_orange));
        }
    }

    //endregion

    //region Init

    public void init(MainActivity activity)
    {
        mediaPlayer = new MediaPlayer();

        currentSong = -1;

        repeat =0;

        shuffle = 0;

        playlist = null;

        this.instance = activity;


        //LISTENERS

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                instance.seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                instance.changeSeekBar();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });
    }


    //endregion


    //region Controls

    public void playSong(int position)
    {
        currentSong = position;

        try
        {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(playlist.get(position).getSongPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            instance.playBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_pause_circle_outline_black_24dp));

            instance.songName.setText(playlist.get(currentSong).getName() + " - " + playlist.get(currentSong).getArtist().getName());

            String duration = instance.getTimeFromMs(mediaPlayer.getDuration());

            instance.setCurrentDuration(duration);

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            instance.albumArtUri = ContentUris.withAppendedId(sArtworkUri,playlist.get(currentSong).getAlbum().getAlbumID());

            if (currentSongFragment!=null)
            {
                currentSongFragment.setAlbumArt();
            }

            instance.updateWidget();
            instance.playBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_pause_circle_outline_black_24dp));

        }
        catch (IOException e)
        {
        }
    }

    public void playNextSong()
    {
        if (shuffle==1 && repeat!=2)
        {
            //SHUFFLE MODE IS ON AND "REPEAT ONE" IS NOT ENABLED
            int count = playlist.size();

            Random r = new Random();

            int nextSong = r.nextInt(count);

            playSong(nextSong);
        }else
        {
            if (repeat==0)
            {
                //NO REPEAT
                if (currentSong!=playlist.size()-1)
                {
                    //IF IT'S NOT THE LAST ON THE LIST, PLAY NEXT ONE
                    playSong(currentSong+1);
                }
                else
                {
                    stopPlaying();
                }
            }
            else if (repeat==1)
            {
                //REPEAT ALL
                if (currentSong!=playlist.size()-1)
                {
                    //IF IT'S NOT THE LAST ON THE LIST, PLAY NEXT ONE
                    playSong(currentSong+1);
                }
                else
                {
                    //ELSE PLAY FROM THE START
                    playSong(0);
                }
            }
            else if (repeat==2)
            {
                //REPEAT ONE --> Repeat Same Song
                playSong(currentSong);
            }
        }
    }

    public void stopPlaying()
    {
        if (mediaPlayer!=null)
        {
            instance.playBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_play_orange));
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }

    public void pauseSong()
    {
        mediaPlayer.pause();
        currentPosition = mediaPlayer.getCurrentPosition();

        instance.pauseWidget();
        instance.playBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_play_orange));
    }

    public void continueSong()
    {
        mediaPlayer.seekTo(currentPosition);
        mediaPlayer.start();
        instance.changeSeekBar();
        instance.updateWidget();
        instance.playBtn.setBackground(ContextCompat.getDrawable(instance.getApplicationContext(),R.drawable.ic_pause_circle_outline_black_24dp));
    }

    //endregion

    //region Other

    public void navigateToCurrentSongView()
    {
        instance.hideAllContainers();

        instance.setCurrentPlaylistContainer();

        currentPlaylistContainer.setCurrentItem(1);
    }

    //endregion
}
