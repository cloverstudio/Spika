package com.clover_studio.spikachatmodule.models;

import com.clover_studio.spikachatmodule.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ubuntu_ivo on 17.07.15..
 */
public class StickerCategory extends BaseModel implements Serializable{

    public String mainPic;
    public List<Sticker> list;

    @Override
    public String toString() {
        return "StickerCategory{" +
                "mainPic='" + mainPic + '\'' +
                ", list=" + list +
                '}';
    }
}
