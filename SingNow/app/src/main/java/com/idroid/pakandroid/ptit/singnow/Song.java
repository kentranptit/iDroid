package com.idroid.pakandroid.ptit.singnow;

/**
 * Created by admin on 1/20/2018.
 */

public class Song {
    public String name;
    public String singer;
    public String link;

    public Song() {
        //used for receiving data from firebase
    }

    public Song(String name, String singer, String link) {
        this.name = name;
        this.singer = singer;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getLink() {
        return link;
    }
}
