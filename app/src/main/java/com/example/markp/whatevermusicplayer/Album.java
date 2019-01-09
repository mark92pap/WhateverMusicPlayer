package com.example.markp.whatevermusicplayer;

public class Album
{
    private String name;

    private Artist artist;

    private long albumID;

    public Album() {
    }

    public Album(String name, Artist artist, long albumID) {
        this.name = name;
        this.artist = artist;
        this.albumID = albumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumArtPath(long albumID) {
        this.albumID = albumID;
    }
}
