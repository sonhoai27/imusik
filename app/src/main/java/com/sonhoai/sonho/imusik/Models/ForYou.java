package com.sonhoai.sonho.imusik.Models;

public class ForYou {
    private String id;
    private String name;
    private int image;
    private int background;

    public ForYou(String id, String name, int image, int background) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.background = background;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "ForYou{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", background=" + background +
                '}';
    }
}
