package com.clover_studio.spikachatmodule.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.clover_studio.spikachatmodule.base.BaseModel;

/**
 * Created by ubuntu_ivo on 21.07.15..
 */
public class LocationModel{

    public double lat;
    public double lng;

    @Override
    public String toString() {
        return "LocationModel{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
