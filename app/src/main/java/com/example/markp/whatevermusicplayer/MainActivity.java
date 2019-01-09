package com.example.markp.whatevermusicplayer;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener
{
    //region Ads

    AdView adView;

    //endregion

    //region Variables

    //region Permission

    private static final int MY_PERMISSION_REQUEST =1;

    //endregion

    //region Fragment Variables

    //region ViewPagers

    public static ViewPager allSongsContainer, currentPlaylistContainer, artistsContainer, albumsContainer, createPlaylistContainer, showCustomPlaylistContainer;

    //endregion

    //region Fragments

    public static AllSongsFragment allSongsFragment;
    public static AlbumsFragment albumsFragment;
    public static ArtistsFragment artistsFragment;
    public static CurrentPlaylistFragment currentPlaylistFragment;
    public static CurrentSongFragment currentSongFragment;

    //endregion

    //endregion

    //region Buttons and Views

    public static WhateverMediaPlayer mediaPlayer = WhateverMediaPlayer.getInstance();

    public Uri albumArtUri;

    //region Seekbar

    public SeekBar seekBar;
    Handler handler;
    Runnable runnable;

    //endregion

    //region Buttons

    public Button menuBtn, artistsBtn, albumsBtn, playBtn, prevBtn, nextBtn, repeatBtn, shuffleBtn;

    public TextView songName, startTime, endTime;

    //endregion

    //endregion

    //region Playlists

    public static ArrayList<Song> allSongs;

    //endregion

    //endregion

    //region Application start

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        launchWelcomeActivity();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();




    }

    private void initAds()
    {
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");

        adView = (AdView)findViewById(R.id.adContainer);

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
    }

    //region Permission Related

    private void launchWelcomeActivity()
    {
        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);

        startActivity(welcomeIntent);
    }

    private void getPermission()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else
        {
            startApplication();

            initAds();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();

                        startApplication();

                        initAds();
                    } else
                    {
                        Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
    }

    //endregion

    //region Initializations

    private void startApplication()
    {
        mediaPlayer.init(this);

        initViews();

        getAllSongs();

        setAllSongsContainer();
    }

    private void initViews()
    {
        //BUTTONS

        menuBtn = findViewById(R.id.menuBtn);
        artistsBtn = findViewById(R.id.artistsBtn);
        albumsBtn = findViewById(R.id.albumsBtn);
        playBtn = findViewById(R.id.playBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);

        //TEXTVIEWS

        songName= findViewById(R.id.songName);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        //VIEWPAGERS

        allSongsContainer = findViewById(R.id.allSongsContainer);
        currentPlaylistContainer = findViewById(R.id.currentPlaylistContainer);
        artistsContainer = findViewById(R.id.artistsContainer);
        albumsContainer = findViewById(R.id.albumsContainer);
        createPlaylistContainer = findViewById(R.id.createPlaylistContainer);
        showCustomPlaylistContainer = findViewById(R.id.showCustomPlaylistContainer);

        //SEEKBAR

        handler = new Handler();

        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    mediaPlayer.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //ON CLICKS
        menuBtn.setOnClickListener(this);
        artistsBtn.setOnClickListener(this);
        albumsBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        repeatBtn.setOnClickListener(this);
        shuffleBtn.setOnClickListener(this);

    }

    private void getAllSongs()
    {
        allSongs = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);

        if (songCursor!=null && songCursor.moveToFirst())
        {
            int title = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int album = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int path = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int id = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            do {
                String currentTitle = songCursor.getString(title);
                String currentArtist = songCursor.getString(artist);
                String currentAlbum = songCursor.getString(album);
                String currentPath = songCursor.getString(path);
                int albumID = songCursor.getInt(id);

                Song temp = new Song(currentTitle,new Artist(currentArtist),new Album(currentAlbum,new Artist(currentArtist),albumID),currentPath);

                allSongs.add(temp);

            } while (songCursor.moveToNext());
        }
        songCursor.close();

        mediaPlayer.setPlaylist(allSongs);
    }


    //endregion

    //region Fragment Functions

    private void setAllSongsContainer()
    {
        hideAllContainers();

        allSongsContainer.setVisibility(View.VISIBLE);

        SectionsStatePagerAdapter pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        allSongsFragment = new AllSongsFragment();

        pagerAdapter.addFragment(allSongsFragment,"AllSongsFragment");

        allSongsContainer.setAdapter(pagerAdapter);

    }

    public void setCurrentPlaylistContainer()
    {
        hideAllContainers();

        currentPlaylistContainer.setVisibility(View.VISIBLE);

        SectionsStatePagerAdapter pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        currentPlaylistFragment = new CurrentPlaylistFragment();

        pagerAdapter.addFragment(currentPlaylistFragment,"CurrentPlaylistFragment");

        currentSongFragment = new CurrentSongFragment();
        pagerAdapter.addFragment(currentSongFragment,"CurrentSongFragment");

        currentPlaylistContainer.setAdapter(pagerAdapter);
    }

    public static SectionsStatePagerAdapter artistsPagerAdapter;

    private void setArtistsContainer()
    {
        hideAllContainers();

        artistsContainer.setVisibility(View.VISIBLE);

        artistsPagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        artistsFragment = new ArtistsFragment();
        artistsPagerAdapter.addFragment(artistsFragment,"ArtistsFragment");

        artistsContainer.setAdapter(artistsPagerAdapter);


    }

    public static SectionsStatePagerAdapter albumsPagerAdapter;

    private void setAlbumsContainer()
    {
        hideAllContainers();

        albumsContainer.setVisibility(View.VISIBLE);

        albumsPagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        albumsFragment = new AlbumsFragment();

        albumsPagerAdapter.addFragment(albumsFragment,"AlbumsFragment");

        albumsContainer.setAdapter(albumsPagerAdapter);
    }

    public void hideAllContainers()
    {
        /*
        allSongsContainer.setVisibility(View.INVISIBLE);
        currentPlaylistContainer.setVisibility(View.INVISIBLE);
        artistsContainer.setVisibility(View.INVISIBLE);
        albumsContainer.setVisibility(View.INVISIBLE);
        createPlaylistContainer.setVisibility(View.INVISIBLE);
        showCustomPlaylistContainer.setVisibility(View.INVISIBLE);
        */
        allSongsContainer.setVisibility(View.GONE);
        currentPlaylistContainer.setVisibility(View.GONE);
        artistsContainer.setVisibility(View.GONE);
        albumsContainer.setVisibility(View.GONE);
        createPlaylistContainer.setVisibility(View.GONE);
        showCustomPlaylistContainer.setVisibility(View.GONE);
    }

    //endregion

    //region Seekbar Functions

    public void changeSeekBar()
    {
        seekBar.setProgress(mediaPlayer.mediaPlayer.getCurrentPosition());

        String time = getTimeFromMs(mediaPlayer.mediaPlayer.getCurrentPosition());
        startTime.setText(time);

        if (mediaPlayer.mediaPlayer.isPlaying())
        {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeSeekBar();
                }
            };

            handler.postDelayed(runnable,100);
        }
    }

    public String getTimeFromMs(int milliseconds)
    {
        long seconds = (milliseconds/1000)%60;

        long minutes = (milliseconds/(1000*60))%60;

        String time = String.format("%02d:%02d",  minutes, seconds);

        return time;
    }

    //endregion

    //endregion

    //region UPDATE UI FOR SONG INFO

    public void updateWidget()
    {
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.whatever_widget);
        ComponentName thisWidget = new ComponentName(context, WhateverWidget.class);

        int currentSong = mediaPlayer.currentSong;
        String songName = mediaPlayer.playlist.get(currentSong).getName();
        String artistName = mediaPlayer.playlist.get(currentSong).getArtist().getName();

        remoteViews.setTextViewText(R.id.songNameWidget, songName+ " - " + artistName);


        remoteViews.setImageViewResource(R.id.playBtnWidget,R.drawable.ic_pause_circle_outline_black_24dp);


        remoteViews.setImageViewUri(R.id.albumArtWidget,albumArtUri);

        appWidgetManager.updateAppWidget(thisWidget,remoteViews);
    }

    public void pauseWidget()
    {
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.whatever_widget);
        ComponentName thisWidget = new ComponentName(context, WhateverWidget.class);

        //set imageview of play button


        remoteViews.setImageViewResource(R.id.playBtnWidget,R.drawable.ic_play_orange);


        appWidgetManager.updateAppWidget(thisWidget,remoteViews);
    }

    public void setCurrentSongText(String text)
    {
        songName.setText(text);
    }

    public void setCurrentDuration(String duration)
    {
        endTime.setText(duration);
    }

    //endregion

    //region PopUpMenu

    private void showPopupMenu(View v)
    {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(this);

        popup.inflate(R.menu.popup_menu);

        popup.show();
    }

    //endregion

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menuBtn:
                showPopupMenu(v);
                break;
            case R.id.logo:
                setAllSongsContainer();
                break;
            case R.id.artistsBtn:
                setArtistsContainer();
                break;
            case R.id.albumsBtn:
                setAlbumsContainer();
                break;
            case R.id.playBtn:
                if (mediaPlayer.currentSong==-1)
                {
                    //PLAY FIRST SONG ON ALL SONGS LIST
                    mediaPlayer.setPlaylist(allSongs);
                    mediaPlayer.playSong(0);
                    playBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_pause_circle_outline_black_24dp));
                }
                else if (mediaPlayer.mediaPlayer.isPlaying())
                {
                    mediaPlayer.pauseSong();
                    playBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_play_orange));
                }
                else
                {
                    mediaPlayer.continueSong();
                    playBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_pause_circle_outline_black_24dp));
                }
                break;
            case R.id.prevBtn:
                if (mediaPlayer.currentSong==0)
                {
                    mediaPlayer.playSong(mediaPlayer.playlist.size()-1);
                }
                else
                {
                    mediaPlayer.playSong(mediaPlayer.currentSong-1);
                }
                setCurrentSongText(mediaPlayer.playlist.get(mediaPlayer.currentSong).getName()
                        + " - " + mediaPlayer.playlist.get(mediaPlayer.currentSong).getArtist().getName());

                setCurrentDuration(getTimeFromMs(mediaPlayer.mediaPlayer.getDuration()));
                break;
            case R.id.nextBtn:
                //WHEN BUTTON IS PRESSED WE ONLY CARE ABOUT SHUFFLE MODE SO SET REPEAT
                //TO 1 TO ONLY CONSIDER SHUFFLE MODE AND THEN RESTORE
                int temp=mediaPlayer.repeat;
                mediaPlayer.repeat=1;
                mediaPlayer.playNextSong();
                mediaPlayer.repeat=temp;
                setCurrentSongText(mediaPlayer.playlist.get(mediaPlayer.currentSong).getName() + " - "
                        + mediaPlayer.playlist.get(mediaPlayer.currentSong).getArtist().getName());
                setCurrentDuration(getTimeFromMs(mediaPlayer.mediaPlayer.getDuration()));
                break;
            case R.id.repeatBtn:
                mediaPlayer.setRepeat(mediaPlayer.repeat+1);
                break;
            case R.id.shuffleBtn:
                mediaPlayer.setShuffle(mediaPlayer.shuffle+1);
                break;
            case R.id.songName:
                setCurrentPlaylistContainer();

                currentPlaylistContainer.setCurrentItem(1);
                break;
        }
    }


    @Override
    protected void onDestroy()
    {
        if (mediaPlayer.mediaPlayer.isPlaying())
        {
            mediaPlayer.stopPlaying();
        }


        //Blank widget

        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.whatever_widget);
        ComponentName thisWidget = new ComponentName(context, WhateverWidget.class);

        remoteViews.setTextViewText(R.id.songNameWidget,"Song Title - Artist");

        remoteViews.setImageViewResource(R.id.albumArtWidget,R.drawable.blank);
        super.onDestroy();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mediaPlayer.mediaPlayer!=null)
        {
            mediaPlayer.navigateToCurrentSongView();
        }
    }

    @Override
    public void onBackPressed() {

        if (allSongsContainer.getVisibility()==View.VISIBLE)
        {
            exitApplication();
        }
        else if (currentPlaylistContainer.getVisibility()==View.VISIBLE)
        {
            int currentFragment = currentPlaylistContainer.getCurrentItem();

            if (currentFragment>0)
            {
                currentPlaylistContainer.setCurrentItem(currentFragment-1);
            }
            else
            {
                setAllSongsContainer();
            }
        }
        else if (artistsContainer.getVisibility()==View.VISIBLE)
        {
            int currentFragment =artistsContainer.getCurrentItem();

            if (currentFragment>0)
            {
                artistsContainer.setCurrentItem(currentFragment-1);
            }
            else
            {
                setCurrentPlaylistContainer();
            }
        }
        else if (albumsContainer.getVisibility()==View.VISIBLE)
        {
            int currentFragment = albumsContainer.getCurrentItem();

            if (currentFragment>0)
            {
                albumsContainer.setCurrentItem(currentFragment-1);
            }
            else
            {
                setCurrentPlaylistContainer();
            }
        }
        else if (createPlaylistContainer.getVisibility()==View.VISIBLE)
        {
            int currentFragment = createPlaylistContainer.getCurrentItem();

            if (currentFragment>0)
            {
                createPlaylistContainer.setCurrentItem(currentFragment-1);
            }
            else
            {
                setCurrentPlaylistContainer();
            }
        }
        else if (showCustomPlaylistContainer.getVisibility()==View.VISIBLE)
        {
            int currentFragment = showCustomPlaylistContainer.getCurrentItem();

            if (currentFragment>0)
            {
                showCustomPlaylistContainer.setCurrentItem(currentFragment-1);
            }
            else
            {
                setCurrentPlaylistContainer();
            }
        }
        else
        {
            exitApplication();
        }
    }

    private void exitApplication()
    {
        //finish();
        onPause();
        this.moveTaskToBack(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Toast.makeText(this,"Menu button was clicked", Toast.LENGTH_SHORT).show();
        return false;
    }
}
