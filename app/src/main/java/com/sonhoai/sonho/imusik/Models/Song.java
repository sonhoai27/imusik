package com.sonhoai.sonho.imusik.Models;


public class Song{
    private int id;
    private int idKind;
    private int idSinger;
    private String nameSong;
    private String nameSinger;
    private String imageSong;
    private String urlSong;
    private String luotNghe;

    public Song(int id, int idKind, int idSinger, String nameSong, String nameSinger, String imageSong, String urlSong, String luotNghe) {
        this.id = id;
        this.idKind = idKind;
        this.idSinger = idSinger;
        this.nameSong = nameSong;
        this.nameSinger = nameSinger;
        this.imageSong = imageSong;
        this.urlSong = urlSong;
        this.luotNghe = luotNghe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdKind() {
        return idKind;
    }

    public void setIdKind(int idKind) {
        this.idKind = idKind;
    }

    public int getIdSinger() {
        return idSinger;
    }

    public void setIdSinger(int idSinger) {
        this.idSinger = idSinger;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getImageSong() {
        return imageSong;
    }

    public void setImageSong(String imageSong) {
        this.imageSong = imageSong;
    }

    public String getUrlSong() {
        return urlSong;
    }

    public void setUrlSong(String urlSong) {
        this.urlSong = urlSong;
    }

    public String getLuotNghe() {
        return luotNghe;
    }

    public void setLuotNghe(String luotNghe) {
        this.luotNghe = luotNghe;
    }
}
