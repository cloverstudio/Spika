package com.clover_studio.spikachatmodule.models;

/**
 * Created by ubuntu_ivo on 21.07.15..
 */
public class SeenByModel {

    public long at;
    public User user;

    @Override
    public String toString() {
        return "SeenByModel{" +
                "at=" + at +
                ", user=" + user +
                '}';
    }
}
