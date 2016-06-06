package com.clover_studio.spikachatmodule.models;

import com.clover_studio.spikachatmodule.base.BaseModel;

import java.io.Serializable;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class Sticker extends BaseModel implements Serializable{

    public String fullPic;
    public String smallPic;

    //for saving
    public int timesClicked = 0;

    @Override
    public String toString() {
        return "Sticker{" +
                "fullPic='" + fullPic + '\'' +
                ", smallPic='" + smallPic + '\'' +
                ", timesClicked=" + timesClicked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sticker sticker = (Sticker) o;

        if (fullPic != null ? !fullPic.equals(sticker.fullPic) : sticker.fullPic != null) return false;
        return !(smallPic != null ? !smallPic.equals(sticker.smallPic) : sticker.smallPic != null);

    }

    @Override
    public int hashCode() {
        int result = fullPic != null ? fullPic.hashCode() : 0;
        result = 31 * result + (smallPic != null ? smallPic.hashCode() : 0);
        result = 31 * result + timesClicked;
        return result;
    }
}
