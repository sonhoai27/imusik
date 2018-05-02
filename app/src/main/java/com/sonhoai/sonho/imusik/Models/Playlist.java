package com.sonhoai.sonho.imusik.Models;

public class Playlist {
    private int id;
    private int idUser;
    private String name;
    private String imagePlaylist;
    private String created_date;

    public Playlist(int id, int idUser, String name, String imagePlaylist, String created_date) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.imagePlaylist = imagePlaylist;
        this.created_date = created_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePlaylist() {
        return imagePlaylist;
    }

    public void setImagePlaylist(String imagePlaylist) {
        this.imagePlaylist = imagePlaylist;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
