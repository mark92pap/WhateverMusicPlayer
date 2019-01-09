package com.example.markp.whatevermusicplayer;

public class Song
{
    private String name;

    private Artist artist;

    private Album album;

    private String songPath;

    //region Constructors and Getters-Setters

    public Song() {
    }

    public Song(String name, Artist artist, Album album, String songPath) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.songPath = songPath;
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    //endregion



}
