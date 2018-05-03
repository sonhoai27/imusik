package com.sonhoai.sonho.imusik.Models;

public class Rate {
    private String user;
    private int noLove;
    private int littleLove;
    private int Love;
    private int lotsofLove;
    private int superLove;

    public Rate(String user, int noLove, int littleLove, int love, int lotsofLove, int superLove) {
        this.user = user;
        this.noLove = noLove;
        this.littleLove = littleLove;
        Love = love;
        this.lotsofLove = lotsofLove;
        this.superLove = superLove;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNoLove() {
        return noLove;
    }

    public void setNoLove(int noLove) {
        this.noLove = noLove;
    }

    public int getLittleLove() {
        return littleLove;
    }

    public void setLittleLove(int littleLove) {
        this.littleLove = littleLove;
    }

    public int getLove() {
        return Love;
    }

    public void setLove(int love) {
        Love = love;
    }

    public int getLotsofLove() {
        return lotsofLove;
    }

    public void setLotsofLove(int lotsofLove) {
        this.lotsofLove = lotsofLove;
    }

    public int getSuperLove() {
        return superLove;
    }

    public void setSuperLove(int superLove) {
        this.superLove = superLove;
    }
}
