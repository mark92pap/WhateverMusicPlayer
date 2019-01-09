package com.example.markp.whatevermusicplayer;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WhateverWidget extends AppWidgetProvider {

    private static final String PlayOnClick = "playOnClickTag";
    private static final String PreviousOnClick = "previousOnClickTag";
    private static final String NextOnClick = "nextOnClickTag";
    private static final String AlbumArtClick = "albumArtClickTag";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.whatever_widget);

        views.setOnClickPendingIntent(R.id.playBtnWidget,getPendingSelfIntent(context, PlayOnClick));
        views.setOnClickPendingIntent(R.id.prevBtnWidget,getPendingSelfIntent(context, PreviousOnClick));
        views.setOnClickPendingIntent(R.id.nextBtnWidget,getPendingSelfIntent(context, NextOnClick));
        views.setOnClickPendingIntent(R.id.albumArtWidget,getPendingSelfIntent(context, AlbumArtClick));

        if (WhateverMediaPlayer.getInstance().mediaPlayer.isPlaying())
        {
            views.setImageViewResource(R.id.playBtnWidget,R.drawable.ic_pause_circle_outline_black_24dp);
            //views.setInt(R.id.playBtnWidget,"setBackgroundResource",R.drawable.ic_pause_circle_outline_black_24dp);

        }
        else
        {
            views.setImageViewResource(R.id.playBtnWidget,R.drawable.ic_play_orange);
            //views.setInt(R.id.playBtnWidget,"setBackgroundResource",R.drawable.ic_play_orange);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context,intent);


        if (PlayOnClick.equals(intent.getAction()))
        {
            if (WhateverMediaPlayer.getInstance().mediaPlayer.isPlaying())
            {
                WhateverMediaPlayer.getInstance().pauseSong();
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.whatever_widget);
                views.setImageViewResource(R.id.playBtnWidget,R.drawable.ic_play_orange);
            }
            else
            {
                WhateverMediaPlayer.getInstance().continueSong();
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.whatever_widget);
                views.setImageViewResource(R.id.playBtnWidget,R.drawable.ic_pause_circle_outline_black_24dp);
            }
        }
        else if (PreviousOnClick.equals(intent.getAction()))
        {
            int currentSong = WhateverMediaPlayer.getInstance().currentSong;

            if (currentSong==0)
            {
                WhateverMediaPlayer.getInstance().playSong(WhateverMediaPlayer.getInstance().playlist.size()-1);
            }
            else
            {
                WhateverMediaPlayer.getInstance().playSong(currentSong-1);
            }
        }
        else if (NextOnClick.equals(intent.getAction()))
        {
            int temp = WhateverMediaPlayer.getInstance().repeat;
            WhateverMediaPlayer.getInstance().repeat=1;
            WhateverMediaPlayer.getInstance().playNextSong();
            WhateverMediaPlayer.getInstance().repeat=temp;
        }
        else if (AlbumArtClick.equals(intent.getAction()))
        {
            Intent intent1 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            context.startActivity(intent1);
        }


    }

    protected PendingIntent getPendingSelfIntent(Context context, String action)
    {
            Intent intent = new Intent(context, getClass());
            intent.setAction(action);
            return PendingIntent.getBroadcast(context,0,intent,0);
    }
}

