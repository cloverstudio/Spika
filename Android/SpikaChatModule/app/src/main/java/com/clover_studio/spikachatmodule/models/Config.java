package com.clover_studio.spikachatmodule.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.clover_studio.spikachatmodule.base.BaseModel;

/**
 * Created by ubuntu_ivo on 21.07.15..
 */
public class Config extends BaseModel implements Parcelable{

    public String apiBaseUrl;
    public String socketUrl;
    public boolean showSidebar;
    public boolean showTitlebar;

    public Config(){};

    public Config (String apiBaseUrl, String socketUrl){
        this.apiBaseUrl = apiBaseUrl;
        this.socketUrl = socketUrl;
        showSidebar = false;
        showTitlebar = false;
    }

    @Override
    public String toString() {
        return "Config{" +
                "apiBaseUrl='" + apiBaseUrl + '\'' +
                ", socketUrl='" + socketUrl + '\'' +
                ", showSidebar=" + showSidebar +
                ", showTitlebar=" + showTitlebar +
                '}';
    }

    protected Config(Parcel in) {
        apiBaseUrl = in.readString();
        socketUrl = in.readString();
        showSidebar = in.readByte() != 0x00;
        showTitlebar = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apiBaseUrl);
        dest.writeString(socketUrl);
        dest.writeByte((byte) (showSidebar ? 0x01 : 0x00));
        dest.writeByte((byte) (showTitlebar ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel in) {
            return new Config(in);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };

}
